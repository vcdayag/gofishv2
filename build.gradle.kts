val appName = "GoFish"
val appLauncher = appName
val appPackaging = appName
val appModuleName = "io.github.p0lbang.gofish"
val appMainClass = "io.github.p0lbang.gofish.App"
val appVersion = "0.0.1"
val javaVersion = 17

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

application {
    mainModule.set(appModuleName)
    mainClass.set(appMainClass)
}

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.26.0"
}

repositories {
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

dependencies {
    implementation("com.google.guava:guava:31.1-jre")
    implementation("com.esotericsoftware:kryo:5.4.0")
    implementation("com.github.crykn:kryonet:2.22.8")
}

javafx {
    version = "$javaVersion"
    modules("javafx.controls", "javafx.fxml")
}

jlink {
    addOptions("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages")
    launcher {
        name = appName
        noConsole = true
    }
    /*jpackage {
        installerType = "app-image"
    }*/
}

