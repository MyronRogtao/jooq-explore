package my.jooq;

import my.jooq.entities.autogen.tables.Address;
import my.jooq.entities.autogen.tables.Person;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;

import static my.jooq.util.PropReaderUtil.getProperty;


public class AppStart {

    public static void main(String... args) {

        String username = getProperty("jdbc.user", "root");
        String password = getProperty("jdbc.pass", "password");
        String url = getProperty("jdbc.url", "jdbc:mysql://localhost:3306/jooqdb");

        try (Connection con = DriverManager.getConnection(url, username, password)) {
            DSLContext context = DSL.using(url, username, password);

            String firstName = "first name";
            String lastName = "last name";
            String city = "Pune";

            //Simple insert statement
            context.insertInto(Person.PERSON)
                    .columns(Person.PERSON.FIRSTNAME, Person.PERSON.LASTNAME)
                    .values(firstName, lastName)
                    .execute();

            //Simple select statement
            Result<Record> result = context.select().from(Person.PERSON)
                    .where(Person.PERSON.FIRSTNAME.eq(firstName)).fetch();

            for (Record record : result) {
                System.out.println(record.get(Person.PERSON.ID));
                System.out.println(record.get(Person.PERSON.FIRSTNAME));
                System.out.println(record.get(Person.PERSON.LASTNAME));
            }

            //fetch single record
           Record singleRecord = context.select().from(Person.PERSON)
                    .where(Person.PERSON.FIRSTNAME.eq(firstName)).fetchSingle();

            //create Address Info
            context.insertInto(Address.ADDRESS)
                    .columns(Address.ADDRESS.CITY, Address.ADDRESS.PERSON_ID)
                    .values(city, singleRecord.get(Person.PERSON.ID))
                    .execute();

            //Select with Joins
            Result<Record2<String, String>> joinResult = context
                    .select(Person.PERSON.FIRSTNAME, Address.ADDRESS.CITY)
                    .from(Person.PERSON)
                    .leftJoin(Address.ADDRESS).on(Address.ADDRESS.PERSON_ID.eq(Person.PERSON.ID))
                    .fetch();
            joinResult.stream()
                    .forEach(record ->
                            System.out.println("First Name : "+ record.get(0) +" ,City : "+ record.get(1)));

            String updatedfirstName = "UpdatedFirstName";

            //Simple Update statement
            context.update(Person.PERSON).set(Person.PERSON.FIRSTNAME, updatedfirstName)
                    .where(Person.PERSON.FIRSTNAME.eq(firstName)).execute();

            //Delete statement
            //context.delete(Person.PERSON).where(Person.PERSON.FIRSTNAME.eq(updatedfirstName)).execute();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
