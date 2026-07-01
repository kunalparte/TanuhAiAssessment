package com.example.tanuhaiassessment.core.text

import android.content.Context
import com.example.tanuhaiassessment.core.inference.text.TokenizedInput
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BertTokenizer @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val MAX_SEQUENCE_LENGTH = 128
        private const val CLS_TOKEN = "[CLS]"
        private const val SEP_TOKEN = "[SEP]"
        private const val PAD_TOKEN = "[PAD]"
        private const val UNK_TOKEN = "[UNK]"
    }

    private val vocab: Map<String, Long> by lazy {
        context.assets.open("vocab.txt")
            .bufferedReader()
            .readLines()
            .mapIndexed { index, token ->
                token.trim() to index.toLong()
            }
            .toMap()
    }

    fun encode(text: String): TokenizedInput {
        val tokens = mutableListOf<String>()

        tokens.add(CLS_TOKEN)

        basicTokenize(text).forEach { word ->
            tokens.addAll(wordPieceTokenize(word))
        }

        tokens.add(SEP_TOKEN)

        val limitedTokens = tokens.take(MAX_SEQUENCE_LENGTH)

        val inputIds = LongArray(MAX_SEQUENCE_LENGTH) {
            vocab[PAD_TOKEN] ?: 0L
        }

        val attentionMask = LongArray(MAX_SEQUENCE_LENGTH)
        val tokenTypeIds = LongArray(MAX_SEQUENCE_LENGTH)

        limitedTokens.forEachIndexed { index, token ->
            inputIds[index] = vocab[token]
                ?: vocab[UNK_TOKEN]
                        ?: 100L

            attentionMask[index] = 1L
            tokenTypeIds[index] = 0L
        }

        return TokenizedInput(
            inputIds = inputIds,
            attentionMask = attentionMask,
            tokenTypeIds = tokenTypeIds
        )
    }

    private fun basicTokenize(text: String): List<String> {
        return text
            .lowercase()
            .replace(Regex("""([.,!?;:()"'/-])"""), " $1 ")
            .replace(Regex("""\s+"""), " ")
            .trim()
            .split(" ")
            .filter { it.isNotBlank() }
    }

    private fun wordPieceTokenize(word: String): List<String> {
        if (vocab.containsKey(word)) {
            return listOf(word)
        }

        val tokens = mutableListOf<String>()
        var start = 0

        while (start < word.length) {
            var end = word.length
            var currentSubstr: String? = null

            while (start < end) {
                val substr = if (start == 0) {
                    word.substring(start, end)
                } else {
                    "##${word.substring(start, end)}"
                }

                if (vocab.containsKey(substr)) {
                    currentSubstr = substr
                    break
                }

                end--
            }

            if (currentSubstr == null) {
                return listOf(UNK_TOKEN)
            }

            tokens.add(currentSubstr)
            start = end
        }

        return tokens
    }
}