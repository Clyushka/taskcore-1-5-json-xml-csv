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

public class Main {
    public static void main(String[] args) {
        //gson
        Gson gson = new GsonBuilder().create();

        /**Task 1**/
        /**CSV - JSON парсер**/

        try (CSVReader csvReader = new CSVReader(new FileReader("data.csv"));
             Writer fw = new FileWriter("data.json")) {
            //strategy for deserialize
            ColumnPositionMappingStrategy<Employee> strategyEmployee =
                    new ColumnPositionMappingStrategy<>();
            strategyEmployee.setType(Employee.class);
            strategyEmployee.setColumnMapping("id", "firstName", "lastName", "country", "age");

            //deserialize
            CsvToBean<Employee> employeeBean = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategyEmployee)
                    .build();

            List<Employee> staff = employeeBean.parse();

            //write to file
            fw.write(gson.toJson(staff));
            fw.flush();
        } catch (IOException e) { // | CsvValidationException e) {
            System.out.println(e.getLocalizedMessage());
        }

        /**Task 2**/
        /**XML - JSON парсер**/
        try (Writer fw = new FileWriter("data2.json")) {
            Document xml = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new File("data.xml"));
            NodeList nodes = xml.getDocumentElement().getChildNodes();

            List<Employee> staff = new ArrayList<>();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node n = nodes.item(i);
                if (Node.ELEMENT_NODE == n.getNodeType() && n.getNodeName().equals("employee")) {
                    Element person = (Element) nodes.item(i);
                    try {
                        staff.add(new Employee(
                                Long.parseLong(person.getElementsByTagName("id").item(0).getTextContent()),
                                person.getElementsByTagName("firstName").item(0).getTextContent(),
                                person.getElementsByTagName("lastName").item(0).getTextContent(),
                                person.getElementsByTagName("country").item(0).getTextContent(),
                                Integer.parseInt(person.getElementsByTagName("age").item(0).getTextContent())
                        ));
                    } catch (NumberFormatException e) {
                        staff.add(new Employee(
                                0,
                                person.getElementsByTagName("firstName").item(0).getTextContent(),
                                person.getElementsByTagName("lastName").item(0).getTextContent(),
                                person.getElementsByTagName("country").item(0).getTextContent(),
                                0
                        ));
                    }
                }
            }

            fw.write(gson.toJson(staff));
            fw.flush();
        } catch (ParserConfigurationException | IOException | SAXException | IllegalArgumentException e) {
            System.out.println(e.getLocalizedMessage());
        }

        /**Task 3**/
        /**JSON парсер**/
        JsonParser jsonParser = new JsonParser();
        try (Reader fr = new FileReader("data.json")) {
            List<Employee> staff = new ArrayList<>();

            Object obj = jsonParser.parse(fr);
            JsonArray jsonArray = (JsonArray) obj;

            for (JsonElement jsonElement : jsonArray) {
                staff.add(gson.fromJson(jsonElement, Employee.class));
            }

            for (Employee employee: staff) {
                System.out.println(employee);
            }
        } catch (IOException | ClassCastException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
