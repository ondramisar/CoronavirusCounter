import com.google.gson.GsonBuilder
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.control.Label
import model.History
import tornadofx.*
import java.io.File
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


//TODO add support for each country
//TODO add specific api for czech republic with more in depth data
class MainView : View("My View") {
    private val DATA = "history.json"

    private val apiController: ApiController by inject()
    private val viewModel: ViewModel by inject()

    //TODO find a better way to store each property
    private val casesString = SimpleStringProperty()
    private var cases: Label by singleAssign()

    private val deathsString = SimpleStringProperty()
    private var deaths: Label by singleAssign()

    private val recoveredString = SimpleStringProperty()
    private var recovered: Label by singleAssign()

    private val todayCasesString = SimpleStringProperty()
    private var todayCases: Label by singleAssign()

    private val todayDeathsString = SimpleStringProperty()
    private var todayDeaths: Label by singleAssign()

    private val todayRecoveredString = SimpleStringProperty()
    private var todayRecovered: Label by singleAssign()


    private val activeString = SimpleStringProperty()
    private var active: Label by singleAssign()

    private val criticalString = SimpleStringProperty()
    private var critical: Label by singleAssign()

    private val chart: ArrayList<LineChart<String, Number>> = ArrayList()


    private val numberOfDaysChart = SimpleStringProperty()


    init {
        //  val dataJson = MainView::class.java.getResourceAsStream(DATA).bufferedReader().use { it.readText() }
        val file = File(DATA)
        if (file.exists()) {
            val dataJson = file.inputStream().bufferedReader().use { it.readText() }

            if (dataJson.isNotEmpty()) {
                val gson = GsonBuilder().create()
                val data = gson.fromJson(dataJson, Array<History>::class.java)
                viewModel.historyList.addAll(data)
            }
        }
    }

