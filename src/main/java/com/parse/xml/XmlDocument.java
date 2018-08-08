package com.parse.xml;

/**
 * 
 * @author SuperWang
 * @version 创建时间：2017年5月25日 上午11:44:49
 */
public interface XmlDocument {
	/** 
	* 建立XML文档 
	* @param fileName 文件全路径名称 
	*/
	public void createXml(String fileName) throws Exception;

	/** 
	* 解析XML文档 
	* @param fileName 文件全路径名称 
	*/
	public void parserXml(String fileName)throws Exception;
}