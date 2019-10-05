package sample.net.sqlitetutorial;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

public class TableFunctions {
    static Path currentRelativePath = Paths.get("");
    static String s = currentRelativePath.toAbsolutePath().toString();
    static ArrayList<sample.net.sqlitetutorial.DVDReturn>dvdArray = new ArrayList<>();
    static ResultSet resultSet = null;
    public static Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:"+s+"\\src\\sample\\net\\sqlitetutorial\\dvd.db";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public static void delete(int id) {
        String sql = "DELETE FROM DVD WHERE id = ?";

        try (Connection connect = connect();
             PreparedStatement del = connect.prepareStatement(sql)) {
            del.setInt(1, id);
            del.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateRow(int id, String titel, String director, int year){
        String sql = "INSERT INTO DVD VALUES('"+id+"', '"+titel+"', '"+director+"', '"+year+"')";

        try (Connection connect = connect();
             PreparedStatement updateRow = connect.prepareStatement(sql)){
            updateRow.executeUpdate();

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void update(String index, String content, int id) {
        String sql = "UPDATE dvd SET "+index+"='"+content+"' WHERE id='"+id+"';";
        try (Connection connect = connect();
             PreparedStatement update = connect.prepareStatement(sql)) {
            update.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewTable() {
        String url = "jdbc:sqlite:"+s+"\\src\\sample\\net\\sqlitetutorial\\dvd.db";

        String sql = "CREATE TABLE IF NOT EXISTS DVD (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	titel text NOT NULL,\n"
                + "	director text NOT NULL,\n"
                + "	year integer NOT NULL\n"
                + ");";
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
            // create a new table
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static ResultSet getData(Connection connection){
        String sql = "Select * from DVD";
        try {
            PreparedStatement gettingData = connection.prepareStatement(sql);
            resultSet = gettingData.executeQuery();
            System.out.println(resultSet);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ArrayList<sample.net.sqlitetutorial.DVDReturn> createDVDList(ResultSet resultSet){
            try {
                while (resultSet.next()){
                    dvdArray.add(new sample.net.sqlitetutorial.DVDReturn(resultSet.getInt(1),resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4)));
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        return dvdArray;
    }
}