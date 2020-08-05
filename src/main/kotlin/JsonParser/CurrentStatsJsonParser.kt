package JsonParser

import com.google.gson.GsonBuilder
import model.CurrentStats
import java.io.File

class CurrentStatsJsonParser : BaseJsonParser<CurrentStats?, CurrentStats>() {

    override fun getJson(): CurrentStats? {
        val file1 = File(CURRENT_DATA)
        if (file1.exists()) {
            val dataJson = file1.getJsonString()

            if (dataJson.isNotEmpty()) {
                val gson = GsonBuilder().create()
                return gson.fromJson(dataJson, CurrentStats::class.java)
            }
        }
        return null
    }

    override fun writeJson(data: CurrentStats) {
        val gson = GsonBuilder().create()
        val jsonString: String = gson.toJson(data)
        val file = File(CURRENT_DATA)
        file.writeText(jsonString)
    }
}