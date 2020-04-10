package com.hpsworldwide.powercard.utils.xml;

/**
 * a simple wrapper for adding syntax content to a generic Exception indicates
 * that the XML document submitted isn't correct compared to an XML Schema
 *
 * @author Hightech Payment Systems (HPS)
 */
public class InvalidXmlDocumentException extends Exception {

    InvalidXmlDocumentException(Exception ex) {
        super(ex);
    }
}
