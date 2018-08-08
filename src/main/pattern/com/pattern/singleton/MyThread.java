package com.pattern.singleton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MyThread extends Thread {

	@Override
	public void run() {
		System.out.println(MySingleton.getInstance().hashCode());
	}

	public static void main(String[] args) {

		MyThread[] mts = new MyThread[10];
		for (int i = 0; i < mts.length; i++) {
			mts[i] = new MyThread();
		}

		for (int j = 0; j < mts.length; j++) {
			mts[j].start();
		}
	}
	public static void test() {
		MySingleton singleton = MySingleton.getInstance();

		File file = new File("MySingleton.txt");

		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(singleton);
			fos.close();
			oos.close();
			System.out.println(singleton.hashCode());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			MySingleton2 rSingleton = (MySingleton2) ois.readObject();
			fis.close();
			ois.close();
			System.out.println(rSingleton.hashCode());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}
