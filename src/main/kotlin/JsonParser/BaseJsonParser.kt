package JsonParser

import tornadofx.Controller

abstract class BaseJsonParser<T, R> : Controller() {
    abstract fun getJson(): T
    abstract fun writeJson(data: R)
}