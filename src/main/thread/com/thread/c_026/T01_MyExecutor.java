/**
 * ��ʶExecutor
 */
package com.thread.c_026;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class T01_MyExecutor implements Executor {

	public static void main(String[] args) {
		new T01_MyExecutor().execute(()->System.out.println("hello executor"));
	}

	@Override
	public void execute(Runnable command) {
		//new Thread(command).run();
		command.run();
	}

}

