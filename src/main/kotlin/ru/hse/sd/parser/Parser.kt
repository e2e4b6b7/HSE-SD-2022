package ru.hse.sd.parser

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.TerminalNode
import ru.hse.sd.merge
import ru.hse.sd.parser.antlr.SShellLex
import ru.hse.sd.parser.antlr.SShellParse

class Parser {
    fun parse(code: String): List<Statement> {
        val charStream = CharStreams.fromString(code)
        val lexer = SShellLex(charStream)
        val tokensStream = CommonTokenStream(lexer)
        val parser = SShellParse(tokensStream)
        val prog = parser.prog()
        return prog.stmt().map(this::transformStmt)
    }

    private fun transformStmt(stmt: SShellParse.StmtContext): Statement {
        return Statement(stmt.task().map(this::transformTask))
    }

    private fun transformTask(task: SShellParse.TaskContext): Task {
        task.assn()?.let {
            return transformAssn(it)
        }
        task.cmd()?.let {
            return transformCmd(it)
        }
        error("Unknown task option")
    }

    private fun transformCmd(cmd: SShellParse.CmdContext): CommandRun {
        val values = mutableListOf<String>()
        val lastValue = mutableListOf<String>()
        for (child in cmd.children) {
            when (child) {
                is TerminalNode -> {
                    when (child.symbol.type) {
                        SShellParse.String -> {
                            lastValue.add(child.text)
                        }
                        SShellParse.Whitespace -> {
                            if (lastValue.isNotEmpty()) {
                                values.add(lastValue.merge())
                                lastValue.clear()
                            }
                        }
                    }
                }
                is SShellParse.QuoteContext -> {
                    lastValue.add(transformQuote(child))
                }
            }
        }
        if (lastValue.isNotEmpty()) {
            values.add(lastValue.merge())
        }
        val commandName = values[0]
        values.removeAt(0)
        return CommandRun(commandName, values)
    }

    private fun transformAssn(assn: SShellParse.AssnContext): Assignment {
        val varName = assn.Assn().text
        val values = mutableListOf<String>()
        val childIterator = assn.children.iterator()
        do {
            val child = childIterator.next()
        } while (child is TerminalNode && child.symbol.type != SShellParse.Assn)
        for (child in childIterator) {
            when (child) {
                is TerminalNode -> {
                    if (child.symbol.type == SShellParse.String) {
                        values.add(child.text)
                    }
                }
                is SShellParse.QuoteContext -> {
                    values.add(transformQuote(child))
                }
            }
        }
        return Assignment(varName, values.merge())
    }

    private fun transformQuote(quote: SShellParse.QuoteContext): String {
        val values = mutableListOf<String>()
        for (child in quote.children) {
            check(child is TerminalNode) { "Unknown node for quote node" }
            if (child.symbol.type == SShellParse.String) {
                values.add(child.text)
            }
        }
        return values.merge()
    }
}
