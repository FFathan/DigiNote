# -*- coding: utf-8 -*-
"""
Detect words on the page
return array of words' bounding boxes
"""
import numpy as np
import matplotlib.pyplot as plt
import cv2

from func import resize, ratio, implt




def detection(image, join=False):
    """Detecting the words bounding boxes.
    Return: numpy array of bounding boxes [x, y, x+w, y+h]
    """
    # Preprocess image for word detection
    blurred = cv2.GaussianBlur(image, (5, 5), 18)
    edge_img = _edge_detect(blurred)
    ret, edge_img = cv2.threshold(edge_img, 50, 255, cv2.THRESH_BINARY)
    bw_img = cv2.morphologyEx(edge_img, cv2.MORPH_CLOSE,
                              np.ones((15,15), np.uint8))

    return _text_detect(bw_img, image, join)


def sort_words(boxes):
    """Sort boxes - (x, y, x+w, y+h) from left to right, top to bottom."""
    mean_height = sum([y2 - y1 for _, y1, _, y2 in boxes]) / len(boxes)
    
    boxes.view('i8,i8,i8,i8').sort(order=['f1'], axis=0)
    current_line = boxes[0][1]
    lines = []
    tmp_line = []
    for box in boxes:
        if box[1] > current_line + mean_height:
            lines.append(tmp_line)
            tmp_line = [box]
            current_line = box[1]            
            continue
        tmp_line.append(box)
    lines.append(tmp_line)
        
    for line in lines:
        line.sort(key=lambda box: box[0])
        
    return lines


# apply magic color thresholding
def magic_color_thresholding(img):
    gray = cv2.cvtColor(img, cv2.COLOR_RGB2GRAY)  # Convert to grayscale
    blur = cv2.GaussianBlur(gray, (5, 5), 0)  # Apply Gaussian blur
    thresh = cv2.adaptiveThreshold(blur, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY, 11, 2)
    
    # Perform morphological operations
    kernel = cv2.getStructuringElement(cv2.MORPH_RECT, (3, 3))
    opening = cv2.morphologyEx(thresh, cv2.MORPH_OPEN, kernel, iterations=1)
    closing = cv2.morphologyEx(opening, cv2.MORPH_CLOSE, kernel, iterations=1)
    
    denoised = cv2.fastNlMeansDenoising(closing, 11, 31, 9) # you may experiment with the constants here
    return denoised

# def _edge_detect(im):
#     """ 
#     Edge detection using sobel operator on each layer individually.
#     Sobel operator is applied for each image layer (RGB)
#     """
#     return np.max(np.array([_sobel_detect(im[:,:, 0]),
#                             _sobel_detect(im[:,:, 1]),
#                             _sobel_detect(im[:,:, 2])]), axis=0)


# def _sobel_detect(channel):
#     """Sobel operator."""
#     sobelX = cv2.Sobel(channel, cv2.CV_16S, 1, 0)
#     sobelY = cv2.Sobel(channel, cv2.CV_16S, 0, 1)
#     sobel = np.hypot(sobelX, sobelY)
#     sobel[sobel > 255] = 255
#     return np.uint8(sobel)


#for filtered
def _edge_detect(im):
    sobel = _sobel_detect(im)
    _, thresh = cv2.threshold(sobel, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)
    return thresh

def _sobel_detect(channel):
    sobelX = cv2.Sobel(channel, cv2.CV_16S, 1, 0)
    sobelY = cv2.Sobel(channel, cv2.CV_16S, 0, 1)
    sobel = np.hypot(sobelX, sobelY)
    sobel[sobel > 255] = 255
    return np.uint8(sobel)



def union(a,b):
    x = min(a[0], b[0])
    y = min(a[1], b[1])
    w = max(a[0]+a[2], b[0]+b[2]) - x
    h = max(a[1]+a[3], b[1]+b[3]) - y
    return [x, y, w, h]

def _intersect(a,b):
    x = max(a[0], b[0])
    y = max(a[1], b[1])
    w = min(a[0]+a[2], b[0]+b[2]) - x
    h = min(a[1]+a[3], b[1]+b[3]) - y
    if w<0 or h<0:
        return False
    return True

def _group_rectangles(rec):
    """
    Union intersecting rectangles.
    Args:
        rec - list of rectangles in form [x, y, w, h]
    Return:
        list of grouped rectangles 
    """
    tested = [False for _ in range(len(rec))]
    final = []
    
    for i in range(len(rec)):
        if not tested[i]:
            rect = rec[i]
            for j in range(i + 1, len(rec)):
                if not tested[j] and _intersect(rect, rec[j]):
                    rect = union(rect, rec[j])
                    tested[j] = True
            final.append(rect)
    
    return final



def _text_detect(img, image, join=False):
    """Text detection using contours."""
    small = resize(img, 2000)
    
    # Finding contours
    mask = np.zeros(small.shape, np.uint8)
    cnt, hierarchy = cv2.findContours(np.copy(small),
                                           cv2.RETR_CCOMP,
                                           cv2.CHAIN_APPROX_SIMPLE)
    
    index = 0    
    boxes = []
     # Go through all contours in top level
    while (index >= 0):
        x,y,w,h = cv2.boundingRect(cnt[index])
        cv2.drawContours(mask, cnt, index, (255, 255, 255), cv2.FILLED)
        maskROI = mask[y:y+h, x:x+w]
        # Ratio of white pixels to area of bounding rectangle
        r = cv2.countNonZero(maskROI) / (w * h)
        
         # Limits for text
        if (r > 0.1  # Adjust the threshold as needed
                and 1600 > w > 10
                and 1600 > h > 10
                and (60 // h) * w < 1000):
            # Add padding to the bounding box coordinates and dimensions
            x -= 20
            y -= 20
            w += 30
            h += 30
            boxes += [[x, y, w, h]]
            
        index = hierarchy[0][index][0]

    if join:
        
        boxes = _group_rectangles(boxes)

    # image for drawing bounding boxes
    small = cv2.cvtColor(small, cv2.COLOR_GRAY2RGB)
    bounding_boxes = np.array([0,0,0,0])
    for (x, y, w, h) in boxes:
        cv2.rectangle(small, (x, y),(x+w,y+h), (0, 255, 0), 2)
        bounding_boxes = np.vstack((bounding_boxes,
                                    np.array([x, y, x+w, y+h])))

        
    # implt(small, t='Bounding rectangles')
    # cv2.namedWindow('Bounding rectangle', cv2.WINDOW_NORMAL)
    # cv2.imshow('Bounding rectangle', small)
    # cv2.waitKey(0)

    
    boxes = bounding_boxes.dot(ratio(image, small.shape[0])).astype(np.int64)
    return boxes[1:]  
    


