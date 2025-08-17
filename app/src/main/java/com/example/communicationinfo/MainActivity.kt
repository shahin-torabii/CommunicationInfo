package com.example.communicationinfo


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.communicationinfo.navigations.InfoNavigation
import com.example.communicationinfo.ui.theme.CommunicationInfoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CommunicationInfoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShowInfoApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ShowInfoApp(modifier: Modifier = Modifier){
    InfoNavigation()
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShowInfoApp()
}