package fr.inria.gforge.spoon.transformation.dbaccess.annotation;


public @interface DBAccess {
    DBType type();
    String database();
    String username();
    String password();
    String tableName();
}
