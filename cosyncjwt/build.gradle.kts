import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	id("kotlin-kapt")
	id("maven-publish")
}

android {
	namespace = "com.cosync.cosyncjwt"
	compileSdk = 33

	defaultConfig {
		minSdk = 21
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		consumerProguardFiles("consumer-rules.pro")
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}

	kotlinOptions {
		jvmTarget = JavaVersion.VERSION_17.toString()
	}

	buildFeatures {
		buildConfig = true
	}

	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.20")

	implementation("androidx.core:core-ktx:1.10.0")
	implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.11")
	implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.retrofit2:converter-gson:2.9.0")
	implementation("com.google.dagger:dagger:2.45")
	kapt("com.google.dagger:dagger-compiler:2.45")

	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.2.0-alpha01")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.6.0-alpha01")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
}

val sourcesJar by tasks.creating(Jar::class) {
	archiveClassifier.set("sources")
	from(android.sourceSets["main"].java.srcDirs)
}

afterEvaluate {
	publishing {
		repositories {
			maven {
				name = "GithubPackages"
				url = uri("https://maven.pkg.github.com/Cosync/CosyncJWTKotlin")
				credentials {
					username = gradleLocalProperties(rootDir).getProperty("github.username")
					password = gradleLocalProperties(rootDir).getProperty("github.token")
				}
			}
			maven {
				name = "LocalTest"
				url = uri("$buildDir/repo")
			}
		}

		publications {
			create<MavenPublication>("cosyncjwt") {
				groupId = "com.cosync"
				artifactId = "cosyncjwt"
				version = "0.1.2"

				artifact("$buildDir/outputs/aar/cosyncjwt-release.aar")
				artifact(sourcesJar)

				pom.withXml {
					val dependenciesNode = asNode().appendNode("dependencies")
					configurations.implementation.get().allDependencies.forEach {
						if (it.group != null && it.version != null &&
							it.name != "unspecified" && it.version != "unspecified") {
							dependenciesNode.appendNode("dependency").apply {
								appendNode("groupId", it.group)
								appendNode("artifactId", it.name)
								appendNode("version", it.version)
							}
						}
					}
				}
			}
		}
	}
}