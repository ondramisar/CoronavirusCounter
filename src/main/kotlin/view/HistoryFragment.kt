package view

import MainViewModel
import javafx.scene.Parent
import model.History
import tornadofx.Fragment
import tornadofx.column
import tornadofx.tableview

class HistoryFragment : Fragment("History") {

    private val viewModel: MainViewModel by inject()

    init {
        viewModel.getHistoryList()
    }

    override val root: Parent = tableview(viewModel.historyList) {
        column("Day", History::day)
        column("Date", History::date)
        column("Cases", History::cases)
        column("D Cases", History::todayCases)
        column("Deaths", History::deaths)
        column("D Deaths", History::todayDeaths)
        column("Recovered", History::recovered)
        column("D Recovered", History::todayRecovered)
        column("D/R recovered", History::deathRatePerRecovered)
        column("D/R infected", History::deathRatePerInfected)
        column("Growth rate", History::growthRate)

        //Needs to be commented when running in idea
        //     columnResizePolicy = SmartResize.POLICY
        prefHeight = 1000.0
        prefWidth = 1200.0
    }
}