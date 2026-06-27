package com.example.tanuhaiassessment.core.checksum

import java.io.File

interface ChecksumValidator {

    suspend fun validate(
        file: File,
        expectedChecksum: String
    ): Boolean
}