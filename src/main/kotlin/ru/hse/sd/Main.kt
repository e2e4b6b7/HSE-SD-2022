package ru.hse.sd

import ru.hse.sd.cmd.*
import ru.hse.sd.env.VariableEnvironment
import ru.hse.sd.env.cmd.*
import ru.hse.sd.parser.Parser
import java.io.File
import java.io.InputStream

fun getExternalCommandEnvironment(): CommandEnvironment {
    return ExternalCommandEnvironment(System.getenv("PATH").split(File.pathSeparatorChar))
}

fun getBuiltinCommandEnvironment(): CommandEnvironment {
    val cmdEnv = BuiltinCommandEnvironment()
    cmdEnv.registerCommand("exit", ExitCommand)
    cmdEnv.registerCommand("echo", Echo)
    cmdEnv.registerCommand("pwd", Pwd)
    cmdEnv.registerCommand("cat", Cat)
    return cmdEnv
}

fun getCommandEnvironments(): List<CommandEnvironment> {
    return listOf(
        getBuiltinCommandEnvironment(),
        getExternalCommandEnvironment()
    )
}

fun getVariableEnvironment() = VariableEnvironment()

fun getUserInteraction() = UserInteraction(System.`in`, System.out)

fun getParser() = Parser()

fun getIO() = IO(InputStream.nullInputStream(), System.out, System.err)

fun main() {
    SShell(
        getCommandEnvironments(),
        getVariableEnvironment(),
        getUserInteraction(),
        getParser(),
        getIO()
    ).start()
}
