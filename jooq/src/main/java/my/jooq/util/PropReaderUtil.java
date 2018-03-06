package my.jooq.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class PropReaderUtil {

    private static Properties properties = new Properties();

    static {
        try {
            properties.load(new FileInputStream(Thread.currentThread().getContextClassLoader()
                    .getResource("").getPath()
                    .concat("application.properties")));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getProperty(String key, String defaultValue) {
        return Optional.ofNullable(properties.getProperty(key)).orElse(defaultValue);
    }
}
