plugins {
    kotlin("multiplatform") version "1.7.21"
    id("maven-publish")
    id("signing")
}

group = "io.github.opendglab"
version = "2.0.1-alpha4"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(BOTH) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }
    nativeTarget.binaries.sharedLib {
        baseName = "libopendglab2"
    }

    
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }
}

val javadocJar = tasks.register("javadocJar", Jar::class.java) {
    archiveClassifier.set("javadoc")
}

val sonatypeUsername: String? = System.getenv("SONATYPE_USERNAME")
val sonatypePassword: String? = System.getenv("SONATYPE_PASSWORD")
publishing {
    publications {
        repositories {
            maven {
                name="oss"
                val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
                credentials {
                    username = sonatypeUsername
                    password = sonatypePassword
                }
            }
        }
    }
    publications {
        withType<MavenPublication> {
            artifact(javadocJar)
            pom {
                name.set("OpenDGLab Core")
                description.set("Open Source Library for DG-Lab Device")
                licenses {
                    license {
                        name.set("AGPL-v3")
                        url.set("https://opensource.org/license/agpl-v3/")
                    }
                }
                url.set("https://github.com/OpenDGLab/OpenDGLab-Core")
                issueManagement {
                    system.set("Github")
                    url.set("https://github.com/OpenDGLab/OpenDGLab-Core/issues")
                }
                scm {
                    connection.set("https://github.com/OpenDGLab/OpenDGLab-Core.git")
                    url.set("https://github.com/OpenDGLab/OpenDGLab-Core")
                }
                developers {
                    developer {
                        name.set("sound-reload")
                        email.set("edge4chaos+opendglab@gmail.com")
                    }
                }
            }
        }
    }
}

signing {
    setRequired {
        // signing is only required if the artifacts are to be published
        gradle.taskGraph.allTasks.any { it is PublishToMavenRepository }
    }
    useInMemoryPgpKeys(
        System.getenv("GPG_PRIVATE_KEY"),
        System.getenv("GPG_PRIVATE_PASSWORD")
    )
    sign(publishing.publications)
}

tasks.withType<PublishToMavenRepository> {
    dependsOn("signJvmPublication", "signJsPublication", "signKotlinMultiplatformPublication", "signNativePublication")
}