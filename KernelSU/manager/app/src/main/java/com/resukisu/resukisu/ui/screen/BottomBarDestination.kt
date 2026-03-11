package com.resukisu.resukisu.ui.screen

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.resukisu.resukisu.Natives
import com.resukisu.resukisu.R
import com.resukisu.resukisu.ui.MainActivity
import com.resukisu.resukisu.ui.component.ksuIsValid
import com.resukisu.resukisu.ui.screen.main.HomePage
import com.resukisu.resukisu.ui.screen.main.KpmPage
import com.resukisu.resukisu.ui.screen.main.ModulePage
import com.resukisu.resukisu.ui.screen.main.SettingsPage
import com.resukisu.resukisu.ui.screen.main.SuperUserPage
import com.resukisu.resukisu.ui.util.getKpmVersion
import dev.chrisbanes.haze.HazeState

enum class BottomBarDestination(
    val direction: @Composable (bottomPadding: Dp, hazeState: HazeState?) -> Unit,
    @param:StringRes val label: Int,
    val iconSelected: ImageVector,
    val iconNotSelected: ImageVector,
    val rootRequired: Boolean,
) {
    Home(
        { bottomPadding, hazeState -> HomePage(bottomPadding, hazeState) },
        R.string.home,
        Icons.Filled.Home,
        Icons.Outlined.Home,
        false
    ),
    Kpm(
        { bottomPadding, hazeState -> KpmPage(bottomPadding, hazeState) },
        R.string.kpm_title,
        Icons.Filled.Archive,
        Icons.Outlined.Archive,
        true
    ),
    SuperUser(
        { bottomPadding, hazeState -> SuperUserPage(bottomPadding, hazeState) },
        R.string.superuser,
        Icons.Filled.AdminPanelSettings,
        Icons.Outlined.AdminPanelSettings,
        true
    ),
    Module(
        { bottomPadding, hazeState -> ModulePage(bottomPadding, hazeState) },
        R.string.module,
        Icons.Filled.Extension,
        Icons.Outlined.Extension,
        true
    ),
    Settings(
        { bottomPadding, hazeState -> SettingsPage(bottomPadding, hazeState) },
        R.string.settings,
        Icons.Filled.Settings,
        Icons.Outlined.Settings,
        false
    );

    companion object {
        fun getPages(settings: MainActivity.SettingsState) : List<BottomBarDestination> {
            if (ksuIsValid()) {
                // 全功能管理器
                val kpmVersion = runCatching {
                    getKpmVersion()
                }.getOrNull()

                val showKpmInfo = settings.showKpmInfo
                return BottomBarDestination.entries.filter {
                    when (it) {
                        Kpm -> {
                            kpmVersion?.isNotEmpty() ?: false && !showKpmInfo && Natives.version >= Natives.MINIMAL_SUPPORTED_KPM
                        }

                        else -> true
                    }
                }
            } else {
                return BottomBarDestination.entries.filter {
                    !it.rootRequired
                }
            }
        }
    }
}
