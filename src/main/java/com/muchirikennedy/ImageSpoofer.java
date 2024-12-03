package com.muchirikennedy;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * The ImageSpoofer class provides functionality to modify a JPG image
 * such that its visual representation remains unchanged while altering
 * the underlying binary data to achieve a specific hash prefix.
 */
public class ImageSpoofer {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java -jar spoof.jar <hexstring> <inputImage> <outputImage>");
            return;
        }
        String hexString = args[0].substring(2); // removing the '0x' part
        String originalImgPath = args[1];
        String alteredImgPath = args[2];

        // check if image is jpg
        if (!isJPG(originalImgPath)) {
            System.out.println("Image must be of jpg fomart.");
            return;
        }

        try {
            BufferedImage originalImage = ImageIO.read(new File(originalImgPath));
            BufferedImage alteredImage = originalImage; // starting with the original image

            String hash;
            int attempts = 0;
            do {
                // modifying image
                alterImage(alteredImage);
                hash = calculateHash(alteredImage);
                attempts++;

                // printing current hash after every 100 attempts
                if (attempts % 100 == 0) {
                    System.out.println("Attempt " + attempts + ":: Hash " + hash);
                }
            } while (!hash.startsWith(hexString));

            ImageIO.write(alteredImage, "jpg", new File(alteredImgPath));
            System.out.println("Altered image saved with hash: " + hash);
        } catch (IOException | NoSuchAlgorithmException e) {
            System.err.println(e);
        }
    }

    /**
     * Modifies the given BufferedImage slightly to alter its binary data.
     * <p>
     * A random x, y value is generated(bounded by width and height of the image).
     * </p>
     * <p>
     * The pixel, in rgb color, at point x, y is then retrieved and modified.
     * </p>
     * *
     * 
     * @param image The BufferedImage to be modified.
     */
    private static void alterImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        // selecting random pixel position
        Random random = new Random();
        int x = random.nextInt(width);
        int y = random.nextInt(height);

        int rgb = image.getRGB(x, y);
        // Changing the last bit of the RGB value to make a tiny adjustment to the color
        int rgbNew = (rgb & 0xFFFFFFFE);
        image.setRGB(x, y, rgbNew);
    }

    /**
     * Calculates the SHA-512 hash of the given BufferedImage.
     * This method converts the image to a byte array and computes the hash.
     *
     * @param image The BufferedImage for which the hash is to be calculated.
     * @return The SHA-512 hash as a hexadecimal string.
     * @throws NoSuchAlgorithmException if the SHA-512 algorithm is not available.
     * @throws IOException              if an error occurs during image processing.
     */
    private static String calculateHash(BufferedImage image) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        // Convert image to byte array
        byte[] imageBytes = imageToByteArray(image);
        byte[] hashBytes = md.digest(imageBytes);
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Converts a BufferedImage into a byte array.
     * This method temporarily saves the image to a file,
     * reads the file's contents into a byte array, and then
     * deletes the temporary file.
     *
     * @param image The BufferedImage to be converted.
     * @return A byte array representing the image data.
     * @throws IOException If an error occurs during file operations.
     */
    private static byte[] imageToByteArray(BufferedImage image) throws IOException {
        File tempFile = File.createTempFile("tempImage", ".jpg");
        ImageIO.write(image, "jpg", tempFile);
        byte[] bytes = java.nio.file.Files.readAllBytes(tempFile.toPath());
        tempFile.delete(); // Clean up
        return bytes;
    }

    /**
     * Checks if the specified file is a JPEG image.
     * This method verifies only the file extension.
     *
     * @param filePath The path to the file to be checked.
     * @return true if the file is a JPEG image; false otherwise.
     */
    private static boolean isJPG(String filePath) {
        if (filePath.toLowerCase().endsWith(".jpg") || filePath.toLowerCase().endsWith(".jpeg")) {
            return true;
        }
        return false;
    }
}
