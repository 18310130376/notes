# 常用工具类

```json

-------------------------------FilenameUtils-------------------------------------------
        getExtension 返回文件后缀名
        getBaseName 返回文件名，不包含后缀名
        getName 返回文件全名
        concat 按命令行风格组合文件路径(详见方法注释)
        removeExtension 删除后缀名
        normalize 使路径正常化
        wildcardMatch 匹配通配符
        seperatorToUnix 路径分隔符改成unix系统格式的，即/
        getFullPath 获取文件路径，不包括文件名
        isExtension 检查文件后缀名是不是传入参数(List<String>)中的一个
--------------------------------------------------------------------------------------
    
-------------------------------org.apache.commons.io.FileUtils------------------------
        deleteDirectory 删除文件夹
        readFileToString 以字符形式读取文件内容。
        deleteQueitly 删除文件或文件夹且不会抛出异常。
        copyFile 复制文件
        writeStringToFile 把字符写到目标文件，如果文件不存在，则创建。
        forceMkdir 强制创建文件夹，如果该文件夹父级目录不存在，则创建父级。
        write 把字符写到指定文件中
        listFiles 列举某个目录下的文件(根据过滤器)
        copyDirectory 复制文件夹
        forceDelete 强制删除文件
-------------------------------------------------------------------------------------    
    
    
-------------------------------StringUtils-------------------------------------------
        isBlank 字符串是否为空 (trim后判断)
        isEmpty 字符串是否为空 (不trim并判断)
        equals 字符串是否相等
        join 合并数组为单一字符串，可传分隔符
        split 分割字符串
        EMPTY 空字符串
        replace 替换字符串
        capitalize 首字符大写
-------------------------------------------------------------------------------------     
    
    
-------------------------------org.apache.commons.lang.ArrayUtils--------------------
           contains 是否包含某字符串
           addAll 添加所有
           clone 克隆一个数组
           isEmpty 是否空数组
           add 向数组添加元素
           subarray 截取数组
           indexOf 查找下标
           isEquals 比较数组是否相等
           toObject 基础类型数据数组转换为对应的Object数组
--------------------------------------------------------------------------------------     
    
--------------------------------org.apache.commons.codec.digest.DigestUtils-----------
          md5Hex MD5加密，返回32位
          sha1Hex SHA-1加密
          sha256Hex SHA-256加密
          sha512Hex SHA-512加密
          md5 MD5加密，返回16位
--------------------------------------------------------------------------------------     
        
    
    
--------------------org.apache.commons.collections.CollectionUtils----------
        isEmpty 是否为空
        select 根据条件筛选集合元素
        transform 根据指定方法处理集合元素，类似List的map()。
        filter 过滤元素，雷瑟List的filter()
        find 基本和select一样
        collect 和transform 差不多一样，但是返回新数组
        forAllDo 调用每个元素的指定方法。
        isEqualCollection 判断两个集合是否一致
--------------------------------------------------------------------------------------    
    
    
--------------------org.apache.commons.beanutils.PropertyUtils----------
        getProperty 获取对象属性值
        setProperty 设置对象属性值
        getPropertyDiscriptor 获取属性描述器
        isReadable 检查属性是否可访问
        copyProperties 复制属性值，从一个对象到另一个对象
        getPropertyDiscriptors 获取所有属性描述器
        isWriteable 检查属性是否可写
        getPropertyType 获取对象属性类型
-------------------------------------------------------------------------------------     
    
    
--------------------org.apache.commons.beanutils.BeanUtils----------
        copyPeoperties 复制属性值，从一个对象到另一个对象
        getProperty 获取对象属性值
        setProperty 设置对象属性值
        populate 根据Map给属性复制
        copyPeoperty 复制单个值，从一个对象到另一个对象。
        cloneBean 克隆
-------------------------------------------------------------------------------------         
    
------------------------TimeUnit-----------------------------------------------------

        System.out.println(TimeUnit.SECONDS.toMillis(1));//     1秒转换为毫秒数  
        System.out.println(TimeUnit.SECONDS.toMinutes(120));//   60秒转换为分钟数  
        System.out.println(TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES));// 1分钟转换为秒数 
        TimeUnit.SECONDS.sleep(100);//休眠100秒
--------------------------------------------------------------------------------------


------------------------------其他----------------------------------------------------  
           
            URL url = ResourceUtils.getURL("classpath:yn-http.properties");

            UrlResource resource = new UrlResource(url);

             //其中EncodedResource可以指定具体的编码格式
            EncodedResource enResource = new EncodedResource(resource,"utf-8");

            String result = FileCopyUtils.copyToString(enResource.getReader());

            Properties properties = PropertiesLoaderUtils.loadAllProperties("http.properties")

            File file = ResourceUtils.getFile("classpath:jdbc.properties");

            FileCopyUtils.copy(new File("p0"), new File("p1"));

            BeanUtils.copyProperties(source, target, ignoreProperties);

            Integer num =NumberUtils.parseNumber("12", Integer.class);
            //去掉最后一个字符
            lang = StringUtils.trimTrailingCharacter(lang,',');

            org.apache.commons.io.IOUtils
            org.apache.http.util.EntityUtils
            StreamUtils
            org.apache.commons.lang3.SystemUtils
            ClassUtils
            org.apache.commons.lang3.time.DateUtils
            org.apache.commons.lang3.RandomUtils
            org.apache.commons.lang3.SystemUtils

------------------------------其他---------------------------------------------------  
```



