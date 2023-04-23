import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
	id(BuildPlugins.androidLibrary)
	kotlin(BuildPlugins.kotlinAndroid)
	kotlin(BuildPlugins.kotlinKapt)
	id(BuildPlugins.mavenPublish)
}

android {
	namespace = CosyncJWT.namespace
	compileSdk = Android.compileSdk

	defaultConfig {
		minSdk = Android.minSdk
		testInstrumentationRunner = TestLibraries.testRunner
		consumerProguardFiles(Android.Progaurd.consumeFile)
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
			proguardFiles(getDefaultProguardFile(Android.Progaurd.optimizeFile), Android.Progaurd.rulesFile)
		}
	}
}

dependencies {
	implementation(Libraries.kotlinStdLib)

	// Unit Testing
	testImplementation(TestLibraries.junit)
	androidTestImplementation(TestLibraries.testExt)
	androidTestImplementation(TestLibraries.espresso)

	// KTX
	implementation(Libraries.ktxCore)

	// OkHttp
	implementation(Libraries.okhttp3)
	implementation(Libraries.okhttp3Logging)

	// Retrofit
	implementation(Libraries.retrofit)
	implementation(Libraries.retrofitGsonConverter)

	// Dagger
	implementation(Libraries.dagger)
	kapt(Libraries.daggerCompiler)

	// Gson
	implementation(Libraries.gson)

	// Guava
	implementation(Libraries.guava)
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
				groupId = Publishing.groupId
				artifactId = Publishing.artifactId
				version = Publishing.version

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