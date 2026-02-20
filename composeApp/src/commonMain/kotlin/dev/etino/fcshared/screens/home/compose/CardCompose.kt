package dev.etino.fcshared.screens.home.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.etino.fcshared.compose.lust
import dev.etino.fcshared.screens.home.view.HomeViewModel
import dev.etino.fcshared.screens.home.view.sidePadding
import dev.etino.fcshared.compose.meniColor
import dev.etino.fcshared.screens.iksica.angledGradientBackground
import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.menza_desc
import fesb_companion_shared.composeapp.generated.resources.menza_title
import fesb_companion_shared.composeapp.generated.resources.no_internet_menza
import fesb_companion_shared.composeapp.generated.resources.ugovori_desc
import fesb_companion_shared.composeapp.generated.resources.ugovori_title
import kotlinx.coroutines.InternalCoroutinesApi
import org.jetbrains.compose.resources.stringResource

@OptIn(InternalCoroutinesApi::class)
@Composable
fun CardsCompose(openMenza: ()-> Unit, homeViewModel: HomeViewModel) {
    Row(Modifier.padding(horizontal = sidePadding)) {
        Box(
            Modifier
                .weight(0.5f)
        ) {
            val noInternetMenza = stringResource(Res.string.no_internet_menza)
            CardCompose(
                stringResource(Res.string.menza_title),
                stringResource(Res.string.menza_desc),
                meniColor,
                meniColor,
                onClick = {
                    openMenza()
                    /*if (homeViewModel.internetAvailable.value == true) {
                        openMenza()
                    } else {
                        homeViewModel.showSnackbar(message = noInternetMenza)
                    }*/
                })
        }
        Box(
            Modifier
                .weight(0.5f)
        ) {
            CardCompose(
                stringResource(Res.string.ugovori_title),
                stringResource(Res.string.ugovori_desc),
                MaterialTheme.colorScheme.secondaryContainer,
                lust,
                onClick = {
                    homeViewModel.launchStudentskiUgovoriApp()
                }
            )
        }
    }
}

@Composable
fun CardCompose(title: String, description: String, color1: Color, color2: Color, onClick: () -> Unit = { }) {
    Column(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .height(200.dp)
            .angledGradientBackground(
                colors = listOf(color1, color2),
                degrees = 60f,
                true
            )
            .padding(15.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = description,
            style = MaterialTheme.typography.titleSmall
        )
    }
}