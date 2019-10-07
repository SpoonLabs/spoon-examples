package fr.inria.gforge.spoon.transformation.dbaccess.template;

import spoon.reflect.factory.Factory;
import spoon.template.ExtensionTemplate;
import spoon.template.Local;
import spoon.template.Parameter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBCodeTemplate extends ExtensionTemplate {
    @Parameter
    public String _database_;

    @Parameter
    public String _username_;

    @Parameter
    public String _password_;

    @Parameter
    public String _tableName_;

    @Parameter
    public String _columnName_;

    Connection connection;

    @Local
    String key;

    public DBCodeTemplate(Factory f, String database, String username,
            String password, String tableName) {
        this._database_ = database;
        this._username_ = username;
        this._password_ = password;
        this._tableName_ = tableName;
    }

    public void initializerCode() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        try {
            connection = DriverManager.getConnection("jdbc:postgresql:"
                    + _database_, _username_, _password_);
        } catch (SQLException e) {
            new Exception("failed to connect to the database with "
                    + "jdbc:postgresql:" + _database_ + "," + _username_ + ","
                    + _password_).printStackTrace();
        }
    }

    public String accessCode() {
        String query = "select " + _columnName_ + " from " + _tableName_
                + " where key=" + key;
        Statement s = null;
        try {
            s = connection.createStatement();
            ResultSet rs = s.executeQuery(query);
            rs.first();
            return rs.getString(1);
        } catch (java.sql.SQLException ex12) {
            ex12.printStackTrace();
        } finally {
            try {
                s.close();
            } catch (Exception ex13) {
                ex13.printStackTrace();
            }
        }
        return null;
    }

}