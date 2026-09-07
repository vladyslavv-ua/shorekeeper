
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.ksp)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.koin.compiler)
    alias(libs.plugins.serialization)
    alias(libs.plugins.androidx.room3)
}

room3 {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    compilerOptions{
        freeCompilerArgs.add("-Xexplicit-backing-fields")
    }
    jvm()
    
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.jooq)

            implementation(ktorLibs.server.core)
            implementation(ktorLibs.server.netty)

            implementation(libs.androidx.room3.runtime)
            implementation(libs.androidx.sqlite.bundled)

            implementation(libs.serialization.json)

            implementation(libs.datastore)
            implementation(libs.datastore.prefs)

            implementation(libs.compose.navigation)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.annotations)
            implementation(libs.koin.coroutines)

            implementation(libs.jcefmaven)


            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    add("kspJvm", libs.androidx.room3.compiler)
}
