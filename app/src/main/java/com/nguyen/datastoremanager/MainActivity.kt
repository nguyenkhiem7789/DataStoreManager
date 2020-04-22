package com.nguyen.datastoremanager

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.nguyen.datastore.KeyStoreManager
import com.nguyen.datastoremanager.DataStoreApplication.Companion.prefsManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_header.*
import java.security.KeyStoreException

class MainActivity : AppCompatActivity() {

    companion object {
        const val ENCRYPTED_KEY = "encrypted_key"
    }

    lateinit var adapter: KeyRecyclerAdapter

    var list = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        generateKeyPair.setOnClickListener {
            if(TextUtils.isEmpty(aliasText.text)) {
                Toast.makeText(applicationContext, getString(R.string.require_alias), Toast.LENGTH_SHORT).show()
            } else {
                // create new keystore
                KeyStoreManager.createNewKeys(this, aliasText.text.toString())
                list.add(aliasText.text.toString())
                adapter.notifyDataSetChanged()
            }
        }

        list = KeyStoreManager.getListKeyAliases()
        adapter = KeyRecyclerAdapter(list, object : OnClickItemListener {
            override fun delete(alias: String) {
                // delete key alias
                deleteAlias(alias)
            }

            override fun encrypt(alias: String) {
                if(TextUtils.isEmpty(startText.text)) {
                    Toast.makeText(applicationContext, getString(R.string.require_start_text), Toast.LENGTH_SHORT).show()
                    return
                }
                // encrypt
                val encrypted = KeyStoreManager.encryptString(applicationContext, alias, startText.text.toString())
                encryptedText.setText(encrypted)
            }

            override fun decrypt(alias: String) {
                if(TextUtils.isEmpty(encryptedText.text)) {
                    Toast.makeText(applicationContext, getString(R.string.require_encrypt_text), Toast.LENGTH_SHORT).show()
                    return
                }
                val decrypted = KeyStoreManager.decryptString(applicationContext, alias, encryptedText.text.toString())
                decryptedText.setText(decrypted)
            }
        })
        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = adapter

        saveToSharedPreferences.setOnClickListener {
            prefsManager.savePref(ENCRYPTED_KEY, encryptedText.text.toString())
            Toast.makeText(applicationContext, getString(R.string.save_to_preferences), Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteAlias(alias: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Delete Key")
            .setMessage("Do you want to delete the key \"$alias\" from the keystore?")
            .setPositiveButton("Yes") { dialog, which ->
                try {
                    KeyStoreManager.deleteKey(this, alias)
                    list.remove(alias)
                    adapter.notifyDataSetChanged()
                } catch (e: KeyStoreException) {
                    Toast.makeText(this, "Exception " + e.message + " occured", Toast.LENGTH_LONG).show()
                    Log.e(KeyStoreManager.TAG, Log.getStackTraceString(e))
                }
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, which -> dialog.dismiss() }
            .create()
        alertDialog.show()
    }
}
