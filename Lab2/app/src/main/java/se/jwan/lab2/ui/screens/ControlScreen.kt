package se.jwan.lab2.ui.screens

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Icon
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import java.util.Locale
import java.util.regex.Pattern


@Composable
fun ControlScreen(viewModel: MainViewModel, onNavigateToGemini: () -> Unit) {
    val deviceState by viewModel.deviceState.collectAsState()
    val turnedOffLamp = ImageVector.vectorResource(R.drawable.lamp_svgrepo_com )
    val turnedOnLamp = ImageVector.vectorResource(R.drawable.lamp_light_svgrepo_com)
    val lockedDoor = ImageVector.vectorResource(R.drawable.door_doorway_svgrepo_com)
    val unlockedDoor = ImageVector.vectorResource(R.drawable.door_exit_door_svgrepo_com)
    val openWindow = ImageVector.vectorResource(R.drawable.window_svgrepo_com)
    val closedWindow = ImageVector.vectorResource(R.drawable.window_svgrepo_com__1_)

    val speechText = remember { mutableStateOf("Your speech will appear here.") }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val data = it.data
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            speechText.value = result?.get(0) ?: "No speech detected."

            processSpeechCommand(speechText.value, viewModel, speechText)
        } else {
            speechText.value = "[Speech recognition failed.]"
        }
    }

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

        Button(onClick = {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Go on then, say something.")
            launcher.launch(intent)
        }) {
            Text(
                text = "Start speech recognition",
            )
        }
        
        Text(text = speechText.value)
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



fun processSpeechCommand(speechText: String, viewModel: MainViewModel, speechTextState: MutableState<String>) {
    // Define regular expressions for commands
    val lightOnPattern = Pattern.compile("\\b(turn on the light|light on|switch on the light)\\b", Pattern.CASE_INSENSITIVE)
    val lightOffPattern = Pattern.compile("\\b(turn off the light|light off|switch off the light)\\b", Pattern.CASE_INSENSITIVE)
    val doorUnlockPattern = Pattern.compile("\\b(unlock the door|open the door)\\b", Pattern.CASE_INSENSITIVE)
    val doorLockPattern = Pattern.compile("\\b(lock the door|close the door)\\b", Pattern.CASE_INSENSITIVE)
    val windowOpenPattern = Pattern.compile("\\b(open the window|window open)\\b", Pattern.CASE_INSENSITIVE)
    val windowClosePattern = Pattern.compile("\\b(close the window|window close)\\b", Pattern.CASE_INSENSITIVE)

    // Match and process commands
    when {
        lightOnPattern.matcher(speechText).find() -> viewModel.toggleSwitch("light", "on")
        lightOffPattern.matcher(speechText).find() -> viewModel.toggleSwitch("light", "off")
        doorUnlockPattern.matcher(speechText).find() -> viewModel.toggleSwitch("door", "unlocked")
        doorLockPattern.matcher(speechText).find() -> viewModel.toggleSwitch("door", "locked")
        windowOpenPattern.matcher(speechText).find() -> viewModel.toggleSwitch("window", "open")
        windowClosePattern.matcher(speechText).find() -> viewModel.toggleSwitch("window", "closed")
        else -> speechTextState.value = "I'm sorry, I didn't understand that command."
    }
}



/*@Preview
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
}*/
