package model

data class History(
    val day: Int,
    val date: String,
    var cases: Int = 0,
    var todayCases: Int = 0,
    var deaths: Int = 0,
    var todayDeaths: Int = 0,
    var recovered: Int = 0,
    var todayRecovered: Int = 0,
    var deathRatePerRecovered: Double = 0.0,
    var deathRatePerInfected: Double = 0.0,
    var growthRate: Double = 0.0
)