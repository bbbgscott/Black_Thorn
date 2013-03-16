package com.ideoma.black_thorn;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {
	
	public Document ParseXMLToDoc(InputStream in) throws SAXException, IOException, ParserConfigurationException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document d =  builder.parse(in);
		return d;
	}
	
	public NodeList ParseDocByTagName(Document doc, String name)
	{
		Element docEle = doc.getDocumentElement();
		return docEle.getElementsByTagName(name);
	}
	
	public String GetTextValueByTagName(Element ele, String tagName)
	{
		NodeList nlist = ele.getElementsByTagName(tagName);
		if(nlist != null && nlist.getLength() > 0)
		{
			Element el = (Element) nlist.item(0);
			return el.getFirstChild().getNodeValue();
		}
		return null;
	}
	
	public String[] GetNodeListValues(NodeList list)
	{
		ArrayList<String> alist = new ArrayList<String>();
		if(list != null && list.getLength()>0)
		{
			for(int i = 0; i < list.getLength(); i++)
			{
				alist.add(list.item(i).getFirstChild().getNodeValue());
			}
		}
		String[] a = new String[alist.size()];
		a = alist.toArray(a);
		return a;
	}

}
