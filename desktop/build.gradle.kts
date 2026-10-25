import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

group = "com.music.bitchord"
version = providers.gradleProperty("bitchord.version")
    .orElse(rootProject.extra["bitchordVersionName"].toString())
    .get()

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(project(":sharedUi"))
}

compose.desktop {
    application {
        mainClass = "com.music.bitchord.desktop.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Deb)
            packageName = "BitChord"
            packageVersion = version.toString().substringBefore('-').ifBlank { "1.0.0" }
            description = "BitChord music player"
            vendor = "BitChord"
            linux {
                packageName = "bitchord"
            }
        }
    }
}
