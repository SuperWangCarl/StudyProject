package com.pattern.singleton;
/**
 * 完善使用enum枚举实现单例模式
 * 不暴露枚举类实现细节的封装代码如下：
 * @author SuperWang
 * @version Create Time：2018年1月14日 下午9:56:11
 */
public class ClassFactory {
	private enum MyEnumSingleton {
		singletonFactory;

		private MySingleton2 instance;

		private MyEnumSingleton() {//枚举类的构造方法在类加载是被实例化  
			instance = new MySingleton2();
		}

		public MySingleton2 getInstance() {
			return instance;
		}
	}

	public static MySingleton2 getInstance() {
		return MyEnumSingleton.singletonFactory.getInstance();
	}
}

class MySingleton2 {//需要获实现单例的类，比如数据库连接Connection  
	public MySingleton2() {
	}
}