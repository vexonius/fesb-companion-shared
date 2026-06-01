package dev.etino.fcshared.navigation

import androidx.navigation3.runtime.NavKey
import fesb_companion_shared.shared.generated.resources.Res
import fesb_companion_shared.shared.generated.resources.icon_attendance
import fesb_companion_shared.shared.generated.resources.icon_home
import fesb_companion_shared.shared.generated.resources.icon_iksica
import fesb_companion_shared.shared.generated.resources.icon_studomat
import fesb_companion_shared.shared.generated.resources.icon_timetable
import fesb_companion_shared.shared.generated.resources.tab_attendance
import fesb_companion_shared.shared.generated.resources.tab_home
import fesb_companion_shared.shared.generated.resources.tab_iksica
import fesb_companion_shared.shared.generated.resources.tab_studomat
import fesb_companion_shared.shared.generated.resources.tab_timetable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class TopLevelRoute(val nameId: StringResource, val route: NavKey, val iconId: DrawableResource)

val topLevelRoutes = listOf(
    TopLevelRoute(Res.string.tab_iksica, Iksica, Res.drawable.icon_iksica),
    TopLevelRoute(Res.string.tab_attendance, Attendance, Res.drawable.icon_attendance),
    TopLevelRoute(Res.string.tab_home, Home, Res.drawable.icon_home),
    TopLevelRoute(Res.string.tab_timetable, TimeTable, Res.drawable.icon_timetable),
    TopLevelRoute(Res.string.tab_studomat, Studomat, Res.drawable.icon_studomat),
)
