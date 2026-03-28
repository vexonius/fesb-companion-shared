package dev.etino.fcshared.features.iksica.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.etino.fcshared.iksica.models.IksicaData
import dev.etino.fcshared.iksica.models.IksicaResult
import dev.etino.fcshared.iksica.models.Receipt
import dev.etino.fcshared.iksica.repository.IksicaRepositoryInterface
import dev.jordond.connectivity.Connectivity
import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.error_fetching_receipts_iksica
import fesb_companion_shared.composeapp.generated.resources.error_general_iksica
import fesb_companion_shared.composeapp.generated.resources.error_receipt_details_iksica
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

@InternalCoroutinesApi
class IksicaViewModel(
    private val repository: IksicaRepositoryInterface,
    private val connectivity: Connectivity
) : ViewModel() {

    private val _showSnackbar = MutableStateFlow<StringResource?>(null)
    val showSnackbar: StateFlow<StringResource?> = _showSnackbar
    val internetAvailable: StateFlow<Boolean> =
        connectivity.statusUpdates
            .map { it.isConnected }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = false
            )

    private val _iksicaData = MutableStateFlow<IksicaData?>(null)
    val iksicaData: StateFlow<IksicaData?> = _iksicaData

    private val _receiptSelected = MutableStateFlow<IksicaReceiptState>(IksicaReceiptState.None)
    val receiptSelected: StateFlow<IksicaReceiptState> = _receiptSelected

    private val _viewState = MutableStateFlow<IksicaViewState>(IksicaViewState.Initial)
    val viewState: StateFlow<IksicaViewState> = _viewState

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch(Dispatchers.Main) {
            println(throwable.message)
            _showSnackbar.update { Res.string.error_general_iksica }
        }
    }

    init {
        loadReceiptsFromCache()
        getReceipts()
    }

    private fun loadReceiptsFromCache() {
        viewModelScope.launch(Dispatchers.Default + coroutineExceptionHandler) {
            _viewState.update({ IksicaViewState.Loading })
            val model = repository.getCache()
            if (model == null) {
                _viewState.update { IksicaViewState.Empty }
                return@launch
            }

            _viewState.update({ IksicaViewState.Success(model) })
            _iksicaData.update({ model })
        }
    }

    fun getReceipts() {
        if (!internetAvailable.value) return
        _iksicaData.value?.let { _viewState.value = IksicaViewState.Fetching(it) }
        viewModelScope.launch(Dispatchers.Default + coroutineExceptionHandler) {
            when (val result = repository.getCardDataAndReceipts()) {
                is IksicaResult.CardAndReceiptsResult.Success -> {
                    val model = result.data

                    _viewState.update({ IksicaViewState.Success(model) })
                    _iksicaData.update({ model })
                }

                is IksicaResult.CardAndReceiptsResult.Failure -> {
                    _showSnackbar.update { Res.string.error_fetching_receipts_iksica }
                }
            }
        }
    }

    fun getReceiptDetails(receipt: Receipt?) {
        if (receipt == null) {
            hideReceiptDetails()
            return
        }
        if (!internetAvailable.value) return
        viewModelScope.launch(Dispatchers.Default + coroutineExceptionHandler) {
            _receiptSelected.update({ IksicaReceiptState.Fetching })
            when (val details = repository.getReceipt(receipt.url)) {
                is IksicaResult.ReceiptResult.Success -> {
                    _receiptSelected.update({ IksicaReceiptState.Success(receipt.copy(receiptDetails = details.data)) })
                }

                is IksicaResult.ReceiptResult.Failure -> {
                    _receiptSelected.update({ IksicaReceiptState.Error(details.throwable.message.toString()) })
                    _showSnackbar.update { Res.string.error_receipt_details_iksica }
                }
            }
        }
    }

    fun hideReceiptDetails() {
        _receiptSelected.update({ IksicaReceiptState.None })
    }
}