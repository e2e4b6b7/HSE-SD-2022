package ru.hse.sd.env

import java.nio.file.Files
import java.nio.file.Path
/**
 * Environment contains all variable defined in scope.
 */
class VariableEnvironment() {
    private val nameToValue = mutableMapOf<String, String>()

    /**
     * Current working directory.
     */
    private var workingDirectory: Path = Path.of("").toAbsolutePath()

    private constructor(initial: Map<String, String>) : this() {
        nameToValue.putAll(initial)
    }

    /**
     * Get current value assigned to variable.
     */
    operator fun get(name: String): String? = nameToValue[name]

    /**
     * Assign new value to the variable.
     */
    operator fun set(name: String, value: String) {
        nameToValue[name] = value
    }

    /**
     * Remove value assigned to variable.
     */
    fun remove(name: String) {
        nameToValue.remove(name)
    }

    /**
     * Create new `VariableEnvironment` with current as parental environment.
     * All current variables visible in child environment.
     * All changes in child environment is not visible in parental environment.
     * Modifying parental environment while one of child environment is alive is not yet supported.
     */
    fun fork(): VariableEnvironment = VariableEnvironment(nameToValue)

    /**
     * Immutable map view on all variables visible in current environment.
     */
    val mapView: Map<String, String> = nameToValue

    /**
     * Gets current working directory.
     */
    fun getWorkingDirectory(): Path = workingDirectory

    /**
     * Sets the initial working directory.
     */
    fun resetWorkingDirectory() {
        workingDirectory = Path.of("").toAbsolutePath()
    }

    /**
     * Changes the current directory depending on the path.
     * If no such path exists, then returns null.
     */
    fun changeWorkingDirectory(path: Path, onError: (String) -> Unit): Path? {
        val newWorkingDirectory = workingDirectory.resolve(path).normalize()
        return if (!Files.exists(newWorkingDirectory)) {
            onError("No such file $path\n")
            null
        } else {
            workingDirectory = newWorkingDirectory
            workingDirectory
        }
    }
}
