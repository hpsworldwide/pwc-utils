package com.hpsworldwide.powercard.utils.xml.inject;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * https://gist.github.com/bmchild/6285705#file-xpathutils-java &
 * https://gist.github.com/bmchild/6285705#file-driverappxmlserviceimpl-java
 *
 * @author bchild (https://gist.github.com/bmchild)
 */
public class XPathUtils {

    private static final Logger LOG = LoggerFactory.getLogger(XPathUtils.class);
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
    public static Node addElementToParent(Document document, String xpath, String value) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("adding Element: " + xpath + " -> " + value);
        }

        String elementName = XPathUtils.getChildElementName(xpath);
        String parentXPath = XPathUtils.getParentXPath(xpath);
        Node parentNode = document.selectSingleNode(parentXPath);
        if (parentNode == null) {
            parentNode = addElementToParent(document, parentXPath, null);
        }

        // create younger siblings if needed
        Integer childIndex = XPathUtils.getChildElementIndex(xpath);
        if (childIndex > 1) {
            List<?> nodelist = document.selectNodes(XPathUtils.createPositionXpath(xpath, childIndex));
            // how many to create = (index wanted - existing - 1 to account for the new element we will create)
            int nodesToCreate = childIndex - nodelist.size() - 1;
            for (int i = 0; i < nodesToCreate; i++) {
                ((Element) parentNode).addElement(elementName);
            }
        }

        // create requested element
        Element created = ((Element) parentNode).addElement(elementName);
        if (null != value) {
            created.addText(value);
        }
        return created;
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
