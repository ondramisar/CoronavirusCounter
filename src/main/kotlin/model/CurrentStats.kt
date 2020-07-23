package model

import tornadofx.JsonModel
import tornadofx.int
import javax.json.JsonObject

class CurrentStats : JsonModel {

    var cases = 0
    var todayCases = 0
    var deaths = 0
    var todayDeaths = 0
    var recovered = 0
    var todayRecovered = 0
    var active = 0
    var critical = 0


    override fun updateModel(json: JsonObject) {
        with(json) {
            cases = int("cases") ?: 0
            todayCases = int("todayCases") ?: 0
            deaths = int("deaths") ?: 0
            todayDeaths = int("todayDeaths") ?: 0
            recovered = int("recovered") ?: 0
            todayRecovered = int("todayRecovered") ?: 0
            active = int("active") ?: 0
            critical = int("critical") ?: 0
        }
    }
}