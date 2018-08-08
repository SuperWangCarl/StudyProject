package com.util;


/**
 *
 *@author
 *@date 2017-9-5 下午07:42:28
 */
public class Constants {
	//默认下线时间
	public static final String DEFAULT_OFFLINETIME = "2099-01-01 00:00:00";
	//默认付费标识
	public static final String DEFAULT_CHARGE = "N";
	//默认评分
	public static final String DEFAULT_SCORE = "7";
	//默认状态
	public static final String DEFAULT_STATUS = "offline";
	//成功描述
	public static final String MESSAGE_SUCCESS = "成功";
	//失败描述
	public static final String MESSAGE_FAILED = "xml内容错误";
	//媒资入库响应地址（测试环境）
	//public static final String RESPONSE_URL = "http://175.6.15.87:30080/cmsfeedback/SubCmsApi/Feedback.action";
	//媒资入库响应地址（生产环境）
	public static final String RESPONSE_URL = "http://175.6.15.136:20080/cmsfeedback/SubCmsApi/Feedback.action";
	//媒资入库响应地址（本地测试环境）
	//public static final String RESPONSE_URL = "http://localhost:8080/admin_new/feedback/feedback.do";
}
