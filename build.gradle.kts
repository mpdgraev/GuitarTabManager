import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    application
}

group = "me.mike"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
    implementation("com.github.kotlin-inquirer:kotlin-inquirer:v0.0.2-alpha") // interactive cli
    implementation("com.github.ajalt.clikt:clikt:3.1.0") // basic cli stuff
    implementation("me.xdrop:fuzzywuzzy:1.3.1")
}

kotlin {
    sourceSets.all {
        languageSettings.enableLanguageFeature("InlineClasses")
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClassName = "MainKt"
}