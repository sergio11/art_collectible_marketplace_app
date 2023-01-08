package com.dreamsoftware.artcollectibles.secretsLib

class SecretsVaultAPI {

    external fun getMasterPassword(): String

    external fun getMasterSalt(): String

    companion object {
        // Used to load the 'secretsLib' library on application startup.
        init {
            System.loadLibrary("secretsLib")
        }
    }
}