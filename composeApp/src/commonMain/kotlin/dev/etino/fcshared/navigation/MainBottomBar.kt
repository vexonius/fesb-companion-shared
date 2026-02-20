package dev.etino.fcshared.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import dev.etino.fcshared.screens.timetable.TimetableViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun MainBottomBar(
    navigate: (NavKey) -> Unit,
    topLevelRoute: NavKey,
    topLevelRoutes: List<TopLevelRoute>,
    timetableViewModel: TimetableViewModel
) {
    val bottomBarHiddenRoutes = listOf(Login, Settings)
    if (!bottomBarHiddenRoutes.contains(topLevelRoute)) {
        NavigationBar(
            contentColor = MaterialTheme.colorScheme.onSurface,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            val currentDestination = topLevelRoute
            topLevelRoutes.forEach { topLevelRoute ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            painterResource(topLevelRoute.iconId),
                            contentDescription = stringResource(topLevelRoute.nameId),
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(topLevelRoute.nameId),
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    },
                    selected = topLevelRoute.route == currentDestination,
                    alwaysShowLabel = false,
                    onClick = {
                        if (currentDestination != topLevelRoute.route) {
                            navigate(topLevelRoute.route)
                        } else if (currentDestination == topLevelRoute.route) {
                            when (topLevelRoute.route) {
                                TimeTable -> {
                                    timetableViewModel.showWeekChooseMenu()
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}