# 生产者-消费者

Producer

```java
package com.audition;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable{

	private final BlockingQueue<String> queue;
	private String data;

	   public Producer(BlockingQueue<String> q,String data){
		   this.data = data;
	       this.queue=q;
	   }

	   @Override
	   public void run() {
	        System.out.println("produce:" + data);
	        try {
				queue.put(data);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	   }

}
```

Consumer

```java
package com.audition;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

	private final BlockingQueue<String> queue;

	public Consumer(BlockingQueue<String> q) {
		this.queue = q;
	}

	@Override
	public void run() {
		String data;
		try {
			data = queue.take();//取走BlockingQueue里排在首位的对象,若BlockingQueue为空,阻断进入等待状态直到Blocking有新的对象被加入为止 
			System.out.println("consume:" + data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
```

测试

```java
package com.audition;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestQueue{

	public static void main(String[] args) {

	        BlockingQueue<String> queue=new ArrayBlockingQueue<String>(1000);
	        ExecutorService service = Executors.newCachedThreadPool();  
	        for (int i = 0; i < 3; i++) {  
	            service.submit(new Consumer(queue));  
	            service.submit(new Consumer(queue));  
	        }  
	        for (int i = 0; i < 3; i++) {  
	            service.submit(new Producer(queue, "000," + i));  
	            service.submit(new Producer(queue, "111" + i));  
	        }  
	        service.shutdown();  
	      }  
	   }
```



# 阻塞队列

1. ArrayBlockingQueue

   1. 内部使用一个数组作为其存储空间，数组的存储空间是`预先分配`的。
   2. 优点：put和take操作不会增加GC的负担
   3. 缺点：put和take操作使用同一个锁，`可能导致锁争用`，导致较多的上下文切换
   4. 适合在生产者线程和消费者线程之间的**并发程序较低**的情况下使用

2. LinkedBlockingQueue

   1. 内部存储空间是一个链表，而链表节点所需的存储空间是`动态分配`的
   2. 优点：put和take操作使用两个显示锁（putLock和takeLock）
   3. 缺点：增加了GC的负担
   4. 适合在生产者线程和消费者线程之间的**并发程序较高**的情况下使用

3. SynchronousQueue

   1. 可以被看做一种特殊的有界队列
   2. 生产者线程生产一个产品之后，会等待消费者线程来取走这个产品，才会接着生产下一个产品
   3. 适合在生产者线程和消费者线程之间的**处理能力相差不大**的情况下使用

   ​

# 转型

```java
package com.boot.web;

public class Animal {
	
	public void eat() {
		System.out.println("===Animal eat");
	};
}
```

