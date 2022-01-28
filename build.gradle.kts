import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
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
