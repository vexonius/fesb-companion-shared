package dev.etino.fcshared.featuresCompose.studomat.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProgressBarCompose(passed: Int, total: Int) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (passed <= total && total != 0) {
            LinearProgressIndicator(
                progress = { passed.toFloat() / total.toFloat() },
                modifier = Modifier.wrapContentWidth(),
                color = MaterialTheme.colorScheme.secondaryContainer,
            )
            Text(
                text = "$passed/$total",
                Modifier
                    .wrapContentWidth()
                    .padding(10.dp, 0.dp),
            )
        }

    }
}