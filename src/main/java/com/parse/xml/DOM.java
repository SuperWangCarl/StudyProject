package com.parse.xml;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException; 

public class DOM implements XmlDocument {
	private Document document;
	private String fileName;

	public void init() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.document = builder.newDocument();
		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
		}
	}

	public void createXml(String fileName) {
		Element root = this.document.createElement("employees");
		this.document.appendChild(root);
		Element employee = this.document.createElement("employee");
		Element name = this.document.createElement("name");
		name.appendChild(this.document.createTextNode("丁宏亮"));
		employee.appendChild(name);
		Element sex = this.document.createElement("sex");
		sex.appendChild(this.document.createTextNode("m"));
		employee.appendChild(sex);
		Element age = this.document.createElement("age");
		age.appendChild(this.document.createTextNode("30"));
		employee.appendChild(age);
		root.appendChild(employee);
		TransformerFactory tf = TransformerFactory.newInstance();
		try {
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "gb2312");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
			System.out.println("生成XML文件成功!");
		} catch (TransformerConfigurationException e) {
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (TransformerException e) {
			System.out.println(e.getMessage());
		}
	}

	public void parserXml(String fileName) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(fileName);
			NodeList employees = document.getChildNodes();
			for (int i = 0; i < employees.getLength(); i++) {
				Node employee = employees.item(i);
				NodeList employeeInfo = employee.getChildNodes();
				for (int j = 0; j < employeeInfo.getLength(); j++) {
					Node node = employeeInfo.item(j);
					NodeList employeeMeta = node.getChildNodes();
					for (int k = 0; k < employeeMeta.getLength(); k++) {
						//System.out.println(employeeMeta.item(k).getNodeName() + ":" + employeeMeta.item(k).getTextContent());
					}
				}
			}
			System.out.println("解析完毕");
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
		} catch (SAXException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	public void parserXml() throws Exception{
		 //创建一个DocumentBuilderFactory的对象
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //创建一个DocumentBuilder的对象
        //创建DocumentBuilder对象
        DocumentBuilder db = dbf.newDocumentBuilder();
        //通过DocumentBuilder对象的parser方法加载books.xml文件到当前项目下
        Document document = db.parse("books.xml");
        //获取所有book节点的集合
        NodeList bookList = document.getElementsByTagName("book");
        //通过nodelist的getLength()方法可以获取bookList的长度
        System.out.println("一共有" + bookList.getLength() + "本书");
        //遍历每一个book节点
        for (int i = 0; i < bookList.getLength(); i++) {
            System.out.println("=================下面开始遍历第" + (i + 1) + "本书的内容=================");
            //通过 item(i)方法 获取一个book节点，nodelist的索引值从0开始
            Node book = bookList.item(i);
            //获取book节点的所有属性集合
            NamedNodeMap attrs = book.getAttributes();
            System.out.println("第 " + (i + 1) + "本书共有" + attrs.getLength() + "个属性");
            //遍历book的属性
            for (int j = 0; j < attrs.getLength(); j++) {
                //通过item(index)方法获取book节点的某一个属性
                Node attr = attrs.item(j);
                //获取属性名
                System.out.print("属性名：" + attr.getNodeName());
                //获取属性值
                System.out.println("--属性值" + attr.getNodeValue());
            }
            //解析book节点的子节点
            NodeList childNodes = book.getChildNodes();
            //遍历childNodes获取每个节点的节点名和节点值
            System.out.println("第" + (i+1) + "本书共有" + 
            childNodes.getLength() + "个子节点");
            for (int k = 0; k < childNodes.getLength(); k++) {
                //区分出text类型的node以及element类型的node
                if (childNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
                    //获取了element类型节点的节点名
                    System.out.print("第" + (k + 1) + "个节点的节点名：" 
                    + childNodes.item(k).getNodeName());
                    //获取了element类型节点的节点值
                    System.out.println("--节点值是：" + childNodes.item(k).getFirstChild().getNodeValue());
                    //System.out.println("--节点值是：" + childNodes.item(k).getTextContent());
                }
            }
            System.out.println("======================结束遍历第" + (i + 1) + "本书的内容=================");
        }
	}
}
