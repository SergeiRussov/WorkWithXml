package util;

import entity.Plant;
import lombok.extern.slf4j.Slf4j;

import javax.xml.stream.*;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class StaxParser {

    public List<Plant> parseToList(String fileName) {

        log.debug("Парсинг XML в лист");

        List<Plant> plantsList = new ArrayList<Plant>();
        Plant plant = null;
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

        try {

            XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(fileName));

            while (reader.hasNext()) {
                XMLEvent xmlEvent = reader.nextEvent();

                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();

                    if (startElement.getName().getLocalPart().equals("PLANT")) {
                        plant = new Plant();
                    } else if (startElement.getName().getLocalPart().equals("COMMON")) {
                        xmlEvent = reader.nextEvent();
                        plant.setCommon(xmlEvent.asCharacters().getData());
                    } else if (startElement.getName().getLocalPart().equals("BOTANICAL")) {
                        xmlEvent = reader.nextEvent();
                        plant.setBotanical(xmlEvent.asCharacters().getData());
                    } else if (startElement.getName().getLocalPart().equals("ZONE")) {
                        xmlEvent = reader.nextEvent();
                        plant.setZone(xmlEvent.asCharacters().getData());
                    } else if (startElement.getName().getLocalPart().equals("LIGHT")) {
                        xmlEvent = reader.nextEvent();
                        plant.setLight(xmlEvent.asCharacters().getData());
                    } else if (startElement.getName().getLocalPart().equals("PRICE")) {
                        xmlEvent = reader.nextEvent();
                        plant.setPrice(xmlEvent.asCharacters().getData());
                    } else if (startElement.getName().getLocalPart().equals("AVAILABILITY")) {
                        xmlEvent = reader.nextEvent();
                        plant.setAvailability(Long.parseLong(xmlEvent.asCharacters().getData()));
                    }
                }

                if (xmlEvent.isEndElement()) {
                    EndElement endElement = xmlEvent.asEndElement();
                    if (endElement.getName().getLocalPart().equals("PLANT")) {
                        plantsList.add(plant);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            log.debug(e.getMessage());
        } catch (XMLStreamException e) {
            log.debug(e.getMessage());
        }

        log.debug("Парсинг прошел успешно");
        return plantsList;
    }
}
