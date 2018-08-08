package com.lucene.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/**
 * SolrJ管理
 * 添加
 * 删除
 * 修改
 * 查询
 * @author SuperWang
 * @version 创建时间：2017年11月12日 下午10:29:55
 */
public class SolrJManager {

	//添加
	@Test
	public void testname() throws Exception{
		String baseURL = "http://localhost:8080/solr/";
		SolrServer solrServer = new HttpSolrServer(baseURL);
		SolrInputDocument doc = new SolrInputDocument();
		doc.setField("", "");
		doc.setField("", "");
		solrServer.add(doc);
		solrServer.commit();
	}
	//删除
	public void testDelete() throws Exception{
		String baseURL = "http://localhost:8080/solr/";
		SolrServer solrServer = new HttpSolrServer(baseURL);
		solrServer.deleteByQuery("*:*", 1000);
	}
	//查询
	public void testSearch() throws Exception{
		String baseURL = "http://localhost:8080/solr/";
		SolrServer solrServer = new HttpSolrServer(baseURL);
		
		SolrQuery solrQuery = new SolrQuery();
		QueryResponse response = solrServer.query(solrQuery);
		//wendanger结果集
		SolrDocumentList results = response.getResults();
		//总条数
		results.getNumFound();
	}
	
}
