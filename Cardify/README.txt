# Cardify

Cardify is an AI-based business card management Android app that automatically extracts information from Korean and English business cards. It combines ML Kit OCR with a custom NER model trained separately.

## Features
- Select or capture an image of a business card.
- ML Kit OCR extracts text from the image.
- SimpleTokenizer converts text into IDs based on `vocab.txt`.
- `ner_model_int8.tflite` predicts named entities.
- Labels from `labels.txt` are mapped to tokens and shown on screen.

## File Structure
- `app/src/main/java/com/example/cardify/` – Kotlin sources.
- `app/src/main/assets/` – Place your `vocab.txt`, `labels.txt`, and `ner_model_int8.tflite` here.
- `app/src/main/res/` – Android resources.
- `build.gradle`, `settings.gradle` – Gradle build files.

Replace the placeholder assets with your actual model files to run.

## Build & Run
1. Open the project in Android Studio (latest stable version).
2. Ensure you have a device or emulator running with API level 24 or higher.
3. Build and run the `app` module.

On launch, use the **Select Image** button to pick a card image. The extracted entities appear below the image.

## Project Notes
This repository demonstrates the integration of the OCR → NER pipeline. The NER model training and conversion to TensorFlow Lite were performed separately (e.g., in Google Colab) using custom datasets. The Android code here focuses on connecting those pieces for on-device inference.
