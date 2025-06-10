package com.example.cardify

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class NERClassifier(context: Context) {
    private val labels = context.assets.open("labels.txt").bufferedReader().readLines()
    private val vocab = context.assets.open("vocab.txt").bufferedReader().readLines()
    private val interpreter: Interpreter
    private val tokenizer = SimpleTokenizer(vocab)

    init {
        val model = loadModelFile(context, "ner_model_int8.tflite")
        interpreter = Interpreter(model)
    }

    private fun loadModelFile(context: Context, modelFile: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelFile)
        FileInputStream(fileDescriptor.fileDescriptor).use { inputStream ->
            val fileChannel = inputStream.channel
            val startOffset = fileDescriptor.startOffset
            val declaredLength = fileDescriptor.declaredLength
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        }
    }

    fun classify(text: String): List<Pair<String, String>> {
        val inputIds = tokenizer.tokenize(text)
        val input = arrayOf(inputIds)
        val output = Array(1) { IntArray(inputIds.size) }
        interpreter.run(input, output)
        return output[0].mapIndexed { index, labelIndex ->
            val word = text.split(" ")[index]
            val label = labels.getOrElse(labelIndex) { "O" }
            word to label
        }
    }
}
