package com.smouldering_durtles.wk.compose

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.LayoutRes
import androidx.compose.runtime.Composable

/**
 * Helper extension for AbstractActivity to add Compose support.
 * Allows activities to opt-in to Compose gradually without replacing XML layouts.
 */
fun <T : ComponentActivity> T.setComposeScreen(
    content: @Composable () -> Unit
) {
    setContent {
        content()
    }
}

/**
 * For activities that want to keep using XML but support Compose in the future.
 * Use this to wrap XML content view in onCreateLocal() if needed.
 */
@Composable
fun LayoutWrapper(@LayoutRes layoutId: Int) {
    // This is a placeholder for future interop
    // Currently activities use setContentView(layoutId) instead
}
