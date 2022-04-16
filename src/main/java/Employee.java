import com.google.gson.*;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Employee {
    public long id;
    public String firstName;
    public String lastName;
    public String country;
    public int age;

    public Employee() {
        // Пустой конструктор
    }

    public Employee(long id, String firstName, String lastName, String country, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.age = age;
    }

    public String toString() {
        return "Employee {" +
                "id=" + this.id + ", " +
                "firstName='" + this.firstName + "', " +
                "lastName='" + this.lastName + "', " +
                "country='" + this.country + "', " +
                "age=" + this.age + "}";
    }

    public static List<Employee> deserializeFromCSV(String[] columnMapping, String fileName) {
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            //strategy for deserialize
            ColumnPositionMappingStrategy<Employee> strategyEmployee =
                    new ColumnPositionMappingStrategy<>();
            strategyEmployee.setType(Employee.class);
            strategyEmployee.setColumnMapping(columnMapping);

            //deserialize
            CsvToBean<Employee> employeeBean = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategyEmployee)
                    .build();

            return employeeBean.parse();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    public static List<Employee> deserializeFromXML(String fileName) {
        try {
            Document xml = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new File("data.xml"));
            NodeList nodes = xml.getDocumentElement().getChildNodes();

            List<Employee> result = new ArrayList<>();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node n = nodes.item(i);
                if (Node.ELEMENT_NODE == n.getNodeType() && n.getNodeName().equals("employee")) {
                    Element person = (Element) nodes.item(i);
                    try {
                        result.add(new Employee(
                                Long.parseLong(person.getElementsByTagName("id").item(0).getTextContent()),
                                person.getElementsByTagName("firstName").item(0).getTextContent(),
                                person.getElementsByTagName("lastName").item(0).getTextContent(),
                                person.getElementsByTagName("country").item(0).getTextContent(),
                                Integer.parseInt(person.getElementsByTagName("age").item(0).getTextContent())
                        ));
                    } catch (NumberFormatException e) {
                        result.add(new Employee(
                                0,
                                person.getElementsByTagName("firstName").item(0).getTextContent(),
                                person.getElementsByTagName("lastName").item(0).getTextContent(),
                                person.getElementsByTagName("country").item(0).getTextContent(),
                                0
                        ));
                    }
                }
            }
            return result;
        } catch (ParserConfigurationException | IOException | SAXException | IllegalArgumentException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    public static String listToJSON(List<Employee> employeeList) {
        return new GsonBuilder().create().toJson(employeeList);
    }

    public static List<Employee> deserializeFromJSON (String fileName) {
        JsonParser jsonParser = new JsonParser();
        Gson gson = new GsonBuilder().create();
        List<Employee> result = new ArrayList<>();

        try (Reader fr = new FileReader(fileName)) {
            Object obj = jsonParser.parse(fr);
            JsonArray jsonArray = (JsonArray) obj;

            for (JsonElement jsonElement : jsonArray) {
                result.add(gson.fromJson(jsonElement, Employee.class));
            }

            return result;
        } catch (IOException | ClassCastException e) {
            return null;
        }
    }
}