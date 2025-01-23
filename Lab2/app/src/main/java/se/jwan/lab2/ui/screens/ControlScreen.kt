package se.jwan.lab2.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import se.jwan.lab2.data.model.DeviceState
import se.jwan.lab2.data.repository.FirebaseRepository
import se.jwan.lab2.viewmodel.MainViewModel

@Composable
fun ControlScreen(viewModel: MainViewModel, onNavigateToGemini: () -> Unit) {
    val deviceState by viewModel.deviceState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SwitchWithLabel(
            label = "Light",
            isChecked = deviceState.light == "on",
            onToggle = { isChecked ->
                viewModel.toggleSwitch("light", if (isChecked) "on" else "off")
            }
        )

        SwitchWithLabel(
            label = "Door",
            isChecked = deviceState.door == "unlocked",
            onToggle = { isChecked ->
                viewModel.toggleSwitch("door", if (isChecked) "unlocked" else "locked")
            }
        )

        SwitchWithLabel(
            label = "Window",
            isChecked = deviceState.window == "open",
            onToggle = { isChecked ->
                viewModel.toggleSwitch("window", if (isChecked) "open" else "closed")
            }
        )

        Button(onClick = { onNavigateToGemini() }) {
            Text(
                text = "Talk to Gemini",
            )

        }
    }
}

@Composable
fun SwitchWithLabel(label: String, isChecked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = label, fontSize = 18.sp)
        Switch(
            checked = isChecked,
            onCheckedChange = onToggle
        )
    }
}
