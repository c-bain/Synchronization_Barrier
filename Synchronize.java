

import java.util.concurrent.Semaphore;

/**
 * Class Synchronize
 * @author corie
 *
 */
 public class Synchronize{
	 public static Barrier[] barrier;//point where the threads wait before crossing
	 public static int numThreads;//Number of threads
	 public static void main(String args[]){
		 Semaphore lock= new Semaphore(0);//Semaphore created although acquire() and release() not used
		 numThreads=10;
		 barrier = new Barrier[numThreads];
		 for (int i = 0; i < numThreads; i++){//creates a few threads based on that random number 
			barrier[i] = new Barrier(i,lock);//randomNum of threads created
			barrier[i].begin();//each thread starts
		}
		try{//used for delay between waiting at barrier then continuing.
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("\nAll threads have called the sync method\n");  
		Barrier.crossBarrier();//threads can continue since they all have called the sync method
	 }
 }   

 /**
  * Class Barrier acts as a monitor for the threads
  * @author corie
  *
  */
class Barrier implements Runnable {
	private Thread currentThread;//thread that is approaching barrier
	private String threadNum;//Number/Name of the thread
	private Semaphore lock;
	
	public Barrier(int threadNum, Semaphore lock){//each thread is assigned a name
		this.threadNum=Integer.toString(threadNum);//Semaphore int value converted to a string for display
		this.lock = lock;
	}
	
	public Semaphore getLock(){//getter for the lock
		return lock;
	}
	
	public Thread getThread(){//getter for the thread
		return currentThread;
	}
	
	public void begin(){
		(new Thread(new Barrier(Integer.parseInt(threadNum),lock))).start();
	}
	
	public void sync(String threadNum){
		System.out.println("Thread "+ threadNum+" calling sync method, Thread "+threadNum+" now waiting...");
	}
	
	public static void crossBarrier(){//After all threads have approached the barrier
		for (int i = 0; i < Synchronize.numThreads; i++) {//the crossBarrier allows them to continue
			Semaphore lock_= Synchronize.barrier[i].getLock();//used so only one thread can wake up and continue at a time
			synchronized(lock_){
				lock_.notify();//notify wakes up each thread and they now continue
			}
		}
	}
	

	@Override
	public void run() {//each thread calls sync method then waits
		synchronized(lock){
			sync(threadNum);//When all threads arrive at barrier they then perform the sync action 
			try {
				lock.wait();//give up lock and go to sleep until notify()
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Thread "+ threadNum+" performing subsequent actions...");
	
	
	
	}
}
