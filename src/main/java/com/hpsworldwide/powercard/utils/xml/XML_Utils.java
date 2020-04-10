package com.hpsworldwide.powercard.utils.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author (c) HPS Solutions
 */
public class XML_Utils {

    private static final SimpleDateFormat PARSE_ISO_DATE_ZONE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    private static final FastDateFormat ENCODE_ISO_DATE_ZONE_FORMAT = DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT;
    private static final SimpleDateFormat PARSE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final FastDateFormat ENCODE_DATE_FORMAT = DateFormatUtils.ISO_DATETIME_FORMAT;
    private static final DecimalFormat AMOUNT_FORMAT = new DecimalFormat("###.00", DecimalFormatSymbols.getInstance(Locale.US));

    /**
     * @param date format : yyyy-MM-dd'T'HH:mm:ssXXX
     */
    public static Date parseISO8601DateTimeZone(String date) throws ParseException {
        return PARSE_ISO_DATE_ZONE_FORMAT.parse(date);
    }

    /**
     * @return format : yyyy-MM-dd'T'HH:mm:ssXXX
     */
    public static String encodeISO8601DateTimeZone(Date date) {
        return ENCODE_ISO_DATE_ZONE_FORMAT.format(date);
    }

    /**
     * @param date format : yyyy-MM-dd'T'HH:mm:ss
     */
    public static Date parseDateTime(String date) throws ParseException {
        return PARSE_DATE_FORMAT.parse(date);
    }

    /**
     * @return format : yyyy-MM-dd'T'HH:mm:ss
     */
    public static String encodeDateTime(Date date) {
        return ENCODE_DATE_FORMAT.format(date);
    }

    public static double parseAmount(String amount) {
        return Double.parseDouble(amount);
    }

    public static String encodeAmount(double amount) {
        return AMOUNT_FORMAT.format(amount);
    }

    public static XMLGregorianCalendar toXmlGregorianCalendar(Date date) {
        try {
            GregorianCalendar gregCalend = new GregorianCalendar();
            gregCalend.setTime(date);
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCalend);
        } catch (DatatypeConfigurationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static byte[] nodeToByteArray(Node node, boolean omitXmlDeclaration) throws IOException, TransformerConfigurationException, TransformerException {
        Source source = new DOMSource(node);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Result result = new StreamResult(out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            if (omitXmlDeclaration) {
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            }
            transformer.transform(source, result);
            return out.toByteArray();
        }
    }

    public static Document readDocument(InputStream is) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(is);
    }

    // TODO : tester fonction
    public static Node getSingleNode(Node srcNode, String strXPath) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodes = (NodeList) xPath.evaluate(strXPath, srcNode, XPathConstants.NODESET);
        int nbNodes = nodes.getLength();
        if (nbNodes != 1) {
            throw new RuntimeException("[" + nbNodes + "] node(s) found instead of 1");
        }
        return nodes.item(0);
    }
    
    public static String getTextContent(Node srcNode, String strXPath) throws XPathExpressionException {
        return getSingleNode(srcNode, strXPath).getTextContent();
    }
    
    public static byte[] getByteArrayContent(Node srcNode, String strXPath) throws XPathExpressionException {
        return Base64.decodeBase64(getSingleNode(srcNode, strXPath).getTextContent());
    }

}
