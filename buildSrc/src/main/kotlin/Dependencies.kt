const val kotlinVersion = "1.8.20"

object CosyncJWT {
	const val versionCode = 5
	const val versionName = "0.0.5"
	const val namespace = "com.cosync.cosyncjwt"
}

object BuildPlugins {
	object Versions {
		const val gradle = "8.0.0"
	}

	const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.gradle}"
	const val androidLibrary = "com.android.library"
	const val kotlinAndroid = "android"
	const val kotlinKapt = "kapt"
	const val mavenPublish = "maven-publish"
	const val dokka = "org.jetbrains.dokka"
	const val orchid = "com.eden.orchidPlugin"
}

object Android {
	const val minSdk = 21
	const val compileSdk = 33

	object Progaurd {
		const val consumeFile = "consumer-rules.pro"
		const val optimizeFile = "proguard-android-optimize.txt"
		const val rulesFile = "proguard-rules.pro"
	}
}

object Libraries {
	private object Versions {
		const val ktx = "1.10.0"
		const val okhttp3 = "5.0.0-alpha.11"
		const val retrofit = "2.9.0"
		const val dagger = "2.45"
		const val gson = "2.10.1"
		const val guava = "31.1-jre"
	}

	const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
	const val ktxCore = "androidx.core:core-ktx:${Versions.ktx}"
	const val okhttp3 = "com.squareup.okhttp3:okhttp:${Versions.okhttp3}"
	const val okhttp3Logging = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp3}"
	const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
	const val retrofitGsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
	const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
	const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
	const val gson = "com.google.code.gson:gson:${Versions.gson}"
	const val guava = "com.google.guava:guava:${Versions.guava}"
}

object TestLibraries {
	private object Versions {
		const val junit = "4.13.2"
		const val testExt = "1.2.0-alpha01"
		const val espresso = "3.6.0-alpha01"
	}
	const val junit = "junit:junit:${Versions.junit}"
	const val testExt = "androidx.test.ext:junit:${Versions.testExt}"
	const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
	const val testRunner = "androidx.test.runner.AndroidJUnitRunner"
}

object Publishing {
	const val groupId = "com.cosync"
	const val artifactId = "cosyncjwt"
	const val version = CosyncJWT.versionName
}