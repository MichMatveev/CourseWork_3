package courseWorks.db;

import java.sql.*;

public class DataBase {
    private static String USER_NAME = "postgres",
            PASSWORD = "12345",
            URL = "jdbc:postgresql://localhost:5432/",
            DB_NAME = "coursework";

    public DataBase() {

    }

    public DataBase(String user_name, String password, String url, String db_name) {
        USER_NAME = user_name;
        PASSWORD = password;
        URL = url;
        DB_NAME = db_name;
    }

    private static Connection connect() {
        try {
            return DriverManager.getConnection(URL + DB_NAME, USER_NAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CONNECTION ERROR");
            return null;
        }
    }

    public static void printStudents() {
        try (Connection connection = connect()) {
            System.out.println("Students:");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.student");

            System.out.printf("%-10s %-30s %-30s %-30s\n", "id", "Second Name", "First Name", "Patronymic");
            while (resultSet.next()) {
                System.out.printf(
                        "%-10s %-30s %-30s %-30s%n",
                        resultSet.getString("id"),
                        resultSet.getString("second_name"),
                        resultSet.getString("first_name"),
                        resultSet.getString("patronymic")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("pSQL PRINT ERROR");
        }
    }

    public static void addStudent(String second_name, String first_name, String patronymic) {
        if (checkValidityStudent(second_name, first_name, patronymic) &&
                !checkStudent(second_name, first_name, patronymic)) {
            try (Connection connection = connect()) {
                Statement statement = connection.createStatement();
                statement.execute(String.format(
                        "INSERT INTO student (second_name, first_name, patronymic) " +
                                "VALUES ('%s', '%s', '%s')", second_name, first_name, patronymic));
                System.out.println("Adding was successful");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("pSQL ADD ERROR");
            }
        } else
            System.out.println("This student is present in the system");
    }

    private static boolean checkValidityStudent(String second_name, String first_name, String patronymic) {
        if (second_name != null && first_name != null)
            return true;
        else {
            System.out.println("Second name or First name not valid");
            return false;
        }
    }

    private static boolean checkStudent(String second_name, String first_name, String patronymic) {
        try (Connection connection = connect()) {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(String.format(
                    "SELECT * FROM student " +
                    "WHERE second_name = '%s' AND first_name = '%s' AND patronymic = '%s'",
                    second_name,first_name, patronymic));

            if (resultSet != null) {
                resultSet.last();
                if (resultSet.getRow() > 0)
                    return true;
                else
                    return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("pSQL CHECK STUDENT ERROR");
        }

        return true;
    }
}
