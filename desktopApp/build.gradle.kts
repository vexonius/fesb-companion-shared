import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(projects.shared)

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)

    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ui.backhandler)
    implementation(libs.connectivity.core)
    implementation(libs.androidx.room.runtime)
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.androidx.sqlite.bundled)
}


compose.desktop {
    application {
        mainClass = "dev.etino.fcshared.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "dev.etino.fcshared"
            packageVersion = "1.0.0"
        }
    }
}
