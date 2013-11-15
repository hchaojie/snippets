package cn.hchaojie.snippets.java.fishdata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    private static final String URL = "jdbc:h2:~/fishgallery";
    private static final String USER = "ciro";
    private static final String PWD = "p@ssw0rd";

    private static final String TABLE_FAMILY = "family";
    private static final String TABLE_FISH = "fish";

    private static final String CREATE_TABLE_SQL = "DROP TABLE family; DROP TABLE fish;"
            + "CREATE TABLE family (id integer primary key auto_increment, name varchar, sequence integer); "
            + "CREATE TABLE fish (id integer auto_increment, name varchar,"
            + "family varchar, desc varchar, latin_name varchar, common_name varchar, image blob);";

    private Connection mConnection;

    public void setup() {
        try {
            Class.forName("org.h2.Driver");
            mConnection = DriverManager.getConnection(URL, USER, PWD);

            Statement st = mConnection.createStatement();
            st.execute(CREATE_TABLE_SQL);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void shutDown() {
        try {
            mConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet execute(String sql) {
        try {
            Statement st = mConnection.createStatement();
            st.execute(sql);
            return st.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Connection getConnection() {
        return mConnection;
    }

    public void addFamily(Family f) {
        PreparedStatement ps;
        try {
            ps = mConnection.prepareStatement("INSERT INTO " + TABLE_FAMILY + " (name) Values (?)");

            ps.setString(1, f.getName());

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addFish(Fish f) {
        PreparedStatement ps;
        try {
            ps = mConnection.prepareStatement("INSERT INTO " + TABLE_FISH + " (name, family, desc, latin_name, common_name, image) Values (?, ?, ?, ?, ?, ?)");

            ps.setString(1, f.getName());
            ps.setString(2, f.getFamily().getName());
            ps.setString(3, f.getDesc());
            ps.setString(4, f.getLatinName());
            ps.setString(5, f.getCommonName());

            String imageFile = f.getImageFile();
            if (imageFile != null && !imageFile.isEmpty() && new File(imageFile).exists()) {
                ps.setBinaryStream(6, new FileInputStream(f.getImageFile()));
            } else {
                ps.setBinaryStream(6, null);
            }

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
