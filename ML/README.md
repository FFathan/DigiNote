# Handwritten OCR using CNN, Bi-RNN, CTC in TensorFlow

This folder repository contains the code for training the model in .ipynb format and the trained model in .h5 format. The model is trained Google Colab. This folder also contains all the necessary files for running the model in .py format for pre-processing purpose.

## Introduction

This project is a simple example of using CNN, Bi-RNN, CTC in TensorFlow for OCR.
The way it works is first, we use CNN to extract the feature of the image and then use Bi-RNN to recognize the text from the feature. The model is trained end-to-end with CTC loss function. Finally, CTC decoder is used to decode the output of the Bi-RNN.

## Requirements

- Python 3.5
- TensorFlow 2.0
- OpenCV 3.2.0
- Numpy
- Matplotlib

All the requirements can be installed by running `pip install -r requirements.txt`

## Dataset

The dataset used in this project is the [Handwritting Recognition] (https://www.kaggle.com/landlord/handwriting-recognition) dataset from Kaggle. The dataset contains 206,799 first names and 207,024 surnames in total. The data was divided into a training set (331,059), testing set (41,382), and validation set (41,382) respectively.

<p align="center"><img src ="doc/dataset_preview.png?raw=true" height="212" alt="Dataset Preview" /></p>

Words images contains characters A-Z in uppercase, and some special characters like space, dash and aphostrope. Images are 256 pixels in width and 64 pixels in height, they are grayscale and anti-aliased. The images are centered in the middle of the image.

Before begin the training process, datasets are pre-processed by removing and renaming the empty and unreadable column in CSV file. (details can be found in the notebook).

## Training

Model trained on Google Colab with GCP VM. Images samples used are 30000 training images and 3000 validation images (only 10% of total datasets, because of limited resource). Training process took 4 hours to complete. The model is trained with 60 epochs and batch size of 128. The model is trained end-to-end with CTC loss function. The model is trained with Adam optimizer with learning rate of 0.001.
Training can be done much faster with GPU support.

## Model Architecture

The model architecture is shown in the figure below. The model consists of 3 CNN layers, 2 Bi-RNN layers, and 1 CTC layer. The input of the model is a 64x256 grayscale image. The output of the model is a sequence of characters.

<p align="center"><img src ="doc/model_structure.png?raw=true" height="340" alt="Model Structure" /></p>

## Results

The model achieved 76% accuracy on character level and 60% accuracy on word level. The model is trained with only 10% of the total datasets, so the accuracy can be improved by training with more data. 

As for now the model is only trained with uppercase characters and some special characters. The model can be improved by training with more variety of words, more characters, and more languages. The recognition is dependent of contour detection, it's not optimized for detecting words with different font style and paper or background with lines.

## Methods for extracting text from image (app implementation)

There are few steps before we can extract the text from the image. First, we need to pre-process the image. The pre-processing steps are as follows:

<p align="center"><img src ="doc/processing-image.png?raw=true" height="400" alt="Dataset Preview" /></p>

1. Cropping the image to detect the page of the image. (Note : you have to capture the image in full page, as it need to detect page later for cropping)
2. Apply adaptive thresholding to the image.
3. Detect the words by bounding the words in boxes.
4. Line segmentation by sorting the words from left to right, top to bottom.
5. Pre-processing the words by resizing the image to 64x256, and convert the image to grayscale and apply some padding.
6. Finally, feed the pre-processed image into the model to begin the recognition process.

## What can be improved
NOTE: If during testing encountered error or fail, thats because the pre-processing image functions (ex: contour detect, bound boxing, thresholding filter) are not very good. It still need some work to get perfect image before it feeded to the model. The model it self works fine. Just need better pre-processing functions. So if captured image has another object besides the text, it will likely detect it as contour and model would not be able to read it. (gives an error exception).

- Train the model with more data, bigger dataset.
- Use a dataset with more variety of words, more characters, and more languages.
- Use better pre-processing function for feeding the data into the model. (Using batch sizing instead of feeding the data one by one).
- Use better word segmentation function to detect the words in the image.
- Use better thresholding filter function to detect the words in the image.
- Use better bounding box function to detect the words in the image.

## References for code, dataset, methods approach

- [Handwritten Text Recognition using TensorFlow](https://towardsdatascience.com/handwritten-text-recognition-using-tensorflow-2-0-f4352b7afe16)
- [Handwritten Text Recognition using Deep Learning](https://towardsdatascience.com/handwritten-text-recognition-using-deep-learning-8a782c93c2fa)
- [Text Recognition With TensorFlow and CTC network](https://pylessons.com/ctc-text-recognition)
- [Handwritting characters database](https://github.com/sueiras/handwritting_characters_database)
- [Handwritting OCR](https://github.com/Breta01/handwriting-ocr)
- [Handwritting Text Recognition](https://medium.com/@mohini.1893/handwriting-text-recognition-236b33c5caa4)
