package ru.hse.sd

import ru.hse.sd.cmd.*
import ru.hse.sd.env.VariableEnvironment
import ru.hse.sd.env.cmd.*
import ru.hse.sd.parser.Parser
import java.io.File
import java.io.InputStream

internal fun getExternalCommandEnvironment(): CommandEnvironment {
    val paths = sequenceOf(".").plus(
        System.getenv("PATH").splitToSequence(File.pathSeparatorChar)
    ).toList()
    return ExternalCommandEnvironment(paths)
}

internal fun getBuiltinCommandEnvironment(): CommandEnvironment {
    val cmdEnv = BuiltinCommandEnvironment()
    cmdEnv.registerCommand("exit", ExitCommand)
    cmdEnv.registerCommand("echo", Echo)
    cmdEnv.registerCommand("pwd", Pwd)
    cmdEnv.registerCommand("cat", Cat)
    cmdEnv.registerCommand("wc", WordCount)
    return cmdEnv
}

internal fun getCommandEnvironments(): List<CommandEnvironment> {
    return listOf(
        getBuiltinCommandEnvironment(),
        getExternalCommandEnvironment()
    )
}

internal fun getVariableEnvironment() = VariableEnvironment()

internal fun getUserInteraction() = UserInteraction(System.`in`, System.out)

internal fun getParser() = Parser()

internal fun getIO() = IO(InputStream.nullInputStream(), System.out, System.err)

fun main() {
    SShell(
        getCommandEnvironments(),
        getVariableEnvironment(),
        getUserInteraction(),
        getParser(),
        getIO()
    ).start()
}
