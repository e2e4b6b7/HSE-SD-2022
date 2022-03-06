package ru.hse.sd.cmd

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.hse.sd.env.VariableEnvironment
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class LsTest {
    @Test
    fun `test ls file`() {
        val testIO = IO("some input")
        val lsRes = Ls.execute(VariableEnvironment(), listOf("README.md"), testIO)
        testIO.checkStreams("README.md\n", "")
        Assertions.assertEquals(ReturnCode.success, lsRes)
    }


    @Test
    fun `test ls not existing directory`() {
        val testIO = IO("some input")
        val lsRes = Ls.execute(VariableEnvironment(), listOf("blablabla"), testIO)
        testIO.checkStreams("", "ls: cannot access 'blablabla': No such file or directory\n")
        Assertions.assertEquals(ReturnCode(1), lsRes)
    }

    @Test
    fun `test ls with 0 argument`() {
        val testIO = IO("some input")
        val lsRes = Ls.execute(VariableEnvironment(), emptyList(), testIO)
        val files = File(".").listFiles()
        Arrays.sort(files)
        val result = StringBuilder()
        for (file in files) {
            result.append(file.name).append("\n")
        }
        System.out.println(result.toString())
        testIO.checkStreams(result.toString(), "")
        Assertions.assertEquals(ReturnCode.success, lsRes)
    }

    @Test
    fun `test ls with 1 argument`() {
        val dirPath: Path  = Files.createTempDirectory("tmpDir")
        val filename1 = Files.createTempFile(dirPath, "file1", ".txt").fileName
        val filename2 = Files.createTempFile(dirPath, "file2", ".txt").fileName
        val testIO = IO("some input")
        val lsRes = Ls.execute(VariableEnvironment(), listOf(dirPath.toString()), testIO)
        testIO.checkStreams("$filename1\n$filename2\n", "")
        Assertions.assertEquals(ReturnCode.success, lsRes)
        dirPath.toFile().deleteOnExit()
        filename1.toFile().deleteOnExit()
        filename2.toFile().deleteOnExit()
    }

    @Test
    fun `test ls with many arguments`() {
        val testIO = IO("some input")
        val lsRes = Ls.execute(VariableEnvironment(), listOf("src", "test"), testIO)
        testIO.checkStreams("", "Too many arguments\n")
        Assertions.assertEquals(ReturnCode(1), lsRes)
    }

    @Test
    fun `test ls empty directory`() {
        val dirPath: Path  = Files.createTempDirectory("tmpDir")
        val testIO = IO("some input")
        val lsRes = Ls.execute(VariableEnvironment(), listOf(dirPath.toString()), testIO)
        testIO.checkStreams("", "")
        Assertions.assertEquals(ReturnCode.success, lsRes)
        dirPath.toFile().deleteOnExit()
    }

    @Test
    fun `test ls with path`() {
        val dirPath1: Path  = Files.createTempDirectory("tmpDir")
        val dirPath2: Path  = Files.createTempDirectory(dirPath1, "tmpDir")
        val filename1 = Files.createTempFile(dirPath2, "file1", ".txt").fileName
        val filename2 = Files.createTempFile(dirPath2, "file2", ".txt").fileName
        val testIO = IO("some input")
        val lsRes = Ls.execute(VariableEnvironment(), listOf(dirPath2.toString()), testIO)
        testIO.checkStreams("$filename1\n$filename2\n", "")
        Assertions.assertEquals(ReturnCode.success, lsRes)
        dirPath1.toFile().deleteOnExit()
        dirPath2.toFile().deleteOnExit()
        filename1.toFile().deleteOnExit()
        filename2.toFile().deleteOnExit()
    }
}
