package validator;

import lombok.extern.slf4j.Slf4j;
import org.xml.sax.SAXException;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

@Slf4j
public class ValidatorXml {

    public boolean validateXml(String source, String xsdSchema) {

        log.debug("Валидация документа " + source);

        Schema schema = null;
        try {
            SchemaFactory factory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            schema = factory.newSchema(new File(xsdSchema));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(source)));
            System.out.println("valid");
            log.debug(source + " is valid");
            return true;
        } catch (SAXException e) {
            System.out.println("not valid");
            log.debug(source + "is not valid");
            return false;
        } catch (IOException e) {
            log.debug(source + " не найден");
            return false;
        }
    }
}
