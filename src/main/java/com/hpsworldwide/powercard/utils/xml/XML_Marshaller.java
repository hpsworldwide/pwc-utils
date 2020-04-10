package com.hpsworldwide.powercard.utils.xml;

import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

/**
 * @author (c) HPS Solutions
 */
public class XML_Marshaller<T> {

    private final Unmarshaller unmarshaller;
    private final Marshaller marshaller;
    private final Class targetClass;
    private static final String NAMESPACE_URI = XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI;

    public XML_Marshaller(Class targetClass, boolean isFormattedOutput) throws JAXBException {
        this.targetClass = targetClass;
        JAXBContext jaxbContext = JAXBContext.newInstance(targetClass);
        unmarshaller = jaxbContext.createUnmarshaller();
        marshaller = jaxbContext.createMarshaller();
        if (isFormattedOutput) {
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        }
    }

    public T unmarshall(InputStream xmlInputStream) throws JAXBException {
        return (T) unmarshaller.unmarshal(new StreamSource(xmlInputStream), targetClass).getValue();
    }

    public void marshall(T t, OutputStream xmlOutputStream) throws JAXBException {
        JAXBElement<T> jaxbElement = new JAXBElement<>(new QName(NAMESPACE_URI, targetClass.getSimpleName()), targetClass, t);
        marshaller.marshal(jaxbElement, xmlOutputStream);
    }

    public void marshall(JAXBElement<T> jaxbElement, OutputStream xmlOutputStream) throws JAXBException {
        marshaller.marshal(jaxbElement, xmlOutputStream);
    }
}
