pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        // 添加Mob Maven地址
        maven ("https://mvn.mob.com/android")

        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    //注意设置repositoriesMode为RepositoriesMode.PREFER_SETTINGS
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        // 添加Mob Maven地址
        maven ("https://mvn.mob.com/android")
    }
}

rootProject.name = "WeiBoxx"
include(":app")
 