```
package com.boot.web;

public class Cat extends Animal{
	public void eat() {
		System.out.println("===Cat eat");
	};
}
```

```
package com.boot.web;

public class Dog extends Animal{
	
	public void eat() {
		System.out.println("===Dog eat");
	};
}
```

```java
package com.boot.web;

public class DoEat {
	
	public static void eat(Animal a){
	    a.eat();
	} 
	public static void main(String[] args) {
		
		Animal cat = new Cat();
		Animal dog = new Dog();
		cat.eat();
		dog.eat();
	}
}
```



# Callable

方式一

```java
package com.audition;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureTest {

	public static class Task implements Callable<String> {
        @Override
        public String call() throws Exception {
        	 Thread.sleep(900);
            return "nihao";
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException{
        ExecutorService es = Executors.newCachedThreadPool();
        Future<String> future= es.submit(new Task());
        String a ="";
		try {
			a = future.get(1000, TimeUnit.MILLISECONDS);
		} catch (TimeoutException e) {
			future.cancel(true);
		}
        boolean b = future.isCancelled();
        System.out.println(a);
        System.out.println(b);
        System.out.println(future.isDone());
    }
}

```

方式二

```java
package com.audition;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureTest {

    BlockingQueue<Future<String>> futures = new LinkedBlockingQueue<>();
  
    public static void main(String[] args){

        ExecutorService threadPool = Executors.newSingleThreadExecutor();  
        Future<String> future = threadPool.submit(new Callable<String>(){  
  
            @Override  
            public String call() throws Exception {  
            	TimeUnit.SECONDS.sleep(3);
                return "future";  
            }  
        });  
        
        try {
           futures.put(submit);
          } catch (InterruptedException e) {
             e.printStackTrace();
         }
        
        String string = null;
		try {
			string = future.get(2000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
		}
        System.out.println(string);
    }
    
     // 消费者
        new Thread() {
            @Override
            public void run() {
                while(true) {
                    for (Future<String> future : futures) {
                        if(future.isDone()) {
                            // 处理业务
                            // .............
                            
                            
                        };
                    }
                }
            }
        }.start();
    }
}
```

# CompletionService

```java
package com.audition;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletionServiceTest {

	static class Task implements Callable<String> {
		private int i;

		public Task(int i) {
			this.i = i;
		}

		@Override
		public String call() throws Exception {
			Thread.sleep(5000);
			return Thread.currentThread().getName() + "执行完任务：" + i;
		}
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		testExecutorCompletionService();
	}

	private static void testExecutorCompletionService() throws InterruptedException, ExecutionException {
		int numThread = 5;
		ExecutorService executor = Executors.newFixedThreadPool(numThread);
		CompletionService<String> completionService = new ExecutorCompletionService<String>(executor);
		try {
			for (int i = 0; i < numThread; i++) {
				completionService.submit(new CompletionServiceTest.Task(i));
			}
			for (int i = 0; i < numThread; i++) {
				// take 方法等待下一个结果并返回 Future 对象。
				// poll 不等待，有结果就返回一个 Future 对象，否则返回 null。
				System.out.println(completionService.take().get() + "-----------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			executor.shutdown();
		}
	}
}
```



# BlockingQueue

**offer(E e)**: 将给定的元素设置到队列中，如果设置成功返回true, 否则返回false. e的值不能为空，否则抛出空指针异常。

**offer(E e, long timeout, TimeUnit unit)**: 将给定元素在给定的时间内设置到队列中，如果设置成功返回true, 否则返回false.

**add(E e)**: 将给定元素设置到队列中，如果设置成功返回true, 否则抛出异常。如果是往限定了长度的队列中设置值，推荐使用offer()方法。

**put(E e)**: 将元素设置到队列中，如果队列中没有多余的空间，该方法会一直阻塞，直到队列中有多余的空间。

**take()**: 从队列中获取值，如果队列中没有值，线程会一直阻塞，直到队列中有值，并且该方法取得了该值。

**poll(long timeout, TimeUnit unit)**: 在给定的时间里，从队列中获取值，如果没有取到会抛出异常。

**remainingCapacity()**：获取队列中剩余的空间。

