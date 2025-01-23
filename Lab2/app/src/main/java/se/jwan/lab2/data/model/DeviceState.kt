package se.jwan.lab2.data.model

data class DeviceState(
    val light: String = "off",
    val door: String = "locked",
    val window: String = "closed"
)
