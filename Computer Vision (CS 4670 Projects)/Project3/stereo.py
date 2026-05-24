import numpy as np

# ==============No additional imports allowed ================================#


def get_ncc_descriptors(img, patchsize):
    """
    Prepare normalized patch vectors for normalized cross
    correlation.

    Input:
        img -- height x width x channels image of type float32
        patchsize -- integer width and height of NCC patch region.
    Output:
        normalized -- height*width*(channels*(patchsize**2)) array

    For every pixel (i,j) in the image, your code should:
    (1) take a patchsize x patchsize window around the pixel,
    (2) compute and subtract the mean for every channel
    (3) flatten it into a single vector
    (4) normalize the vector by dividing by its L2 norm
    (5) store it in the (i,j)th location in the output

    If the window extends past the image boundary, zero out the descriptor

    If the norm of the vector is <1e-6 before normalizing, zero out the vector.

    """
    # Assume that patchsize is odd
    normalized = np.zeros((img.shape[0], img.shape[1], img.shape[2] * patchsize**2))
    half_patch = patchsize // 2
    for x in range(img.shape[0]):
        for y in range(img.shape[1]):
            xbounds = (x - half_patch, x + half_patch + 1)
            ybounds = (y - half_patch, y + half_patch + 1)
            if xbounds[0] >= 0 and xbounds[1] <= img.shape[0] and ybounds[0] >= 0 and ybounds[1] <= img.shape[1]:
                temp = img[xbounds[0]:xbounds[1], ybounds[0]:ybounds[1], :].copy()
                c_means = temp.mean(axis=(0, 1))
                temp -= c_means
                temp = temp.flatten()
                norm = np.linalg.norm(temp)
                if norm < 1e-6:
                    normalized[x, y, :] = np.zeros_like(temp)
                else:
                    normalized[x, y, :] = temp / norm

    return normalized


def compute_ncc_vol(img_right, img_left, patchsize, dmax):
    """
    Compute the NCC-based cost volume
    Input:
        img_right: the right image, H x W x C
        img_left: the left image, H x W x C
        patchsize: the patchsize for NCC, integer
        dmax: maximum disparity
    Output:
        ncc_vol: A dmax x H x W tensor of scores.

    ncc_vol(d,i,j) should give a score for the (i,j)th pixel for disparity d.
    This score should be obtained by computing the similarity (dot product)
    between the patch centered at (i,j) in the right image and the patch centered
    at (i, j+d) in the left image.

    Your code should call get_ncc_descriptors to compute the descriptors once.
    """

    ncc1 = get_ncc_descriptors(img_right, patchsize)
    ncc2 = get_ncc_descriptors(img_left, patchsize)
    ncc_vol = np.zeros((dmax, img_right.shape[0], img_right.shape[1]))
    for d in range(dmax):
        for i in range(img_right.shape[0]):
            for j in range(img_right.shape[1]):
                if j + d < img_left.shape[1]:
                    ncc_vol[d, i, j] = ncc1[i, j] @ ncc2[i, j + d]
    return ncc_vol


def get_disparity(ncc_vol):
    """
    Get disparity from the NCC-based cost volume
    Input:
        ncc_vol: A dmax X H X W tensor of scores
    Output:
        disparity: A H x W array that gives the disparity for each pixel.

    the chosen disparity for each pixel should be the one with the largest score for that pixel
    """
    disparity = np.argmax(ncc_vol, axis=0)
    return disparity
