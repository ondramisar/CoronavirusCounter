import javafx.collections.FXCollections
import javafx.collections.ObservableList
import model.History
import tornadofx.Controller

class ViewModel : Controller() {
    val historyList: ObservableList<History> = FXCollections.observableArrayList()

    fun getCases(days: Int?): List<History> {
        return historyList.toMutableList()
            .sortedByDescending { it.day }
            .subList(0, days ?: 0)
            .sortedBy { it.day }
    }

}