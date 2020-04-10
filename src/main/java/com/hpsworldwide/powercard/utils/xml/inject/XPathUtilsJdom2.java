package com.hpsworldwide.powercard.utils.xml.inject;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * https://gist.github.com/bmchild/6285705#file-xpathutils-java &
 * https://gist.github.com/bmchild/6285705#file-driverappxmlserviceimpl-java
 *
 * @author bchild (https://gist.github.com/bmchild)
 */
public class XPathUtilsJdom2 {

    private static final Logger LOG = LoggerFactory.getLogger(XPathUtilsJdom2.class);
    private static final String SLASH = "/";
    private static final String R_BRACKET = "]";
    private static final String L_BRACKET = "[";

    /**
     * Recursive method to create an element and, if necessary, its parents and
     * siblings
     *
     * @param document
     * @param xpath to single element
     * @param value if null an empty element will be created
     * @return the created Node
     */
    public static Element addElementToParent(Document document, String xpath, String value) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("adding Element: " + xpath + " -> " + value);
        }

        String elementName = getChildElementName(xpath);
        String parentXPath = getParentXPath(xpath);
        Element parentNode = getElement(parentXPath, document);
        if (parentNode == null) {
            parentNode = addElementToParent(document, parentXPath, null);
        }

        // create younger siblings if needed
        Integer childIndex = getChildElementIndex(xpath);
        if (childIndex > 1) {
            XPathExpression<?> xPath = XPathFactory.instance().compile(createPositionXpath(xpath, childIndex));
            List<?> nodeList = xPath.evaluate(document);
            // how many to create = (index wanted - existing - 1 to account for the new element we will create)
            int nodesToCreate = childIndex - nodeList.size() - 1;
            for (int i = 0; i < nodesToCreate; i++) {
                ((Element) parentNode).addContent(new Element(elementName));
            }
        }

        // create requested element
        Element created = ((Element) parentNode).addContent(new Element(elementName));
        if (null != value) {
            created.setText(value);
        }
        return created;
    }

    private static Element getElement(String xPath, Document document) {
        return XPathFactory.instance().compile(xPath, Filters.element()).evaluateFirst(document);
    }

    /**
     * Looks for the last '/' and returns the name of the last element
     *
     * @param xpath
     * @return the child element name or null
     */
    public static final String getChildElementName(String xpath) {
        if (StringUtils.isEmpty(xpath)) {
            return null;
        }
        String childName = xpath.substring(xpath.lastIndexOf(SLASH) + 1);
        return stripIndex(childName);
    }

    /**
     * returns the xpath if traversing up the tree one node i.e.
     * /root/suspension_rec returns /root
     *
     * @param xpath
     * @return
     */
    public static final String getParentXPath(String xpath) {
        if (StringUtils.isEmpty(xpath) || xpath.lastIndexOf(SLASH) <= 0) {
            return null;
        }
        return xpath.substring(0, xpath.lastIndexOf(SLASH));
    }

    /**
     * returns the index of the child element xpath i.e. /suspension_rec[3]
     * returns 3. /suspension_rec defaults to 1
     *
     * @param xpath
     * @return 1, the index, or null if the provided xpath is empty
     */
    public static Integer getChildElementIndex(String xpath) {
        if (StringUtils.isEmpty(xpath)) {
            return null;
        }

        if (xpath.endsWith(R_BRACKET)) {
            String value = xpath.substring(xpath.lastIndexOf(L_BRACKET) + 1, xpath.lastIndexOf(R_BRACKET));
            if (StringUtils.isNumeric(value)) {
                return Integer.valueOf(value);
            }
        }
        return 1;
    }

    /**
     * @param xpath
     * @param childIndex
     * @return
     */
    public static String createPositionXpath(String xpath, Integer childIndex) {
        if (StringUtils.isEmpty(xpath)) {
            return null;
        }
        return stripIndex(xpath) + "[position()<" + childIndex + "]";
    }

    /**
     * @param childName
     * @return
     */
    private static String stripIndex(String childName) {
        if (childName.endsWith(R_BRACKET)) {
            return childName.substring(0, childName.lastIndexOf(L_BRACKET));
        } else {
            return childName;
        }
    }
}
