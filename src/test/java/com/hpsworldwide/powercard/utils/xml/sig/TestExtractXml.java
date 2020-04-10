package com.hpsworldwide.powercard.utils.xml.sig;

import ch.qos.logback.core.encoder.ByteArrayUtil;
import com.hpsworldwide.powercard.utils.xml.XML_Utils;
import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;

/**
 *
 * @author (c) HPS Solutions
 */
public class TestExtractXml {

    public static void main(String[] args) throws Exception {
        testFile("D:\\data\\ISO20022\\caaa.003.AccptrAuthstnReq_1.xml");
        testFile("D:\\data\\ISO20022\\caaa.003.AccptrAuthstnReq_1 - Copie.xml");
    }

    private static boolean testFile(String filePath) throws Exception {
        File fXmlFile = new File(filePath);
        byte[] baFullDoc = FileUtils.readFileToByteArray(fXmlFile);
        Document doc;
        try (FileInputStream fileInputStream = new FileInputStream(fXmlFile)) {
            doc = XML_Utils.readDocument(fileInputStream);
        }
        byte[] ba = XML_Utils.nodeToByteArray(doc.getDocumentElement().getElementsByTagName("AuthstnReq").item(0), true);
        System.out.println(new String(ba));
        boolean containsOK = ByteArrayUtil.toHexString(baFullDoc).contains(ByteArrayUtil.toHexString(ba));
        System.out.println(ByteArrayUtil.toHexString(baFullDoc));
        System.out.println(ByteArrayUtil.toHexString(ba));
        System.out.println("containsOK? " + containsOK);
        return containsOK;
    }

}
