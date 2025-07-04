pluginManagement {
    plugins {
        id("com.android.application") version "8.11.0" apply false
        id("org.jetbrains.kotlin.android") version "2.4.0" apply false
        id("com.google.gms.google-services") version "4.4.2"

    }
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Toko"
include(":app")
