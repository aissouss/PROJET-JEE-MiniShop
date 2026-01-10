package com.minishop.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for password hashing and verification
 * Uses SHA-256 for hashing (Note: In production, use BCrypt or Argon2)
 */
public class PasswordUtil {

    private static final Logger LOGGER = Logger.getLogger(PasswordUtil.class.getName());
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;

    /**
     * Hash a password using SHA-256
     * Note: For production, use BCrypt, Argon2, or PBKDF2 instead
     *
     * @param password the plaintext password
     * @return the hashed password
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Error hashing password", e);
            throw new RuntimeException("Failed to hash password", e);
        }
    }

    /**
     * Hash password with salt for better security
     *
     * @param password the plaintext password
     * @param salt     the salt
     * @return the hashed password with salt
     */
    public static String hashPasswordWithSalt(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Error hashing password with salt", e);
            throw new RuntimeException("Failed to hash password", e);
        }
    }

    /**
     * Generate a random salt
     *
     * @return base64 encoded salt
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Verify a password against a hash
     *
     * @param password the plaintext password
     * @param hash     the stored hash
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String hash) {
        String hashedInput = hashPassword(password);
        return hashedInput.equals(hash);
    }

    /**
     * Verify a password with salt against a hash
     *
     * @param password the plaintext password
     * @param salt     the salt used
     * @param hash     the stored hash
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPasswordWithSalt(String password, String salt, String hash) {
        String hashedInput = hashPasswordWithSalt(password, salt);
        return hashedInput.equals(hash);
    }

    /**
     * Convert byte array to hex string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    /**
     * Validate password strength
     *
     * @param password the password to validate
     * @return true if password meets minimum requirements
     */
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isWhitespace(c)) {
                hasSpecialChar = true;
            }
        }

        // At least 8 chars with 3 of the 4 categories
        int categories = 0;
        if (hasUpperCase) categories++;
        if (hasLowerCase) categories++;
        if (hasDigit) categories++;
        if (hasSpecialChar) categories++;

        return categories >= 3;
    }

    /**
     * Get password strength message
     *
     * @param password the password to check
     * @return message describing password strength requirements
     */
    public static String getPasswordStrengthMessage(String password) {
        if (password == null || password.isEmpty()) {
            return "Le mot de passe ne peut pas être vide";
        }

        if (password.length() < 8) {
            return "Le mot de passe doit contenir au moins 8 caractères";
        }

        if (!isPasswordStrong(password)) {
            return "Le mot de passe doit contenir au moins 3 des éléments suivants: " +
                   "majuscules, minuscules, chiffres, caractères spéciaux";
        }

        return "Mot de passe valide";
    }

    // For demo/development purposes - simple MD5 hash like in database
    public static String hashPasswordMD5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Error with MD5 hashing", e);
            throw new RuntimeException("Failed to hash password", e);
        }
    }
}
