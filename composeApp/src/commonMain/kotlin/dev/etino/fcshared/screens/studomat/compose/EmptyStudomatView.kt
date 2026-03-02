package dev.etino.fcshared.screens.studomat.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.no_data
import fesb_companion_shared.composeapp.generated.resources.no_data_icon
import fesb_companion_shared.composeapp.generated.resources.page_not_found
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun EmptyStudomatView() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Icon(
            painter = painterResource(Res.drawable.no_data_icon),
            contentDescription = stringResource(Res.string.page_not_found),
            modifier = Modifier
                .padding(12.dp, 80.dp, 12.dp, 12.dp)
                .size(80.dp)
        )
        Text(stringResource(Res.string.no_data))
    }
}