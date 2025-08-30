// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        // 增加MobSDK插件配置
        classpath("com.mob.sdk:MobSDK2:+")
        // 增加google services插件配置，用于集成FCM，不集成FCM可不配置
        classpath("com.google.gms:google-services:4.3.14")
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.mob.sdk")version("+")apply false
}