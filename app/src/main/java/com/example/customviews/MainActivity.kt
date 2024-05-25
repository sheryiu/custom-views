package com.example.customviews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.customviews.ui.theme.CustomViewsTheme
import com.example.customviews.ui.view.BigButtonView
import com.example.customviews.ui.view.GateControlView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            var showing by remember { mutableStateOf("bigButton") }
            CustomViewsTheme {
                ModalNavigationDrawer(
                    drawerContent = {
                        ModalDrawerSheet {
                            Text(text = "Custom Views", modifier = Modifier.padding(all = 16.dp))
                            Divider(modifier = Modifier.padding(bottom = 12.dp))
                            NavigationDrawerItem(
                                label = { Text(text = "Big Button") },
                                selected = false,
                                onClick = { showing = "bigButton" })
                            NavigationDrawerItem(
                                label = { Text(text = "Gate Control") },
                                selected = false,
                                onClick = { showing = "gateControl" })
                        }
                    },
                    gesturesEnabled = true
                ) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            when (showing) {
                                "bigButton" -> BigButtonView()
                                "gateControl" -> GateControlView()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CustomViewsTheme {
        Greeting("Android")
    }
}