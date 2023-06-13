import numpy as np
import matplotlib.pyplot as plt
import cv2

SMALL_HEIGHT = 800

def implt(img, cmp=None, t=''):
    """Show image using plt."""
    plt.imshow(img, cmap=cmp)
    plt.title(t)
    plt.show()


def resize(img, height=SMALL_HEIGHT, always=False):
    """Resize image to given height."""
    if (img.shape[0] > height or always):
        rat = height / img.shape[0]
        return cv2.resize(img, (int(rat * img.shape[1]), height))
    
    return img


def ratio(img, height=SMALL_HEIGHT):
    """Getting scale ratio."""
    return img.shape[0] / height


def img_extend(img, shape):
    """Extend 2D image (numpy array) in vertical and horizontal direction.
    Shape of result image will match 'shape'
    Args:
        img: image to be extended
        shape: shape (touple) of result image
    Returns:
        Extended image
    """
    x = np.zeros(shape, np.uint8)
    x[:img.shape[0], :img.shape[1]] = img
    return x

def preprocess(img):
    (h, w) = img.shape
    
    final_img = np.ones([64, 256])*255 # blank white image
    
    # crop
    if w > 256:
        img = img[:, :256]
        
    if h > 64:
        img = img[:64, :]
    
    
    final_img[:h, :w] = img
    return cv2.rotate(final_img, cv2.ROTATE_90_CLOCKWISE)


def preprocess2(img):
    (h, w) = img.shape

    # Resize the image by 40% while maintaining aspect ratio
    img = cv2.resize(img, (int(w * 0.5), int(h * 0.47)))
    

    # Create a blank white image of size 256x64
    final_img = np.ones([64, 256]) * 255

    # Get the resized image dimensions
    resized_h, resized_w = img.shape

    # Calculate the padding required to center the resized image
    pad_h = (64 - resized_h) // 2
    pad_w = (256 - resized_w) // 2

    # Place the resized image onto the blank white image, centered
    final_img[pad_h:pad_h + resized_h, pad_w:pad_w + resized_w] = img
    
    return cv2.rotate(final_img, cv2.ROTATE_90_CLOCKWISE)