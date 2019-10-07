package fr.inria.gforge.spoon.transformation.dbaccess.src;

import fr.inria.gforge.spoon.transformation.dbaccess.annotation.DBAccess;
import fr.inria.gforge.spoon.transformation.dbaccess.annotation.DBType;

@DBAccess(type = DBType.RELATIONAL, database = "test", username = "renaud", password = "", tableName = "c_instances")
public class Person {

    String key;

    public Person(String key) {
        this.key = key;
    }

    public String getLastName() {
        return null;
    }

    public String getFirstName() {
        return null;
    }

}
