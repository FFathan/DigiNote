
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import tensorflow as tf
import cv2
import imutils

from sklearn.preprocessing import LabelBinarizer
LB = LabelBinarizer()
import joblib

# Load the LabelBinarizer from a file
LB = joblib.load('ML/data/label_binarizer.pkl')

#fit label binarizer with placeholder training labels

# training_labels = ['a','b','c','d','e','f','g','h','i','j',
#                    'k','l','m','n', 'o','p','q','r','s','t',
#                    'u','v','w','x','y','z', '0','1','2','3',
#                    '4','5','6','7','8','9', 'A','B','C','D',
#                    'E','F','G','H','I','J', 'K','L','M','N',
#                    'O','P','Q','R','S','T', 'U','V','W','X',
#                    'Y','Z']


# Load model
model = tf.keras.models.load_model('ML/model/model_v1.h5')

def sort_contours(cnts, method="left-to-right"):
    reverse = False
    i = 0
    if method == "right-to-left" or method == "bottom-to-top":
        reverse = True
    if method == "top-to-bottom" or method == "bottom-to-top":
        i = 1
    boundingBoxes = [cv2.boundingRect(c) for c in cnts]
    (cnts, boundingBoxes) = zip(*sorted(zip(cnts, boundingBoxes),
    key=lambda b:b[1][i], reverse=reverse))
    # return the list of sorted contours and bounding boxes
    return (cnts, boundingBoxes)

def get_letters(img):
    letters = []
    image = cv2.imread(img)
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    ret,thresh1 = cv2.threshold(gray ,127,255,cv2.THRESH_BINARY_INV)
    dilated = cv2.dilate(thresh1, None, iterations=2)

    cnts = cv2.findContours(dilated.copy(), cv2.RETR_EXTERNAL,cv2.CHAIN_APPROX_SIMPLE)
    cnts = imutils.grab_contours(cnts)
    cnts = sort_contours(cnts, method="left-to-right")[0]

    # Fit LabelBinarizer on training labels
    # LB.fit(training_labels)

    # loop over the contours
    for c in cnts:
        if cv2.contourArea(c) > 10:
            (x, y, w, h) = cv2.boundingRect(c)
            cv2.rectangle(image, (x, y), (x + w, y + h), (0, 255, 0), 2)
        roi = gray[y:y + h, x:x + w]
        thresh = cv2.threshold(roi, 0, 255,cv2.THRESH_BINARY_INV | cv2.THRESH_OTSU)[1]
        thresh = cv2.resize(thresh, (32, 32), interpolation = cv2.INTER_CUBIC)
        thresh = thresh.astype("float32") / 255.0
        thresh = np.expand_dims(thresh, axis=-1)
        thresh = thresh.reshape(1,32,32,1)
        ypred = model.predict(thresh)
        ypred = LB.inverse_transform(ypred)
        [x] = ypred
        letters.append(x)
    return letters, image

def get_word(letter):
    word = "".join(letter)
    return word

# Load image, taruh image di folder yang sama dengan file ini
IMG = 'ML/image-test/test.png' 

letter,image = get_letters(IMG)
word = get_word(letter)

#ini contoh nge print outputnya
print(word)

# tinggal ambil variabel word masukin ke json(?) biar bisa kirim ke MD

