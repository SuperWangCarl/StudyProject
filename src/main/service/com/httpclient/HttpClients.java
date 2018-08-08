package com.httpclient;

public class HttpClients {

	public static void main(String[] args) throws Exception {
		/*String api_key = "9ae12c9bce7d72de3636344eb2e7e51d";
		long t = System.currentTimeMillis();
		//String url = "http://ott.hd.sohu.com/hd/demo/drama.xml?t=" + t +"&api_key=" + api_key;
		String url = "https://bo.ixx-youku.com/20180403/ERVhmaG0/index.m3u8";
		HttpClient http = new HttpClient();
		GetMethod get = new GetMethod(url);
		int resultCode = http.executeMethod(get);
		System.out.println(resultCode);
		String str = get.getResponseBodyAsString();
		String[] split = str.split("\n");
		System.out.println(Arrays.toString(split));
		//InputStream input = get.getResponseBodyAsStream();
		//input.
		//System.out.println(input.read());
		System.out.println("----------------------------------------------");
		//String xml = get.getResponseBodyAsString();
		//System.out.println(xml);
		//Data data = parseXmlByDOM4J(input);
		//System.out.println(data.getAlbumList().size());
*/	
		
		String url = "https://www.ixxplayer.com/video.php?url=https://bo.ixx-youku.com/20180403/ERVhmaG0/index.m3u8&type=ckm3u8";
		String m3u8url = url.substring(url.lastIndexOf("url=")+4, url.lastIndexOf("&type"));
		String prefix = url.substring(url.lastIndexOf("url=")+4, url.lastIndexOf(".com/")+4);
		System.out.println(m3u8url);
		System.out.println(prefix);
	}
}
