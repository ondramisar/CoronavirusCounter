import model.CurrentStats
import tornadofx.Controller
import tornadofx.Rest
import tornadofx.toModel
import javax.json.JsonObject

class ApiController : Controller() {

    val api: Rest by inject()

    fun getCurrentStats(): CurrentStats =
        api.get("all").one().toModel()

    fun getHistory(): JsonObject =
        api.get("historical/all?lastdays=220").one()

}