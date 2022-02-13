import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm") version "1.6.0"
    jacoco
    antlr
    application
}

group = "ru.hse.sd"

repositories {
    mavenCentral()
}

dependencies {
    antlr("org.antlr", "antlr4", "4.9.3")
    testImplementation(kotlin("test"))
    testImplementation("org.apache.commons", "commons-lang3", "3.12.0")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        // set options for log level LIFECYCLE
        events = setOf(
            TestLogEvent.FAILED,
            TestLogEvent.SKIPPED,
            TestLogEvent.STANDARD_OUT
        )
        exceptionFormat = TestExceptionFormat.SHORT
        showExceptions = true
        showCauses = true
        showStackTraces = true

        // set options for log level DEBUG and INFO
        debug {
            events = setOf(
                TestLogEvent.FAILED,
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED,
                TestLogEvent.STANDARD_OUT
            )
            exceptionFormat = TestExceptionFormat.FULL
        }
        info.events = debug.events
        info.exceptionFormat = debug.exceptionFormat
        addTestListener(
            object : TestListener {
                override fun beforeSuite(suite: TestDescriptor) = Unit
                override fun beforeTest(testDescriptor: TestDescriptor) = Unit
                override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) = Unit
                override fun afterSuite(desc: TestDescriptor, result: TestResult) {
                    if (desc.parent == null) {
                        val output = "Results: ${result.resultType} " +
                            "(${result.testCount} tests," +
                            " ${result.successfulTestCount} passed," +
                            " ${result.failedTestCount} failed," +
                            " ${result.skippedTestCount} skipped)" +
                            " in ${(result.endTime - result.startTime) / 1000.0} seconds"
                        val startItem = "|  "
                        val endItem = "  |"
                        val repeatLength = startItem.length + output.length + endItem.length
                        println(
                            "\n" + "-".repeat(repeatLength) + "\n" +
                            startItem + output + endItem + "\n" +
                            "-".repeat(repeatLength)
                        )
                    }
                }
            }
        )
    }
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    reports {
        html.required.set(true)
        xml.required.set(false)
        csv.required.set(false)
    }
    dependsOn(tasks.test)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
    dependsOn("generateGrammarSource")
}

tasks.generateGrammarSource {
    arguments.add("-no-listener")
    arguments.add("-package")
    arguments.add("ru.hse.sd.parser.antlr")
    outputDirectory = File("$buildDir/generated-src/antlr/main/ru/hse/sd/parser/antlr/")
}

application {
    mainClass.set("ru.hse.sd.MainKt")
}

tasks.register("SShellFatJar", type = Jar::class) {
    archiveBaseName.set(project.name)
    manifest {
        attributes["Main-Class"] = "ru.hse.sd.MainKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks["jar"] as CopySpec)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    dependsOn("build")
}
