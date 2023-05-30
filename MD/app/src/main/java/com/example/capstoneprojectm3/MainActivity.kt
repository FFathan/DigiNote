package com.example.capstoneprojectm3

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.capstoneprojectm3.ui.navigation.MainNavHost
import com.example.capstoneprojectm3.ui.theme.CapstoneProjectM3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CapstoneProjectM3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavHost()
                }
            }
        }
    }
}

@Preview(
    name = "MainActivity",
    widthDp = 360,
    heightDp = 640,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun MainActivityPreview() {
    CapstoneProjectM3Theme {
        Surface(color = MaterialTheme.colorScheme.background) {
            MainNavHost()
        }
    }
}