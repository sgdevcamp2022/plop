package com.plop.plopmessenger.presentation.screen.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.plop.plopmessenger.presentation.screen.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InitialActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginRoot {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}