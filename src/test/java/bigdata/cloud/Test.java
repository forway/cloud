package bigdata.cloud;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.pool2.ObjectPool;

public class Test {
	
	public static void main(String[] args) throws Exception {
		
		BlockingQueue<String> esClientQueue = new ArrayBlockingQueue<String>(5);
		
		esClientQueue.offer("aaa");
		esClientQueue.offer("bbb");
		System.out.println(esClientQueue.take());
		System.out.println(esClientQueue.take());
		System.out.println(esClientQueue.take());
		
		
	}

}
