package com.dipanjan.project;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;



public class Utils {

    /**
     *
     * @return a random string of 100 characters
     */
    public static String generateRandomString() {
        int length = 100;
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    /**
     *
     * @param configurableMean : the configurable mean for publishing distribution
     * @return a random period for the publisher to wait before publishing
     */
    public static String getConfigurableMean (Integer configurableMean) {

        return String.valueOf(new Random().nextInt(configurableMean));

    }

}
