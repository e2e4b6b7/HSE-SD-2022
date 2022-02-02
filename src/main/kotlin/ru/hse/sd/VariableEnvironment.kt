package ru.hse.sd

class VariableEnvironment() {
    private val nameToValue = mutableMapOf<String, String>()

    private constructor(initial: Map<String, String>) : this() {
        nameToValue.putAll(initial)
    }

    operator fun get(name: String): String? = nameToValue[name]

    operator fun set(name: String, value: String) {
        nameToValue[name] = value
    }

    fun remove(name: String) {
        nameToValue.remove(name)
    }

    fun fork(): VariableEnvironment = VariableEnvironment(nameToValue)

    val mapView: Map<String, String> = nameToValue
}
