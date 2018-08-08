package com.util.epg;

import java.util.UUID;

public class Utils1 {

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}

/*	public static String getCookieValue(String name, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; ++i) {
				if (name.equals(cookies[i].getName())) {
					return cookies[i].getValue();
				}
			}
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getUserInfo(HttpServletRequest request) {
		return (Map<String, Object>) request.getSession().getAttribute("userInfo");
	}

	public static String getUserId(HttpServletRequest request) throws Exception {
		Object user_id = getUserInfo(request).get("user_id");
		if ("".equals(user_id) || user_id == null) {
			user_id = getCookieValue("user_id", request);
		}
		return user_id.toString();
	}

	public static String getAreaNum(HttpServletRequest request) throws Exception {
		Object area_num = getUserInfo(request).get("area_num");
		if ("".equals(area_num) || area_num == null) {
			area_num = getCookieValue("area_num", request);
		}
		return area_num.toString();

	}

	public static String getUserToken(HttpServletRequest request) throws Exception {
		Object user_token = getUserInfo(request).get("user_token");
		if ("".equals(user_token) || user_token == null) {
			user_token = getCookieValue("user_token", request);
		}
		return user_token.toString();
	}

	public static String getStbId(HttpServletRequest request) throws Exception {
		Object stb_id = getUserInfo(request).get("stb_id");
		if ("".equals(stb_id) || stb_id == null) {
			stb_id = getCookieValue("stb_id", request);
		}
		return stb_id.toString();
	}

	public static String getOrderMark(HttpServletRequest request) throws Exception {
		Object order_mark = getUserInfo(request).get("order_mark");
		if ("".equals(order_mark) || order_mark == null) {
			order_mark = getCookieValue("order_mark", request);
		}
		if ("unOrder".equals(order_mark)) {
			String ADD_URL ="http://61.191.45.118:7002/itv-api/has_order?";//正式
			String desKey = "1464b900346646099b265c7bca650d59";//影视包正式
		//	String ADD_URL = "http://61.191.45.116:7002/itv-api/has_order?";// 测试
		//	String desKey = "7749b74b9dee40b689f24c33c437ec1f";// 影视包测试
			String itvAccount = com.api.util.Utils.getUserId(request);
			String providerId = "ahdx";
			String productId = "ahdx_ysvip";
			String resultCode = new com.epg.order.IsOrder().isOrder(providerId, productId, itvAccount, desKey, ADD_URL);
			if ("0".equals(resultCode)) {
				order_mark = "month";
				getUserInfo(request).put("order_mark", order_mark);
			}else{
				productId = "ahdx_ysvip_halfyear";
				resultCode = new com.epg.order.IsOrder().isOrder(providerId, productId, itvAccount, desKey, ADD_URL);
				if ("0".equals(resultCode)) {
					order_mark = "month";
					getUserInfo(request).put("order_mark", order_mark);
				}
			}
		}
		return order_mark.toString();
	}

	public static String getBoxType(HttpServletRequest request) throws Exception {
		Object box_type = getUserInfo(request).get("box_type");
		if ("".equals(box_type) || box_type == null) {
			box_type = getCookieValue("box_type", request);
		}
		return box_type.toString();
	}

	public static String isSpecialBoxType(HttpServletRequest request) throws Exception {
		List<String> specialBoxTypes = Arrays.asList("E900", "E910");
		Object box_type = getUserInfo(request).get("box_type");
		if (specialBoxTypes.contains(box_type))
			return "Y";
		else
			return "N";
	}

	public static String getProvince(HttpServletRequest request) throws Exception {
		Object province = getUserInfo(request).get("province");
		if ("".equals(province) || province == null) {
			province = getCookieValue("province", request);
		}
		return province.toString();
	}

	public static String getPlatform(HttpServletRequest request) throws Exception {
		Object platform = getUserInfo(request).get("platform");
		if ("".equals(platform) || platform == null) {
			platform = getCookieValue("platform", request);
		}
		return platform.toString();
	}

	public static String getEdition(HttpServletRequest request) throws Exception {
		Object edition = getUserInfo(request).get("edition");
		if ("".equals(edition) || edition == null) {
			edition = getCookieValue("edition", request);
		}
		return edition.toString();
	}

	public static String getProgramaId(HttpServletRequest request) throws Exception {
		Object programa_id = getUserInfo(request).get("programa_id");
		if ("".equals(programa_id) || programa_id == null) {
			programa_id = getCookieValue("programa_id", request);
		}
		return programa_id.toString();
	}

	public static String getEpgInfo(HttpServletRequest request) throws Exception {
		Object epgInfo = getUserInfo(request).get("epg_info");
		if ("".equals(epgInfo) || epgInfo == null) {
			epgInfo = getCookieValue("epg_info", request);
		}
		return epgInfo.toString();
	}

	public static String getBackEpgUrl(HttpServletRequest request) throws Exception {
		Object backEpgUrl = getUserInfo(request).get("back_epg_url");
		if ("".equals(backEpgUrl) || backEpgUrl == null) {
			backEpgUrl = getCookieValue("back_epg_url", request);
		}
		return backEpgUrl.toString();
	}

	public static String getBackHallUrl(HttpServletRequest request) throws Exception {
		Object backEpgUrl = getUserInfo(request).get("back_hall_url");
		if ("".equals(backEpgUrl) || backEpgUrl == null) {
			backEpgUrl = getCookieValue("back_hall_url", request);
		}
		return backEpgUrl.toString();
	}

	public static String getVAStoEPG(HttpServletRequest request) throws Exception {
		Object VAStoEPG = getUserInfo(request).get("VAStoEPG");
		if ("".equals(VAStoEPG) || VAStoEPG == null) {
			VAStoEPG = getCookieValue("VAStoEPG", request);
		}
		return VAStoEPG.toString();
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getWaitPlayVideo(HttpServletRequest request) {
		return (Map<String, Object>) request.getSession().getAttribute("waitPlayVideo");
	}

	@SuppressWarnings("unchecked")
	public static String getPrevUrl(HttpServletRequest request, String prev) throws Exception {
		Map<String, String> prevUrlMap = (Map<String, String>) request.getSession().getAttribute("prevUrlMap");
		if (prevUrlMap != null) {
			String prevUrl = prevUrlMap.get(prev);
			if (!StringUtils.isBlank(prevUrl)) {
				return prevUrl;
			} else {
				return request.getContextPath() + "/programa/sy/action.jsp";
			}
		}
		return "";
	}*/

	public static String getUserIdMod(String user_id) {
		int mod = Math.abs(user_id.hashCode() % 10);
		if (mod < 0 || mod > 9) {
			mod = 0;
		}
		return mod + "";
	}
	public static String getUserIdMod(String user_id,int much) {
		int mod = Math.abs(user_id.hashCode() % much);
		return mod + "";
	}

}
