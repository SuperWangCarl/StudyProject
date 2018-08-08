package com.pattern.observe;

public class Test {  
	  
    /** 
     * @Title: main 
     * @Description: TODO 
     * @param args: 
     * @author  
     * @date 2013-5-2 
     */  
    public static void main(String[] args) {  
  
        // 实例化一个被观察者  
        MainObservable ob = MainObservable.getInstance();  
  
        // 实例化观察者  
        ProcessObserver pro = new ProcessObserver();  
        TaskbarObserver task = new TaskbarObserver();  
  
        // 添加观察者  
        ob.addObserver("proc", pro);  
        ob.addObserver("task", task);  
  
        System.out.println("添加后,Map=" + ob.obs);  
  
        // 处理业务,当关闭一个eclipse时,通知两个观察者同时也移除该任务  
        System.out.println("发送一条进程信息...");  
        ob.doBusiness("proc:进程启动");  
        try {  
            Thread.sleep(5000);  
        } catch (InterruptedException e) {  
            Thread.currentThread().interrupt();  
        }  
        System.out.println("发送一条任务信息...");  
        ob.doBusiness("task:我给任务器推送消息了");  
  
        // 移除一个观察者  
        try {  
            Thread.sleep(5000);  
        } catch (InterruptedException e) {  
            Thread.currentThread().interrupt();  
        }  
        ob.doBusiness("task:删除.");  
        ob.deleteObserver(pro);  
        System.out.println("删除后,Map=" + ob.obs);  
        System.out.println("发送一条进程信息...");  
  
        ob.doBusiness("task:重新添加.");  
          
        task = new TaskbarObserver();  
  
        ob.addObserver("task111", task);  
  
        System.out.println("再次添加后,Map=" + ob.obs);  
  
        ob.doBusiness("task:修改.");  
          
        // 修改  
        TaskbarObserver task1 = new TaskbarObserver();  
        ob.updateObserver("task111", task1);  
  
        System.out.println("修改后,Map=" + ob.obs);  
         
    }  
}  