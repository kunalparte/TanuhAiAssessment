package com.example.tanuhaiassessment.core.inference.text

import javax.inject.Inject

class TextPreProcessor @Inject constructor() {

    fun preprocess(
        text: String
    ): String {

        return text
            .trim()
            .lowercase()
    }
}