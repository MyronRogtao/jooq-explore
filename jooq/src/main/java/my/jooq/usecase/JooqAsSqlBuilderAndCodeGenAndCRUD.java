package my.jooq.usecase;

import my.jooq.entities.autogen.Keys;
import my.jooq.entities.autogen.tables.Address;
import my.jooq.entities.autogen.tables.Person;
import my.jooq.entities.autogen.tables.records.PersonRecord;
import my.jooq.util.DBConnection;
import org.jooq.DSLContext;
import org.jooq.Key;
import org.jooq.Record;
import org.jooq.impl.DSL;

import static my.jooq.util.PropReaderUtil.getProperty;
import static my.jooq.util.RandomStringGen.generate;

public class JooqAsSqlBuilderAndCodeGenAndCRUD {

    public static void main(String... args) {

        DSLContext context = DSL.using(DBConnection.URL, DBConnection.USER, DBConnection.PASS);

        /*
        Straightforward insert statement
        context.insertInto(Person.PERSON)
                .columns(Person.PERSON.FIRSTNAME, Person.PERSON.LASTNAME)
                .values(generate(8), generate(8))
                .execute();

        //After insert find the ID of the last inserted record.
        //But does not guarantee the exact record if we do not have an unique column
        int personId = context.select()
                .from(Person.PERSON)
                .where(Person.PERSON.ID.eq(context.select(Person.PERSON.ID.max()).from(Person.PERSON)))
                .fetchSingle()
                .get(Person.PERSON.ID);
        */

        //Better Approach to insert and get the ID of the inserted records
        Integer personId = context.insertInto(Person.PERSON)
                        .set(Person.PERSON.FIRSTNAME, generate(8))
                        .set(Person.PERSON.LASTNAME, generate(8))
                        .returning(Person.PERSON.ID)
                        .fetchOne()
                        .getValue(Person.PERSON.ID, Integer.class);

        //Simple select statement
        context.select().from(Person.PERSON)
                .where(Person.PERSON.ID.eq(personId)).fetch()
                .forEach(record ->
                        System.out.println(String.format("Id : %d, Firstname : %s, Lastname : %s",
                                record.get(Person.PERSON.ID),
                                record.get(Person.PERSON.FIRSTNAME),
                                record.get(Person.PERSON.LASTNAME))));

        //fetch single record
        Record singleRecord = context.select().from(Person.PERSON)
                .where(Person.PERSON.ID.eq(personId)).fetchSingle();

        //create Address Info
        context.insertInto(Address.ADDRESS)
                .columns(Address.ADDRESS.CITY, Address.ADDRESS.PERSON_ID)
                .values(generate(4), singleRecord.get(Person.PERSON.ID))
                .execute();

        //Select with Joins
        context.select(Person.PERSON.FIRSTNAME, Address.ADDRESS.CITY)
                .from(Person.PERSON)
                .leftJoin(Address.ADDRESS).on(Address.ADDRESS.PERSON_ID.eq(Person.PERSON.ID))
                .fetch()
                .forEach(record ->
                        System.out.println("First Name : " + record.get(0) + " ,City : " + record.get(1)));

        //Simple Update statement
        context.update(Person.PERSON).set(Person.PERSON.FIRSTNAME, generate(10))
                .where(Person.PERSON.ID.eq(personId)).execute();

        //Get updated record
        context.select().from(Person.PERSON)
                .where(Person.PERSON.ID.eq(personId)).fetch()
                .forEach(record -> {
                            System.out.println(String.format("Id : %d, Firstname : %s, Lastname : %s",
                                    record.get(Person.PERSON.ID),
                                    record.get(Person.PERSON.FIRSTNAME),
                                    record.get(Person.PERSON.LASTNAME)));
                            ((PersonRecord) record).fetchChildren(Keys.ADDRESS_IBFK_1)
                                    .forEach(addressRecord -> {
                                        System.out.println(String.format("Id : %d, City : %s, State : %s",
                                                        addressRecord.get(Address.ADDRESS.ID),
                                                        addressRecord.get(Address.ADDRESS.CITY),
                                                        addressRecord.get(Address.ADDRESS.STATE)));
                                        //Update Address record on the Fly
                                        //addressRecord.setCity("MyOwnCity");
                                        //addressRecord.update();
                                            }

                                    );
                        }
                        );

        //Delete statement
        //context.delete(Person.PERSON).where(Person.PERSON.ID.eq(personId)).execute();

    }
}
