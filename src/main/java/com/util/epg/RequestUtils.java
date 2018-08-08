package com.util.epg;

public class RequestUtils {
/*
	public static String getStringValue(HttpServletRequest request, String name, String defaultValue) {
		String parameterValue = request.getParameter(name);
		if (StringUtils.isBlank(parameterValue)) {
			parameterValue = defaultValue;
		}
		return parameterValue;
	}

	public static int getIntValue(HttpServletRequest request, String name, int defaultValue) {
		int returnValue = defaultValue;
		String parameterValue = request.getParameter(name);
		if (!StringUtils.isBlank(parameterValue)) {
			if (parameterValue.indexOf(".") != -1) {
				parameterValue = parameterValue.substring(0, parameterValue.indexOf("."));
			}
			try {
				returnValue = Integer.parseInt(parameterValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return returnValue;
	}

	@SuppressWarnings("unchecked")
	public static void storeRequestURI(HttpServletRequest request, String name, List exclude) throws Exception {
		String requestURI = request.getRequestURI();
		Map<String, String[]> parameterMap = (HashMap<String, String[]>) request.getParameterMap();
		requestURI = requestURI + "?";
		if (!parameterMap.isEmpty()) {
			for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
				String key = entry.getKey();
				if (!exclude.contains(key)) {
					String[] values = entry.getValue();
					for (int i = 0; i < values.length; ++i) {
						requestURI += (key + "=" + URLEncoder.encode(values[i], "gbk") + "&");
					}
				}
			}
		}
		request.getSession().setAttribute(name, requestURI.substring(0, requestURI.length() - 1));
	}

	public static void storeRequestURI(HttpServletRequest request, String name, String... exclude) throws Exception {
		storeRequestURI(request, name, Arrays.asList(exclude));
	}

	@SuppressWarnings("unchecked")
	public static String getStoreRequestURI(HttpServletRequest request, String name, List extra) {
		String returnValue = "";
		String attributeValue = (String) request.getSession().getAttribute(name);
		if (!StringUtils.isBlank(attributeValue)) {
			int index = attributeValue.indexOf('?');
			if (index == -1) {
				attributeValue += "?";
			} else {
				attributeValue += "&";
			}
			for (int i = 0; i < extra.size(); ++i) {
				attributeValue += (String) extra.get(i) + "&";
			}
			returnValue = attributeValue.substring(0, attributeValue.length() - 1);
		}
		return returnValue;
	}

	public static String getStoreRequestURI(HttpServletRequest request, String name, String... extra) {
		return getStoreRequestURI(request, name, Arrays.asList(extra));
	}*/

}
