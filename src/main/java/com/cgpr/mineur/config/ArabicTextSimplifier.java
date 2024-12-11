package com.cgpr.mineur.config;

public class ArabicTextSimplifier {

    // Simplification des mots arabes
    public static String simplifyArabicWord(String word) {
        if (word == null || word.isEmpty()) return "";

        // Étape 1: Supprimer les caractères décoratifs (ــ)
        word = word.replaceAll("[ـ]+", "");

        // Étape 2: Supprimer les diacritiques (harakat)
        word = word.replaceAll("[\\u0610-\\u061A\\u064B-\\u065F]", "");

        // Étape 3: Supprimer les préfixes définis ou inutiles
        word = word.replaceAll("^(?:ال|أل|آل|بِ|لِ)+", "");

        // Étape 4: Normaliser les lettres, incluant les lettres "A"
        word = word.replaceAll("[\u0622\u0623\u0625\u0627\u0626]", "\u0627"); // آ, أ, إ, ا, ئ → ا

        word = word.replaceAll("[\u0624\u0626]", "\u0621"); // ؤ, ئ → ء
        word = word.replaceAll("[\u0649]", "\u064A");       // ى → ي
        word = word.replaceAll("[\u0629]", "\u0647");       // ة → ه

        // Étape 5: Supprimer les espaces inutiles
        word = word.trim();

        return word;
    }

    // Extraire et simplifier la phrase en fonction des conditions spécifiées
    public static String simplifyArabicPhrase(String phrase) {
        if (phrase == null || phrase.isEmpty()) return "";

        // Diviser la phrase en mots en utilisant l'espace comme séparateur
        String[] words = phrase.split("\\s+");

        // Si le premier mot commence par "عبد", on garde le deuxième mot
        if (words[0].startsWith("عبد") && words.length > 1) {
            return simplifyArabicWord(words[1]);
        }

        // Si le dernier mot se termine par "الدين" ou "دين", on garde le premier mot
        if (words[words.length - 1].endsWith("الدين") || words[words.length - 1].endsWith("دين")) {
            return simplifyArabicWord(words[0]);
        }

        // Si aucune condition n'est remplie, on garde le dernier mot
        return simplifyArabicWord(words[words.length - 1]);
    }

  
}
