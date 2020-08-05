package view

import MainViewModel
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Parent
import javafx.scene.control.Label
import tornadofx.*
import java.text.NumberFormat
import java.util.*

class CurrentDataFragment : Fragment("Current data") {

    private val viewModel: MainViewModel by inject()

    private var cases: Label by singleAssign()
    private var deaths: Label by singleAssign()
    private var recovered: Label by singleAssign()
    private var todayCases: Label by singleAssign()
    private var todayDeaths: Label by singleAssign()
    private var todayRecovered: Label by singleAssign()
    private var active: Label by singleAssign()
    private var critical: Label by singleAssign()

    init {
        viewModel.getCurrentData()

        viewModel.casesString.onChange {
            cases.text = NumberFormat.getNumberInstance(Locale.getDefault()).format(it?.toInt())
        }
        viewModel.todayCasesString.onChange {
            todayCases.text = NumberFormat.getNumberInstance(Locale.getDefault()).format(it?.toInt())
        }
        viewModel.deathsString.onChange {
            deaths.text = NumberFormat.getNumberInstance(Locale.getDefault()).format(it?.toInt())
        }
        viewModel.todayDeathsString.onChange {
            todayDeaths.text = NumberFormat.getNumberInstance(Locale.getDefault()).format(it?.toInt())
        }
        viewModel.recoveredString.onChange {
            recovered.text = NumberFormat.getNumberInstance(Locale.getDefault()).format(it?.toInt())
        }
        viewModel.todayRecoveredString.onChange {
            todayRecovered.text =
                NumberFormat.getNumberInstance(Locale.getDefault()).format(it?.toInt())
        }
        viewModel.activeString.onChange {
            active.text = NumberFormat.getNumberInstance(Locale.getDefault()).format(it?.toInt())
        }
        viewModel.criticalString.onChange {
            critical.text = NumberFormat.getNumberInstance(Locale.getDefault()).format(it?.toInt())
        }
    }

    //TODO add better styling
    override val root: Parent = gridpane {
        children.addClass(Styles.labelBold)
        row {
            label("Cases") { addClass(Styles.labelBold) }
            cases = label(formatNumber(viewModel.casesString))
        }
        row {
            label("Today Cases") { addClass(Styles.labelBold) }
            todayCases = label(formatNumber(viewModel.todayCasesString))
        }
        row {
            label("Deaths") { addClass(Styles.labelBold) }
            deaths = label(formatNumber(viewModel.deathsString))
        }
        row {
            label("Today Deaths") { addClass(Styles.labelBold) }
            todayDeaths = label(formatNumber(viewModel.todayDeathsString))
        }
        row {
            label("Recovered") { addClass(Styles.labelBold) }
            recovered = label(formatNumber(viewModel.recoveredString))
        }
        row {
            label("Today Recovered") { addClass(Styles.labelBold) }
            todayRecovered = label(formatNumber(viewModel.todayRecoveredString))
        }
        row {
            label("Active") { addClass(Styles.labelBold) }
            active = label(formatNumber(viewModel.activeString))
        }
        row {
            label("Critical") { addClass(Styles.labelBold) }
            critical = label(formatNumber(viewModel.criticalString))
        }
    }

    private fun formatNumber(number: SimpleStringProperty?): String {
        return if (number?.value != null) {
            NumberFormat.getNumberInstance(Locale.getDefault()).format(number.value?.toInt()) ?: ""
        } else {
            ""
        }
    }
}