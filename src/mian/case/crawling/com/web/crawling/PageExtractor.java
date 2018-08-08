package com.web.crawling;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.jmx.snmp.Timestamp;

public class PageExtractor {

	//爬虫间隔
	private static int INTERVAL_SLEEP_TIME = 2;
	//存放历史爬取的视频名
	private static List<String> nameList = null;
	//视频 名称 地址 爬取后所存放的位置
	private static String CURRENT_VIDEO_PATH = "E:/Test/craw_current/";
	//历史所爬过的文件所放的位置
	private static String HISTORY_VIDEO_PATH = "E:/Test/craw_history";
	//处理后的ffmpeg文件所存放的位置
	private static String FFMPEG_VIDEO_PATH = "E:/Test/ffmpeg_url/";
	//视频存放位置
	private static String VIDEO_SRC_PATH = "E:/Test/91video/";
	/**
	 * jsoup 只可以获取当前页面
	 * @param args
	 * @throws Exception 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		/*
		 * **************种类 1:自拍  2:剧情 3:中文 4:AV 5:欧美 6:无码 7:有码 8:动漫 9:女女 10:群
		 * http://91bb91.com/index.php?c=whole&key=
		 * **************自拍
		 * 最热
		 * http://91bb91.com/index.php?c=whole&key=1~~~~~~~0~hits~1
		 * 最新
		 * http://91bb91.com/index.php?c=whole&key=1~~~~~~~0~id~1
		 */
		//爬取的页数
		int maxPage = 10;
		//类型
		int key = 10;
		//最热---hits   最新---id
		String flag = "hits";
		String[] typeArr = { "", "自拍", "剧情", "中文", "AV", "欧美", "无码", "有码", "动漫", "女女", "群" };

		// 类型    最热/最新    爬的最大页数   时间戳
		String name = typeArr[key] + "--" + ("hits".equals(flag) ? "最热" : "最新") + "--" + maxPage + "--" + new Timestamp().getDateTime() + ".txt";

