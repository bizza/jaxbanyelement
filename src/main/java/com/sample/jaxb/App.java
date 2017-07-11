package com.sample.jaxb;

import org.tempuri.purchaseorderschema.*;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;

/**
 * Created by bizza on 11.07.17.
 */
public class App {

    public static void main(String... args) {

        String payloadAsString = "<payload><item><name>Sample</name><code>SI-1234</code><price>123</price><store></store></item></payload>";

        StringWriter sw = new StringWriter();

        USAddress billTo = createUSAddress("San Francisco", "US", "CA", "Free Street", "Bill Smith", "12345");
        USAddress shipTo = createUSAddress("Dallas", "US", "TX", "Oil Street", "Walter White", "6543");

        ShipAddresses shipAddresses = new ObjectFactory().createShipAddresses();
        shipAddresses.getShipTos().add(shipTo);

        PurchaseOrder purchaseOrderType = new ObjectFactory().createPurchaseOrder();
        purchaseOrderType.setBillTo(billTo);
        purchaseOrderType.setShipToList(shipAddresses);


        PayloadType payloadType = new ObjectFactory().createPayloadType();
        Element node = null;
        try {
            node = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(new ByteArrayInputStream(payloadAsString.getBytes()))
                    .getDocumentElement();
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        payloadType.setAny(node);

        purchaseOrderType.setPayload(payloadType);


        try {
            JAXBContext context = JAXBContext.newInstance(PurchaseOrder.class);

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(purchaseOrderType, sw);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        String returnValue = sw.toString();

        System.out.println(returnValue);

    }

    private static USAddress createUSAddress(String city, String country, String state, String street, String name, String zip) {
        USAddress usAddress = new ObjectFactory().createUSAddress();
        usAddress.setCity(city);
        usAddress.setCountry(country);
        usAddress.setState(state);
        usAddress.setStreet(street);
        usAddress.setName(name);
        BigInteger zipCode = new BigInteger(zip);
        usAddress.setZip(zipCode);
        return usAddress;
    }
}
