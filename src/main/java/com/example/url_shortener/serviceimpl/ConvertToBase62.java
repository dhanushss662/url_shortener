package com.example.url_shortener.serviceimpl;

public class ConvertToBase62 {
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int SHORT_URL_LENGTH = 7;

    public static String convertToBase62() {
        long uniqueNumber = generateUniqueNumber();
        StringBuilder shortUrl = new StringBuilder();
        while (uniqueNumber > 0) {
            int remainder = (int) (uniqueNumber % 62);
            shortUrl.append(ALPHABET.charAt(remainder));
            uniqueNumber /= 62;
        }
        if(shortUrl.length() < SHORT_URL_LENGTH) {
            while (shortUrl.length() < SHORT_URL_LENGTH) {
                shortUrl.append(ALPHABET.charAt(0));
            }
        }
        return shortUrl.reverse().toString();
    }

    private static long generateUniqueNumber() {
        String date = java.time.LocalDate.now().toString().replace("-", "");
        String time = java.time.LocalTime.now().toString().replace(":", "").replace(".", "");
        String uniqueString = date + time;
        return Long.parseLong(uniqueString);
    }
}
