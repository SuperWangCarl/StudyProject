package com.java8;

public interface InterA {

	public static void dowork(){
		System.out.println("JdkIface.hello()");
	}
	public default void hello(){}
}
