package ru.hse.sd

import org.apache.commons.lang3.RandomStringUtils
import java.io.File
import java.nio.file.Path

class TmpFile(path: String = RandomStringUtils.randomAlphanumeric(16)) : AutoCloseable {
    val file: File = Path.of(path).toFile()

    override fun close() {
        file.delete()
    }
}
