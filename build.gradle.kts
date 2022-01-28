import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    jacoco
    antlr
}

group = "ru.hse.sd"

repositories {
    mavenCentral()
}

dependencies {
    antlr("org.antlr", "antlr4", "4.9.3")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
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
    arguments.add("-package")
    arguments.add("ru.hse.sd.parser.antlr")
    outputDirectory = File("$buildDir/generated-src/antlr/main/ru/hse/sd/parser/antlr/")
}
