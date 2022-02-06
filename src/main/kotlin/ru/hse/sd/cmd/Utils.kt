package ru.hse.sd.cmd

import java.io.File
import java.nio.file.Path

inline fun checkedFile(path: String, onError: (String) -> Unit): File? {
    val file = Path.of(path).toFile()
    if (!file.exists()) {
        onError("No such file $path")
        return null
    }
    if (!file.isFile) {
        onError("$path is not a file")
        return null
    }
    return file
}
