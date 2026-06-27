package com.example.tanuhaiassessment.core.inference.text

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MobileBertTokenizer@Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    suspend fun tokenize(
        text: String
    ): IntArray {

        // tokenize

        return IntArray(128)
    }
}