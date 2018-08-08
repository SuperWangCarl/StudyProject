package com.rabbitmq;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.rabbitmq.client.Channel;
import com.util.Constants;

public class MediaAssetsListener implements ChannelAwareMessageListener {

	public static void main(String[] args) {
		MediaAssetsListener l=new MediaAssetsListener();
	try {
		l.onMessage(null);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (DocumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	public String onMessage(Message message) throws IOException, DocumentException {
		String action="success";
		System.out.println(new Date()+"  1.获取工单");
		String assetInfo="";
		assetInfo = new String(message.getBody(), "utf-8");//utf-8待修改，根据具体设置
		
		
			//System.out.println("[" + CommonUtils.dateToString(new Date()) + "] receive: " + assetInfo);
		
			Document document = DocumentHelper.parseText(assetInfo);
			//跟元素
			Element root = document.getRootElement();
			//获得媒资类型和操作
			String assetdesc = root.elementText("assetdesc");// 1 代表是集合(clip); 2是媒资分集(clippart); 3文件内容注入(file)
			String assetoperation = root.elementText("assetoperation");// 1增加修改; 2 删除;3 取消发布
			System.out.println(new Date()+"  2.内容类型（1集合2分集3文件）："+assetdesc+"  执行的操作（1增加修改2 删除3 取消发布）："+assetoperation);
			Element content = root.element("content");
			String pushCode = root.element("info").elementText("pushcode");
			System.out.println(new Date()+"  3.工单号："+pushCode);
			String assetid = "";
			String partid = content.elementText("partid") == null ? "" : content.elementText("partid");
			String fileid = "";
			int result = -1;
			if ("1".equals(assetdesc)) {
				assetid = content.elementText("assetid") == null ? "" : content.elementText("assetid");
				//result = itemService.editItem(content ,assetoperation);
			} else if ("2".equals(assetdesc)) {
				partid = content.elementText("partid") == null ? "" : content.elementText("partid");
				//result = videoService.editVideo(content ,assetoperation);
			} else {
				//Map<String,Object> resultmap=videoService.editVideoChildren(content ,assetoperation);
				//fileid=resultmap.get("fileid").toString();
				//result=Integer.valueOf(resultmap.get("result").toString());
			}
			
			String response = null;
			if (result >= 0) {
				response = genResponseXml(assetdesc, 1, pushCode, assetid, partid, fileid, Constants.MESSAGE_SUCCESS);
			}else if(result==-2){ 
				response = genResponseXml(assetdesc, 0, pushCode, assetid, partid, fileid, Constants.MESSAGE_FAILED+":无海报");
			}else {
				response = genResponseXml(assetdesc, 0, pushCode, assetid, partid, fileid, Constants.MESSAGE_FAILED);
			}
			// 创建默认的httpClient实例.
			CloseableHttpClient httpclient = HttpClients.createDefault();
			// 创建httppost
			HttpPost httppost = new HttpPost(Constants.RESPONSE_URL);
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
	        formparams.add(new BasicNameValuePair("cmsresult", response));  
	        UrlEncodedFormEntity uefEntity;
	        uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
            httppost.setEntity(uefEntity);  
            System.out.println("executing request " + httppost.getURI());  
            CloseableHttpResponse resp = httpclient.execute(httppost);  
            try {  
                HttpEntity entity = resp.getEntity();  
                if (entity != null) {  
                    System.out.println("--------------------------------------");  
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));  
                    System.out.println("--------------------------------------");  
                }  
            } finally {  
            	resp.close();  
            }  
		
		return action;
	}

	private String genResponseXml(String assetdesc, int result, String pushCode,
			String assetid, String partid, String fileid, String desc) throws IOException {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("xmlresult");
		root.addElement("msgid").setText(pushCode);
		root.addElement("state").setText(String.valueOf(result));
		root.addElement("msg").setText(desc);
		Element info = root.addElement("info");
		info.addElement("cdn_id");
		info.addElement("mg_asset_type").setText(assetdesc);
		info.addElement("mg_asset_id").setText(assetid);
		info.addElement("mg_part_id").setText(partid);
		info.addElement("mg_file_id").setText(fileid);
		info.addElement("site_id");
		
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		StringWriter writer = new StringWriter();
		// 格式化输出流
		XMLWriter xmlWriter = new XMLWriter(writer, format);
		// 将document写入到输出流
		xmlWriter.write(document);
		xmlWriter.close();
		return writer.toString();
		
	}

	@Override
	public void onMessage(Message message, Channel channel) {
		String action="";
		try {
			action=this.onMessage(message);
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if (action.equals("success")){
				try {
					channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				try {
					channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		}
	
	}
	/*private static void getData(ConnectionFactory factory) {
		Connection connection;
		Channel channel = null;
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();

			// 读取队列，并且阻塞，即在读到消息之前在这里阻塞，直到等到消息，完成消息的阅读后，继续阻塞循环
			int i = 1;
			while (true) {
				String message="";
				Envelope envelope = null;
				GetResponse response = channel.basicGet(qe_name, false);
				if (response != null) {
					message = new String(response.getBody());
					envelope = response.getEnvelope();
				}else{
					continue;
				}
				// 应答
				//channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
			}
			
			 //关闭连接 
			channel.close(); 
			connection.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (TimeoutException e1) {
			e1.printStackTrace();
		}
	}*/

}
