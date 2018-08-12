package com.httpclient;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

/**
 * 
 * 
 * This example demonstrates the use of the {@link ResponseHandler} to simplify
 * the process of processing the HTTP response and releasing associated resources.
 */
public class ClientWithResponseHandler {

	/**
	 * 普通的get方式调用
	 * @throws Exception
	 * 
	 */
    public final static void commonServiceGet() throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet("http://www.baidu.com/");

            System.out.println("Executing request " + httpget.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            String responseBody = httpclient.execute(httpget, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
        } finally {
            httpclient.close();
        }
    }

    /**
     * 绕过ssl 的get方式调用
     * @throws Exception
     */
    public final static void sslServiceGet() throws Exception {

        String body = "";

        //采用绕过验证的方式处理https请求  
        SSLContext sslcontext = createIgnoreVerifySSL();  

        //设置协议http和https对应的处理socket链接工厂的对象  
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()  
            .register("http", PlainConnectionSocketFactory.INSTANCE)  
            .register("https", new SSLConnectionSocketFactory(sslcontext))  
            .build();  
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);  
        HttpClients.custom().setConnectionManager(connManager); 


        //创建自定义的httpclient对象  
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();  
        //CloseableHttpClient client = HttpClients.createDefault();  

        try{
            //创建get方式请求对象  
            HttpGet get = new HttpGet("https://www.baidu.com/");

            //指定报文头Content-type、User-Agent  
            get.setHeader("Content-type", "application/x-www-form-urlencoded");  
            get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");

            //执行请求操作，并拿到结果（同步阻塞）  
            CloseableHttpResponse response = client.execute(get);  

            //获取结果实体  
            HttpEntity entity = response.getEntity(); 
            if (entity != null) {  
                //按指定编码转换结果实体为String类型  
                body = EntityUtils.toString(entity, "UTF-8");  
            }  

            EntityUtils.consume(entity);  
            //释放链接  
            response.close(); 
            System.out.println("body:" + body);
        } finally{
            client.close();
       }
    }
    
    /**
     * 绕过ssl 的post方式调用
     * @param args
     * @throws Exception
     */
    public final static void sslServidePost() throws Exception {

        String body = "";

        //采用绕过验证的方式处理https请求  
        SSLContext sslcontext = createIgnoreVerifySSL();  
        //设置协议http和https对应的处理socket链接工厂的对象  
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()  
            .register("http", PlainConnectionSocketFactory.INSTANCE)  
            .register("https", new SSLConnectionSocketFactory(sslcontext))  
            .build();  
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);  
        HttpClients.custom().setConnectionManager(connManager); 

        //创建自定义的httpclient对象  
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();  
        //CloseableHttpClient client = HttpClients.createDefault();

        try{
            //创建post方式请求对象  
            HttpPost httpPost = new HttpPost("https://api.douban.com/v2/book/1220562"); 


            //指定报文头Content-type、User-Agent
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");  

            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");


            //执行请求操作，并拿到结果（同步阻塞）  
            CloseableHttpResponse response = client.execute(httpPost);  

            //获取结果实体  
            HttpEntity entity = response.getEntity(); 
            if (entity != null) {  
                //按指定编码转换结果实体为String类型  
                body = EntityUtils.toString(entity, "UTF-8");  
            }  

            EntityUtils.consume(entity);  
            //释放链接  
            response.close(); 
            System.out.println("body:" + body);
        }finally{
            client.close();
        }
    }
    
    /** 
    * 绕过验证 
    *   
    * @return 
    * @throws NoSuchAlgorithmException  
    * @throws KeyManagementException  
    */  
    public static SSLContext createIgnoreVerifySSL() throws Exception {  
            SSLContext sc = SSLContext.getInstance("SSLv3");  

            // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法  
            X509TrustManager trustManager = new X509TrustManager() {  
                @Override  
                public void checkClientTrusted(  
                        java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
                        String paramString) throws CertificateException {  
                }  

                @Override  
                public void checkServerTrusted(  
                        java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
                        String paramString) throws CertificateException {  
                }  

                @Override  
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
                    return null;  
                }  
            };  

            sc.init(null, new TrustManager[] { trustManager }, null);  
            return sc;  
        }
}