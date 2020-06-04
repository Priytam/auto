package com.auto.framework.utils;

import com.auto.framework.reporter.TestReporter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/*
@author : 'Vipul Popli'
*/
public class XMLUtils {

    private final Document xmlDocument;

    private static final XmlMapper xmlMapper = createXmlMapper();


    public XMLUtils(String xmlString) {
        this.xmlDocument = getXmlDocument(xmlString);
    }

    public List<Double> getDoubleValues(String tagName) {
        return getValues(tagName).stream().map(val -> Double.parseDouble(val.trim())).collect(Collectors.toList());
    }

    public List<Long> getLongValues(String tagName) {
        return getValues(tagName).stream().map(val -> Long.parseLong(val.trim())).collect(Collectors.toList());
    }

    public List<Integer> getIntValues(String tagName) {
        return getValues(tagName).stream().map(val -> Integer.parseInt(val.trim())).collect(Collectors.toList());
    }

    public List<Boolean> getBooleanValues(String tagName) {
        return getValues(tagName).stream().map(val -> Boolean.parseBoolean(val.trim())).collect(Collectors.toList());
    }

    public List<String> getValues(String tagName) {
        List<String> values = Lists.newArrayList();
        List<Node> nodeList = getNodeList(tagName);
        nodeList.forEach(node -> values.add(node.getTextContent()));
        return values;
    }

    public Map<String, List<String>> getValues(List<String> tagName) {
        Map<String, List<String>> valuesMap = Maps.newHashMap();
        tagName.forEach(tag -> valuesMap.put(tag, getValues(tag)));
        return valuesMap;
    }

    private List<Node> getNodeList(String tagName) {
        NodeList elementsByTagName = xmlDocument.getElementsByTagName(tagName);
        return castNodeList(elementsByTagName);
    }

    private static List<Node> castNodeList(NodeList elementsByTagName) {
        List<Node> nodeList = Lists.newArrayList();
        if (Objects.nonNull(elementsByTagName)) {
            for (int i = 0; i < elementsByTagName.getLength(); i++) {
                nodeList.add(elementsByTagName.item(i));
            }
        }
        return nodeList;
    }

    public Document getXmlDocument() {
        return xmlDocument;
    }

    public static Document getXmlDocument(String xmlString) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document xmlDocument = dBuilder.parse(new InputSource(new StringReader(xmlString)));
            xmlDocument.getDocumentElement().normalize();
            return xmlDocument;
        } catch (Exception e) {
            TestReporter.TRACE(e);
            throw new RuntimeException(e);
        }
    }

    private static XmlMapper createXmlMapper() {
        return new XmlMapper();
    }

    public static XmlMapper getXmlMapper() {
        return xmlMapper;
    }

    public static <T> T deSerialize(String xml, Class<T> tClass) {
        try {
            return xmlMapper.readValue(xml, tClass);
        } catch (Exception e) {
            throw new RuntimeException("Exception deSerialize xml " + xml, e);
        }
    }

    public static <T> T deSerialize(InputStream in, Class<T> tClass) {
        try {
            return xmlMapper.readValue(in, tClass);
        } catch (Exception e) {
            throw new RuntimeException("Exception deSerialize xml", e);
        }
    }

    public static <T> String serialize(T obj) {
        try {
            return xmlMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Exception serialising xml", e);
        }
    }
}
