package com.pattern.observe;
import java.util.HashMap;  
import java.util.Iterator;  
import java.util.Map;  
import java.util.Observable;  
import java.util.Observer;  
  
/** 
 * @ClassName: MainObserver 
 * @Description: 被观察者 
 * @author  
 * @company  
 * @date 2013-5-2 
 * @version V1.0 
 */  
public class MainObservable extends Observable {  
	  
    private static final MainObservable instance = new MainObservable();  
  
    protected Map<String, Object> obs = new HashMap<String, Object>();  
  
    private MainObservable() {}  
  
    public static MainObservable getInstance() {  
        return instance;  
    }  
  
    /** 
     * @Title: doBusiness 
     * @Description: 当被观察者有Changed时,通知观察者 
     * @param arg 
     * @author  
     * @date 2013-5-2 
     */  
    public void doBusiness(Object arg) {  
        // 设置修改状态  
        super.setChanged();  
        // 通知观察者  
        this.notifyObservers(arg);  
    }  
  
    /** 
     * @Title: notifyObservers 
     * @Description: 模仿不同的业务通知对应业务的观察者 
     * @param arg 
     * @see java.util.Observable#notifyObservers(java.lang.Object) 
     * @author  
     */  
    public void notifyObservers(Object arg) {  
  
        String msg = arg.toString();  
  
        String[] msgs = msg.split(":");  
  
        if (obs.containsKey(msgs[0])) {  
            Observer ob = (Observer) obs.get(msgs[0]);  
            ob.update(this, msgs[1]);  
        }  
    }  
  
    /** 
     * @Title: addObserver 
     * @Description: 添加一个观察者 
     * @param name 观察者名称 
     * @param o 观察者对象 
     * @author  
     * @date 2013-5-2 
     */  
    public synchronized void addObserver(String name, Observer o) {  
  
        System.out.println("添加一个观察者:" + name);  
  
        obs.put(name, o);  
    }  
  
    /** 
     * @Title: updateObserver 
     * @Description: 修改一个观察者 
     * @param name 观察者名称 
     * @param o:观察者对象 
     * @author  
     * @date 2013-5-2 
     */  
    public synchronized void updateObserver(String name, Observer o) {  
  
        Iterator<String> it = obs.keySet().iterator();  
  
        String key = null;  
  
        while (it.hasNext()) {  
  
            key = it.next();  
  
            if (key.equals(name)) {  
                System.out.println("被修改的key为:" + key);  
                obs.put(key, o);  
                break;  
            }  
        }  
    }  
  
    /** 
     * @Title: deleteObserver 
     * @Description: 删除观察者 
     * @param o: 观察者对象 
     * @see java.util.Observable#deleteObserver(java.util.Observer) 
     * @author  
     */  
    public synchronized void deleteObserver(Observer o) {  
  
        if (obs.values().contains(o)) {  
  
            Iterator<String> it = obs.keySet().iterator();  
  
            String key = null;  
  
            while (it.hasNext()) {  
  
                key = it.next();  
  
                if (obs.get(key).equals(o)) {  
                    System.out.println("被删除的key为:" + key);  
                    obs.remove(key);  
                    break;  
                }  
            }  
        }  
    }  
}  