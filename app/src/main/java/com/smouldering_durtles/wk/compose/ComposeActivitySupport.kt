package com.smouldering_durtles.wk.compose

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.LayoutRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

fun ComponentActivity.setComposeContentView(@LayoutRes layoutId: Int) {
    setContent {
        ComposeLayoutHost(layoutId)
    }
}

@Composable
fun ComposeLayoutHost(@LayoutRes layoutId: Int) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            FrameLayout(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                addView(LayoutInflater.from(context).inflate(layoutId, this, false))
            }
        }
    )
}
