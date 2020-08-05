import tornadofx.*
import view.CurrentDataFragment
import view.GraphsFragment
import view.HistoryFragment


//TODO add support for each country
//TODO add specific api for czech republic with more in depth data
class MainView : View("My View") {

    private val mainViewModel: MainViewModel by inject()

    override val root = vbox {
        title = "Coronavirus counter"
        add(RestProgressBar::class)
        menubar {
            menu("Data") {
                item("Get Current data").action {
                    mainViewModel.parseCurrentStatsAndSave()
                }
                item("Get history data").action {
                    mainViewModel.parseHistoryAndSave()
                }
            }
        }
        tabpane {
            tab<CurrentDataFragment>()
            tab<HistoryFragment>()
            tab<GraphsFragment>()
        }
    }
}
