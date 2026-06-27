package com.example.tanuhaiassessment.core.checksum

import java.io.File
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChecksumValidatorImpl @Inject constructor()
    : ChecksumValidator{
    override suspend fun validate(
        file: File,
        expectedChecksum: String
    ): Boolean {
        val actualChecksum =
            sha256(file)

        return actualChecksum.equals(
            expectedChecksum,
            ignoreCase = true
        )
    }

}


    private fun sha256(file: File): String{
        val digest =
            MessageDigest.getInstance(
                "SHA-256"
            )

        file.inputStream().use { input ->

            val buffer =
                ByteArray(8192)

            var read: Int

            while (
                input.read(buffer)
                    .also {
                        read = it
                    } != -1
            ) {

                digest.update(
                    buffer,
                    0,
                    read
                )
            }
        }

        return digest.digest()
            .joinToString("") {
                "%02x".format(it)
    }
}