		PrintWriter pw = new PrintWriter(CURRENT_VIDEO_PATH + name);
		for (int i = 1; i < maxPage; i++) {
			String url = "http://91bb91.com/index.php?c=whole&key=" + key + "~~~~~~~0~" + flag + "~" + i;
			//将爬出的内容输出到指定路径
			System.out.println("***********************第" + i + "页***********************************");
			pw.println("***********************第" + i + "页***********************************");

			try {
				Connection connect = Jsoup.connect(url);

				//TimeUnit.SECONDS.sleep(new Random().nextInt(INTERVAL_SLEEP_TIME));
				Document doc = connect.get();

				//TimeUnit.SECONDS.sleep(new Random().nextInt(INTERVAL_SLEEP_TIME));
				String cssQuery = "[class=well well-sm] a";
				Elements select = doc.select(cssQuery);

				//得到首页的各个链接
				for (Element e : select) {
					//TimeUnit.SECONDS.sleep(new Random().nextInt(INTERVAL_SLEEP_TIME));
					try {
						String usrSeconf = e.attr("abs:href");
						Connection connectSecond = Jsoup.connect(usrSeconf);
						Document docSecond = connectSecond.get();

						String cssQuerySecondName = "#wrapper > div.container > div:nth-child(1) > div > h3";
						Elements selectSecondName = docSecond.select(cssQuerySecondName);
						String videoName = selectSecondName.first().text();
						//视频不存在是 下载
						//if (!isExistFileByName(videoName)) {
							System.out.println(videoName);
							pw.println(videoName);

							String cssQuerySecondHref = "#flash > iframe";
							Elements selectSecondHref = docSecond.select(cssQuerySecondHref);

							TimeUnit.SECONDS.sleep(new Random().nextInt(INTERVAL_SLEEP_TIME));
							String usrThird = selectSecondHref.first().attr("src");
							//读取解密后的 m3u8地址
							readIndexM3u8(usrThird, pw);
							System.out.println("--------------------------------------------------------------------------------------------");
							pw.println("--------------------------------------------------------------------------------------------");
							pw.flush();
						//} else {
						//	System.out.println(videoName);
						//}
					} catch (Exception ex) {
						System.out.println("------------------------------------End----------------------------------------------");
						ex.printStackTrace();
					}
				}
			} catch (Exception e) {
				System.out.println("------------------------------------End----------------------------------------------");
				e.printStackTrace();
			}
		}
		pw.close();
	}

	/**
	 * 读取 文本 获取消息体的加密内容
	 * @param url
	 * @param pw
	 * @throws Exception
	 */
	public static void readIndexM3u8(String url, PrintWriter pw) throws Exception {
		try {
			String m3u8url = url.substring(url.lastIndexOf("url=") + 4, url.lastIndexOf("&type"));
			//String prefix = url.substring(url.lastIndexOf("url=") + 4, url.lastIndexOf(".com/") + 4);
			System.out.println(m3u8url);
			pw.println(m3u8url);
			/*
			if (m3u8url.lastIndexOf("index.m3u8") != -1) {
				String prefix = m3u8url.substring(0, m3u8url.lastIndexOf("index.m3u8"));
				//System.out.println(m3u8url);
				//System.out.println(prefix);
				//System.out.println("================================================");
				//System.out.println(m3u8url);
				HttpClient http = new HttpClient();
				GetMethod get = new GetMethod(m3u8url);
				int resultCode = http.executeMethod(get);
				String str = get.getResponseBodyAsString();

				String[] split = str.split("\n");
				String videoM3U8Url = prefix + split[2];
				System.out.println(videoM3U8Url);
				pw.println(videoM3U8Url);
			}
			*/
		} catch (Exception e) {

		}
	}

	/**
	 * 文件 ffmpeg下载
	 * @throws Exception
	 */
	@Test
	public void parseFileFfmpeg() throws Exception {
		//源路径
		File file = new File(CURRENT_VIDEO_PATH);
		File[] files = file.listFiles();
		for (File f : files) {
			//文件内容处理后的所放路径
			String fileName = f.getName();
			String videoDownPath = VIDEO_SRC_PATH +  fileName.subSequence(0, fileName.lastIndexOf(".txt"));
			new File(videoDownPath).mkdir();
			PrintWriter pw = new PrintWriter(new File(FFMPEG_VIDEO_PATH + fileName));
			PrintWriter pwv = new PrintWriter(new File(videoDownPath + "/" +fileName));
			Scanner sc = new Scanner(f);
			String urlName = "";
			String url = "";
			while (sc.hasNext()) {
				/**
				 * ffmpeg -i "https://bo.ixx-youku.com/20180401/BXR6Rzzr/1000kb/hls/index.m3u8" -c copy "年后和96女孩第一次.mp4"
				 */
				String str = sc.nextLine();
				if (str.startsWith("http") && str.endsWith(".m3u8")) {
					url = str;
				}
				if (!str.startsWith("http") && !str.startsWith("***") && !str.startsWith("----") && !str.startsWith("　")) {
					urlName = replaceVideoName(str);

				}
				if (str.startsWith("----") && !"".equals(url) && !"".equals(urlName)) {
					String context = " -i \"" + url + "\" -c copy \"" + urlName + ".mp4\"";
					System.out.println(context);
					pw.println(context);
					pwv.println(context);
					urlName = "";
					str = "";
					pw.flush();
					pwv.flush();
				}
			}
			pw.close();
			pwv.close();
			sc.close();
		}

	}

	/**
	 * 清洗文本名称  去掉空格 和括号
	 * @param videoName
	 * @return
	 */
	public static String replaceVideoName(String videoName) {
		videoName = videoName.replaceAll("　", "");
		videoName = videoName.replaceAll("\\(", "");
		videoName = videoName.replaceAll("\\)", "");
		return videoName;
	}

	public static void traversal(String path) {
		//源路径
		File file = new File(path);
		File[] files = file.listFiles();
		for (File f : files) {
			if (f.isFile()) {
				String fileName = f.getName();
				if (fileName.endsWith(".mp4")) {
					nameList.add(fileName.substring(0, fileName.lastIndexOf(".mp4")));
					System.out.println(fileName);
				}
			} else {
				traversal(f.getPath());
			}
		}
	}

	/**
	 * 判断文件是否存在  
	 * 按照视频名称
	 * @throws Exception 
	 */
	public static boolean isExistFileByName(String videoName) throws Exception {
		videoName = replaceVideoName(videoName);

		//为空 将历史文件名 加载到list中
		if (nameList == null) {
			nameList = new ArrayList<String>(5000);
			traversal(VIDEO_SRC_PATH);
		}
		if (nameList.contains(videoName)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断文件是否存在  
	 * 按照文本内容判断
	 * @throws Exception 
	 */
	public static boolean isExistFileByContent(String videoName) throws Exception {
		//为空 将历史文件名 加载到list中
		if (nameList == null) {
			nameList = new ArrayList<String>(5000);
			//源路径
			File file = new File(HISTORY_VIDEO_PATH);
			File[] files = file.listFiles();
			for (File f : files) {
				Scanner sc = new Scanner(f);
				String urlName = "";
				String url = "";
				while (sc.hasNext()) {
					String str = sc.nextLine();
					if (str.startsWith("http") && str.endsWith(".m3u8")) {
						url = str;
					}
					if (!str.startsWith("http") && !str.startsWith("***") && !str.startsWith("----") && !str.startsWith("　")) {
						urlName = str;
					}

					if (str.startsWith("----") && !"".equals(url) && !"".equals(urlName)) {
						nameList.add(urlName);
						//System.out.println(urlName);
						urlName = "";
					}
				}
				sc.close();
			}
		}
		if (nameList.contains(videoName)) {
			return true;
		}
		return false;
	}

	/**
	 * 文件读取  wget 下载
	 * @throws Exception
	 */
	@Test
	public void parseFileWget() throws Exception {
		/*wget -O /etc/yum.repos.d/epel.repo http://mirrors.aliyun.com/repo/epel-6.repo */
		Scanner sc = new Scanner(new File("E:/Test/file/自拍-最新-30.txt"));
		String urlName = "";
		while (sc.hasNext()) {
			/**
			 * ffmpeg -i "https://bo.ixx-youku.com/20180401/BXR6Rzzr/1000kb/hls/index.m3u8" -c copy "年后和96女孩第一次.mp4"
			 */
			String str = sc.nextLine();
			if (str.startsWith("http")) {
				System.out.println(urlName + str + "  --no-check-certificate");
				urlName = "";
			}
			if (!str.startsWith("http") && !str.startsWith("***") && !str.startsWith("----") && !str.startsWith("　")) {
				str = str.replaceAll("　", "");
				str = str.replaceAll("\\(", "");
				str = str.replaceAll("\\)", "");
				urlName = "wget -O ./" + str + "  ";
				System.out.println();
			}
		}
		sc.close();
	}

	/**
	 * htmlunit
	 * 加载动态js 获取执行js方法的返回值   可以获得  js渲染后的页面
	 * @param urlVideo
	 * @return
	 */
	private static String getVidoUrl(String urlVideo) {
		String url = "";
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		try {

			webClient.getOptions().setCssEnabled(true);
			webClient.getOptions().setJavaScriptEnabled(true); // 设置支持JavaScript。  
			// 去拿网页  
			HtmlPage htmlPage = webClient.getPage(urlVideo);
			System.out.println("--------------=====================---------");
			// String s = "更改后的数字";  
			ScriptResult t = htmlPage.executeJavaScriptIfPossible("player.analysedVideoUrl().VA", "injected script", 500);
			//ScriptResult t = htmlPage.executeJavaScriptIfPossible(" document.getElementById(\"suggest_memo\")", "injected script", 500);  
			// 这里是在500行插入这一小行JavaScript代码段，因为如果在默认的1行，那不会有结果  
			// 因为js是顺序执行的，当你执行第一行js代码时，可能还没有渲染body里面的标签。  
			HtmlPage myPage = (HtmlPage) t.getNewPage();

			String nextPage = myPage.asXml().toString();
			String nextPage2 = myPage.asText().toString();

			System.out.println(nextPage);
			System.out.println(nextPage2);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			webClient.close();
		}
		return url;
	}

	/**
	 * htmlunit  获得  js渲染后的页面
	 * @throws Exception
	 */
	public static void testUserHttpUnit(String urlVideo) throws Exception {
		//urlVideo = "https://bo.ixx-youku.com/20180403/aNBMFsVc/index.m3u8";
		/** HtmlUnit请求web页面 */
		WebClient wc = new WebClient(BrowserVersion.CHROME);
		wc.getOptions().setUseInsecureSSL(true);
		wc.getOptions().setJavaScriptEnabled(true); // 启用JS解释器，默认为true  
		wc.getOptions().setCssEnabled(false); // 禁用css支持  
		wc.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常  
		wc.getOptions().setTimeout(100000); // 设置连接超时时间 ，这里是10S。如果为0，则无限期等待  
		wc.getOptions().setDoNotTrackEnabled(false);
		HtmlPage page = wc.getPage(urlVideo);

		System.out.println(page.asXml());
		/* DomNodeList<DomElement> links = page.getElementsByTagName("a");  
		
		 for (DomElement link : links) {  
		     System.out  
		             .println(link.asText() + "  " + link.getAttribute("href"));  
		 }  */
	}

	/** 
	 * 通过htmlunit来获得一些搜狗的网址。 
	 * 通过模拟鼠标点击事件来实现 
	 * @param key 
	 * @return 
	 * @throws Exception 
	 */
	public String getNextUrl(String key) {
		String page = new String();
		try {
			WebClient webClient = new WebClient();
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			//去拿网页  
			HtmlPage htmlPage = webClient.getPage("http://pic.sogou.com/");
			//得到表单  
			HtmlForm form = htmlPage.getFormByName("searchForm");
			//得到提交按钮  
			HtmlSubmitInput button = form.getInputByValue("搜狗搜索");
			//得到输入框  
			HtmlTextInput textField = form.getInputByName("query");
			//输入内容  
			textField.setValueAttribute(key);
			//点一下按钮  
			HtmlPage nextPage = button.click();
			String str = nextPage.toString();
			//page = cutString(str);  
			webClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}

	public String getNextUrl2(String key) {
		try {
			WebClient webClient = new WebClient();
			webClient.getOptions().setJavaScriptEnabled(true); //启用JS解释器，默认为true    
			webClient.getOptions().setCssEnabled(false); //禁用css支持        
			webClient.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常     
			webClient.getOptions().setTimeout(20000);
			HtmlPage page = webClient.getPage("http://www.hao123.com");
			//我认为这个最重要  
			String pageXml = page.asXml(); //以xml的形式获取响应文本    

			/**jsoup解析文档*/
			Document doc = Jsoup.parse(pageXml, "http://cq.qq.com");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
