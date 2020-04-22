package com.nguyen.datastoremanager

import android.app.Application
import com.nguyen.datastore.SharedPreferencesManager

class DataStoreApplication: Application() {
    init {
        instance = this
    }

    companion object {

        private var instance: DataStoreApplication? = null

        private fun applicationContext(): DataStoreApplication {
            return instance as DataStoreApplication
        }

        val prefsManager: SharedPreferencesManager by lazy {
            SharedPreferencesManager(applicationContext())
        }
    }
}