**remove(Object o)**: 从队列中移除指定的值。

**contains(Object o)**: 判断队列中是否拥有该值。

**drainTo(Collection c)**: 将队列中值，全部移除，并发设置到给定的集合中。

```java
package com.audition;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BlockingQueueTest {

	 /**
     * 实例化一个队列，队列中的容量为10
     */
    private static BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>(10);
    public static void main(String[] args) {
        ScheduledExecutorService product = Executors.newScheduledThreadPool(1);
        Random random = new Random();
        product.scheduleAtFixedRate(() -> {
            int value = random.nextInt(101);
            try{
            	int remainingCapacity = blockingQueue.remainingCapacity();
            	if(remainingCapacity!=0) {
            		 blockingQueue.offer(value);  //offer()方法就是网队列的尾部设置值
            		 System.out.println("------------生产数据："+value);
            	}
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }, 0, 200, TimeUnit.MILLISECONDS);//每200毫秒执行线程

        new Thread(() -> {
            while(true){
                try {
                    Thread.sleep(2000);
                    System.out.println("开始取值");
                    List<Integer> list = new LinkedList<>();
                    blockingQueue.drainTo(list);  //drainTo()将队列中的值全部从队列中移除，并赋值给对应集合
                    list.forEach(System.out::println);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
```



# 回调

```
package com.audition;

public interface ICallBack<T> {
	public void onSuccess(T result);  
    public void onFailure(T result);  
}
```

```java
package com.audition;

import java.util.Random;

public class Server {

    public void doRequest(ICallBack<String> callBack) {  
        boolean result = getDoRequestResult();  
        if(result)  
            callBack.onSuccess("服务器Message：成功！");  
        else  
            callBack.onFailure("服务器Message：失败！");  
    }  
  
    private boolean getDoRequestResult() {  
        System.out.println("服务器正在处理...");  
        int status = Math.abs(new Random().nextInt());  
        return status % 2 == 0 ? false : true;  
    }  
}
```

```java
package com.audition;

public class Client{

	 public static void main(String[] args) {  
	        Server server = new Server();
	        server.doRequest(new ICallBack<String>() {  
	  
	            @Override  
	            public void onSuccess(String result) {  
	                System.out.println(result);  
	            }  
	            
				@Override
				public void onFailure(String result) {
					System.out.println(result);  
				}  
	        });  
	    }  
}
```



# 单例

```java
package com.audition;
public class Singleton {
    
	private Singleton() {
        
	}
	private static Singleton singleton = null;
	
	public static Singleton newInstance() {
		
		if(singleton == null) {
			synchronized (Singleton.class) {
				if(singleton == null) {
					singleton = new Singleton();
				}
			}
		}
		return singleton;
	}
}
```



# 文件夹监听变化

```java
package com.audition;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class FileWatchServiceDemo {

	public static void main(String[] args) throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();

        String filePath = "C:\\Users\\789\\Desktop\\新建文件夹";

        Paths.get(filePath).register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

        while(true){
            WatchKey key = watchService.take();
            List<WatchEvent<?>> watchEvents = key.pollEvents();
            for (WatchEvent<?> event : watchEvents) {
                if(StandardWatchEventKinds.ENTRY_CREATE == event.kind()){
                    System.out.println("创建：[" + filePath + "/" + event.context() + "]");
                    System.out.println(event.count());
                }
                if(StandardWatchEventKinds.ENTRY_MODIFY == event.kind()){
                    System.out.println("修改：[" + filePath + "/" + event.context() + "]");
                }
                if(StandardWatchEventKinds.ENTRY_DELETE == event.kind()){
                    System.out.println("删除：[" + filePath + "/" + event.context() + "]");
                }
            }
            key.reset();
        }
    }
}
```

# wait /notify/notifyAll

 这些方法只有在Synchronized方法或Synchronized代码块中才能使用

- 如果线程调用了对象的 wait()方法，那么线程便会处于该对象的**等待池**中，等待池中的线程**不会去竞争该对象的锁**。

