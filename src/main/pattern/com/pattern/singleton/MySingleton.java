package com.pattern.singleton;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * 饿汉式单例
 * 饿汉式单例是指在方法调用前，实例就已经创建好了。下面是实现代码：
 * @author SuperWang
 * @version Create Time：2018年1月14日 下午9:35:02
 */
/*
public class MySingleton {

	private static MySingleton instance = new MySingleton();  
    
    private MySingleton(){}  
      
    public static MySingleton getInstance() {  
        return instance;  
    }  
}
*/
/**
 * 懒汉式单例
 * 懒汉式单例是指在方法调用获取实例时才创建实例，因为相对饿汉式显得"不急迫"，所以被叫做“懒汉模式”。下面是实现代码：
 * @author SuperWang
 * @version Create Time：2018年1月14日 下午9:35:02
 * Problem
 */
/*
public class MySingleton {

	private static MySingleton instance = null;

	private MySingleton() {
	}

	
	public static MySingleton getInstance() {  
	    if(instance == null){//懒汉式  
	        instance = new MySingleton();  
	    }  
	     //这里实现了懒汉式的单例，但是熟悉多线程并发编程的朋友应该可以看出，在多线程并发下这样的实现是无法保证实例实例唯一的，甚至可以说这样的失效是完全错误的，
	     //下面我们就来看一下多线程并发下的执行情况，这里为了看到效果，我们对上面的代码做一小点修改：
	    try {   
	        if(instance != null){//懒汉式   
	              
	        }else{  
	            //创建实例之前可能会有一些准备性的耗时工作   
	            Thread.sleep(300);  
	            instance = new MySingleton();  
	        }  
	    } catch (InterruptedException e) {   
	        e.printStackTrace();  
	    }
	    // 测试之后可以看出，单例的线程安全性并没有得到保证，那要怎么解决呢？
	    return instance;
	}
	
	
	//用 synchronized 效率低下
	public synchronized static MySingleton getInstance() {
		try {
			//同样的效率很低下
			synchronized (MySingleton.class) {
				if (instance != null) {//懒汉式   

				} else {
					//单独放在此处线程不安全
					synchronized (MySingleton.class) {
						//创建实例之前可能会有一些准备性的耗时工作   
						Thread.sleep(300);
						instance = new MySingleton();
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return instance;
	}
	
}
*/
/**
 *  Double Check Locking 双检查锁机制（推荐）
 *  为了达到线程安全，又能提高代码执行效率，我们这里可以采用DCL的双检查锁机制来完成，代码实现如下：
 * @author SuperWang
 * @version Create Time：2018年1月14日 下午9:35:02
 * 从运行结果来看，该中方法保证了多线程并发下的线程安全性。
 * 这里在声明变量时使用了volatile关键字来保证其线程间的可见性；在同步代码块中使用二次检查，以保证其不被重复实例化。集合其二者，这种实现方式既保证了其高效性，也保证了其线程安全性。
 */
/*public class MySingleton {  
    
    //使用volatile关键字保其可见性  
    volatile private static MySingleton instance = null;  
      
    private MySingleton(){}  
       
    public static MySingleton getInstance() {  
        try {    
            if(instance != null){//懒汉式   
                  
            }else{  
                //创建实例之前可能会有一些准备性的耗时工作   
                Thread.sleep(300);  
                synchronized (MySingleton.class) {  
                    if(instance == null){//二次检查  
                        instance = new MySingleton();  
                    }  
                }  
            }   
        } catch (InterruptedException e) {   
            e.printStackTrace();  
        }  
        return instance;  
    }  
}*/  
/**
 *使用静态内置类实现单例模式
 *DCL解决了多线程并发下的线程安全问题，其实使用其他方式也可以达到同样的效果，代码实现如下：
 * @author SuperWang
 * @version Create Time：2018年1月14日 下午9:35:02
 */
/*public class MySingleton {  
    
    //内部类  
    private static class MySingletonHandler{  
        private static MySingleton instance = new MySingleton();  
    }   
      
    private MySingleton(){}  
       
    public static MySingleton getInstance() {   
        return MySingletonHandler.instance;  
    }  
}*/
/**
 * 序列化与反序列化的单例模式实现
 * 静态内部类虽然保证了单例在多线程并发下的线程安全性，但是在遇到序列化对象时，默认的方式运行得到的结果就是多例的。
 * @author SuperWang
 * @version Create Time：2018年1月14日 下午9:35:02
 * 从结果中我们发现，序列号对象的hashCode和反序列化后得到的对象的hashCode值不一样，说明反序列化后返回的对象是重新实例化的，单例被破坏了。那怎么来解决这一问题呢？
 * 解决办法就是在反序列化的过程中使用readResolve()方法，单例实现的代码如下：
 */
public class MySingleton implements Serializable {  
    
    private static final long serialVersionUID = 1L;  
  
    //内部类  
    private static class MySingletonHandler{  
        private static MySingleton instance = new MySingleton();  
    }   
      
    private MySingleton(){}  
       
    public static MySingleton getInstance() {   
        return MySingletonHandler.instance;  
    }  
    //该方法在反序列化时会被调用，该方法不是接口定义的方法，有点儿约定俗成的感觉  
    protected Object readResolve() throws ObjectStreamException {  
        System.out.println("调用了readResolve方法！");  
        return MySingletonHandler.instance;   
    }  
}  
/**
 *使用static代码块实现单例
 *静态代码块中的代码在使用类的时候就已经执行了，所以可以应用静态代码块的这个特性的实现单例设计模式。
 * @author SuperWang
 * @version Create Time：2018年1月14日 下午9:35:02
 */
/*public class MySingleton{  
    
    private static MySingleton instance = null;  
       
    private MySingleton(){}  
  
    static{  
        instance = new MySingleton();  
    }  
      
    public static MySingleton getInstance() {   
        return instance;  
    }   
} */