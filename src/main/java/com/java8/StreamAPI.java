package com.java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import com.sun.org.apache.xpath.internal.operations.UnaryOperation;

/**
 * Stream 的重点在于数据的操作
 * @author SuperWang
 * @version 创建时间：2018年6月11日 下午11:33:00
 */
public class StreamAPI {

	List<User> us = new ArrayList<>();

	@Before
	public void prepared() {
		Random random = new Random();
		for (int i = 0; i < 100; i++) {
			us.add(new User("user" + i, random.nextInt(50) + 50));
		}
	}

	/**
	 * 列出班上超过85分的学生姓名,并且按照分数降序输出用户名字
	 */
	@Test
	public void test1() {
		List<String> ret = new ArrayList<>();
		List<User> temp = new ArrayList<>();
		for (User u : us) {
			if (u.getScore() > 85)
				temp.add(u);
		}
		temp.sort(new Comparator<User>() {
			public int compare(User o1, User o2) {
				return Integer.compare(o2.getScore(), o1.getScore());
			}
		});
		for (User u : temp) {
			ret.add(u.getName());
		}
		System.out.println(ret);

		//以上复杂啰嗦 ,业务代码少 
		//使用Stream
		//1.得到集合的流对象
		//2.使用filter方法完成分数大于85的过滤
		//3.使用sorted方法完成排序
		//4.使用map方法 将user的流编程String的流
		//5.使用collect把String的流编程Llst<String>
		ret = us.stream().filter(u -> u.getScore() > 85)
				//.sorted((u1,u2)->Integer.compare(u2.getScore(), u1.getScore()))
				//.sorted(Comparator.comparing(u->u.getScore()))
				//.sorted(Comparator.comparing(User::getScore  ).reversed())
				.map(u -> u.getName()).collect(Collectors.toList());

		System.out.println(ret);
	}

	/**
	 * 求平均数
	 */
	@Test
	public void test2() {
		double totalSocre = 0D;
		for (User u : us) {
			totalSocre += u.getScore();
		}
		if (us.size() > 0) {
			System.out.println(totalSocre / us.size());
		}
		//使用流
		//1.编程一个int的流
		//2.使用average求这个平均值
		us.stream()
				//.mapToInt(u->u.getScore()).average()
				.mapToInt(User::getScore).average().ifPresent(System.out::println);
	}

	/**
	 * 内循环和外循环的区别
	 * @param x
	 * @return
	 */
	public boolean compare(int x) {
		System.out.println("执行了比较");
		return x > 5;
	}

	public void test3() {
		Integer[] ints = new Integer[] { 1, 2, 3, 4, 5, 6, 8 };
		List<Integer> ret = new ArrayList<>();
		//外部循环代码混淆
		for (Integer i : ints) {
			if (i > 5)
				ret.add(i);
		}
		//Stream:内部循环,代码里面不需要写循环操作
		//1.Stream是不会存储数据的
		//2.Stream是不会修改源数据的
		//3.Stream是单向的,不可重复操作
		//4.Stream的部分操作是延迟的
		//	*调用了一个方法,马上执行,我们叫做迫切执行方法,如果调用了一个方法,并不会立即执行,叫做延迟执行方法
		//	*1.只要Stream的方法返回的是Stream那么这些方法就是延迟执行的方法
		//	*2.延迟执行的方法一定要等到一个迫切执行方法执行的时候才会执行;(在Stream里面,返回的不是一个Stream的基本都是迫切执行方法)
		Stream<Integer> stream = Stream.of(ints).filter(this::compare);
		System.out.println("=================");
		stream.collect(Collectors.toList()).forEach(System.out::println);
	}

	/**Stream的创建
	 * 1.把数组编程Stream -> Arrays.stream();
	 * 2.Stream.of
	 * 3.集合直接调用
	 */
	public void arrayStream() {
		int[] ints = new int[] { 1, 2, 3, 4, 5 };
		//通过Arrays.stream(int[])得到一个IntStream ,IntStream是一种特殊的stream
		IntStream intStream = Arrays.stream(ints);

		//针对对象的数组,Arrays.stream->Stream<T>
		User[] us = new User[] {};
		Stream<User> usStream = Arrays.stream(us);

		//Stream.of()
		//不能直接把简单类型的数组作为Stream.of的阐述(返回Stream<int[]>)
		Stream<int[]> intStream1 = Stream.of(ints);
		//自动拆装箱
		//IntStream 和 Stream<Integer>是不一样的
		Stream<Integer> integerStream = Stream.of(1, 2, 3, 4, 5, 6);

		//对于集合直接调用
		List<String> strs = Arrays.asList(new String[] { "1", "" });
		Stream<String> stream = strs.stream();
		//得到并行stream
		Stream<String> parallelStream = strs.parallelStream();
	}
	public void emptyStream() {
		Stream<Object> empty = Stream.empty();
	}
	/**
	 * 在遍历Stream元素的时候,才去生成要处理的下一个元素
	 * 就有可能创建一个逻辑上无限大的Stream:延迟
	 * 可以创建大量的数据
	 */
	public void umLimitStream() {
		//无限生成
		Stream.generate(() -> "haha").forEach(System.out::println);
		//limit()  截取limit个数据
		Stream.generate(() -> "haha").limit(100).forEach(System.out::println);
	}
	/**
	 * 产生规律数据
	 * 使用Stream.iterate产生均匀数据
	 */
	public void umLimitStream2() {
		//产生自己返回自己
		//Stream.iterate(0, x->x).limit(100).forEach(System.out::println);
		//Stream.iterate(0, UnaryOperator.identity()).limit(100).forEach(System.out::println);
		Stream.iterate(0, x -> x + 1).limit(100).forEach(System.out::println);
	}
}

class User {
	private String name;
	private int score;

	public User(String name, int score) {
		super();
		this.name = name;
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", score=" + score + "]";
	}
}