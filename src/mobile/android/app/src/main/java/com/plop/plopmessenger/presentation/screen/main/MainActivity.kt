package com.plop.plopmessenger.presentation.screen.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.plop.plopmessenger.presentation.screen.login.InitialActivity
import com.plop.plopmessenger.presentation.screen.login.LoginRoot
import com.plop.plopmessenger.presentation.theme.PlopMessengerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainRootScreen {
                startActivity(Intent(this, InitialActivity::class.java))
                finish()
            }
        }
    }
}