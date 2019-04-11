import entity.Author;
import entity.Book;
import entity.Plant;
import org.xml.sax.SAXException;
import util.DomParser;
import util.StaxParser;
import validator.ValidatorXml;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {

//      Чтение и вывод на экран XML файла
//      STAX
        List<Plant> plantsListStax = new StaxParser().parseToList("src/main/resources/plant_catalog.xml");

        System.out.println("Парсим через STAX");

        for (Plant plant : plantsListStax) {
            System.out.println(plant.toString());
        }

        System.out.println();

//      DOM
        List<Plant> plantsListDom = new DomParser().parseToList("src/main/resources/plant_catalog.xml");

        System.out.println("Парсим через DOM");

        for (Plant plant : plantsListDom) {
            System.out.println(plant.toString());
        }

        System.out.println();

        System.out.println("Модифицируем XML файл с растениями и сохраняем в отдельный XML");

        new DomParser().modifyXml("src/main/resources/plant_catalog.xml", "modify.xml", 1, "COMMON", "changed field");

        System.out.println();

        System.out.println("Создаем XML файл с книгами и проверяем его на соответствие XSD схеме");

        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(new Author("Василий", "Иванович", "Орехов"),
                375, "Зона поражения", "Питер"));
        bookList.add(new Book(new Author("Андрей", "Юрьевич", "Левицкий"),
                348, "Выбор оружия", "Питер"));
        bookList.add(new Book(new Author("Вячеслав", "Владимирович", "Шалыгин"),
                333, "Обратный отсчет", "Питер"));

        new DomParser().createXmlWithXsd(bookList, "result.xml");
        new ValidatorXml().validateXml("result.xml", "src/main/resources/books.xsd");
    }
}
