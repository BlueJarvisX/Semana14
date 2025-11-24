// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false

    // Plugin de servicios de Google para Firebase
    id("com.google.gms.google-services") version "4.4.4" apply false
}
