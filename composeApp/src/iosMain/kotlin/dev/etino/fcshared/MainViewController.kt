package dev.etino.fcshared

import androidx.compose.ui.window.ComposeUIViewController
import dev.etino.fcshared.application.ApplicationWrapper
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    return ComposeUIViewController {
        ApplicationWrapper()
    }
}