package view

import MainViewModel
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Parent
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import tornadofx.*

class GraphsFragment : Fragment("Graphs") {
    private val mainViewModel: MainViewModel by inject()

    private val chart: ArrayList<LineChart<String, Number>> = ArrayList()

    private val numberOfDaysChart = SimpleStringProperty()

    override val root: Parent = scrollpane {
        isFitToWidth = true
        isFitToHeight = true
        prefHeight = 1000.0
        vbox {
            label("Number of days (max: ${mainViewModel.historyList.size})")
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
                if (!numberOfDays.isNullOrEmpty() && numberOfDays.toInt() <= mainViewModel.historyList.size) {
                    chart.forEach { it.removeFromParent() }
                    if (casesDisplay.value == true) {
                        chart.add(linechart("Cumulative cases", CategoryAxis(), NumberAxis()) {
                            series("Cases") {
                                mainViewModel.getHistoryForDays(numberOfDays.toInt())
                                    .forEach { history ->
                                        data(history.date, history.cases)
                                    }
                            }
                        })
                    }

                    if (deathsDisplay.value == true) {
                        chart.add(linechart("Cumulative deaths", CategoryAxis(), NumberAxis()) {
                            series("Deaths") {
                                mainViewModel.getHistoryForDays(numberOfDays.toInt())
                                    .forEach {
                                        data(it.date, it.deaths)
                                    }
                            }
                        })
                    }

                    if (recoveredDisplay.value == true) {
                        chart.add(linechart("Cumulative recovered", CategoryAxis(), NumberAxis()) {
                            series("Recovered") {
                                mainViewModel.getHistoryForDays(numberOfDays.toInt())
                                    .forEach {
                                        data(it.date, it.recovered)
                                    }
                            }
                        })
                    }
                    if (dailyCasesDisplay.value == true) {
                        chart.add(linechart("Daily cases", CategoryAxis(), NumberAxis()) {
                            series("Recovered") {
                                mainViewModel.getHistoryForDays(numberOfDays.toInt())
                                    .forEach {
                                        data(it.date, it.todayCases)
                                    }
                            }
                        })
                    }
                    if (dailyDeathsDisplay.value == true) {
                        chart.add(linechart("Daily deaths", CategoryAxis(), NumberAxis()) {
                            series("Recovered") {
                                mainViewModel.getHistoryForDays(numberOfDays.toInt())
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
                                    mainViewModel.getHistoryForDays(numberOfDays.toInt())
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
                                    mainViewModel.getHistoryForDays(numberOfDays.toInt())
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
                                    mainViewModel.getHistoryForDays(numberOfDays.toInt())
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
                                    mainViewModel.getHistoryForDays(numberOfDays.toInt())
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