- 当有线程调用了对象的 **notifyAll**()方法（唤醒所有 wait 线程）或 **notify**()方法（只随机唤醒一个 wait 线程），`被唤醒的线程便会进入该对象的锁池中，锁池中的线程会去竞争该对象锁`。也就是说，调用了notify后只要一个线程会由等待池进入锁池，而notifyAll会将该对象等待池内的所有线程移动到锁池中，等待锁竞争

- `优先级高的线程竞争到对象锁的概率大`，假若某线程没有竞争到该对象锁，它**还会留在锁池中**，唯有线程再次调用 wait()方法，它才会重新回到等待池中。而竞争到对象锁的线程则继续往下执行，直到执行完了 synchronized 代码块，它会释放掉该对象锁，这时锁池中的线程会继续竞争该对象锁。

  ​

  综上，所谓唤醒线程，另一种解释可以说是将线程由等待池移动到锁池，notifyAll调用后，会将全部线程由等待池移到锁池，然后参与锁的竞争，竞争成功则继续执行，如果不成功则留在锁池等待锁被释放后再次参与竞争。而notify只会唤醒一个线程。

  `有了这些理论基础，后面的notify可能会导致死锁，而notifyAll则不会的例子也就好解释了(等待池--锁池，但是没有得到锁，又不能再次调用wait，导致死锁)`



## 生产-消费模型

```java
package com.audition;

import java.util.Queue;

public class Producer implements Runnable{

	private Queue<String> queue;
    private int maxSize;

    public Producer(Queue<String> queue, int maxSize){
        this.queue = queue;
        this.maxSize = maxSize;
    }
    
    int index = 0;

    @Override
    public void run() {
        while (true){
            synchronized (queue){
                while (queue.size() == maxSize){
                    try{
                        System.out.println("Queue is Full");
                        queue.wait();
                    }catch (InterruptedException ie){
                        ie.printStackTrace();
                    }
                }
                index++;
                System.out.println("Produce:" +index);
                queue.add(index+"");
                queue.notifyAll();
            }
        }
    }
}
```



```java
package com.audition;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable{

	private BlockingQueue<String> queue;

    public Consumer(BlockingQueue<String> queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true){
            synchronized (queue){
                while (queue.isEmpty()){
                    System.out.println("Queue is Empty");
                    try{
                        queue.wait();
                    }catch (InterruptedException ie){
                        ie.printStackTrace();
                    }
                }
                String data = null;
				try {
					data = queue.take();//获取并移除此队列的头部
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
                System.out.println("Consume " + data);
                queue.notifyAll();
            }
        }
    }
}
```

```java
package com.audition;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {

	 public static void main(String[] args){
		    BlockingQueue<String> queue = new LinkedBlockingQueue<>();
	        int maxSize = 10;
	        Producer producer = new Producer(queue, maxSize);
	        Consumer consumer = new Consumer(queue);
	        new Thread(producer).start();
	        new Thread(consumer).start();
	    }
}
```



# CountDownLatch

CountDownLatch是一个同步的辅助类，允许一个或多个线程，等待其他一组线程完成操作，再继续执行

```java
package com.audition;

import java.util.concurrent.CountDownLatch;

public class CustomerThread extends Thread{
	
	CountDownLatch countDownLatch = null;
	
	 public CustomerThread(CountDownLatch latch) {
		 this.countDownLatch = latch;
	}

	public void run() {
		 System.out.println("======customerThread=====");
		 countDownLatch.countDown();
	 }
}
```

```java
package com.audition;

import java.util.concurrent.CountDownLatch;

public class CustomerRunable implements Runnable{

	CountDownLatch countDownLatch = null;
	public CustomerRunable(CountDownLatch latch) {
		this.countDownLatch = latch;
	}

	@Override
	public void run() {
		System.out.println("======customerRunable====");
		countDownLatch.countDown();
	}
}
```

