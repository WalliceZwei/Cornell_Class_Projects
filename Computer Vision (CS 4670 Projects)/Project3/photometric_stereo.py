import numpy as np

##======================== No additional imports allowed ====================================##


def photometric_stereo_singlechannel(I, L):
    # L is 3 x k
    # I is k x n
    G = np.linalg.inv(L @ L.T) @ L @ I
    # G is  3 x n
    albedo = np.sqrt(np.sum(G * G, axis=0))

    # Avoid division by zero by adding a small epsilon
    epsilon = 1e-12
    normals = G / np.maximum(albedo.reshape((1, -1)), epsilon)
    return albedo, normals


def photometric_stereo(images, lights):
    """
    Use photometric stereo to compute albedos and normals
    Input:
        images: A list of N images, each a numpy float array of size H x W x 3
        lights: 3 x N array of lighting directions.
    Output:
        albedo, normals
        albedo: H x W x 3 array of albedo for each pixel
        normals: H x W x 3 array of normal vectors for each pixel

    Assume light intensity is 1.
    Compute the albedo and normals for red, green and blue channels separately.
    The normals should be approximately the same for all channels, so average the three sets
    and renormalize so that they are unit norm

    """
    H, W, _ = images[0].shape

    I_r = np.array([img[:, :, 0].flatten() for img in images])
    I_g = np.array([img[:, :, 1].flatten() for img in images])
    I_b = np.array([img[:, :, 2].flatten() for img in images])

    albedo_r, normals_r = photometric_stereo_singlechannel(I_r, lights)
    albedo_g, normals_g = photometric_stereo_singlechannel(I_g, lights)
    albedo_b, normals_b = photometric_stereo_singlechannel(I_b, lights)

    albedo = np.stack(
        [albedo_r.reshape(H, W), albedo_g.reshape(H, W), albedo_b.reshape(H, W)], axis=2
    )

    normals_avg = (normals_r + normals_g + normals_b) / 3
    norm_mag = np.linalg.norm(normals_avg, axis=0, keepdims=True)
    normals_avg = normals_avg / np.maximum(norm_mag, 1e-12)
    normals = np.stack(
        [
            normals_avg[0, :].reshape(H, W),
            normals_avg[1, :].reshape(H, W),
            normals_avg[2, :].reshape(H, W),
        ],
        axis=2,
    )
    return albedo, normals
