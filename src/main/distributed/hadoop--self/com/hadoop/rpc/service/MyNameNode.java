package com.hadoop.rpc.service;

import com.hadoop.rpc.protocol.ClientNamenodeProtocol;

public class MyNameNode implements ClientNamenodeProtocol{
	
	//模拟namenode的业务方法之一：查询元数据
	@Override
	public String getMetaData(String path){
		System.out.println();
		return path+": 3 - {BLK_1,BLK_2} ....";
		
	}
	

}
