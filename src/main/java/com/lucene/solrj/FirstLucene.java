package com.lucene.solrj;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 
 * @author SuperWang
 * @version 创建时间：2017年11月1日 下午11:45:31
 * 创建索引
 * 查询索引
 */
public class FirstLucene {

	//创建索引
	
	public void testIndex() throws Exception {
		//1.创建一个indexwriter对象(作用:该对象存入库中)
		//定位索引坐在目录
		Directory directory = FSDirectory.open(new File("E:/WorkSpace/Eclipse/Lucene/temp/index"));//硬盘索引
		//Directory directory =  new RAMDirectory(); 内存索引
		//创建分词器;
		StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
		//创建索引配置(版本,分词器)
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3, standardAnalyzer);
		//创建索引流
		IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
		//	1.1指定索引库的位置
		//	1.2指定一个分析器,对文档内容分析;
		//2.创建一个document对象
		//3.创建field对象
		File file = new File("E:/Develop/Information/XmindPictureData/Unix/基础/shell/脚本/分发");
		File[] listFile = file.listFiles();
		for (File f : listFile) {
			Document document = new Document();
			//文件名称
			String file_name = f.getName();
			Field fileNameField = new TextField("fileName", file_name, Store.YES);
			//文件大小
			long file_size = FileUtils.sizeOf(f);
			Field fileSizeField = new LongField("fileSize", file_size, Store.YES);
			//文件路径
			String file_path = f.getPath();
			Field filePathField = new StoredField("filePath", file_path);
			//文件内容
			String file_content = FileUtils.readFileToString(f);
			Field fileContentField = new TextField("fileContent", file_content, Store.YES);
			document.add(fileNameField);
			document.add(fileSizeField);
			document.add(filePathField);
			document.add(fileContentField);
			//4.使用indexwriter对象document对象写入库中,此过程进行索引创建,并将索引和document对象写入索引库
			indexWriter.addDocument(document);
		}
		//5.关流
		indexWriter.close();
	}

	//查询索引
	@Test
	public void testSearch() throws Exception {
		//1.创建一个Dirctory,也就是索引的位置
		Directory directory = FSDirectory.open(new File("E:/WorkSpace/Eclipse/Lucene/temp/index"));
		//2.创建 indexReader对象,需要指定Directory对象
		IndexReader indexReader = DirectoryReader.open(directory);
		//3.创建一个indexsearch对象,需要指定IndexReader对象
		IndexSearcher indexSearch = new IndexSearcher(indexReader);
		//4.创建一个TermQuery,指定查询的域和查询的关键词
		Query query = new TermQuery(new Term("fileName", "sh"));
		//5.执行查询
		TopDocs topDocs = indexSearch.search(query, 2);
		//6.遍历结果,得到所有索引
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		for (ScoreDoc scoreDoc : scoreDocs) {
			//得到索引id
			int doc = scoreDoc.doc;
			//通过索引id获取文档
			Document document = indexSearch.doc(doc);
			//文件路径
			String fileName = document.get("fileName");
			String fileContent = document.get("fileContent");
			String fileSize = document.get("fileSize");
			String filePath = document.get("filePath");
			System.out.println(fileName);
			System.out.println(fileContent);
			System.out.println(fileSize);
			System.out.println(filePath);
			System.out.println("----------------------------------------------");
		}
		//7.关流
		indexReader.close();
	}

	
	// 查看标准分析器的分词效果
	public void testTokenStream() throws Exception {
		// 创建一个标准分析器对象
		//老外使用
		//Analyzer analyzer = new StandardAnalyzer();
		//最早使用的中日韩分词器
		//Analyzer analyzer = new CJKAnalyzer();
		//聪明的分词器,扩展性差,放弃
		//Analyzer analyzer = new SmartChineseAnalyzer();
		//第三方中文分词器()IKAnalyzer();
		Analyzer analyzer = new IKAnalyzer();
		// 获得tokenStream对象
		// 第一个参数：域名，可以随便给一个
		// 第二个参数：要分析的文本内容
		//			TokenStream tokenStream = analyzer.tokenStream("test",
		//					"The Spring Framework provides a comprehensive programming and configuration model.");
		TokenStream tokenStream = analyzer.tokenStream("test", "高富帅可以用二维表结构来逻辑表达实现的数据");
		// 添加一个引用，可以获得每个关键词
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		// 添加一个偏移量的引用，记录了关键词的开始位置以及结束位置
		OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
		// 将指针调整到列表的头部
		tokenStream.reset();
		// 遍历关键词列表，通过incrementToken方法判断列表是否结束
		while (tokenStream.incrementToken()) {
			// 关键词的起始位置
			System.out.println("start->" + offsetAttribute.startOffset());
			// 取关键词
			System.out.println(charTermAttribute);
			// 结束位置
			System.out.println("end->" + offsetAttribute.endOffset());
		}
		tokenStream.close();
	}
}
