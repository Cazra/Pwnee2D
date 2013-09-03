package pwnee.util;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;


/** A class for making it easier to parse XML and process XML elements. */
public class XMLParser {
  
  /** Produces DocumentBuilders for this class's methods to use. */
  private static DocumentBuilder getDocumentBuilder() throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    return factory.newDocumentBuilder();
  }
  
  /** Parses an XML Document from a file. If it is successful, the element node for the document is returned. Otherwise it returns null. */
  public static Element parse(File f) {
    try {
      DocumentBuilder docBuilder = getDocumentBuilder();
      Document doc =  docBuilder.parse(f);
      Element docElem = doc.getDocumentElement();
      docElem.normalize();
      return docElem;
    }
    catch(Exception e) {
      return null;
    }
  }
  
  
  /** 
   * Gets the first instance of a child Element (immediate descendant) with the 
   * specified type contained in some Element. 
   */
  public static Element getChild(Element parent, String type) {
    try {
      NodeList nl = parent.getChildNodes();
      for(int i = 0; i < nl.getLength(); i++) {
        Node node = nl.item(i);
        if(node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(type)) {
          return (Element) nl.item(i);
        }
      }
      
      return null;
    }
    catch(Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  
  /** 
   * Gets the list of children Elements (immediate descendants) with the 
   * specified type contained in some Element. 
   * This is returned as a List for easy iteration, rather than as a NodeList. 
   */
  public static List<Element> getChildren(Element parent, String type) {
    try {
      List<Element> result = new ArrayList<>();
      
      NodeList nl = parent.getChildNodes();
      for(int i = 0; i < nl.getLength(); i++) {
        Node node = nl.item(i);
        if(node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(type)) {
          result.add((Element) node);
        }
      }
      
      return result;
    }
    catch(Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  
  /** Gets all the child Elements of an Element, returned as a List for easy iteration. */
  public static List<Element> getChildren(Element parent) {
    try {
      List<Element> result = new ArrayList<>();
      
      NodeList nl = parent.getChildNodes();
      for(int i = 0; i < nl.getLength(); i++) {
        Node node = nl.item(i);
        if(node.getNodeType() == Node.ELEMENT_NODE) {
          result.add((Element) node);
        }
      }
      
      return result;
    }
    catch(Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  
  /** Gets the value of an attribute for an Element, if the attribute exists. If it doesn't exist, return "". */
  public static String getAttribute(Element elem, String name) {
    try {
      return elem.getAttribute(name);
    }
    catch(Exception e) {
      e.printStackTrace();
      return "";
    }
  }
  
  
  
  /** Gets the text wrapped by an Element, or "" if it doesn't wrap text.*/
  public static String getText(Element elem) {
    try {
      return elem.getTextContent();
    }
    catch(Exception e) {
      e.printStackTrace();
      return "";
    }
  }
  
  
  /** Returns the entire contents of an Node, in text form. */
  public static String getContent(Node node) {
    try {
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      StringWriter buffer = new StringWriter();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.transform(new DOMSource(node), new StreamResult(buffer));
      return buffer.toString();
    }
    catch(Exception e) {
      e.printStackTrace();
      return "";
    }
  }
  
  
  public static void main(String[] args) {
    String path = args[0];
    
    Element doc = XMLParser.parse(new File(path));
    
    System.out.println(doc.getNodeName());
    
    System.out.println("--------------");
    
    for(Element child : XMLParser.getChildren(doc)) {
      System.out.println(child.getNodeName() + "\n" + XMLParser.getContent(child) + "\n");
    }
    
  }
  
}
