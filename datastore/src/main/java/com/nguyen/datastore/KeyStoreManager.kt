package com.nguyen.datastore

import android.app.AlertDialog
import android.content.Context
import android.security.KeyPairGeneratorSpec
import android.util.Base64
import android.util.Log
import android.widget.Toast
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.PrivateKey
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.security.auth.x500.X500Principal
import kotlin.collections.ArrayList

interface KeyStoreManagerImpl {

    fun createNewKeys(context: Context, alias: String)

    fun getListKeyAliases(): ArrayList<String>

    fun deleteKey(context: Context, alias: String)

    fun encryptString(context: Context, alias: String, text: String): String?

    fun decryptString(context: Context, alias: String?, cipherText: String): String?
}

object KeyStoreManager: KeyStoreManagerImpl {

    const val TAG = "SimpleKeystoreApp"
    const val CIPHER_TYPE = "RSA/ECB/PKCS1Padding"
    const val CIPHER_PROVIDER = "AndroidKeyStoreBCWorkaround"

    val keyStore: KeyStore by lazy {
        KeyStore.getInstance("AndroidKeyStore")
    }

    init {
        keyStore.load(null)
    }

    override fun createNewKeys(context: Context, alias: String) {
        try {
            // Create new key if needed
            if(!keyStore.containsAlias(alias)) {
                val start = Calendar.getInstance()
                val end = Calendar.getInstance()
                end.add(Calendar.YEAR, 1)
                val spec = KeyPairGeneratorSpec.Builder(context)
                    .setAlias(alias)
                    .setSubject(X500Principal("CN=Sample Name, O=Android Authority"))
                    .setSerialNumber(BigInteger.ONE)
                    .setStartDate(start.time)
                    .setEndDate(end.time)
                    .build()
                val generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore")
                generator.initialize(spec)
                generator.generateKeyPair() //create KeyPair
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Exception " + e.message + " occured", Toast.LENGTH_LONG).show()
            Log.e(TAG, Log.getStackTraceString(e))
        }
    }

    override fun getListKeyAliases(): ArrayList<String> {
        var keyAliases = arrayListOf<String>()
        try {
            val aliases = keyStore.aliases()
            while (aliases.hasMoreElements()) {
                keyAliases.add(aliases.nextElement())
            }
        } catch (e: Exception) {
            Log.e(TAG, Log.getStackTraceString(e))
        }
        return keyAliases
    }

    override fun deleteKey(context: Context, alias: String) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Delete Key")
            .setMessage("Do you want to delete the key \"$alias\" from the keystore?")
            .setPositiveButton("Yes") { dialog, which ->
                try {
                    keyStore.deleteEntry(alias)
                } catch (e: KeyStoreException) {
                    Toast.makeText(context, "Exception " + e.message + " occured", Toast.LENGTH_LONG).show()
                    Log.e(TAG, Log.getStackTraceString(e))
                }
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, which -> dialog.dismiss() }
            .create()
        alertDialog.show()
    }

    override fun encryptString(context: Context, alias: String, text: String): String? {
        try {
            val privateKeyEntry = keyStore.getEntry(alias, null) as KeyStore.PrivateKeyEntry
            val publicKey = privateKeyEntry.certificate.publicKey
            Log.v(TAG, "public key = $publicKey")
            if (text.isEmpty()) {
                Toast.makeText(context, "Enter text in the 'Initial Text' widget", Toast.LENGTH_LONG).show()
                return null
            }
            val inCipher: Cipher = Cipher.getInstance(CIPHER_TYPE, CIPHER_PROVIDER)
            inCipher.init(Cipher.ENCRYPT_MODE, publicKey)
            val outputStream = ByteArrayOutputStream()
            val cipherOutputStream = CipherOutputStream(outputStream, inCipher)
            cipherOutputStream.write(text.toByteArray(charset("UTF-8")))
            cipherOutputStream.close()
            val vals = outputStream.toByteArray()
            return Base64.encodeToString(vals, Base64.DEFAULT)
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, "Exception " + e.message + " occured", Toast.LENGTH_LONG).show()
            Log.e(TAG, Log.getStackTraceString(e))
        }
        return null
    }

    override fun decryptString(context: Context, alias: String?, cipherText: String): String? {
        try {
            val privateKeyEntry = keyStore.getEntry(alias, null) as KeyStore.PrivateKeyEntry
            val privateKey = privateKeyEntry.privateKey as PrivateKey
            Log.v(TAG, "private key = $privateKey")
            val output: Cipher = Cipher.getInstance(CIPHER_TYPE, CIPHER_PROVIDER)
            output.init(Cipher.DECRYPT_MODE, privateKey)
            val cipherInputStream = CipherInputStream(
                ByteArrayInputStream(Base64.decode(cipherText, Base64.DEFAULT)), output
            )
            val values = ArrayList<Byte>()
            var nextByte: Int
            while (cipherInputStream.read().also { nextByte = it } != -1) {
                values.add(nextByte.toByte())
            }
            val bytes = ByteArray(values.size)
            for (i in bytes.indices) {
                bytes[i] = values[i]
            }
            return String(bytes, 0, bytes.size, charset("UTF-8"))
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, "Exception " + e.message + " occured", Toast.LENGTH_LONG).show()
            Log.e(TAG, Log.getStackTraceString(e))
        }
        return null
    }

}