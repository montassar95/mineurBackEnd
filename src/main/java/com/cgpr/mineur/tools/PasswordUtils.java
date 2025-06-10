package com.cgpr.mineur.tools;

import java.security.SecureRandom;

public class PasswordUtils {
    private static final String CHAR_LOWER = "abcdefvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "*+=";

    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomPassword(int length, boolean withSpecialAndUpper) {
        if (length < 8) length = 8; // longueur minimale

        String baseChars = CHAR_LOWER + DIGITS;
        if (withSpecialAndUpper) {
            baseChars += CHAR_UPPER + SPECIAL_CHARS;
        }

        StringBuilder password = new StringBuilder(length);

        // Toujours ajouter minuscule + chiffre
        password.append(randomChar(CHAR_LOWER));
        password.append(randomChar(DIGITS));

        // Si demandé, ajouter aussi majuscule + spécial
        if (withSpecialAndUpper) {
            password.append(randomChar(CHAR_UPPER));
            password.append(randomChar(SPECIAL_CHARS));
        } else {
            // Pour garder longueur de 8, ajouter deux chars en plus si on ne met pas majuscule/special
            password.append(randomChar(baseChars));
            password.append(randomChar(baseChars));
        }

        // Remplir le reste aléatoirement
        for (int i = password.length(); i < length; i++) {
            password.append(randomChar(baseChars));
        }

        // Mélanger les caractères pour ne pas avoir toujours l'ordre fixe
        return shuffleString(password.toString());
    }

    private static char randomChar(String input) {
        int index = random.nextInt(input.length());
        return input.charAt(index);
    }

    private static String shuffleString(String input) {
        char[] a = input.toCharArray();
        for (int i = a.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }
        return new String(a);
    }
}
