import java.io.*;
import java.util.List;

public class Main {
    static final String[] COLUMN_MAPPING = {"id", "firstName", "lastName", "country", "age"};
    static final String CSV_FILE_NAME = "data.csv";
    static final String JSON_FILE_NAME = "data.json";
    static final String JSON_2_FILE_NAME = "data2.json";
    static final String XML_FILE_NAME = "data.xml";

    public static void main(String[] args) {
        /**Task 1**/
        /**CSV - JSON парсер**/
//        List<Employee> staff = Employee.deserializeFromCSV(COLUMN_MAPPING, CSV_FILE_NAME);
//        String json = Employee.listToJSON(staff);
//        writeStringToFile(json, JSON_FILE_NAME);
        writeStringToFile(Employee.listToJSON(Employee.deserializeFromCSV(COLUMN_MAPPING, CSV_FILE_NAME)), JSON_FILE_NAME);

        /**Task 2**/
        /**XML - JSON парсер**/
//        List<Employee> staff = Employee.deserializeFromXML(XML_FILE_NAME);
//        String json = Employee.listToJSON(staff);
//        writeStringToFile(json, JSON_2_FILE_NAME);
        writeStringToFile(Employee.listToJSON(Employee.deserializeFromXML(XML_FILE_NAME)), JSON_2_FILE_NAME);

        /**Task 3**/
        /**JSON парсер**/
        List<Employee> staff = Employee.deserializeFromJSON(JSON_FILE_NAME);
        for (Employee employee : staff) {
            System.out.println(employee);
        }
    }

    public static boolean writeStringToFile(String content, String fileName) {
        File file = new File(fileName);
        try (Writer fw = new FileWriter(file)) {
            if (file.isFile() && !file.exists()) {
                file.createNewFile();
            }

            fw.write(content);
            fw.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
