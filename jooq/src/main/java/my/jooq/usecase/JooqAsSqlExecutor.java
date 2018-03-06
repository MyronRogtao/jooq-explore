package my.jooq.usecase;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import static my.jooq.util.DBConnection.*;

public class JooqAsSqlExecutor {

    public static void main(String...args) {

        //Given an SQL to be executed
        String givenSql = "select * from person";

        // Fetch results using jOOQ
        DSLContext context = DSL.using(URL, USER, PASS);
        context.fetch(givenSql)
                .forEach(record -> System.out.println(String.format("Id : %s, Firstname : %s, Lastname : %s",
                        record.get(0),
                        record.get(1),
                        record.get(2))));

        // Or execute that SQL with JDBC, fetching the ResultSet with jOOQ:
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            ResultSet rs = con.createStatement().executeQuery(givenSql);
            context.fetch(rs)
                    .forEach(record -> System.out.println(String.format("Id : %s, Firstname : %s, Lastname : %s",
                            record.get(0),
                            record.get(1),
                            record.get(2))));

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
