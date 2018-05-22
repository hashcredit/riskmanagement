package com.newdumai.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsynchronousThreadHelper {

	public static Future<Object> runNewThread(String classNameAndMethodName, Class<?>[] classes, Object... args) {
		ExecutorService threadPool = Executors.newSingleThreadExecutor();
		Future<Object> future = threadPool.submit(new Callable<Object>() {
			public Object call() {
				Object obj = null;
				try {
					obj = InvokeMethod.invoke(classNameAndMethodName, classes, args);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return obj;
			}
		});
		threadPool.shutdown();
		return future;
	}
	
	public static void runNewThread2(String classNameAndMethodName, Class<?>[] classes, Object... args) {
		Thread thread = new Thread() {
			public void run() {
				try {
					InvokeMethod.invoke(classNameAndMethodName, classes, args);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}
	
}
