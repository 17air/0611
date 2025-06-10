package com.example.cardify

class SimpleTokenizer(private val vocab: List<String>) {
    private val tokenToId = vocab.withIndex().associate { it.value to it.index }

    fun tokenize(text: String): IntArray {
        val tokens = text.split(" ")
        return tokens.map { tokenToId[token] ?: 0 }.toIntArray()
    }
}