```java
package com.audition;

import java.util.concurrent.CountDownLatch;

public class CustomerCountDownLatch {
	
	final static CountDownLatch latch = new CountDownLatch(2) {
		
		@Override
		public void await() throws InterruptedException {
			super.await();
			System.out.println("count down is ok");
		}
	};
	
	public static void main(String[] args) throws InterruptedException {
		  CustomerThread customerThread = new CustomerThread(latch);
		  customerThread.start();
		  new Thread(new CustomerRunable(latch)).start();
		  latch.await();
		  latch.await(4,TimeUnit.SECONDS);//等待其他线程4秒，如果4秒内计数器还不是0就不等待了，继续向下执行。
		  System.out.println("=====所有线程执行完毕=====");
   }
}
```

# CyclicBarrier

假设有一个场景，每个线程代表一个跑步的运动员，当运动员都准备好之后，才一起出发，只要有一个运动员还没有准备好，所有线程就一起等待。



1、CountDownLatch简单的说就是一个线程等待，直到他所等待的其他线程都执行完成并且调用countDown()方法发出通知后，当前线程才可以继续执行。

2、cyclicBarrier是所有线程都进行等待，直到所有线程`都准备好进入await()方法`之后，所有线程同时开始执行！

3、CountDownLatch的计数器只能使用一次。而CyclicBarrier的计数器可以使用reset() 方法重置。所以CyclicBarrier能处理更为复杂的业务场景，比如如果计算发生错误，可以重置计数器，并让线程们重新执行一次。

4,、CyclicBarrier还提供其他有用的方法，比如getNumberWaiting方法可以获得CyclicBarrier阻塞的线程数量。isBroken方法用来知道阻塞的线程是否被中断。如果被中断返回true，否则返回false。



内部实现：

1. 维护了一个计数器变量count = 参与方的个数
2. 调用await方法可以使得count-1；
3. 当判断到是最后一个参与方时，调用singalAll唤醒所有线程

例子 一

```java
package com.audition;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BarrierDemo {

	  final static CyclicBarrier barrier = new CyclicBarrier(5, new Runnable() {
		  
      public void run() {
              System.out.println("所有线程已到达栅栏点");
              try {
                  TimeUnit.SECONDS.sleep(1);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
      });
	
	public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(5);
      
        for (int i = 0; i < 5; i++) {
            service.execute(new Player("玩家" + i, barrier));
        }
        service.shutdown();
    }
}
```



```java
package com.audition;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Player implements Runnable{

	private final String name;
    private final CyclicBarrier barrier;

    public Player(String name, CyclicBarrier barrier) {
        this.name = name;
        this.barrier = barrier;
    }

    public void run() {
            System.out.println(name + "已准备,等待其他玩家准备...");
            try {
				barrier.await();
				System.out.println(name + "已加入游戏");
				barrier.await();
				System.out.println(name + "游戏结束");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
     }
}
```

例子 二

```java
package com.audition;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BarrierDemo2 {

	  final static CyclicBarrier barrier = new CyclicBarrier(2, new Runnable() {
		  
      public void run() {
              System.out.println("所有线程已到达栅栏点");
              try {
                  TimeUnit.SECONDS.sleep(1);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
      });
	
	public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(new CustomerThread(barrier));
        service.execute(new CustomerRunable(barrier));
        service.shutdown();
    }
}
```

```java
package com.audition;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class CustomerThread extends Thread {

	String playName = "A";

	CyclicBarrier cyclicBarrier = null;

	public CustomerThread(CyclicBarrier cyclicBarrier) {
		this.cyclicBarrier = cyclicBarrier;
	}

	public void run() {
		System.out.println(playName + ":开始准备");
		try {
			TimeUnit.SECONDS.sleep(10);
			cyclicBarrier.await();
			System.out.println(playName + "已加入游戏");
			cyclicBarrier.await();
			System.out.println(playName + "游戏结束");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
```

```java
package com.audition;

import java.util.concurrent.CyclicBarrier;

public class CustomerRunable implements Runnable {

	String playName = "B";

	CyclicBarrier cyclicBarrier = null;

	public CustomerRunable(CyclicBarrier cyclicBarrier) {
		this.cyclicBarrier = cyclicBarrier;
	}

	@Override
	public void run() {
		System.out.println(playName + ":开始准备");
		try {
			cyclicBarrier.await();
			System.out.println(playName + "已加入游戏");
			cyclicBarrier.await();
			System.out.println(playName + "游戏结束");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
```



