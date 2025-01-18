package se.jwan.lab2.data.repository

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import se.jwan.lab2.data.model.DeviceState

open class FirebaseRepository {
    private val database = FirebaseDatabase.getInstance() // Get the database instance
    private val ref = database.getReference("deviceState")  // Reference to the device state

    private val _deviceState = MutableStateFlow(DeviceState()) // Initial state
    val deviceState: StateFlow<DeviceState> get() = _deviceState // Expose the state

    init {
        // Add a real-time listener to Firebase
        ref.addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                snapshot.getValue(DeviceState::class.java)?.let {
                    _deviceState.value = it // Update the state flow with the new data
                }
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                // Handle errors here (e.g., log or show a message to the user)
            }
        })
    }

    open suspend fun fetchDeviceState() {
        val snapshot = ref.get().await()  // Fetch the data once
        snapshot.getValue(DeviceState::class.java)?.let {
            _deviceState.emit(it)  // Emit the data to the state flow
        }
    }

    open fun updateDeviceState(key: String, value: String) {
        ref.child(key).setValue(value) // Update the value in Firebase
    }
}