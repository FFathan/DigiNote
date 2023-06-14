from flask import Flask, request, jsonify
import sys
import numpy as np
import tensorflow as tf
import cv2
from keras import backend as K

sys.path.append('./function')  # Replace '/path/to/folder' with the actual folder path
from function import page, process, func, dictionary

app = Flask(__name__)

# Load model
MODEL_PATH = 'model/model.h5'  # replace with the actual model path
model = tf.keras.models.load_model(MODEL_PATH)

@app.route('/predict', methods=['POST'])
def predict():
    # Get the image file from the request
    image_file = request.files['image']

    # Read image using cv2 as RGB
    image = cv2.cvtColor(cv2.imdecode(np.fromstring(image_file.read(), np.uint8), cv2.IMREAD_COLOR),
                         cv2.COLOR_BGR2RGB)

    # Crop the image to detect page
    crop = page.detection(image)

    # Preprocess image
    filtered = process.magic_color_thresholding(crop)

    # Preprocess image for word detection
    boxes = process.detection(filtered)
    lines = process.sort_words(boxes)

    # Initialize paragraph string
    paragraph = ""

    # Save each cropped word from boxing image
    for line_index, line in enumerate(lines):
        for word_index, word_boxes in enumerate(line):
            word_image = filtered[word_boxes[1]:word_boxes[3], word_boxes[0]:word_boxes[2]]

            # Preprocess the image
            word_image = func.preprocess2(word_image)

            # Normalize the pixel values to the range of 0-1
            word_image = word_image / 255.0

            # Predict the word
            pred = model.predict(word_image.reshape(1, 256, 64, 1))
            decoded = K.get_value(K.ctc_decode(pred, input_length=np.ones(pred.shape[0]) * pred.shape[1],
                                               greedy=True)[0][0])

            # Concatenate the predicted word to the paragraph string
            paragraph += dictionary.num_to_label(decoded[0]) + " "

    # Return the predicted paragraph
    return jsonify({'predicted_paragraph': paragraph.lower()})

if __name__ == '__main__':
    app.run()
