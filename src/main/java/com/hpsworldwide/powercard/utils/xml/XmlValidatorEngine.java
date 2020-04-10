package com.hpsworldwide.powercard.utils.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @see
 * http://www.ibm.com/developerworks/xml/library/x-javaxmlvalidapi/index.html
 * @author Hightech Payment Systems (HPS)
 */
public class XmlValidatorEngine {

    private static final String SCHEMA_LANGUAGE = XMLConstants.W3C_XML_SCHEMA_NS_URI;
    private final Validator validator;
    private final DocumentBuilder parser;

    public XmlValidatorEngine(Schema xmlSchema) throws ParserConfigurationException {
        validator = xmlSchema.newValidator();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        parser = factory.newDocumentBuilder();
    }

    public XmlValidatorEngine(File xmlSchema) throws SAXException, ParserConfigurationException {
        this(SchemaFactory.newInstance(SCHEMA_LANGUAGE).newSchema(xmlSchema));
    }

    public XmlValidatorEngine(Source xmlSchema) throws SAXException, ParserConfigurationException {
        this(SchemaFactory.newInstance(SCHEMA_LANGUAGE).newSchema(xmlSchema));
    }

    public XmlValidatorEngine(Source[] xmlSchema) throws SAXException, ParserConfigurationException {
        this(SchemaFactory.newInstance(SCHEMA_LANGUAGE).newSchema(xmlSchema));
    }

    public XmlValidatorEngine(URL xmlSchema) throws SAXException, ParserConfigurationException {
        this(SchemaFactory.newInstance(SCHEMA_LANGUAGE).newSchema(xmlSchema));
    }

    /**
     * @see
     * http://forum.hardware.fr/hfr/Programmation/Java/comment-xml-xsd-sujet_97232_1.htm
     * the following warnings can be ignored for now : Error: Document is
     * invalid: no grammar found. Error: Document root element , must match
     * DOCTYPE root "null".
     */
    public void isValid(File xmlDocument) throws InvalidXmlDocumentException {
        try {
            isValid(parser.parse(xmlDocument));
        } catch (SAXException | IOException ex) {
            throw new InvalidXmlDocumentException(ex);
        }
    }

    public void isValid(InputStream xmlDocument) throws InvalidXmlDocumentException {
        try {
            isValid(parser.parse(xmlDocument));
        } catch (SAXException | IOException ex) {
            throw new InvalidXmlDocumentException(ex);
        }
    }

    public void isValid(InputSource xmlDocument) throws InvalidXmlDocumentException {
        try {
            isValid(parser.parse(xmlDocument));
        } catch (SAXException | IOException ex) {
            throw new InvalidXmlDocumentException(ex);
        }
    }

    public void isValid(Document xmlDocument) throws InvalidXmlDocumentException {
        try {
            validator.validate(new DOMSource(xmlDocument));
        } catch (SAXException | IOException ex) {
            throw new InvalidXmlDocumentException(ex);
        }
    }
}
