import numpy as np
import cv2
from feature_detection import (
    computeHarrisValues,
    detectCorners,
    computeMOPSDescriptors,
    computeLocalMaximaHelper,
)
import traceback

from PIL import Image


# Saving and loading cv2 points
def pickle_cv2(arr):
    index = []
    for point in arr:
        temp = (point[0], point[1], point[2], point[3])
        index.append(temp)
    return np.array(index)


def unpickle_cv2(arr):
    index = []
    for point in arr:
        temp = (point[0], point[1], point[2], point[3])
        index.append(temp)
    return np.array(index)


# Functions for testing elementwise correctness
def compare_array(arr1, arr2):
    return np.allclose(arr1, arr2, rtol=1e-3, atol=1e-3)


def compare_cv2_points(pnt1, pnt2):
    if not np.isclose(pnt1[0], pnt2[0], rtol=1e-3, atol=1e-5):
        return False
    if not np.isclose(pnt1[1], pnt2[1], rtol=1e-3, atol=1e-5):
        return False
    if not np.isclose(pnt1[2], pnt2[2], rtol=1e-3, atol=1e-5):
        return False
    if not np.isclose(pnt1[3], pnt2[3], rtol=1e-3, atol=1e-5):
        return False
    return True


# Testing function
def try_this(todo, run, truth, compare, *args, **kargs):
    """
    Run a function, test the output with compare, and print and error if it doesn't work
    @arg todo (int or str): The Todo number
    @arg run (func): The function to run
    @arg truth (any): The correct output of the function
    @arg compare (func->bool): Compares the output of the `run` function to truth and provides a boolean if correct
    @arg *args (any): Any arguments that should be passed to `run`
    @arg **kargs (any): Any kargs that should be passed to compare

    @return (int): The amount of things that failed
    """
    print("Starting test for TODO {}".format(todo))
    failed = 0
    try:
        output = run(*args)
    except Exception:
        traceback.print_exc()
        print("TODO {} threw an exception, see exception above".format(todo))
        return
    if type(output) is list or type(output) is tuple:
        if len(output) == len(truth):
            for i in range(len(output)):
                if not compare(output[i], truth[i], **kargs):
                    print("TODO {} doesn't pass test: {}".format(todo, i))
                    failed += 1
        else:
            print("TODO {} doesn't pass test".format(todo))
    else:
        if not compare(output, truth, **kargs):
            print("TODO {} doesn't pass test".format(todo))
            failed += 1
    return failed


image = np.array(Image.open("resources/triangle1.jpg"))
grayImage = cv2.cvtColor(image.astype(np.float32) / 255.0, cv2.COLOR_BGR2GRAY)


def compute_and_save():
    (a, b) = computeHarrisValues(grayImage)  # Todo1
    c = computeLocalMaximaHelper(a)  # Todo2
    d = detectCorners(a, b)  # Todo3
    f = computeMOPSDescriptors(image, d)  # Todo4/5
    d_proc = pickle_cv2(d)
    np.savez("resources/arrays", a=a, b=b, c=c, d_proc=d_proc, f=f)


# Uncomment next line to overwrite test data (not recommended)
# compute_and_save()
