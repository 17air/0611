package com.example.cardify

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.cardify.databinding.ActivityMainBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var classifier: NERClassifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        classifier = NERClassifier(this)

        val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val bitmap = uriToBitmap(uri)
                binding.imageView.setImageBitmap(bitmap)
                runOcr(bitmap)
            }
        }

        binding.buttonSelect.setOnClickListener {
            getImage.launch("image/*")
        }
    }

    private fun uriToBitmap(uri: android.net.Uri): Bitmap {
        contentResolver.openInputStream(uri).use { inputStream ->
            return BitmapFactory.decodeStream(inputStream)
        }
    }

    private fun runOcr(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val text = visionText.text
                val results = classifier.classify(text)
                val display = results.joinToString("\n") { "${it.first}: ${it.second}" }
                binding.textView.text = display
            }
            .addOnFailureListener { e ->
                binding.textView.text = e.message
            }
    }
}
