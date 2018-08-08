package com.thread.c_001;

import java.util.function.Consumer;

public class Funcationdd {
	public static void main(String[] args) {
		Consumer<Integer> byName = Funcationdd::test;
		System.out.println(byName.toString());
		Funcationdd.test(11);
	}
	public static void test(Integer a ){
		System.out.println("----------------------------------------------");
	}
	class Person {
		  private final String name = null;
		  private final int age = 0;

		  public int getAge() { return age; }
		  public String getName() {return name; }
		}
}