    //TODO each view should be split, single view is starting to be too big
    override val root = vbox {
        title = "Calculator"
        add(RestProgressBar::class)
        gridpane {
            row {
                //TODO this should all be refactored using workspaces from tornadofx
                //TODO add progress bad
                //TODO better style
                button("Get Current data") {
                    setOnAction {
                        val currentStats = apiController.getCurrentStats()

                        casesString.value = currentStats.cases.toString()
                        deathsString.value = currentStats.deaths.toString()
                        recoveredString.value = currentStats.recovered.toString()

                        todayCasesString.value = currentStats.todayCases.toString()
                        todayDeathsString.value = currentStats.todayDeaths.toString()
                        todayRecoveredString.value = currentStats.todayRecovered.toString()

                        activeString.value = currentStats.active.toString()
                        criticalString.value = currentStats.critical.toString()
                    }
                }
                button("Get History") {
                    setOnAction {
                        val historyJson = apiController.getHistory()

                        viewModel.historyList.clear()
                        var day = 1
                        historyJson.getJsonObject("cases").forEach {
                            val value = it.value.toString().toInt()
                            viewModel.historyList.add(History(day, it.key, value))
                            day++
                        }
                        viewModel.historyList.forEach {
                            val death = historyJson.getJsonObject("deaths")[it.date].toString().toInt()
                            val recovered = historyJson.getJsonObject("recovered")[it.date].toString().toInt()
                            it.deaths = death
                            it.recovered = recovered
                        }

                        viewModel.historyList.forEachIndexed { index, history ->
                            if (index > 0) {
                                history.todayCases = history.cases - viewModel.historyList[index - 1].cases
                                history.todayDeaths = history.deaths - viewModel.historyList[index - 1].deaths
                                history.todayRecovered = history.recovered - viewModel.historyList[index - 1].recovered
                            }
                            history.deathRatePerInfected =
                                ((history.deaths.toDouble() / history.cases.toDouble()) * 100)
                            history.deathRatePerRecovered =
                                ((history.deaths.toDouble() / (history.deaths.toDouble() + history.recovered.toDouble())) * 100)
                            if (index != viewModel.historyList.size - 1) {
                                history.growthRate =
                                    viewModel.historyList[index + 1].cases.toDouble() / history.cases.toDouble()
                            }

                        }

                        val gson = GsonBuilder().create()
                        val jsonString: String = gson.toJson(viewModel.historyList)
                        val file = File(DATA)
                        file.writeText(jsonString)
                    }
                }
            }
        }
        tabpane {
            //TODO need to add json that saves current data
            //TODO add better styling
            tab("Current data") {
                gridpane {
                    row {
                        label("Cases") {
                            addClass(Styles.labelBold)
                        }
                        cases = label("") {
                            useMaxWidth = true
                        }
                        casesString.onChange {
                            cases.text = NumberFormat.getNumberInstance(Locale.getDefault()).format(it?.toInt())
                        }
                    }
                    row {
                        label("Today Cases") {
                            addClass(Styles.labelBold)
                        }
                        todayCases = label("") {
                            useMaxWidth = true
                        }
                        todayCasesString.onChange {
                            todayCases.text = NumberFormat.getNumberInstance(Locale.getDefault()).format(it?.toInt())
                        }
                    }
                    row {
                        label("Deaths") {
                            addClass(Styles.labelBold)
                        }
                        deaths = label("") {
                            useMaxWidth = true
                        }
                        deathsString.onChange {
                            deaths.text = NumberFormat.getNumberInstance(Locale.getDefault()).format(it?.toInt())
                        }
                    }
                    row {
                        label("Today Deaths") {
                            addClass(Styles.labelBold)
                        }
                        todayDeaths = label("") {
                            useMaxWidth = true
                        }
                        todayDeathsString.onChange {
                            todayDeaths.text = NumberFormat.getNumberInstance(Locale.getDefault()).format(it?.toInt())
                        }
                    }
                    row {
                        label("Recovered") {
                            addClass(Styles.labelBold)
                        }
                        recovered = label("") {
                            useMaxWidth = true
                        }
                        recoveredString.onChange {
                            recovered.text = NumberFormat.getNumberInstance(Locale.getDefault()).format(it?.toInt())
                        }
                    }
                    row {
                        label("Today Recovered") {
                            addClass(Styles.labelBold)
                        }
                        todayRecovered = label("") {
                            useMaxWidth = true
                        }
                        todayRecoveredString.onChange {
                            todayRecovered.text =
                                NumberFormat.getNumberInstance(Locale.getDefault()).format(it?.toInt())
                        }
                    }
                    row {
                        label("Active") {
                            addClass(Styles.labelBold)
                        }
                        active = label("") {
                            useMaxWidth = true
                        }
                        activeString.onChange {
                            active.text = NumberFormat.getNumberInstance(Locale.getDefault()).format(it?.toInt())
                        }
                    }
                    row {
                        label("Critical") {
                            addClass(Styles.labelBold)
                        }
                        critical = label("") {
                            useMaxWidth = true
                        }
                        criticalString.onChange {
                            critical.text = NumberFormat.getNumberInstance(Locale.getDefault()).format(it?.toInt())
                        }
                    }
                }
            }
            tab("History") {
                tableview(viewModel.historyList) {
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
            tab("Cases") {
                scrollpane {
                    isFitToWidth = true
                    isFitToHeight = true
                    prefHeight = 1000.0
                    vbox {
                        label("Number of days (max: ${viewModel.historyList.size})")
                        textfield(numberOfDaysChart)
                        //TODO find a better way to store proerty
                        val casesDisplay = SimpleBooleanProperty()
                        val deathsDisplay = SimpleBooleanProperty()
                        val recoveredDisplay = SimpleBooleanProperty()
                        val dailyCasesDisplay = SimpleBooleanProperty()
                        val dailyDeathsDisplay = SimpleBooleanProperty()
                        val dailyRecoveredDisplay = SimpleBooleanProperty()
                        val deathRatePerRecoveredDisplay = SimpleBooleanProperty()
                        val deathRatePerInfectedDisplay = SimpleBooleanProperty()
                        val growthRateDisplay = SimpleBooleanProperty()

                        //TODO add the right styling
                        hbox {
                            checkbox("Cases", casesDisplay)
                            checkbox("Deaths", deathsDisplay)
                            checkbox("Recovered", recoveredDisplay)
                        }
                        hbox {
                            checkbox("Daily cases", dailyCasesDisplay)
                            checkbox("Daily deaths", dailyDeathsDisplay)
                            checkbox("Daily recovered", dailyRecoveredDisplay)
                        }
                        hbox {
                            checkbox("Death rate % per recovered", deathRatePerRecoveredDisplay)
                            checkbox("Death rate % per infected", deathRatePerInfectedDisplay)
                            checkbox("Growth rate", growthRateDisplay)
                        }

                        //TODO refactor by adding factory for build
                        //TODO add refresh when using check box
                        //TODO style better so that numbers are more visible or add a way to show value hovered over
                        numberOfDaysChart.onChange { numberOfDays ->
                            if (!numberOfDays.isNullOrEmpty() && numberOfDays.toInt() <= viewModel.historyList.size) {
                                chart.forEach { it.removeFromParent() }
                                if (casesDisplay.value == true) {
                                    chart.add(linechart("Cumulative cases", CategoryAxis(), NumberAxis()) {
                                        series("Cases") {
                                            viewModel.getCases(numberOfDays.toInt())
                                                .forEach { history ->
                                                    data(history.date, history.cases)
                                                }
                                        }
                                    })
                                }

                                if (deathsDisplay.value == true) {
                                    chart.add(linechart("Cumulative deaths", CategoryAxis(), NumberAxis()) {
                                        series("Deaths") {
                                            viewModel.getCases(numberOfDays.toInt())
                                                .forEach {
                                                    data(it.date, it.deaths)
                                                }
                                        }
                                    })
                                }

                                if (recoveredDisplay.value == true) {
                                    chart.add(linechart("Cumulative recovered", CategoryAxis(), NumberAxis()) {
                                        series("Recovered") {
                                            viewModel.getCases(numberOfDays.toInt())
                                                .forEach {
                                                    data(it.date, it.recovered)
                                                }
                                        }
                                    })
                                }
                                if (dailyCasesDisplay.value == true) {
                                    chart.add(linechart("Daily cases", CategoryAxis(), NumberAxis()) {
                                        series("Recovered") {
                                            viewModel.getCases(numberOfDays.toInt())
                                                .forEach {
                                                    data(it.date, it.todayCases)
                                                }
                                        }
                                    })
                                }
                                if (dailyDeathsDisplay.value == true) {
                                    chart.add(linechart("Daily deaths", CategoryAxis(), NumberAxis()) {
                                        series("Recovered") {
                                            viewModel.getCases(numberOfDays.toInt())
                                                .forEach {
                                                    data(it.date, it.todayDeaths)
                                                }
                                        }
                                    })
                                }
                                if (dailyRecoveredDisplay.value == true) {
                                    chart.add(
                                        linechart("Daily recovered", CategoryAxis(), NumberAxis()) {
                                            series("Recovered") {
                                                viewModel.getCases(numberOfDays.toInt())
                                                    .forEach {
                                                        data(it.date, it.todayRecovered)
                                                    }
                                            }
                                        })
                                }
                                if (deathRatePerRecoveredDisplay.value == true) {
                                    chart.add(
                                        linechart("Death rate % per recovered", CategoryAxis(), NumberAxis()) {
                                            series("Recovered") {
                                                viewModel.getCases(numberOfDays.toInt())
                                                    .forEach {
                                                        data(it.date, it.deathRatePerRecovered)
                                                    }
                                            }
                                        })
                                }
                                if (deathRatePerInfectedDisplay.value == true) {
                                    chart.add(
                                        linechart("Death rate % per infected", CategoryAxis(), NumberAxis()) {
                                            series("Recovered") {
                                                viewModel.getCases(numberOfDays.toInt())
                                                    .forEach {
                                                        data(it.date, it.deathRatePerInfected)
                                                    }
                                            }
                                        })
                                }
                                if (growthRateDisplay.value == true) {
                                    chart.add(
                                        linechart("Growth rate", CategoryAxis(), NumberAxis()) {
                                            series("Recovered") {
                                                viewModel.getCases(numberOfDays.toInt())
                                                    .forEach {
                                                        data(it.date, it.growthRate)
                                                    }
                                            }
                                        })
                                }
                            }
                        }
                    }
                }
            }
            /* tab("Deaths") {
                 vbox {
                     label("Number of days (max: ${viewModel.historyList.size})")
                     textfield(deathsInDays)
                     deathsInDays.onChange {
                         if (!it.isNullOrEmpty() && it.toInt() <= viewModel.historyList.size) {
                             barchart?.removeFromParent()
                             barchart = linechart("Cumulative deaths", CategoryAxis(), NumberAxis()) {
                                 series("Deaths") {
                                     viewModel.getCases(it.toInt())
                                         .forEach {
                                             data(it.date, it.deaths)
                                         }
                                 }
                             }
                         }
                     }
                 }
             }*/
        }
    }
}
