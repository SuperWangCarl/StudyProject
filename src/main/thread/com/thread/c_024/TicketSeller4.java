/**
 * ��N�Ż�Ʊ��ÿ��Ʊ����һ�����
 * ͬʱ��10�����ڶ�����Ʊ
 * ��дһ��ģ�����
 * 
 * ��������ĳ�����ܻ������Щ���⣿
 * �ظ����ۣ��������ۣ�
 * 
 * ʹ��Vector����Collections.synchronizedXXX
 * ����һ�£������ܽ��������
 * 
 * �������A��B����ͬ���ģ���A��B��ɵĸ��ϲ���Ҳδ����ͬ���ģ���Ȼ��Ҫ�Լ�����ͬ��
 * ������������ж�size�ͽ���remove������һ������ԭ�Ӳ���
 * 
 * ʹ��ConcurrentQueue��߲�����
 * 
 * @author ��ʿ��
 */
package com.thread.c_024;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class TicketSeller4 {
	static Queue<String> tickets = new ConcurrentLinkedQueue<>();
	
	
	static {
		for(int i=0; i<1000; i++) tickets.add("Ʊ ��ţ�" + i);
		ArrayList<Object> arrayList = new ArrayList<>();
		arrayList.forEach((t)->{});
	}
	
	public static void main(String[] args) {
		
		for(int i=0; i<10; i++) {
			new Thread(()->{
				while(true) {
					String s = tickets.poll();
					if(s == null) break;
					else System.out.println("������--" + s);
				}
			}).start();
		}
	}
}
