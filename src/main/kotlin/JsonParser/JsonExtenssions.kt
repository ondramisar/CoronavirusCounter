package JsonParser

import java.io.File


const val DATA = "history.json"
const val CURRENT_DATA = "current_data.json"

fun File.getJsonString() = this.inputStream().bufferedReader().use { it.readText() }