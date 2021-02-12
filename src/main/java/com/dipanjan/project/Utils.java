package com.dipanjan.project;

import org.apache.commons.lang3.RandomStringUtils;

public class Utils {

    public static String generateRandomString() {
        int length = 100;
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

}
