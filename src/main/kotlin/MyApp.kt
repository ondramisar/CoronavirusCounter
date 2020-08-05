import javafx.stage.Stage
import tornadofx.App
import tornadofx.Rest
import tornadofx.reloadStylesheetsOnFocus

class MyApp : App(MainView::class, Styles::class) {
    private val api: Rest by inject()
    override val primaryView = MainView::class

    init {
        api.baseURI = "https://disease.sh/v3/covid-19"
        reloadStylesheetsOnFocus()
    }

    override fun start(stage: Stage) {
        //  stage.isMaximized = true
        super.start(stage)
    }
}