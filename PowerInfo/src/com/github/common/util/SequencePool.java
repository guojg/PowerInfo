package com.github.common.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;


public class SequencePool {

	private static final ConcurrentHashMap<String, Future<?>> CHM = new ConcurrentHashMap<String, Future<?>>();
	
	private static final ArrayBlockingQueue<String> ABQ = new ArrayBlockingQueue<String>(1000);
	
	private static final ExecutorService execServer = Executors.newSingleThreadExecutor();

	public static SequencePool sequencePool = new SequencePool();
	
	private SequencePool() {
		
	}
	
	private void loadSeq() {
		//System.out.println("进入装载--");
		Future<?> ft = CHM.get("list");
		if (ft == null) {
			FutureTask<?> f = new FutureTask(new Callable() {
				public Object call() throws Exception {
					//装载ABQ
					String[] strs = SnUtil.newGetNewID();
					int min = Integer.parseInt(strs[1]);
					int max = Integer.parseInt(strs[2]);
					for (; min<=max; min++) {
						ABQ.offer(strs[0]+new java.text.DecimalFormat("#").format(min));
					}
					return null;
				}
			});
			ft = CHM.putIfAbsent("list", f);
			if (ft == null) {
				ft = f;
				execServer.submit(f);
			}
		} 
		try {
			ft.get();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		} catch (ExecutionException e) {
			//CHM.remove("list");
			e.printStackTrace();
		} finally {
			CHM.remove("list");
		}
			
	}
	
	public String takeSeq() {
		//System.out.println("ABQ.size----" + ABQ.size());
		String newSeq = ABQ.poll();
		if(newSeq != null) {
			return newSeq;
		} else {
			this.loadSeq();
			return takeSeq();
		}
	}
	
	public static SequencePool getInstance()	{
		if (sequencePool != null) 
			return sequencePool;
		else {
			sequencePool = new SequencePool();
			return sequencePool;
		}
	}
}
