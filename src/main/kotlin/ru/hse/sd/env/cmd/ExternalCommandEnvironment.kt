package ru.hse.sd.env.cmd

import ru.hse.sd.cmd.Command
import ru.hse.sd.cmd.ExternalProcess
import java.io.File

/**
 * Environment of external commands.
 */
class ExternalCommandEnvironment(
    /**
     * Paths to directories where executable is searched.
     */
    paths: List<String>
) : CommandEnvironment {
    private val paths = paths.toMutableList()
    /**
     * Search command with name [commandName].
     * If the command is found then return it, otherwise return null
     */
    override fun getCommand(commandName: String): Command? {
        for (path in paths) {
            val file = File(path).listFiles()?.firstOrNull { it.name == commandName }
            if (file != null) {
                return ExternalProcess(file.toPath())
            }
        }
        return null
    }

    /**
     * Add new [path] to directories where executable is searched.
     */
    fun addPath(path: String) {
        paths.add(path)
    }
}
