package com.todo.hashtag.service;

public class RandomColorGenerator {

    public static String randomColorGenerate() {
        String R = generateRandomColorIntegerToHaxString();
        String G = generateRandomColorIntegerToHaxString();
        String B = generateRandomColorIntegerToHaxString();
        return "#" + R + G + B;
    }

    private static String generateRandomColorIntegerToHaxString() {
        int color = (int) (Math.random() * 256);
        return Integer.toHexString(color);
    }
}
