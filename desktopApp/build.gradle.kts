import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(project.dependencies.platform(libs.koin.bom))

    implementation(libs.koin.core)
    implementation(project(":shared"))

//    implementation(compose.desktop.currentOs)

    implementation(libs.compose.desktop.linuxX64)
    implementation(libs.compose.desktop.winX64)
    implementation(libs.compose.desktop.macosX64)
    implementation(libs.compose.desktop.macosArm64)

    implementation(libs.kotlinx.coroutinesSwing)

    implementation(libs.compose.uiToolingPreview)
}

compose.desktop {
    application {
        mainClass = "io.vladyslavvua.shorekeeper.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "io.vladyslavvua.shorekeeper"
            packageVersion = "1.0.0"
        }
    }
}