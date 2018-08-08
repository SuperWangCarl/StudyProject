package com.java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

/**
 * 1.lambda
 * 2.函数式接口  @FunctionalInterface
 * 3.引用  方法引用,构造器引用
 * 4.接口的  默认方法,静态方法
 * 5.stream流
 * @author SuperWang
 * @version 创建时间：2018年6月11日 上午11:20:30
 */
public class Java8Common {

	/**
	 * 方法引用
	 * 类::静态方法
	 * 对象::方法
	 * 对象::静态方法
	 */
	public void referencesMethod() {
		Integer[] is = new Integer[] { 1, 2, 3, 4, 5, 6, 4, 3 };

		//1.
		Arrays.sort(is, new Comparator<Integer>() {
			public int compare(Integer x, Integer y) {
				return Integer.compare(x, y);
			}
		});
		
		//2.演化为 lambda表达式
		Arrays.sort(is, (x, y) -> Integer.compare(x, y));
		//3.演化为  方法引用   也是lambda表达式的一种
		Arrays.sort(is, Integer::compare);

		//=================================
		List<Integer> la = Arrays.asList(1, 2, 3, 4, 5, 3, 2, 6);
		//1.
		la.forEach(x -> System.out.println(x));
		//2.演化为
		la.forEach(System.out::println);
	}

	/**
	 * 构造引用
	 * 类::new
	 */
	@Test
	public void referencesConstruc() {
		System.out.println(1<<2);
		List<Integer> la = Java8Common.asList(ArrayList::new,1, 2, 3, 4, 5, 3, 2, 6);
		la.forEach(x -> System.out.println(x));
		la.forEach(System.out::println);
	}
	public static <T> List<T> asList(IMyCreate<List<T>> imy,T... a) {
		List<T> create = imy.create();
		for (T tt : a) {
			create.add(tt);
		}
		return create;
	}

	/**
	 * 接口中的静态方法和默认方法
	 */
	public void interStaticMethod(){
		InterA.dowork();
		InterA impl = new Impl();
		
	}
}

class Person {
	private String name;
	private int age;

	public int getAge() {
		return age;
	}

	public String getName() {
		return name;
	}
}
