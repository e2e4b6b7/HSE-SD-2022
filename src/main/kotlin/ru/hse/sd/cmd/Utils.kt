package ru.hse.sd.cmd

import java.io.File
import java.nio.file.Path

/** Function for checking correctness of file.
 * If file located by [path] does not exits or is not a file, than [onError] callback is used
 **/
inline fun checkedFile(path: String, onError: (String) -> Unit): File? {
    val file = Path.of(path).toFile()
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
