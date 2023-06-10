package com.example.capstoneprojectm3.ui.page.signup

import android.content.Context
import android.content.res.Configuration
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
fun SignUp(
    onNavigateToLogin: (username: String) -> Unit = {},
    viewModel: SignUpViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository(),
            DatastorePreferences.getInstance(LocalContext.current.dataStore))
    )
) {
    val context = LocalContext.current
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState(), enabled = true),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = "Sign Up",
            style = MaterialTheme.typography.headlineLarge
        )
        OutlinedTextField(
            value = username,
            onValueChange = { username = it},
            label = { Text("Username") },
            singleLine = true
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it},
            label = { Text("Email") },
            singleLine = true
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        Button(onClick = { viewModel.signUp(username, email, password, context, onNavigateToLogin) }) {
            Text("Sign Up")
        }
        TextButton(onClick = { onNavigateToLogin("") }) {
            Text("or Login", textDecoration = TextDecoration.Underline)
        }
    }
}

@Preview(
    name = "Sign Up",
    widthDp = 360,
    heightDp = 640,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun SignUpPreview() {
    CapstoneProjectM3Theme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SignUp()
        }
    }
}