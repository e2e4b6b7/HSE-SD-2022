package ru.hse.sd.parser

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.TerminalNode
import ru.hse.sd.merge
import ru.hse.sd.parser.antlr.SShellLex
import ru.hse.sd.parser.antlr.SShellParse

/** `SShell` parser */
class Parser {
    /**
     * Transform `SShell` code to list of pre-statements.
     *
     * [code] `SShell` code
     */
    fun parse(code: String): List<PreStatement> {
        val charStream = CharStreams.fromString(code)
        val lexer = SShellLex(charStream)
        val tokensStream = CommonTokenStream(lexer)
        val parser = SShellParse(tokensStream)
        return parser.prog().stmt()
    }

    /**
     * Substitutes variables in pre-statement and transform it to finalized statement.
     */
    fun subst(preStatement: PreStatement, variables: Map<String, String>): Statement {
        return transformStmt(preStatement, variables)
    }

    private fun transformStmt(stmt: SShellParse.StmtContext, variables: Map<String, String>): Statement {
        return Statement(stmt.task().mapNotNull { transformTask(it, variables) })
    }

    private fun transformTask(task: SShellParse.TaskContext, variables: Map<String, String>): Task? {
        task.assn()?.let {
            return transformAssn(it, variables)
        }
        task.cmd()?.let {
            return transformCmd(it, variables)
        }
        return null
    }

    private fun transformCmd(cmd: SShellParse.CmdContext, variables: Map<String, String>): CommandRun? {
        val children = cmd.children ?: return null

        val values = mutableListOf<String>()
        val lastValue = mutableListOf<String>()
        for (child in children) {
            when (child) {
                is TerminalNode -> {
                    when (child.symbol.type) {
                        SShellParse.Whitespace -> {
                            if (lastValue.isNotEmpty()) {
                                values.add(lastValue.merge())
                                lastValue.clear()
                            }
                        }
                        else -> lastValue.add(text(child, variables))
                    }
                }
                is SShellParse.QuoteContext -> {
                    lastValue.add(transformQuote(child, variables))
                }
            }
        }
        if (lastValue.isNotEmpty()) {
            values.add(lastValue.merge())
        }

        if (values.isEmpty()) return null

        val commandName = values[0]
        values.removeAt(0)
        return CommandRun(commandName, values)
    }

    private fun transformAssn(assn: SShellParse.AssnContext, variables: Map<String, String>): Assignment {
        val varName = assn.Assn().text
        val values = mutableListOf<String>()
        val childIterator = assn.children.iterator()
        do {
            val child = childIterator.next()
        } while (child is TerminalNode && child.symbol.type != SShellParse.Assn)
        for (child in childIterator) {
            when (child) {
                is TerminalNode ->
                    values.add(text(child, variables))
                is SShellParse.QuoteContext ->
                    values.add(transformQuote(child, variables))
            }
        }
        return Assignment(varName, values.merge())
    }

    private fun transformQuote(quote: SShellParse.QuoteContext, variables: Map<String, String>): String {
        val values = mutableListOf<String>()
        for (child in quote.children.subList(1, quote.children.size - 1)) {
            check(child is TerminalNode) { "Unknown node for quote node" }
            values.add(text(child, variables))
        }
        return values.merge()
    }

    private fun text(node: TerminalNode, variables: Map<String, String>): String {
        return when (node.symbol.type) {
            SShellParse.String -> {
                node.text
            }
            SShellParse.Subst -> {
                variables[node.text] ?: ""
            }
            else -> error("Invalid token")
        }
    }
}
