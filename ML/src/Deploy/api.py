from flask import Flask, request, jsonify
import cv2
import numpy as np
from tensorflow.keras.models import load_model
from sklearn.preprocessing import LabelBinarizer

app = Flask(__name__)

# Load the model
model = load_model('model.h5')

# Load the label binarizer
label_binarizer = LabelBinarizer()
label_binarizer.classes_ = np.load('label_classes.npy')

@app.route('/predict', methods=['POST'])
def predict():
    # Get the image file from the request
    file = request.files['file']

    # Read and preprocess the image
    img = cv2.imdecode(np.fromstring(file.read(), np.uint8), cv2.IMREAD_GRAYSCALE)
    img = cv2.resize(img, (32, 32))
    img = img.astype("float32") / 255.0
    img = np.expand_dims(img, axis=-1)
    img = np.expand_dims(img, axis=0)

    # Perform prediction
    predictions = model.predict(img)
    predicted_labels = label_binarizer.inverse_transform(predictions)

    # Return the predicted labels as JSON response
    response = {'predictions': predicted_labels.tolist()}
    return jsonify(response)

if __name__ == '__main__':
    app.run()