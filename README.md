# DataStoreManager


How to

To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

    gradle
    maven
    sbt
    leiningen

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.nguyenkhiem7789:DataStoreManager:0.1.0'
	}

## Usage

create new keystore:
	
	KeyStoreManager.createNewKeys(this, aliasText.text.toString())

encrypt:

	val encrypted = KeyStoreManager.encryptString(applicationContext, alias, startText.text.toString())
	
decrypt:

	val decrypted = KeyStoreManager.decryptString(applicationContext, alias, encryptedText.text.toString())
