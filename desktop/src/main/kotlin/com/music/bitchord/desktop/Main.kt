package com.music.bitchord.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

/**
 * Initial Linux desktop entry point.
 *
 * This is intentionally a desktop-native host rather than an Android APK
 * wrapper. Shared player and catalogue services still need platform adapters
 * before the full mobile experience can be enabled on desktop.
 */
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "BitChord",
    ) {
        MaterialTheme(
            colorScheme = darkColorScheme(
                background = Color(0xFF080808),
                surface = Color(0xFF171717),
                primary = Color(0xFFE7E7E7),
                onPrimary = Color(0xFF111111),
                onBackground = Color(0xFFF5F5F5),
                onSurface = Color(0xFFF5F5F5),
            ),
        ) {
            DesktopHome()
        }
    }
}

@androidx.compose.runtime.Composable
private fun DesktopHome() {
    var query by remember { mutableStateOf("") }
    var selectedPage by remember { mutableStateOf("Home") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Row(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .width(220.dp)
                    .fillMaxSize()
                    .background(Color(0xFF111111))
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text("BitChord", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(18.dp))
                listOf("Home", "Explore", "Library", "Sources", "Settings").forEach { page ->
                    Button(
                        onClick = { selectedPage = page },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text(page)
                    }
                }
                Spacer(Modifier.weight(1f))
                Text(
                    "Linux desktop preview",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF929292),
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 36.dp, vertical = 28.dp),
            ) {
                Text(selectedPage, style = MaterialTheme.typography.headlineLarge)
                Spacer(Modifier.height(20.dp))

                when (selectedPage) {
                    "Home" -> {
                        Text("Your music, on Linux.", style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "This is the first native desktop host. Catalogue, account, library, and playback adapters are still being connected; this preview does not yet provide full mobile feature parity.",
                            color = Color(0xFFAAAAAA),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Spacer(Modifier.height(28.dp))
                        OutlinedTextField(
                            value = query,
                            onValueChange = { query = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Search music (not connected yet)") },
                            singleLine = true,
                        )
                    }
                    "Explore" -> PagePlaceholder("Explore", "Music discovery will be connected to the shared catalogue.")
                    "Library" -> PagePlaceholder("Library", "Library persistence and playlists need a desktop store adapter.")
                    "Sources" -> PagePlaceholder("Sources", "Source-link backup and restore will be available here after source persistence is wired to the desktop host.")
                    else -> PagePlaceholder("Settings", "Desktop preferences and platform integrations are not connected yet.")
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
private fun PagePlaceholder(title: String, detail: String) {
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
        Column {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text(detail, color = Color(0xFFAAAAAA))
        }
    }
}
