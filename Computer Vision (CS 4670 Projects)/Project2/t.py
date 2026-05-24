

#!/usr/bin/env python3
"""
Test program for the produceMatches function in feature_detection.py
Uses yosemite1.jpg and yosemite2.jpg from resources/yosemite/
"""
﻿
import cv2
import numpy as np
from feature_detection import (
    computeHarrisValues, 
    detectCorners, 
    computeMOPSDescriptors, 
    produceMatches
)
﻿
def load_and_prepare_image(image_path):
    """
    Load an image and convert it to the format expected by the feature detection functions
    """
    # Load image using OpenCV
    image = cv2.imread(image_path)
    if image is None:
        raise FileNotFoundError(f"Could not load image: {image_path}")
    
    # Convert to float32 and normalize to [0, 1] range
    image = image.astype(np.float32)
    image /= 255.0
    
    return image
﻿
def test_produce_matches():
    """
    Test the produceMatches function using yosemite images
    """
    print("Testing produceMatches function...")
    
    # Load images
    img1_path = "resources/yosemite/yosemite1.jpg"
    img2_path = "resources/yosemite/yosemite2.jpg"
    
    print(f"Loading {img1_path}...")
    img1 = load_and_prepare_image(img1_path)
    
    print(f"Loading {img2_path}...")
    img2 = load_and_prepare_image(img2_path)
    
    # Convert to grayscale for Harris detection
    gray1 = cv2.cvtColor(img1, cv2.COLOR_BGR2GRAY)
    gray2 = cv2.cvtColor(img2, cv2.COLOR_BGR2GRAY)
    
    print("Computing Harris values for image 1...")
    harris1, orientation1 = computeHarrisValues(gray1)
    
    print("Computing Harris values for image 2...")
    harris2, orientation2 = computeHarrisValues(gray2)
    
    print("Detecting corners for image 1...")
    corners1 = detectCorners(harris1, orientation1)
    
    print("Detecting corners for image 2...")
    corners2 = detectCorners(harris2, orientation2)
    
    print(f"Found {len(corners1)} corners in image 1")
    print(f"Found {len(corners2)} corners in image 2")
    
    # Only use top N corners for efficiency (optional)
    # Sort by detector response (last element in tuple) and take top 100
    corners1 = sorted(corners1, key=lambda x: x[3], reverse=True)[:100]
    corners2 = sorted(corners2, key=lambda x: x[3], reverse=True)[:100]
    
    print("Computing MOPS descriptors for image 1...")
    desc1 = computeMOPSDescriptors(img1, corners1)
    
    print("Computing MOPS descriptors for image 2...")
    desc2 = computeMOPSDescriptors(img2, corners2)
    
    print(f"Descriptor shape for image 1: {desc1.shape}")
    print(f"Descriptor shape for image 2: {desc2.shape}")
    
    print("Producing matches...")
    matches = produceMatches(desc1, desc2)
    
    print(f"Found {len(matches)} matches")
    
    # Print some sample matches
    print("\nSample matches (index1, index2, score):")
    for i, match in enumerate(matches[:10]):  # Show first 10 matches
        # Convert numpy types to native Python types for cleaner output
        clean_match = (int(match[0]), int(match[1]), float(match[2]))
        print(f"  Match {i}: {clean_match}")
    
    # Sort matches by score (lower is better)
    matches_sorted = sorted(matches, key=lambda x: x[2])
    
    # Convert numpy types to native Python types for cleaner output
    best_match = (int(matches_sorted[0][0]), int(matches_sorted[0][1]), float(matches_sorted[0][2]))
    worst_match = (int(matches_sorted[-1][0]), int(matches_sorted[-1][1]), float(matches_sorted[-1][2]))
    
    print(f"\nBest match (lowest score): {best_match}")
    print(f"Worst match (highest score): {worst_match}")
    
    # Count good matches (typically score < 0.5 or 0.6 is considered good)
    good_matches = [m for m in matches if m[2] < 0.6]
    print(f"Good matches (score < 0.6): {len(good_matches)} out of {len(matches)}")
    
    return matches
﻿
if __name__ == "__main__":
    try:
        matches = test_produce_matches()
        print("\nTest completed successfully!")
    except Exception as e:
        print(f"Error during testing: {e}")
        import traceback
        traceback.print_exc()