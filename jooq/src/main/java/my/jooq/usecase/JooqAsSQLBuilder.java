package my.jooq.usecase;

import static org.jooq.impl.DSL.*;

public class JooqAsSQLBuilder {

    public String generateQuery() {

        //JOOQ as a Query Builder
        //This does not require any codegeneration library nor the Query execution facilities
        //Endless possibilities.
        return select(
                  field("person.firstname"),
                  field("person.lastname"))
                .from(table("person"))
                .where(field("person.id").eq(1)).getSQL();
    }
}
