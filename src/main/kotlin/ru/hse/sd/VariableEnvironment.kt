package ru.hse.sd

class VariableEnvironment() {
    private val nameToValue = mutableMapOf<String, String>()

    private constructor(initial: Map<String, String>) : this() {
        nameToValue.putAll(initial)
    }

    operator fun get(name: String): String = nameToValue[name] ?: ""

    operator fun set(name: String, value: String) {
        if (value == "") {
            nameToValue.remove(name)
        } else {
            nameToValue[name] = value
        }
    }

    fun fork(): VariableEnvironment {
        return VariableEnvironment(nameToValue)
    }

    val mapView: Map<String, String> = nameToValue
}
