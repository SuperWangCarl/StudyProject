package com.parse.xml;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.util.StringUtils;

public class XMLUtil {
	/**
	 * 
	 * @param seriesMapList 	item
	 * @param programMapList 	video
	 * @param movieMapList 		videoChild
	 * @param action 			操作类型:发布－REGIST 更新－UPDATE 审核－CHECK 删除－DELETE
	 */
	public static Map<String,String> creatXML(List seriesMapList, List programMapList, List movieMapList, String action,String url,String rukou){
		String cmid="";
		Map<String, String> rmap=new HashMap<String, String>();
		rmap.put("type", "1");
		for(Object sObj : seriesMapList){
			if (sObj instanceof Map) {
				Map sMap = (Map) sObj;
				cmid=(String)sMap.get("item_id");
				Document document = DocumentHelper.createDocument();	
				//ADI
				Element rootElement = document.addElement("ADI");
				//Objects
				Element objsElement = rootElement.addElement("Objects");
				//Mappings
				Element mapsElement = rootElement.addElement("Mappings");
				addSeriesElement(action, sMap, objsElement);
				for(Object pObj : programMapList) {
					Map pMap = null;
					if (pObj instanceof Map) {
						pMap = (Map) pObj;
					}
					//入口是为了聚合管理里面操作的时候用video_id做文件名
					if((!StringUtils.objToString(sMap.get("item_type")).equals("电影") && !StringUtils.objToString(sMap.get("item_type")).equals("电视剧")) || rukou.equals("2")){
						cmid=(String)pMap.get("video_id");
						rmap.put("type", "2");
					}
					rmap.put("cmid", cmid);
					if (((String)pMap.get("video_item_id")).equals((String)sMap.get("item_id"))) {
						addProgramElement(action, objsElement, pMap, sMap);
						Element pMappingElement = mapsElement.addElement("Mapping");
						pMappingElement.addAttribute("Action", action)
							.addAttribute("ParentType", "Series")
							.addAttribute("ElementType", "Program")
							.addAttribute("ParentID", StringUtils.objToString(sMap.get("item_id")))
							.addAttribute("ElementID", StringUtils.objToString(pMap.get("video_id")))
							.addAttribute("ParentCode", StringUtils.objToString(sMap.get("item_id")))
							.addAttribute("ElementCode", StringUtils.objToString(pMap.get("video_id")))
							.addElement("Property")
							.addAttribute("Name", "Type");
						
						for(Object mObj : movieMapList) {
							Map mMap = null;
							if (mObj instanceof Map) {
								mMap = (Map) mObj;
							}
							String flid=StringUtils.objToString(mMap.get("video_id"))+StringUtils.objToString(mMap.get("fileId"));
							if ((StringUtils.objToString(mMap.get("video_id"))).equals(StringUtils.objToString(pMap.get("video_id")))) {
								addMovieElement(action, objsElement, mMap);
								Element mMappingElement = mapsElement.addElement("Mapping");
								mMappingElement.addAttribute("Action", action)
								.addAttribute("ParentType", "Program")
								.addAttribute("ElementType", "Movie")
								.addAttribute("ParentID", StringUtils.objToString(pMap.get("video_id")))
								.addAttribute("ElementID", flid)
								.addAttribute("ParentCode", StringUtils.objToString(pMap.get("video_id")))
								.addAttribute("ElementCode", flid)
								.addElement("Property")
								.addAttribute("Name", "Type");
							}
						}
					}
					
				}
				XMLWriter writer = null;
				try{
					//后改
					File file = new File(url + cmid + ".xml");
					OutputFormat xmlFormat = new OutputFormat();
					 xmlFormat.setEncoding("UTF-8");
					 // 设置换行
					 xmlFormat.setNewlines(true);
					 // 生成缩进
					 xmlFormat.setIndent(true);
					 // 使用4个空格进行缩进, 可以兼容文本编辑器
					 xmlFormat.setIndent("    ");
					 writer = new XMLWriter(new FileWriter(file), xmlFormat);
					 writer.write(document);
					 writer.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		return rmap;
	}
	
	/**
	 * 添加Series Object节点
	 * @param action
	 * @param objsElement objects节点
	 * @param mMap
	 */
	private static void addMovieElement(String action, Element objsElement,
			Map mMap) {
		//Movie
		Element movie = objsElement.addElement("Object");
		//Movie attribute
		String flid=StringUtils.objToString(mMap.get("video_id"))+StringUtils.objToString(mMap.get("fileId"));
		movie.addAttribute("ElementType", "Movie")
			.addAttribute("ID", flid)
			.addAttribute("Action", action)
			.addAttribute("Code", flid);
		//Movie property
		String mMD5 = StringUtils.objToString(mMap.get("md5"));
		String mPlayURL = StringUtils.objToString(mMap.get("playUrl"));
		String mType = "";
		if(mPlayURL.trim().endsWith("ts")){
			mType = "1";
		} 
		if(mPlayURL.trim().endsWith("m3u8")){
			mType = "2";
		}
		if(mPlayURL.trim().endsWith("flv")){
			mType = "20";
		}
		if(mPlayURL.trim().endsWith("mp4")){
			mType = "30";
		}
		String Definition=StringUtils.objToString(mMap.get("rate"));
		if(Definition.equals("SD")){
			Definition="1";
		}else if(Definition.equals("CD")){
			Definition="1";
		}else if(Definition.equals("CD2")){
			Definition="2";
		}else if(Definition.equals("HD")){
			Definition="3";
		}else if(Definition.equals("FHD")){
			Definition="4";
		}else if(Definition.equals("LG")){
			Definition="5";
		}else if(Definition.equals("4K")){
			Definition="6";
		}
		String mHeadLength = "";//片头长度
		String mTailLength = "";//片尾长度
		String mDuration = "";
		
		movie.addElement("Property").addAttribute("Name","MD5").setText(mMD5);
		movie.addElement("Property").addAttribute("Name","Type").setText(mType);
		movie.addElement("Property").addAttribute("Name","PlayURL").setText(mPlayURL);
		movie.addElement("Property").addAttribute("Name","HeadLength").setText(mHeadLength);
		movie.addElement("Property").addAttribute("Name","TailLength").setText(mTailLength);
		movie.addElement("Property").addAttribute("Name","Duration").setText(mDuration);
		movie.addElement("Property").addAttribute("Name","Status").setText("0");
		movie.addElement("Property").addAttribute("Name","Definition").setText(Definition);
	}

	/**
	 * 添加Program Object节点
	 * @param action
	 * @param objsElement objects节点
	 * @param pMap
	 */
	private static void addProgramElement(String action, Element objsElement,
			Map pMap, Map sMap) {
		//program
		Element program = objsElement.addElement("Object");
		//program attribute
		program.addAttribute("ElementType", "Program")
			.addAttribute("ID", StringUtils.objToString(pMap.get("video_id")))
			.addAttribute("Action", action)
			.addAttribute("Code", StringUtils.objToString(pMap.get("video_id")));
		//program property
		String pName = StringUtils.objToString(pMap.get("video_name"));
		String pVolumnCount = StringUtils.objToString(pMap.get("video_episode"));
		String pVName = "";
		String pPosterUrl1 = StringUtils.objToString(sMap.get("picurl1"));//
		String pPosterUrl2 = StringUtils.objToString(sMap.get("picurl2"));//副海报，视频截图
		String pStatus = (String)pMap.get("video_status");
		String pBcharging = StringUtils.objToString(sMap.get("item_charge")).equals("Y")? "1" : "0";
		String stype=sMap.get("item_type").toString();
		if(pBcharging.equals("1")){
			if((stype.equals("电视剧") || stype.equals("动画"))&& (pVolumnCount.equals("1") || pVolumnCount.equals("2") || pVolumnCount.equals("3"))){
				pBcharging="0";
			}else if((stype.equals("综艺") || stype.equals("时尚") || stype.equals("纪实")) && pVolumnCount.equals("1")){
				pBcharging="0";
			}
		}
		String pBpreview = "0";//是否是预告片 0:正片  1:预告片
		
		program.addElement("Property").addAttribute("Name","Name").setText(pName);
		program.addElement("Property").addAttribute("Name","VolumnCount").setText(pVolumnCount);
		program.addElement("Property").addAttribute("Name","VName").setText(pVName);
		program.addElement("Property").addAttribute("Name","PosterUrl1").setText(pPosterUrl1);
		program.addElement("Property").addAttribute("Name","PosterUrl2").setText(pPosterUrl2);
		program.addElement("Property").addAttribute("Name","Status").setText(pStatus.equals("online")?"0":"1");
		program.addElement("Property").addAttribute("Name","Bcharging").setText(pBcharging);
		program.addElement("Property").addAttribute("Name","Bpreview").setText(pBpreview);
	}

	/**
	 * 添加Movie Object节点
	 * @param action
	 * @param sMap
	 * @param objsElement
	 */
	private static void addSeriesElement(String action, Map sMap,
			Element objsElement) {
		String type="0";
		//series
		Element series = objsElement.addElement("Object");
		//series attribute
		series.addAttribute("ElementType", "Series")
			.addAttribute("ID", StringUtils.objToString(sMap.get("item_id")))
			.addAttribute("Action", action)
			.addAttribute("Code", StringUtils.objToString(sMap.get("item_id")));
		//series property
		String name = StringUtils.objToString(sMap.get("item_name"));
		String originalName = "";
		String orgAirDate = "";
		if(sMap.get("item_onlineTime") != null){
			orgAirDate = StringUtils.dateToString((Date)sMap.get("item_onlineTime"));
		}
		String shortDesc = StringUtils.objToString(sMap.get("item_viewPoint"));
		String bcharging = StringUtils.objToString(sMap.get("item_charge")).equals("Y")? "1" : "0";
		String chargingmode = "1";
		
		String price = "0";
		
		String volumnCount = sMap.get("item_fileCount") == null ? "" : sMap.get("item_fileCount").toString();
		String status = (String)sMap.get("item_status");
		String description = StringUtils.objToString(sMap.get("item_memo"));
		String actorDisplay = (StringUtils.objToString(sMap.get("item_director"))).replace(" ", ";").replace(",", ";");
		String writerDisplay = (StringUtils.objToString(sMap.get("item_guest"))).replace(" ", ";").replace(",", ";");
		String originalCountry = StringUtils.objToString(sMap.get("item_area"));
		String copyRight = StringUtils.objToString(sMap.get("item_copyright"));//
		String contentProvider = StringUtils.objToString(sMap.get("item_cp"));//
		String tags =  StringUtils.objToString(sMap.get("item_category"));
		String releaseYear = StringUtils.objToString(sMap.get("item_year"));
		String programType = StringUtils.objToString(sMap.get("item_type"));
		if(programType.equals("电影")){
			type="1";
		}
		String programType2 = StringUtils.objToString(sMap.get("programa_name"));//
		String enName = "";
		String definition = "";
		String phonePosterUrl1 = "";
		String phonePosterUrl2 = "";
		String tvPosterUrl1 = StringUtils.objToString(sMap.get("picurl1"));//
		String tvPosterUrl2 = StringUtils.objToString(sMap.get("picurl2"));//
		String terminalId = "tv";
		String compere = (StringUtils.objToString(sMap.get("item_guest"))).replace(" ", ";").replace(",", ";");
		String guest = (StringUtils.objToString(sMap.get("item_guest"))).replace(" ", ";").replace(",", ";");
		
		series.addElement("Property").addAttribute("Name","Name").setText(name);
		series.addElement("Property").addAttribute("Name","OriginalName").setText(originalName);
		series.addElement("Property").addAttribute("Name","OrgAirDate").setText(orgAirDate);
		series.addElement("Property").addAttribute("Name","ShortDesc").setText(shortDesc);
		series.addElement("Property").addAttribute("Name","Bcharging").setText(bcharging);
		series.addElement("Property").addAttribute("Name","Chargingmode").setText(chargingmode);
		series.addElement("Property").addAttribute("Name","Price").setText(price);
		series.addElement("Property").addAttribute("Name","VolumnCount").setText(volumnCount);
		series.addElement("Property").addAttribute("Name","Status").setText(status.equals("online")?"0":"1");
		series.addElement("Property").addAttribute("Name","Description").setText(description);
		series.addElement("Property").addAttribute("Name","ActorDisplay").setText(actorDisplay);
		series.addElement("Property").addAttribute("Name","WriterDisplay").setText(writerDisplay);
		series.addElement("Property").addAttribute("Name", "SeriesType").setText(type.equals("1")?"2":"1");
		series.addElement("Property").addAttribute("Name","OriginalCountry").setText(originalCountry);
		series.addElement("Property").addAttribute("Name","Tags").setText(tags);
		series.addElement("Property").addAttribute("Name","ContentProvider").setText(contentProvider);
		series.addElement("Property").addAttribute("Name","CopyRight").setText(copyRight);
		series.addElement("Property").addAttribute("Name","ReleaseYear").setText(releaseYear);
		series.addElement("Property").addAttribute("Name","ProgramType").setText("教育");
		series.addElement("Property").addAttribute("Name","Tags").setText(StringUtils.objToString(sMap.get("item_class")) + "," + StringUtils.objToString(sMap.get("item_subject")) + "," + StringUtils.objToString(sMap.get("item_version")) + ",");
		series.addElement("Property").addAttribute("Name","ProgramType2").setText(StringUtils.objToString(sMap.get("item_class")));
		series.addElement("Property").addAttribute("Name","EnName").setText(enName);
		series.addElement("Property").addAttribute("Name","Definition").setText(definition);
		series.addElement("Property").addAttribute("Name","PhonePosterUrl1").setText(phonePosterUrl1);
		series.addElement("Property").addAttribute("Name","PhonePosterUrl2").setText(phonePosterUrl2);
		series.addElement("Property").addAttribute("Name","TvPosterUrl1").setText(tvPosterUrl1);
		series.addElement("Property").addAttribute("Name","TvPosterUrl2").setText(tvPosterUrl2);
		series.addElement("Property").addAttribute("Name","Compere").setText(compere);
		series.addElement("Property").addAttribute("Name","Guest").setText(guest);
		series.addElement("Property").addAttribute("Name","terminalId").setText(terminalId);
		series.addElement("Property").addAttribute("Name","metaType").setText(type);
	}
}
