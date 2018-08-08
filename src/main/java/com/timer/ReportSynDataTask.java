package com.timer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

import org.springframework.stereotype.Component;
@Component
public class ReportSynDataTask extends TimerTask {
	
	List<Object> parameter_list = null;
	//来源库
	public static final String READ_DB = "zyjh.";
	//临时目标库
	public static final String WRITE_DB = "tempdata.";
	public void run() {
			
			Calendar calander = Calendar.getInstance();
			calander.set(Calendar.DATE, calander.get(Calendar.DATE) -1);
			String queryBeginDate = new SimpleDateFormat("yyyy-MM-dd").format(calander.getTime());
		/*String queryBeginDate = "2017-06-29";
		 
		Date da = null;
		try{
		 da = new SimpleDateFormat("yyyy-MM-dd").parse(queryBeginDate);
		}catch(Exception e){
			
		}
		Calendar install = Calendar.getInstance();
		install.setTime(da);
		for (int i = 0; i < 3; i++) { //11号
			install.add(Calendar.DATE, 1);
			queryBeginDate = new SimpleDateFormat("yyyy-MM-dd").format(install.getTime());*/
			parameter_list = new ArrayList<Object>();
			parameter_list.add(0,null);
			parameter_list.add(1,queryBeginDate);
			parameter_list.add(2,null);
			parameter_list.add(3,WRITE_DB);
			parameter_list.add(4,null);
			parameter_list.add(5,null);
			try {
				System.out.println("[-----------------数据统计开始------------------]");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		/*}	*/
	}

	
}
