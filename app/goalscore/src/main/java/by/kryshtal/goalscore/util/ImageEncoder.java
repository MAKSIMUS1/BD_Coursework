package by.kryshtal.goalscore.util;


import java.util.Base64;

public class ImageEncoder {
    public static String encodeImage(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
