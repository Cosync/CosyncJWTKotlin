import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

buildscript {
	repositories {
		google()
		mavenCentral()
	}

	dependencies {
		classpath(BuildPlugins.androidGradlePlugin)
	}
}

plugins {
	kotlin("jvm") version kotlinVersion
	id("com.github.ben-manes.versions") version "0.46.0"
	`maven-publish`
}

allprojects {
	repositories {
		mavenLocal()

		google()
		mavenCentral()

		maven {
			name = "CosyncJWTKotlinSdk"
			url = uri("https://maven.pkg.github.com/Cosync/CosyncJWTKotlin")
			credentials {
				username = gradleLocalProperties(rootDir).getProperty("github.username")
				password = gradleLocalProperties(rootDir).getProperty("github.token")
			}
		}
	}
}

tasks.register("cleanAll").configure {
	delete("build", "buildSrc/build", "cosyncjwt/build")
}