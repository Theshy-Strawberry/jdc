package com.senyoboss.tool;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 多线程工具类
 * @title ThreadUtils.java
 * @description
 * @company Senyoboss
 * @author Jr.REX
 * @version 1.0
 */
public class ThreadUtils {

	/**
	 * sleep 
	 * @param millis
	 */
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// ignore
		}
	}

	/**
	 * 延迟执行
	 */
	public interface LazyAction{
		void exec();
	}

	/**
	 * 利用timer、TimerTask实现延迟执行
	 */
	public static void lazyRun(long millis, final LazyAction lazyAction) {
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				lazyAction.exec();
				this.cancel();
				timer.cancel();
			}
		};
		timer.schedule(task, millis);
	}

	/**
	 * 在某个时间执行
	 */
	public static void lazyRun(Date date, final LazyAction lazyAction) {
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				lazyAction.exec();
				this.cancel();
				timer.cancel();
			}
		};
		timer.schedule(task, date);
	}

}
