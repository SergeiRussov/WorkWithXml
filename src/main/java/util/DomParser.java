package util;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import entity.Book;
import entity.Plant;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DomParser {

    public List<Plant> parseToList(String fileName) {

        log.debug("Парсинг XML в лист");

        List<Plant> plantsList = new ArrayList<>();
        Plant plant = null;
        DOMParser parser = new DOMParser();

        try {
            parser.parse(fileName);
        } catch (SAXException e) {
            log.debug(e.getMessage());
        } catch (IOException e) {
            log.debug(e.getMessage());
        }

        Document document = parser.getDocument();
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getElementsByTagName("PLANT");

        for (int i = 0; i < nodeList.getLength(); i++) {
            plant = new Plant();
            Element plantElement = (Element) nodeList.item(i);
            plant.setCommon(getSingleChild(plantElement, "COMMON").getTextContent().trim());
            plant.setBotanical(getSingleChild(plantElement, "BOTANICAL").getTextContent().trim());
            plant.setZone(getSingleChild(plantElement, "ZONE").getTextContent().trim());
            plant.setLight(getSingleChild(plantElement, "LIGHT").getTextContent().trim());
            plant.setPrice(getSingleChild(plantElement, "PRICE").getTextContent().trim());
            plant.setAvailability(Long.parseLong(getSingleChild(plantElement, "AVAILABILITY")
                    .getTextContent().trim()));

            plantsList.add(plant);
        }

        log.debug("Парсинг прошел успешно");
        return plantsList;
    }

    private Element getSingleChild(Element element, String childName) {

        NodeList nlist = element.getElementsByTagName(childName);
        Element child = (Element) nlist.item(0);

        return child;
    }

    public void modifyXml(String source, String resultPath, int index, String field, String data) {

        log.debug("Модификация xml файла");

        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(source);

            Node root = document.getFirstChild();
            Node plant = document.getElementsByTagName("PLANT").item(index);
            NodeList nodeList = plant.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node nextNode = nodeList.item(i);

                if (nextNode.getNodeName().equals(field)) {
                    nextNode.setTextContent(data);
                }
            }

            log.debug("Документ модифицирован");
            saveXml(document, resultPath);
        } catch (ParserConfigurationException e) {
            log.debug(e.getMessage());
        } catch (SAXException e) {
            log.debug(e.getMessage());
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
    }

    public void createXmlWithXsd(List<Book> books, String resultPath) {

        log.debug("Создание XML файла");

        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("Books");
            document.appendChild(root);

            for (Book book : books) {
                Element element = document.createElement("Book");
                root.appendChild(element);

                Element title = document.createElement("Title");
                title.appendChild(document.createTextNode(book.getTitle()));
                element.appendChild(title);

                Element author = document.createElement("Author");
                element.appendChild(author);

                Element firstName = document.createElement("FirstName");
                firstName.appendChild(document.createTextNode(book.getAuthor().getFirstName()));
                author.appendChild(firstName);

                Element lastName = document.createElement("LastName");
                lastName.appendChild(document.createTextNode(book.getAuthor().getLastName()));
                author.appendChild(lastName);

                Element secondName = document.createElement("SecondName");
                secondName.appendChild(document.createTextNode(book.getAuthor().getSecondName()));
                author.appendChild(secondName);

                Element publisher = document.createElement("Publisher");
                publisher.appendChild(document.createTextNode(book.getPublisher()));
                element.appendChild(publisher);

                Element numberOfPages = document.createElement("NumberOfPages");
                numberOfPages.appendChild(document.createTextNode(book.getNumberOfPages() + ""));
                element.appendChild(numberOfPages);
            }

            log.debug("Документ создан");
            saveXml(document, resultPath);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void saveXml(Document document, String resultPath) {

        log.debug("Сохранение документа в XML");

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("src/main/resources/" + resultPath));
            transformer.transform(domSource, streamResult);
        } catch (TransformerException e) {
            log.debug(e.getMessage());
        }

        log.debug("Документ сохранен");
        System.out.println("Документ сохранен");
    }

}
