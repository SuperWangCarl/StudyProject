/**
 * synchronized�ؼ���
 * ��ĳ���������
 * @author mashibing
 */

package com.thread.c_004;

public class T {

	private static int count = 10;
	
	public synchronized static void m() { //�����ͬ��synchronized(yxxy.c_004.T.class)
		count--;
		System.out.println(Thread.currentThread().getName() + " count = " + count);
	}
	
	public static void mm() {
		synchronized(T.class) { //����һ������дsynchronized(this)�Ƿ���ԣ�
			count --;
		}
	}

}
