package ru.hse.sd.env.cmd

import ru.hse.sd.cmd.Command
import ru.hse.sd.cmd.ExternalProcess
import java.io.File

class ExternalCommandEnvironment(paths: List<String>) : CommandEnvironment {
    private val paths = paths.toMutableList()

    override fun getCommand(commandName: String): Command? {
        for (path in paths) {
            val file = File(path).listFiles()?.firstOrNull { it.name == commandName }
            if (file != null) {
                return ExternalProcess(file.toPath())
            }
        }
        return null
    }

    fun addPath(path: String) {
        paths.add(path)
    }
}
