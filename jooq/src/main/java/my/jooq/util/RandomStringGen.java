package my.jooq.util;

import org.apache.commons.lang.RandomStringUtils;

public class RandomStringGen {

    public static String generate(int length) {
        return RandomStringUtils.random(length, true, false);
    }
}
