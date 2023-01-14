package com.qiuxinyu.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class RandomUtils {
    public static String getVerifyCode(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            try {
                int num = SecureRandom.getInstance("SHA1PRNG").nextInt(10);
                stringBuffer.append(num);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        return new String(stringBuffer);
    }

    public static void main(String[] args) {
        System.out.println(getVerifyCode(6));
    }
}
