package com.example.toeicapplication.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptPassword {
    public static String encrypt(String password){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] hash = messageDigest.digest(password.getBytes());
            return byteToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String byteToHex(byte[] input){
        StringBuilder sb = new StringBuilder();
        for (byte b : input) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
