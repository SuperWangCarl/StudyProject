package com.parse.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAX implements XmlDocument{
	public void createXml(String fileName) {
		System.out.println("<<" + fileName + ">>");
	}
	public void parserXml(String fileName) {
		//String fileName = "D:/WorkSpace/Eclipse/Utils/src/com/parsexml/book.xml";
		//获得sax工厂
		SAXParserFactory saxfac = SAXParserFactory.newInstance();
		try {
			//创建一个解析器
			SAXParser saxparser = saxfac.newSAXParser();
			//获得输入流
			InputStream is = new FileInputStream(fileName);
			//按照一定的解析规则对输入流进行解析
			saxparser.parse(is, new MySAXHandler());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class MySAXHandler extends DefaultHandler {
	boolean hasAttribute = false;
	Attributes attributes = null;

	public void startDocument() throws SAXException {
		System.out.println("文档开始打印了");
	}

	public void endDocument() throws SAXException {
		System.out.println("文档打印结束了");
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		System.out.println("uri " + uri );
		System.out.println("localName " + localName );
		System.out.println("qName " + qName );
		System.out.println("attributes " + attributes );
		
		if (qName.equals("employees")) {
			return;
		}
		if (qName.equals("employee")) {
			System.out.println(qName );
		}
		if (attributes.getLength() > 0) {
			this.attributes = attributes;
			this.hasAttribute = true;
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (hasAttribute && (attributes != null)) {
			for (int i = 0; i < attributes.getLength(); i++) {
				System.out.println(attributes.getQName(0) + attributes.getValue(0));
			}
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		System.out.println(new String(ch, start, length));
	}
}

class SAXParserHandler extends DefaultHandler {
    String value = null;
    Book book = null;
    private ArrayList<Book> bookList = new ArrayList<Book>();
    public ArrayList<Book> getBookList() {
        return bookList;
    }

    int bookIndex = 0;
    /**
     * 用来标识解析开始
     */
    @Override
    public void startDocument() throws SAXException {
        // TODO Auto-generated method stub
        super.startDocument();
        System.out.println("SAX解析开始");
    }
    
    /**
     * 用来标识解析结束
     */
    @Override
    public void endDocument() throws SAXException {
        // TODO Auto-generated method stub
        super.endDocument();
        System.out.println("SAX解析结束");
    }
    
    /**
     * 解析xml元素
     */
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        //调用DefaultHandler类的startElement方法
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals("book")) {
            bookIndex++;
            //创建一个book对象
            book = new Book();
            //开始解析book元素的属性
            System.out.println("======================开始遍历某一本书的内容=================");
            //不知道book元素下属性的名称以及个数，如何获取属性名以及属性值
            int num = attributes.getLength();
            for(int i = 0; i < num; i++){
                System.out.print("book元素的第" + (i + 1) +  "个属性名是："
                        + attributes.getQName(i));
                System.out.println("---属性值是：" + attributes.getValue(i));
                if (attributes.getQName(i).equals("id")) {
                    book.setId(attributes.getValue(i));
                }
            }
        }
        else if (!qName.equals("name") && !qName.equals("bookstore")) {
            System.out.print("节点名是：" + qName + "---");
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        //调用DefaultHandler类的endElement方法
        super.endElement(uri, localName, qName);
        //判断是否针对一本书已经遍历结束
        if (qName.equals("book")) {
            bookList.add(book);
            book = null;
            System.out.println("======================结束遍历某一本书的内容=================");
        }
        else if (qName.equals("name")) {
            book.setName(value);
        }
        else if (qName.equals("author")) {
            book.setAuthor(value);
        }
        else if (qName.equals("year")) {
            book.setYear(value);
        }
        else if (qName.equals("price")) {
            book.setPrice(value);
        }
        else if (qName.equals("language")) {
            book.setLanguage(value);
        }
    }
    
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        // TODO Auto-generated method stub
        super.characters(ch, start, length);
        value = new String(ch, start, length);
        if (!value.trim().equals("")) {
            System.out.println("节点值是：" + value);
        }
    }
}