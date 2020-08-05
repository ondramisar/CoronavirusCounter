import model.CurrentStats
import tornadofx.Controller
import tornadofx.Rest
import tornadofx.toModel
import javax.json.JsonObject

class ApiController : Controller() {

    private val api: Rest by inject()

    fun getCurrentStats(): CurrentStats =
        api.get(ALL).one().toModel()

    fun getHistory(): JsonObject =
        api.get(HISTORY).one()

    companion object {
        private const val HISTORY = "historical/all?lastdays=220"
        private const val ALL = "all"
    }
}