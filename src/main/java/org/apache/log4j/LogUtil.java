package org.apache.log4j;

import org.apache.log4j.Logger;

public class LogUtil {

	static Logger logger = Logger.getLogger(LogUtil.class);
	public static void main(String[] args) {
		logger.debug("111111111");
		te();
		logger.error("233333333333333");
//		[main] DEBUG org.impl.Log4jDemo - debug
//		[main] INFO org.impl.Log4jDemo - info
//		[main] ERROR org.impl.Log4jDemo - error
	}
	public static void te(){
		logger.info("222222222");
		
	}
}

