package net.mpoisv.weapon.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
	private static ExecutorService es = Executors.newFixedThreadPool(1);
	
	public static void startThread(Runnable clazz) {
		es.submit(clazz);
	}
	
	public static void stop() {
		es.shutdownNow();
		while(!es.isShutdown());
	}
}
