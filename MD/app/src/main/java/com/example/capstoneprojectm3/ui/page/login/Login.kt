package com.example.capstoneprojectm3.ui.page.login

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.capstoneprojectm3.DatastorePreferences
import com.example.capstoneprojectm3.ViewModelFactory
import com.example.capstoneprojectm3.di.Injection
import com.example.capstoneprojectm3.ui.theme.CapstoneProjectM3Theme

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

@Composable
fun Login(
    onNavigateToSignUp: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    justSignedUpUsername: String = "",
    viewModel: LoginViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(),
            DatastorePreferences.getInstance(LocalContext.current.dataStore))
    )
) {
//    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit){
        viewModel.navigateIfHasLoggedInBefore(onNavigateToHome)
    }
    var username by rememberSaveable { mutableStateOf(justSignedUpUsername) }
    var password by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState(), enabled = true),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineLarge
        )
        OutlinedTextField(
            value = username,
            onValueChange = { username = it},
            label = { Text("Username or Email") },
            singleLine = true
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        Button(onClick = { viewModel.login(username, password, context, onNavigateToHome) }) {
            Text("Login")
        }
        TextButton(onClick = { onNavigateToSignUp() }) {
            Text("or Sign Up", textDecoration = TextDecoration.Underline)
        }
    }
}

@Preview(
    name = "Login",
    widthDp = 360,
    heightDp = 640,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun LoginPreview() {
    CapstoneProjectM3Theme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Login()
        }
    }
}