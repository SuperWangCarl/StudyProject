package com.pattern.singleton;
/**
 *使用枚举数据类型实现单例模式
 *枚举enum和静态代码块的特性相似，在使用枚举时，构造方法会被自动调用，利用这一特性也可以实现单例：
 * @author SuperWang
 * @version Create Time：2018年1月14日 下午9:35:02
 */
public enum EnumFactory {

    singletonFactory;  
    
    private MySingleton1 instance;  
      
    private EnumFactory(){//枚举类的构造方法在类加载是被实例化  
        instance = new MySingleton1();  
    }  
          
    public MySingleton1 getInstance(){  
        return instance;  
    }  
      
}  
  
class MySingleton1{//需要获实现单例的类，比如数据库连接Connection  
    public MySingleton1(){}   
} 