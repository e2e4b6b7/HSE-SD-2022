package ru.hse.sd.cmd

import ru.hse.sd.env.VariableEnvironment
import java.io.File
import java.nio.file.Path

/**
 * Function for safe conversion path to File.
 * If file located by [path] does not exits or is not a file, than [onError] callback is used and null returned.
 **/
inline fun checkedFile(env: VariableEnvironment, path: String, onError: (String) -> Unit): File? {
    val filePath = env.getWorkingDirectory().resolve(path).toAbsolutePath()
    val file = filePath.toFile()
    if (!file.exists()) {
        onError("No such file $path\n")
        return null
    }
    if (!file.isFile) {
        onError("$path is not a file\n")
        return null
    }
    return file
}