# ThreadLocal

```java
package com.audition;

import java.io.Serializable;

public class Principal implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static transient ThreadLocal<Principal> instance = new ThreadLocal<Principal>();
	
	protected String loginName;
	private Long companyId;   

	public static ThreadLocal<Principal> getInstance() {
		return instance;
	}

	public static void setInstance(ThreadLocal<Principal> instance)   {
		Principal.instance = instance;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public static void set(Principal principal){
		instance.set(principal);
	}

	public static void remove(){
		instance.remove();
	}
	
	public static Principal get(){
		return instance.get();
		
	}

	public Principal(String loginName, Long companyId){
		this.loginName = loginName;
		this.companyId = companyId;
	}
}
```

```java
package com.audition;

public class ThreadLocalDemo implements Runnable{

	private String loginName;
	private Long companyId;
	
  public ThreadLocalDemo(String loginName,Long companyId ) {
		this.loginName = loginName;
		this.companyId = companyId;
	}
	
  @Override
  public void run() {
		Principal.set(new Principal(loginName, companyId));
		businessService();
	}
	
  public static void main(String[] args) {
		ThreadLocalDemo threadLocalDemo  = new ThreadLocalDemo("123",10000L);
		new Thread(threadLocalDemo).start();
		threadLocalDemo  = new ThreadLocalDemo("456",20000L);
		new Thread(threadLocalDemo).start();
	}
	
  public void businessService () {
		String loginName = Principal.get().getLoginName();
		Long companyId = Principal.get().getCompanyId();
		System.out.println("loginName:"+loginName+"companyId:"+companyId);
      callMethod();
	}
    
  public void callMethod(){
		String loginName = Principal.get().getLoginName();
		Long companyId = Principal.get().getCompanyId();
		System.out.println("loginName:"+loginName+"companyId:"+companyId);
	}
}
```



# 同步代码块

一  ：所谓加锁，就是为了防止多个线程同时操作一份数据，如果多个线程操作的数据都是各 自的，那么就没有加锁的必要 

二  ：共享数据的锁对于访问他们的线程来说必须是同一份，否则锁只能私有的锁，各锁个的， 起不到保护共享数据的目的，试想一下将 Object lock 的定义放到 run 方法里面，每次都 会实例化一个 lock，每个线程获取的锁都是不一样的，也就没有争抢可言，说的在通俗 一点甲楼有一个门上了锁，A 要进门，乙楼有一个门上了锁 B 要进门，A 和 B 抢的不是 一个门，因此不存在数据保护或者共享；

三  ：锁的定义可以是任意的一个对象，该对象可以不参与任何运算，只要保证在访问的多个 线程看来他是唯一的即可； 

```java
package com.stydy;

public class TicketWindow2 implements Runnable {

	private int max_value = 0;
	private Object lock = new Object();

	@Override
	public void run() {
		while (true) {
			synchronized (lock) {//每次只能有一个线程进行访问,加在临界点这里
				if (max_value > 50)
					break;
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {

				}
				System.out.println(Thread.currentThread().getName() + ":"+ max_value++);
			}
			//其他线程才有可能获取到前一个线程所释放掉的锁，重新执行代码逻辑
		}
	}
}
```

```java
package com.stydy;

public class Bank2 {
	public static void main(String[] args)
	{
	TicketWindow2 tw2 = new TicketWindow2();
	Thread t1 = new Thread(tw2);
	Thread t2 = new Thread(tw2);
	Thread t3 = new Thread(tw2);
	t1.start();
	t2.start();
	t3.start();
	}
}
```

方式二

```java
package com.stydy;

public class TicketWindow2 implements Runnable {

	private int max_value = 0;

	public void run() {
		while (true) {
			if (ticket())
				break;
		}
	}
	
	private synchronized boolean ticket() {
		if (max_value > 500) {
			return true;
		}
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}
		System.out
				.println(Thread.currentThread().getName() + ":" + max_value++);
		return false;
	}
}
```



