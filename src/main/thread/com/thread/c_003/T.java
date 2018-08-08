/**
 * synchronized�ؼ���
 * ��ĳ���������
 * @author mashibing
 */

package com.thread.c_003;

import java.util.function.Consumer;

public class T {

	private int count = 10;
	
	public synchronized void m() { //��ͬ���ڷ����Ĵ���ִ��ʱҪsynchronized(this)
		count--;
		System.out.println(Thread.currentThread().getName() + " count = " + count);
	}

}
