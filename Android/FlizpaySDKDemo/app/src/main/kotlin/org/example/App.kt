package org.example

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import flizpay2.flizpaysdk.FlizpaySDK
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class App : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlizpayPaymentScreen(this)
        }
    }

}

@Composable
fun FlizpayPaymentScreen(context: ComponentActivity) {
    val backendURL = "http://192.168.2.34"
    val testApiKey = "0413bfa6c2ec433350c5eab97ec34f8ac6ca133c83680913e3a592296eb99171"

    var userAmount by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = userAmount,
            onValueChange = { userAmount = it },
            label = { Text("Enter amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userEmail,
            onValueChange = { userEmail = it },
            label = { Text("Enter email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    val token = fetchToken(backendURL, testApiKey)
                    if (token != null) {
                        println("Received token: $token")
                        launchPayment(context, token, userAmount, userEmail)
                    } else {
                        println("Failed to fetch token")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pay with Fliz")
        }
    }
}

private suspend fun fetchToken(backendURL: String, testApiKey: String): String? {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("${backendURL}/auth/verify-apikey")
        .post(ByteArray(0).toRequestBody(null, 0, 0))  // Empty POST request
        .addHeader("x-api-key", testApiKey)
        .build()

    return withContext(Dispatchers.IO) {
        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    println("Failed request: ${response.message}")
                    return@withContext null
                }
                response.body?.string()?.let { responseBody ->
                    val json = JSONObject(responseBody)
                    return@withContext json.getJSONObject("data").getString("token")
                }
            }
        } catch (e: IOException) {
            println("Network error: ${e.message}")
            null
        }
    }
}

private fun launchPayment(context: ComponentActivity, token: String, amount: String, email: String) {
    context.currentFocus?.let {
        FlizpaySDK.initiatePayment(
            context,
            token,
            amount,
            email
        ) { error ->
            println("Payment failed: ${error.message}")
        }
    }
}
