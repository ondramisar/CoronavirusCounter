package JsonParser

import com.google.gson.GsonBuilder
import model.History
import java.io.File
import javax.json.JsonObject

class HistoryJsonParser : BaseJsonParser<Array<History>?, JsonObject>() {

    override fun getJson(): Array<History>? {
        val file = File(DATA)
        if (file.exists()) {
            val dataJson = file.getJsonString()

            if (dataJson.isNotEmpty()) {
                val gson = GsonBuilder().create()
                return gson.fromJson(dataJson, Array<History>::class.java)
            }
        }
        return null
    }

    override fun writeJson(data: JsonObject) {
        val historyList = ArrayList<History>()
        var day = 1
        data.getJsonObject("cases").forEach {
            val value = it.value.toString().toInt()
            historyList.add(History(day, it.key, value))
            day++
        }
        historyList.forEach {
            val death = data.getJsonObject("deaths")[it.date].toString().toInt()
            val recovered = data.getJsonObject("recovered")[it.date].toString().toInt()
            it.deaths = death
            it.recovered = recovered
        }

        historyList.forEachIndexed { index, history ->
            if (index > 0) {
                history.todayCases = history.cases - historyList[index - 1].cases
                history.todayDeaths = history.deaths - historyList[index - 1].deaths
                history.todayRecovered =
                    history.recovered - historyList[index - 1].recovered
            }
            history.deathRatePerInfected =
                ((history.deaths.toDouble() / history.cases.toDouble()) * 100)
            history.deathRatePerRecovered =
                ((history.deaths.toDouble() / (history.deaths.toDouble() + history.recovered.toDouble())) * 100)
            if (index != historyList.size - 1) {
                history.growthRate =
                    historyList[index + 1].cases.toDouble() / history.cases.toDouble()
            }

        }

        val gson = GsonBuilder().create()
        val jsonString: String = gson.toJson(historyList)
        val file = File(DATA)
        file.writeText(jsonString)
    }
}
