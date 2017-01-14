package bigdata.cloud;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Test {
	
	public static void main(String[] args) {
		
		BlockingQueue<String> esClientQueue = new ArrayBlockingQueue<String>(0);
		
		esClientQueue = new ArrayBlockingQueue<String>(1);
		System.out.println(esClientQueue.size());
		//System.out.println(esClientQueue.remainingCapacity());
		
		
		
		
	}

}