# 静态锁

静态锁，锁是类的字节码信息，因此如果一个类的函数为静态方法，那么我们需要通过 该类的 class 信息进行加锁； 

# 死锁

假设有两个线程 A 和 B，其中 A 持有 B 想要的锁，而 B 持有 A 想要的锁，两个都在等 待各自释放所需要的锁，这样的情况很容易引起死锁现象的发生



# yield 

线程的 yield 方法就是短暂放弃 CPU 执行权，但是它刹那点就和其他线程争抢 CPU 执行 权 

```javascript
thread.yield ()
```

# Join 

线程的 Join 方法就是临时加入一个线程，等到该线程执 行结束之后才能运行主线程 

# 线程出现异常捕获 

```java
package com.wenhuisoft.chapter7;

public class FetalException {
	static class MyRunnable implements Runnable {
		public void run() {
			throw new Error();
		}
	}

	public static void main(String[] args) {
		Thread t = new Thread(new MyRunnable());
		t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println(t.getName());
				System.out.println(e);
			}
		});
		t.start();
	}
}
```



# 线程管理

Executors提供创线程池的方式：

newCachedThreadPool

newFixedThreadPool

newSingleThreadExecutor

ScheduledExecutorService product = Executors.newScheduledThreadPool(1)



ThreadPoolExecutor提供了线程池监控相关方法



# 抽象类

```java
package com.stydy;

public abstract class Diagram {

	abstract protected void doBusiness(int size);
	
	abstract protected void afterDoBusiness();

	public final void exec(String msg) {
		int len = msg.getBytes().length;
		doBusiness(len);
		afterDoBusiness();
	}
}
```

```java
package com.stydy;

public class StarDiagram extends Diagram {

	@Override
	protected void doBusiness(int size) {
		System.out.println("doBusiness");
	}

	@Override
	protected void afterDoBusiness() {
		System.out.println("afterDoBusiness");
	}
}
```

```java
package com.stydy;

public class TemplateTest {

	public static void main(String[] args) {
		Diagram diagram = new StarDiagram();
		diagram.exec("wangwenjun");
	}
}
```



# 设计模式

## 策略模式

```java
package com.wenhuisoft.chapter2;
/**
* 策略接口，主要是规范或者让结构程序知道如何进行调用
*/
interface CalcStrategy
{
int calc(int x,int y);
}
/**
* 程序的结构，里面约束了整个程序的框架和执行的大概流程，但并未涉及到业务层面的东西
* 只是将一个数据如何流入如何流出做了规范，只是提供了一个默认的逻辑实现
* @author Administrator
*/
class Calculator
{
private int x = 0;
private int y = 0;
private CalcStrategy strategy = null;
public Calculator(int x,int y)
{
this.x = x;
this.y = y;
}
public Calculator(int x,int y,CalcStrategy strategy)
{
this(x,y);
this.strategy = strategy;
}
public int calc(int x,int y)
{
   return x+y;
}
    
/**
* 只需关注接口，并且将接口用到的入参传递进去即可，并不关心到底具体是要如何进行业务封装
* @return
*/
public int result()
{
if(null!=strategy)
{
return strategy.calc(x, y);
}
return calc(x, y);
}
```

```java
class AddStrategy implements CalcStrategy
{
public int calc(int x, int y)
{
return x+y;
}
}
class SubStrategy implements CalcStrategy
{
public int calc(int x, int y)
{
return x-y;
}
}
```

```java
public class StrategyTest
{
public static void main(String[] args)
{
//没有任何策略时的结果
Calculator c = new Calculator(30, 24);
System.out.println(c.result());
//传入减法策略的结果
Calculator c1 = new Calculator(10,30,new SubStrategy());
System.out.println(c1.result());
//看到这里就可以看到策略模式强大了，算法可以随意设置，系统的结构并不会发生任何变化
Calculator c2 = new Calculator(30, 40, new CalcStrategy()
{
public int calc(int x, int y)
{
return ((x+10)-(y*3))/2;
}
});
System.out.println(c2.result());
  }  
}
```

