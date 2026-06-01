package dev.etino.fcshared.navigation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fesb_companion_shared.shared.generated.resources.Res
import fesb_companion_shared.shared.generated.resources.no_internet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun NoInternetIcon() {
    var expand by remember { mutableStateOf(false) }
    var modifier = if (expand) Modifier
    else Modifier.width(56.dp)
    FloatingActionButton(
        onClick = { expand = !expand },
        containerColor = MaterialTheme.colorScheme.inverseSurface,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = modifier
            .height(56.dp)
            .padding(8.dp)
            .animateContentSize(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (expand) {
                Spacer(modifier = Modifier.width(8.dp))
            }
            Icon(
                painter = painterResource(Res.drawable.no_internet),
                contentDescription = stringResource(Res.string.no_internet),
                modifier = Modifier.size(24.dp)
            )
            if (expand) {
                Text(
                    text = stringResource(Res.string.no_internet),
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}