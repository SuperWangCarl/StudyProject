package com.pattern.observe;
import java.util.Observable;  
import java.util.Observer;  
  
/** 
 * @ClassName: ProcessObserver 
 * @Description: 观察者 
 * @author  
 * @company  
 * @date 2013-5-2 
 * @version V1.0 
 */  
  
public class ProcessObserver implements Observer {  
  
    /** 
     * @Title: update 
     * @Description:实现观察者接口,当发现被观察者有Changed时执行该方法 
     * @param o 观察者对象 
     * @param arg: 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object) 
     * @author  
     */  
    @Override  
    public void update(Observable o, Object arg) {  
        System.out.println("update:" + arg);  
    }  
  
}  