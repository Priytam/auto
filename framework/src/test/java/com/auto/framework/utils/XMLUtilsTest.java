package com.auto.framework.utils;


import com.auto.framework.check.Check;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.List;
import java.util.Map;

public class XMLUtilsTest {
    private final String xmlString;

    public XMLUtilsTest() {
        this.xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><bookstore><book category=\"cooking\"><title lang=\"en\">Everyday Italian</title><author>Giada De Laurentiis</author><year>2005</year><price>30.00</price></book><book category=\"children\"><title lang=\"en\">Harry Potter</title><author>J K. Rowling</author><year>2005</year><price>29.99</price></book><book category=\"web\"><title lang=\"en\">Learning XML</title><author>Erik T. Ray</author><year>2003</year><price>39.95</price></book></bookstore>";
    }

    @Test
    public void xmlDoc() {
        Document xmlDocument = XMLUtils.getXmlDocument(xmlString);  /*w3c document is created with xml string to access the different nodes of xml*/
        Check.assertEquals(3, xmlDocument.getElementsByTagName("book").getLength(), "Book count in xml mismatched");
    }

    @Test
    public void xmlMethods() {
        XMLUtils xmlUtils = new XMLUtils(xmlString);
        List<Double> price = xmlUtils.getDoubleValues("price");
        Check.assertTrue(price.contains(30.00) && price.contains(29.99) && price.contains(39.95), "Xml test price mismatched");
        List<Integer> year = xmlUtils.getIntValues("year");
        Check.assertTrue(year.contains(2005) && year.contains(2003), "Xml test year mismatched");
    }

    @Test
    public void xmlStaticMethodTest() {
        XMLUtils xmlUtils = new XMLUtils(xmlString);
        List<Node> nodes = xmlUtils.filterParentNodesByValue("year", "2005");
        Check.assertEquals(2, nodes.size(), "Xml test filter by year size mismatched");
        List<String> author = XMLUtils.getValues(nodes, "author");
        Check.assertTrue(author.contains("Giada De Laurentiis") && author.contains("J K. Rowling"), "Xml test book mismatched");
        List<Double> price = XMLUtils.getDoubleValues(nodes, "price");
        Check.assertTrue(price.contains(30.00) && price.contains(29.99), "Xml test price mismatched");
        Map<String, List<String>> map = XMLUtils.getValues(nodes, Lists.newArrayList("year", "price"));
        Check.assertTrue(map.containsKey("year") && map.containsKey("price"));
        Check.assertTrue(map.get("year").contains("2005"), "Xml test year mismatched");
        Check.assertTrue(map.get("price").contains("29.99"), "Xml test price mismatched");
    }

    @Test
    public void xmlMapperTest() {
        String xmlTestString = XMLUtils.serialize(new XmlTest());
        Check.assertEquals("<XmlTest>" +
                "<rollNumber>1</rollNumber>" +
                "<firstName>Xml</firstName>" +
                "<lastName>Test</lastName>" +
                "</XmlTest>", xmlTestString);
        XmlTest xmlTest = XMLUtils.deSerialize(xmlTestString, XmlTest.class);
        Check.assertEquals(1, xmlTest.getRollNumber(), "Xml Test roll no mismatched");
        Check.assertEquals("Xml", xmlTest.getFirstName(), "Xml Test fName mismatched");
        Check.assertEquals("Test", xmlTest.getLastName(), "Xml Test lName no mismatched");

    }

    private static class XmlTest {
        private int rollNumber = 1;
        private String firstName = "Xml";
        private String lastName = "Test";

        public int getRollNumber() {
            return rollNumber;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }
    }
}