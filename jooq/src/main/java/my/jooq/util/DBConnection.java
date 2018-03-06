package my.jooq.util;

import static my.jooq.util.PropReaderUtil.getProperty;

public class DBConnection {

    public static String USER = getProperty("jdbc.user", "jdbc:mysql://localhost:3306/jooqdb");
    public static String PASS = getProperty("jdbc.pass", "root");
    public static String URL = getProperty("jdbc.url", "password");
}
