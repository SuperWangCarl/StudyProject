<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>upload</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
  </head>
  
  <body>	
  	 <form name="frm_test" action="${pageContext.request.contextPath }/upload" method="post" enctype="multipart/form-data">
  	 	 用户名：<input type="text" name="userName">  <br/>
  	 	文件：   <input type="file" name="file_img">   <br/>
  	 	
  	 	<input type="submit" value="注册">
   	 </form>
  </body>
</html>
