import JsonParser.CurrentStatsJsonParser
import JsonParser.HistoryJsonParser
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import model.History
import tornadofx.Controller

class MainViewModel : Controller() {

    private val apiController: ApiController by inject()

    private val historyJsonParser: HistoryJsonParser by inject()
    private val currentStatsJsonParser: CurrentStatsJsonParser by inject()

    val historyList: ObservableList<History> = FXCollections.observableArrayList()

    val casesString = SimpleStringProperty()
    val deathsString = SimpleStringProperty()
    val recoveredString = SimpleStringProperty()
    val todayCasesString = SimpleStringProperty()
    val todayDeathsString = SimpleStringProperty()
    val todayRecoveredString = SimpleStringProperty()
    val activeString = SimpleStringProperty()
    val criticalString = SimpleStringProperty()

    fun getHistoryForDays(days: Int?): List<History> {
        return historyList.toMutableList()
            .sortedByDescending { it.day }
            .subList(0, days ?: 0)
            .sortedBy { it.day }
    }

    fun parseHistoryAndSave() {
        val historyJson = apiController.getHistory()
        historyJsonParser.writeJson(historyJson)
        getHistoryList()
    }

    fun parseCurrentStatsAndSave() {
        val currentStats = apiController.getCurrentStats()
        currentStatsJsonParser.writeJson(currentStats)
        getCurrentData()
    }

    fun getHistoryList() {
        historyJsonParser.getJson()?.let {
            historyList.addAll(it)
        }
    }

    fun getCurrentData() {
        currentStatsJsonParser.getJson()?.let { data ->
            casesString.value = data.cases.toString()
            deathsString.value = data.deaths.toString()
            recoveredString.value = data.recovered.toString()

            todayCasesString.value = data.todayCases.toString()
            todayDeathsString.value = data.todayDeaths.toString()
            todayRecoveredString.value = data.todayRecovered.toString()

            activeString.value = data.active.toString()
            criticalString.value = data.critical.toString()
        }
    }
}