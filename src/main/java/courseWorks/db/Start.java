package courseWorks.db;

public class Start {
    public static void main(String[] args) {
        DataBase db = new DataBase();
        db.printStudents();
        db.addStudent("Пермяков", "Александр", "Александрович");
    }
}
