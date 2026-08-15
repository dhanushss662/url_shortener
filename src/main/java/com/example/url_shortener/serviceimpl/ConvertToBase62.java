package com.example.url_shortener.serviceimpl;

public class ConvertToBase62 {
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static long counter = 0;
    private static final long MOD = 3521614606208L;

    public static String convertToBase62(long uniqueNumber) {
        StringBuilder shortUrl = new StringBuilder();
        uniqueNumber%= MOD; // Ensure the number is within the range of 7 characters in base 62
        while (uniqueNumber > 0) {
            int remainder = (int) (uniqueNumber % 62);
            shortUrl.append(ALPHABET.charAt(remainder));
            uniqueNumber /= 62;
        }

        return shortUrl.reverse().toString();
    }
}
