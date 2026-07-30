package com.project.standard_login.data.local

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class SecureStorage(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply {
        load(null)
    }

    init {
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            generateSecretKey()
        }
    }

    private fun generateSecretKey() {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
        val spec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()

        keyGenerator.init(spec)
        keyGenerator.generateKey()
    }

    private fun getSecretKey(): SecretKey {
        return (keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry).secretKey
    }

    private fun encrypt(rawText: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(rawText.toByteArray(Charsets.UTF_8))
        val combinedBytes = iv + encryptedBytes
        return Base64.encodeToString(combinedBytes, Base64.DEFAULT)
    }

    private fun decrypt(encryptedTextBase64: String): String {
        val combinedBytes = Base64.decode(encryptedTextBase64, Base64.DEFAULT)
        val iv = combinedBytes.copyOfRange(0, IV_SIZE_BYTES)
        val encryptedBytes = combinedBytes.copyOfRange(IV_SIZE_BYTES, combinedBytes.size)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
        return String(cipher.doFinal(encryptedBytes), Charsets.UTF_8)
    }

    fun saveEmail(email: String) {
        val encrypted = encrypt(email)
        sharedPreferences.edit().putString(KEY_USER_EMAIL, encrypted).apply()
    }

    fun getEmail(): String? {
        val encrypted = sharedPreferences.getString(KEY_USER_EMAIL, null)
        return encrypted?.let {
            try { decrypt(it) } catch (e: Exception) { null }
        }
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "standard_login_secure_storage"
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val KEY_ALIAS = "StandardLoginKeyAlias"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val IV_SIZE_BYTES = 12
        private const val KEY_USER_EMAIL = "encrypted_user_email"
    }
}
