package se.jwan.lab2.ui.screens

import android.graphics.drawable.Icon
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import se.jwan.lab2.R
import se.jwan.lab2.data.model.DeviceState
import se.jwan.lab2.data.repository.FirebaseRepository
import se.jwan.lab2.viewmodel.MainViewModel



@Composable
fun ControlScreen(viewModel: MainViewModel, onNavigateToGemini: () -> Unit) {
    val deviceState by viewModel.deviceState.collectAsState()
    val turnedOffLamp = ImageVector.vectorResource(R.drawable.lamp_svgrepo_com )
    val turnedOnLamp = ImageVector.vectorResource(R.drawable.lamp_light_svgrepo_com)
    val lockedDoor = ImageVector.vectorResource(R.drawable.door_doorway_svgrepo_com)
    val unlockedDoor = ImageVector.vectorResource(R.drawable.door_exit_door_svgrepo_com)
    val openWindow = ImageVector.vectorResource(R.drawable.window_svgrepo_com)
    val closedWindow = ImageVector.vectorResource(R.drawable.window_svgrepo_com__1_)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SwitchWithLabel(
            label = "Light:",
            imVector = if (deviceState.light == "on") turnedOnLamp else turnedOffLamp,
            value = MutableStateFlow(deviceState.light),
            isChecked = deviceState.light == "on",
            onToggle = { isChecked ->
                viewModel.toggleSwitch("light", if (isChecked) "on" else "off")
            }
        )

        SwitchWithLabel(
            label = "Door:",
            value = MutableStateFlow(deviceState.door),
            imVector = if (deviceState.door == "unlocked") unlockedDoor else lockedDoor,
            isChecked = deviceState.door == "unlocked",
            onToggle = { isChecked ->
                viewModel.toggleSwitch("door", if (isChecked) "unlocked" else "locked")
            }
        )

        SwitchWithLabel(
            label = "Window:",
            value = MutableStateFlow(deviceState.window),
            imVector = if (deviceState.window == "open") openWindow else closedWindow,
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
fun SwitchWithLabel(
    label: String,
    value: MutableStateFlow<String>,
    imVector: ImageVector,
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row {
            Text(
                text = label,
                fontSize = 18.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(
                text = value.value.uppercase(),
                fontSize = 18.sp,
                modifier = Modifier.width(100.dp)
            )
        }

        Icon(
            imageVector = imVector,
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )

        Switch(
            checked = isChecked,
            onCheckedChange = onToggle,
            colors = androidx.compose.material3.SwitchDefaults.colors(
                checkedThumbColor = androidx.compose.ui.graphics.Color.DarkGray,
                checkedTrackColor = androidx.compose.ui.graphics.Color.LightGray
            )
        )
    }
}

@Preview
@Composable
fun SwitchWithLabelPreview() {
    val randomVec = ImageVector.vectorResource(R.drawable.door_exit_door_svgrepo_com)
    SwitchWithLabel(
        label = "Light:",
        value = MutableStateFlow("on"),
        imVector = randomVec,
        isChecked = true,
        onToggle = {}
    )
}
