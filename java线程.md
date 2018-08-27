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
String collectionToDelimitedString = StringUtils.collectionToDelimitedString(list, ",");
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

```java
package com.boot.web;

public class Cat extends Animal{
	public void eat() {
		System.out.println("===Cat eat");
	};
}
```

```java
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



方式三：

```java
// 创建一个线程池
ExecutorService executorService = Executors.newFixedThreadPool(10);

// 存储执行结果的List
List<Future<String>> results = new ArrayList<Future<String>>();

// 提交10个任务
for ( int i=0; i<10; i++ ) {
    Future<String> result = executorService.submit( new Callable<String>(){
        public String call(){
            int sleepTime = new Random().nextInt(1000);
            Thread.sleep(sleepTime);
            return "线程"+i+"睡了"+sleepTime+"秒";
        }
    } );
    // 将执行结果存入results中
    results.add( result );
}

// 获取10个任务的返回结果
for ( int i=0; i<10; i++ ) {
    // 获取包含返回结果的future对象
    Future<String> future = results.get(i);
    // 从future中取出执行结果（若尚未返回结果，则get方法被阻塞，直到结果被返回为止）
    String result = future.get();
    System.out.println(result);
}
```

此方法的弊端：

1. 需要自己创建容器维护所有的返回结果，比较麻烦；
2. 从list中遍历的每个Future对象并不一定处于完成状态，这时调用get()方法就会被阻塞住，如果系统是设计成每个线程完成后就能根据其结果继续做后面的事，这样对于处于list后面的但是先完成的线程就会增加了额外的等待时间。

方式四：

```java
// 创建一个线程池
ExecutorService executorService = Executors.newFixedThreadPool(10);

// 创建存储任务的容器
List<Callable<String>> tasks = new ArrayList<Callable<String>>();

// 提交10个任务
for ( int i=0; i<10; i++ ) {
    Callable<String> task = new Callable<String>(){
        public String call(){
            int sleepTime = new Random().nextInt(1000);
            Thread.sleep(sleepTime);
            return "线程"+i+"睡了"+sleepTime+"秒";
        }
    };
    executorService.submit( task );
    // 将task添加进任务队列
    tasks.add( task );
}

// 获取10个任务的返回结果
List<Future<String>> results = executorService.invokeAll( tasks );

// 输出结果
for ( int i=0; i<10; i++ ) {
    // 获取包含返回结果的future对象
    Future<String> future = results.get(i);
    // 从future中取出执行结果（若尚未返回结果，则get方法被阻塞，直到结果被返回为止）
    String result = future.get();
    System.out.println(result);
}
```

本方法能解决第一个弊端，即并不需要自己去维护一个存储返回结果的容器。当我们需要获取线程池所有的返回结果时，只需调用invokeAll函数即可。 
但是，这种方式需要你自己去维护一个用于存储任务的容器。



方式五：

```java
ExecutorService exec = Executors.newFixedThreadPool(10);

final BlockingQueue<Future<Integer>> queue = new LinkedBlockingDeque<Future<Integer>>(  
                10);  
        //实例化CompletionService  
        final CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(  
                exec, queue); 

// 提交10个任务
for ( int i=0; i<10; i++ ) {
    executorService.submit( new Callable<String>(){
        public String call(){
            int sleepTime = new Random().nextInt(1000);
            Thread.sleep(sleepTime);
            return "线程"+i+"睡了"+sleepTime+"秒";
        }
    } );
}

// 输出结果
for ( int i=0; i<10; i++ ) {
    // 获取包含返回结果的future对象（若整个阻塞队列中还没有一条线程返回结果，那么调用take将会被阻塞，当然你可以调用poll，不会被阻塞，若没有结果会返回null，poll和take返回正确的结果后会将该结果从队列中删除）
    Future<String> future = completionService.take();
    // 从future中取出执行结果，这里存储的future已经拥有执行结果，get不会被阻塞
    String result = future.get();
    System.out.println(result);
}
```



# FutureTask

```java
package futuretask;
 
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
 
public class FutureTaskForMultiCompute {
    
    public static void main(String[] args) {
        
        FutureTaskForMultiCompute inst=new FutureTaskForMultiCompute();
        // 创建任务集合
        List<FutureTask<Integer>> taskList = new ArrayList<FutureTask<Integer>>();
        // 创建线程池
        ExecutorService exec = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            // 传入Callable对象创建FutureTask对象
            FutureTask<Integer> ft = new FutureTask<Integer>(inst.new ComputeTask(i, ""+i));
            taskList.add(ft);
            // 提交给线程池执行任务，也可以通过exec.invokeAll(taskList)一次性提交所有任务;
            exec.submit(ft);
        }
        
        System.out.println("所有计算任务提交完毕, 主线程接着干其他事情！");
 
        // 开始统计各计算线程计算结果
        Integer totalResult = 0;
        for (FutureTask<Integer> ft : taskList) {
            try {
                //FutureTask的get方法会自动阻塞,直到获取计算结果为止
                totalResult = totalResult + ft.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
 
        // 关闭线程池
        exec.shutdown();
        System.out.println("多任务计算后的总结果是:" + totalResult);
 
    }
 
    private class ComputeTask implements Callable<Integer> {
 
        private Integer result = 0;
        private String taskName = "";
        
        public ComputeTask(Integer iniResult, String taskName){
            result = iniResult;
            this.taskName = taskName;
            System.out.println("生成子线程计算任务: "+taskName);
        }
        
        public String getTaskName(){
            return this.taskName;
        }
        
        @Override
        public Integer call() throws Exception {
            // TODO Auto-generated method stub
 
            for (int i = 0; i < 100; i++) {
                result =+ i;
            }
            // 休眠5秒钟，观察主线程行为，预期的结果是主线程会继续执行，到要取得FutureTask的结果是等待直至完成。
            Thread.sleep(5000);
            System.out.println("子线程计算任务: "+taskName+" 执行完成!");
            return result;
        }
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



# CompletableFuture

CompletableFuture类实现了CompletionStage和Future接口。Future是Java 5添加的类，用来描述一个异步计算的结果，但是获取一个结果时方法较少,要么通过轮询isDone，确认完成后，调用get()获取值，要么调用get()设置一个超时时间。但是这个get()方法会阻塞住调用线程，这种阻塞的方式显然和我们的异步编程的初衷相违背。

为了解决这个问题，JDK吸收了guava的设计思想，加入了Future的诸多扩展功能形成了CompletableFuture。



- 将两个异步计算合并为一个——这两个异步计算之间相互独立，同时第二个又依赖于第一个的结果。
- 等待 Future 集合中的所有任务都完成。
- 仅等待 Future集合中最快结束的任务完成（有可能因为它们试图通过不同的方式计算同一个值），并返回它的结果。
- 通过编程方式完成一个Future任务的执行（即以手工设定异步操作结果的方式）。
- 应对 Future 的完成事件（即当 Future 的完成事件发生时会收到通知，并能使用 Future 计算的结果进行下一步的操作，不只是简单地阻塞等待操作的结果）



使用supplyAsync使得方法变为异步

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureTest {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "hello world";
		});
		System.out.println("111-----------");
		System.out.println(future.get()); 
		System.out.println("222-----------");
	}
}
```

异常处理

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureTest {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		 CompletableFuture<String> completableFuture = new CompletableFuture<>();
		    new Thread(() -> {
		        // 模拟执行耗时任务
		        System.out.println("task doing...");
		        try {
		            Thread.sleep(3000);
		            int i = 1/0;
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		        // 告诉completableFuture任务已经完成
		        completableFuture.complete("ok");
		    }).start();
		    // 获取任务结果，如果没有完成会一直阻塞等待
		    System.out.println("main thread doSthing...");
		    String result = completableFuture.get();
		    System.out.println("计算结果:" + result);
	}
}
```

上述代码int i = 1/0;出现了异常，异常会被限制在执行任务的线程的范围内，最终会杀死该线程，而这会导致等待`get`方法返回结果的线程永久地被阻塞。CompletableFuture的`completeExceptionally`方法将导致CompletableFuture内发生问题的异常抛出。这样，当执行任务发生异常时，调用`get()`方法的线程将会收到一个 `ExecutionException`异常，该异常接收了一个包含失败原因的Exception 参数。

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureTest {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		 CompletableFuture<String> completableFuture = new CompletableFuture<>();
		    new Thread(() -> {
		        // 模拟执行耗时任务
		        System.out.println("task doing...");
		        try {
		            Thread.sleep(3000);
		            int i = 1/0;
		        } catch (Exception e) {
		        	 completableFuture.completeExceptionally(e);
		        }
		        // 告诉completableFuture任务已经完成
		        completableFuture.complete("ok");
		    }).start();
		    // 获取任务结果，如果没有完成会一直阻塞等待
		    System.out.println("main thread doSthing...");
		    String result = "";;
			try {
				result = completableFuture.get();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		    System.out.println("计算结果:" + result);
	}
}
```

等待任务完成

JDK CompletableFuture 自带多任务组合方法allOf和anyOf

`allOf`是等待所有任务完成，构造后CompletableFuture完成

`anyOf`是只要有一个任务完成，构造后CompletableFuture就完成

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureTest {

	 public static void main(String[] args) {
	        Long start = System.currentTimeMillis();
	        // 结果集
	        List<String> list = new ArrayList<>();
	        ExecutorService executorService = Executors.newFixedThreadPool(10);
	        List<Integer> taskList = Arrays.asList(2, 1, 3, 4, 5, 6, 7, 8, 9, 10);
	        // 全流式处理转换成CompletableFuture[]+组装成一个无返回值CompletableFuture，join等待执行完毕。返回结果whenComplete获取
	        CompletableFuture[] cfs = taskList.stream()
	                .map(integer -> CompletableFuture.supplyAsync(() -> calc(integer), executorService)
	                                .thenApply(h->Integer.toString(h))
	                                .whenComplete((s, e) -> {
	                                    System.out.println("任务"+s+"完成!result="+s+"，异常 e="+e+","+new Date());
	                                    list.add(s);
	                                })
	                ).toArray(CompletableFuture[]::new);
	        // 封装后无返回值，必须自己whenComplete()获取
	        CompletableFuture.allOf(cfs).join();
	        System.out.println("list="+list+",耗时="+(System.currentTimeMillis()-start));
	    }

	    public static Integer calc(Integer i) {
	        try {
	            if (i == 1) {
	                Thread.sleep(3000);//任务1耗时3秒
	            } else if (i == 5) {
	                Thread.sleep(5000);//任务5耗时5秒
	            } else {
	                Thread.sleep(1000);//其它任务耗时1秒
	            }
	            System.out.println("task线程：" + Thread.currentThread().getName()
	                    + "任务i=" + i + ",完成！+" + new Date());
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        return i;
	    }
}
```

这里的关键词是`thenApply`：

1. `then`是指在当前阶段正常执行完成后（正常执行是指没有抛出异常）进行的操作。在本例中，当前阶段已经完成并得到值`message`。
2. `Apply`是指将一个`Function`作用于之前阶段得出的结果

`Function`是阻塞的，这意味着只有当大写操作执行完成之后才会执行`getNow()`方法。



```java
 /**
     * 异步的获取产品价格
     *
     * @param product 产品名
     * @return 最终价格
     */
    public Future<Double> getPriceAsync(String product) {
        //创建CompletableFuture 对象，它会包含计算的结果
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        //在另一个线程中以异步方式执行计算
        new Thread(() -> {
            try {
                double price = calculatePrice(product);
                //如果价格计算正常结束，完成Future操作并设置商品价格
                futurePrice.complete(price);
            } catch (Exception ex) {
                //否则就抛出导致失败的异常，完成这 次Future操作
                futurePrice.completeExceptionally(ex);
            }
        }).start();
        // 无需等待还没结束的计算，直接返回Future对象
        return futurePrice;
    }
    
public class ShopMain {

  public static void main(String[] args) {
    Shop shop = new Shop("BestShop");
    long start = System.nanoTime();
    //查询商店，试图 取得商品的价格
    Future<Double> futurePrice = shop.getPriceAsync("my favorite product");
    long invocationTime = ((System.nanoTime() - start) / 1_000_000);
    System.out.println("Invocation returned after " + invocationTime 
                                                    + " msecs");
    // 执行更多任务，比如查询其他商店
    doSomethingElse();
    // 在计算商品价格的同时
    try {
        //从Future对象中读 取价格，如果价格 未知，会发生阻塞
        double price = futurePrice.get();
        System.out.printf("Price is %.2f%n", price);
    } catch (ExecutionException | InterruptedException e) {
        throw new RuntimeException(e);
    }
    long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
    System.out.println("Price returned after " + retrievalTime + " msecs");
  }

  private static void doSomethingElse() {
      System.out.println("Doing something else...");
  }
}
```

getNow 其中的getNow有点特殊，如果结果已经计算完则返回结果或抛异常，否则返回给定的valueIfAbsent的值。

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureTest {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		 CompletableFuture<String> completableFuture = new CompletableFuture<>();
		    new Thread(() -> {
		        // 模拟执行耗时任务
		        System.out.println("task doing...");
		        try {
		        } catch (Exception e) {
		        	 completableFuture.completeExceptionally(e);
		        }
		        // 告诉completableFuture任务已经完成
		        completableFuture.complete("ok");
		    }).start();
		    // 获取任务结果，如果没有完成会一直阻塞等待
		    System.out.println("main thread doSthing...");
		    String result = "";;
		    Thread.sleep(1000);
			result = completableFuture.getNow("bbbbbbbbbbbbbb");
		    System.out.println("计算结果:" + result);
	}
}
```



```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CompletableFutureTest {

	public static void main(String[] args) {
		CompletableFuture<Object> objectCompletableFuture = CompletableFuture.anyOf(CompletableFuture.runAsync(() -> {
			try {
				System.out.println(Thread.currentThread().getName() + ": 1====开始");
				TimeUnit.SECONDS.sleep(5);
				System.out.println(Thread.currentThread().getName() + ": 1====结束");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}), CompletableFuture.supplyAsync(() -> {
			try {
				System.out.println(Thread.currentThread().getName() + ": 2====开始");
				TimeUnit.SECONDS.sleep(8);
				System.out.println(Thread.currentThread().getName() + ": 2====结束");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "ssssssssssssss";
		}));
		try {
			System.out.println("获取返回值：" + objectCompletableFuture.get());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
```

```coffeescript
ForkJoinPool.commonPool-worker-1: 1====开始
ForkJoinPool.commonPool-worker-2: 2====开始
ForkJoinPool.commonPool-worker-1: 1====结束
获取返回值：null
```

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CompletableFutureTest {

	public static void main(String[] args) {
		CompletableFuture<Void> objectCompletableFuture = CompletableFuture.allOf(CompletableFuture.runAsync(() -> {
			try {
				System.out.println(Thread.currentThread().getName() + ": 1====开始");
				TimeUnit.SECONDS.sleep(5);
				System.out.println(Thread.currentThread().getName() + ": 1====结束");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}), CompletableFuture.supplyAsync(() -> {
			try {
				System.out.println(Thread.currentThread().getName() + ": 2====开始");
				TimeUnit.SECONDS.sleep(8);
				System.out.println(Thread.currentThread().getName() + ": 2====结束");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "ssssssssssssss";
		}));
		try {
			System.out.println("获取返回值：" + objectCompletableFuture.get());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
```

```
ForkJoinPool.commonPool-worker-1: 1====开始
ForkJoinPool.commonPool-worker-2: 2====开始
ForkJoinPool.commonPool-worker-1: 1====结束
ForkJoinPool.commonPool-worker-2: 2====结束
获取返回值：null
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



## 方法区别

add  添加元素的时候，若超出了度列的长度会直接抛出异常

put  若向队尾添加元素的时候发现队列已经满了会发生阻塞一直等待空间，以加入元素

offer 在添加元素时，如果发现队列已满无法添加的话，会直接返回false



poll: 若队列为空，返回null。

remove:若队列为空，抛出NoSuchElementException异常。

take:若队列为空，发生阻塞，等待有元素。



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



# 生产-消费模型

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



# Semaphore

Java并发包中的信号量Semaphore实际上是一个功能完毕的计数信号量，从概念上讲，它维护了一个许可集合，对控制一定资源的消费与回收有着很重要的意义。Semaphore可以控制某个资源被同时访问的任务数，它通过acquire（）获取一个许可，release（）释放一个许可。如果被同时访问的任务数已满，则其他acquire的任务进入等待状态，直到有一个任务被release掉，它才能得到许可



Semaphore管理一系列许可证。每个acquire方法阻塞，直到有一个许可证可以获得然后拿走一个许可证；每个release方法增加一个许可证，这可能会释放一个阻塞的acquire方法。然而，其实并没有实际的许可证这个对象，Semaphore只是维持了一个可获得许可证的数量。 
Semaphore经常用于限制获取某种资源的线程数量。下面举个例子，比如说操场上有5个跑道，一个跑道一次只能有一个学生在上面跑步，一旦所有跑道在使用，那么后面的学生就需要等待，直到有一个学生不跑了

```java
package com.audition;

import java.util.concurrent.Semaphore;

public class SemaphoreTest {
	public static void main(String[] args) {
		int N = 8; // 工人数
		Semaphore semaphore = new Semaphore(5); // 机器数目 Semaphore可以控制某个资源被同时访问的任务数
		for (int i = 0; i < N; i++)
			new Worker(i, semaphore).start();
	}

	static class Worker extends Thread {
		private int num;
		private Semaphore semaphore;

		public Worker(int num, Semaphore semaphore) {
			this.num = num;
			this.semaphore = semaphore;
		}

		@Override
		public void run() {
			try {
				semaphore.acquire();//acquire（）获取一个许可
				System.out.println("工人" + this.num + "占用一个机器在生产...");
				Thread.sleep(2000);//release（）释放一个许可
				System.out.println("工人" + this.num + "释放出机器");
				semaphore.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
```



```java
package com.audition;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {
	public static void main(String[] args) {
		
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(10);
		//Semaphore这个参数是同一时间内，最多允许多少个线程同时执行acquire方法和release方法之间的代码
		Semaphore semaphore = new Semaphore(5);
		
		for(int i =0 ;i < 10 ;i ++) {
			newFixedThreadPool.execute(new Task(i,semaphore));
		}
	}
	
	
	static class Task implements Runnable{
		
		private int num;
		private Semaphore semaphore;
		
		public Task(int num,Semaphore semaphore) {
			this.num = num;
			this.semaphore = semaphore;
		}
		
		@Override
		public void run() {
			try {
                
				semaphore.acquire();
				System.out.println("==="+num+":占用");
				Thread.sleep(2000);
				System.out.println("==="+num+"释放");
				semaphore.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
```



# Phaser适用场景

CountDownLatch和CyclicBarrier都是JDK 1.5引入的，而Phaser是JDK 1.7引入的。Phaser的功能与CountDownLatch和CyclicBarrier有部分重叠，同时也提供了更丰富的语义和更灵活的用法。

Phaser顾名思义，与阶段相关。Phaser比较适合这样一种场景，一种任务可以分为多个阶段，现希望多个线程去处理该批任务，对于每个阶段，多个线程可以并发进行，但是希望保证只有前面一个阶段的任务完成之后才能开始后面的任务。这种场景可以使用多个CyclicBarrier来实现，每个CyclicBarrier负责等待一个阶段的任务全部完成。但是使用CyclicBarrier的缺点在于，需要明确知道总共有多少个阶段，同时并行的任务数需要提前预定义好，且无法动态修改。而Phaser可同时解决这两个问题。



```java
public class PhaserDemo {
  public static void main(String[] args) throws IOException {
    int parties = 3;
    int phases = 4;
    final Phaser phaser = new Phaser(parties) {
      @Override  
      protected boolean onAdvance(int phase, int registeredParties) {  
          System.out.println("====== Phase : " + phase + " ======");  
          return registeredParties == 0;  
      }  
    };
    for(int i = 0; i < parties; i++) {
      int threadId = i;
      Thread thread = new Thread(() -> {
        for(int phase = 0; phase < phases; phase++) {
          System.out.println(String.format("Thread %s, phase %s", threadId, phase));
          phaser.arriveAndAwaitAdvance();
        }
      });
      thread.start();
    }
  }
}
```

执行结果如下

```
Thread 0, phase 0
Thread 1, phase 0
Thread 2, phase 0
====== Phase : 0 ======
Thread 2, phase 1
Thread 0, phase 1
Thread 1, phase 1
====== Phase : 1 ======
Thread 1, phase 2
Thread 2, phase 2
Thread 0, phase 2
====== Phase : 2 ======
Thread 0, phase 3
Thread 1, phase 3
Thread 2, phase 3
====== Phase : 3 ======
```

从上面的结果可以看到，多个线程必须等到其它线程的同一阶段的任务全部完成才能进行到下一个阶段，并且每当完成某一阶段任务时，Phaser都会执行其*onAdvance*方法。



# Exchanger

用于进行线程间的数据交换

两个线程通过exchange方法交换数据，如果一个线程先执行exchange方法，它会一直等待第二个线程也执行exchange方法
当两个线程都达到同步点时，这两个线程就可以交换数据，将本线程生产出来的数据传递给对方

```java
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Exchange_Thread {

	public static void main(String[] args) {
		ExecutorService executorSer = Executors.newCachedThreadPool();
		final Exchanger<String> exchanger = new Exchanger<String>();
		executorSer.execute(new Runnable() {
			String a = "yangyu";
			public void run() {
				try {
					String b = (String) exchanger.exchange(a);
					System.out.println("this is A,but B is:" + b);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		executorSer.execute(new Runnable() {
			String b = "java code";

			public void run() {
				try {
					Thread.sleep((long) (Math.random() * 100));
					String a = (String) exchanger.exchange(b);
					System.out.println("this is B,but A is:" + a);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		executorSer.shutdown();
	
}
```





# 综合例子

题目：多线程之间需要等待协调，才能完成某种工作，问怎么设计这种协调方案？如：子线程循环10次，接着主线程循环100，接着又回到子线程循环10次，接着再回到主线程又循环100，如此循环50次。

在并发编程中经常会使用到一些并发工具类，来对线程的并发量、执行流程、资源依赖等进行控制。这里我们主要探讨三个经常使用的并发工具类：CountDownLatch，CyclicBarrier和Semaphore。

## 一 CountDownLatch 



从CountDownLatch的字面意思就可以体现出其设计模型，countdown在英语里具有倒计时的（倒数）意思，Latch就是门闩的意思。CountDownLatch的构造函数接受一个int值作为计数器的初始值N，当程序调用countDown()的时候，N便会减1（体现出了倒数的意义），当N值减为0的时候，阻塞在await()的线程便会唤醒，继续执行。这里通过一个例子来说明其应用场景。

假设我们主线程需要创建5个工作线程来分别执行5个任务，主线程需要等待5个任务全部完成后才会进行后续操作，那么我们就可以声明N=5的CountDownLatch，来进行控制。

代码如下：

```java
public class CountDownLatchDemo {

    private static final CountDownLatch countDownLatch = new CountDownLatch(5);

    public static void main(String[] args) throws InterruptedException {

        //循环创建5个工作线程
        for( int ix = 0; ix != 5; ix++ ){

            new Thread(new Runnable() {
                public void run() {
                    try{
                        System.out.println( Thread.currentThread().getName() + " start" );
                        Thread.sleep(1000);
                        countDownLatch.countDown();
                        System.out.println( Thread.currentThread().getName() + " stop" );

                    } catch ( InterruptedException ex ){
                    }
                }
            }, "Task-Thread-" + ix ).start();
            Thread.sleep(500);
        }
        //主线程等待所有任务完成
        countDownLatch.await();
        System.out.println("All task has completed.");
    }
}
```

运行结果：

Task-Thread-0 start

Task-Thread-1 start

Task-Thread-0 stop

Task-Thread-2 start

Task-Thread-1 stop

Task-Thread-3 start

Task-Thread-2 stop

Task-Thread-4 start

Task-Thread-3 stop

Task-Thread-4 stop

All task has completed.

在主线程创建了5个工作线程后，就会阻塞在countDownLatch.await()，等待5个工作线程全部完成任务后返回。任务的执行顺序可能会不同，但是任务完成的Log一定会在最后显示。CountDownLatch通过计数器值的控制，实现了允许一个或多个线程等待其他线程完成操作的并发控制。

## 二 CyclicBarrier 



CyclicBarrier就字面意思是可循环的屏障，其体现了两个特点，可循环和屏障。调用CyclicBarrier的await()方法便是在运行线程中插入了屏障，当线程运行到这个屏障时，便会阻塞在await()方法中，直到等待所有线程运行到屏障后，才会返回。CyclicBarrier的构造函数同样接受一个int类型的参数，表示屏障拦截线程的数目。另一个特点循环便是体现处出了CyclicBarrier与CountDownLatch不同之处了，CyclicBarrier可以通过reset()方法，将N值重置，循环使用，而CountDownLatch的计数器是不能重置的。此外，CyclicBarrier还提供了一个更高级的用法，允许我们设置一个所有线程到达屏障后，便立即执行的Runnable类型的barrierAction（注意：barrierAction不会等待await()方法的返回才执行，是立即执行！）机会，这里我们通过以下代码来测试一下CyclicBarrier的特性。

代码如下：

```java
public class CyclicBarrierDemo {

    private final static CyclicBarrier cyclicBarrier = new CyclicBarrier(5, new MyBarrierAction());

    private final static AtomicInteger atcIx = new AtomicInteger(1);

    public static void main(String[] args) {

        for (int ix = 0; ix != 10; ix++){
            new Thread(new Runnable() {
                public void run() {
                    try{
                        System.out.println(Thread.currentThread().getName() + " start");
                        Thread.sleep(atcIx.getAndIncrement() * 1000 );
                        cyclicBarrier.await();
                        System.out.println( Thread.currentThread().getName() + " stop" );
                    } catch ( Exception ex){
                    }
                }
            }, "Thread-" + ix).start();
        }
    }

    private static class MyBarrierAction implements Runnable {

        @Override
        public void run() {
            System.out.println("MyBarrierAction is call.");
        }
    }
}
```

运行结果：

Thread-0 start

Thread-1 start

Thread-2 start

Thread-3 start

Thread-4 start

MyBarrierAction is call.

Thread-4 stop

Thread-0 stop

Thread-1 stop

Thread-2 stop

Thread-3 stop

根据运行结果，我们可以看到一下几点：

1. 首先在线程没有调用够N次cyclicBarrier.await()时，所有线程都会阻塞在cyclicBarrier.await()上，也就是说必须N个线程同时到达屏障，才会所有线程越过屏障继续执行。
2. 验证了BarrierAction的执行时机是所有阻塞线程都到达屏障之后，并且BarrierAction执行后，所有线程才会从await()方法返回，继续执行。

## 三 Semaphore 



Semaphore信号量并发工具类，其提供了aquire()和release()方法来进行并发控制。Semaphore一般用于资源限流，限量的工作场景，例如数据库连接控制。假设数据库的最大负载在10个连接，而现在有100个客户端想进行数据查询，显然我们不能让100个客户端同时连接上来，找出数据库服务的崩溃。那么我们可以创建10张令牌，想要连接数据库的客户端，都必须先尝试获取令牌（Semaphore.aquire()），当客户端获取到令牌后便可以进行数据库连接，并在完成数据查询后归还令牌（Semaphore.release()），这样就能保证同时连接数据库的客户端不超过10个，因为只有10张令牌，这里给出该场景的模拟代码。

代码如下：

```java
public class SemaphoreDemo {

    private static final Semaphore semaphoreToken = new Semaphore(10);

    public static void main(String[] args) {

        for (int ix = 0; ix != 100; ix++) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        semaphoreToken.acquire()
                        System.out.println("select * from xxx");
                        semaphoreToken.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
```

也许有同学会问，aquire()函数获取许可证的顺序和调用的先后顺序有关系吗，也就是说该例子中客户端是否是排队获取令牌的？答案不是，因为Semaphore默认是非公平的，当然其构造函数提供了设置为公平信号量的参数。

四

本例答案 

```java
public class Question12 {

    public static void main(String[] args) throws InterruptedException {

        final Object object = new Object();

        new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 50; i++) {
                    synchronized (object) {
                        for (int j = 0; j < 10; j++) {
                            System.out.println("SubThread:" + (j + 1));
                        }
                        object.notify();
                        try {
                            object.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }).start();

        for (int i = 0; i < 50; i++) {

            synchronized (object) {

                //主线程让出锁，等待子线程唤醒

                object.wait();

                for (int j = 0; j < 100; j++) {

                    System.out.println("MainThread:" + (j + 1));

                }
                object.notify();
            }
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



# volatile

```java
package com.audition;

import java.util.concurrent.CountDownLatch;

public class Counter {
	public static volatile int num = 0;
    //使用CountDownLatch来等待计算线程执行完
    static CountDownLatch countDownLatch = new CountDownLatch(30);
    
    Object obj = new Object();
    
    public static void main(String []args) throws InterruptedException {
        //开启30个线程进行累加操作
        for(int i=0;i<30;i++){
            new Thread(){
                public void run(){
                    for(int j=0;j<10000;j++){
                    	synchronized (Object.class) {/**如果不加会造成结果错误，因为num不是原子操作**/
                    		num++;//自加操作
						}
                    }
                    countDownLatch.countDown();
                }
            }.start();
        }
        //等待计算线程执行完
        countDownLatch.await();
        System.out.println(num);
    }
}
```

以上问题由于num++不是原子操作，所以需要加锁，可以使用下面方式解决。

```java
package com.audition;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
	
	public static AtomicInteger num = new AtomicInteger(0);

	static CountDownLatch countDownLatch = new CountDownLatch(30);
    
    Object obj = new Object();
    
    public static void main(String []args) throws InterruptedException {
        for(int i=0;i<30;i++){
            new Thread(){
                public void run(){
                    for(int j=0;j<10000;j++){
                    	 num.incrementAndGet();//原子性的num++,通过循环CAS方式
                    }
                    countDownLatch.countDown();
                }
            }.start();
        }
        countDownLatch.await();
        System.out.println(num);
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

在某些情况下，主线程创建并启动了子线程，如果子线程中需要进行大量的耗时运算，主线程往往将早于子线程结束之前结束，如果主线程想等待子线程执行完毕后，获得子线程中的处理完的某个数据，就要用到join方法了，方法join（）的作用是等待线程对象

```java
package com.audition;

public class JoinTest {
	
	public static class MyThread extends Thread {
		@Override
		public void run() {
			
			try {
				int m = (int) (Math.random() * 10000);
				System.out.println("我在子线程中会随机睡上0-9秒，时间为="+m);
				Thread.sleep(m);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) throws InterruptedException {
		MyThread myThread =new MyThread();
		myThread.start();
		myThread.join();
		//myThread.join(1000);//等待子线程100秒，效果等同于sleep
		System.out.println("正常情况下肯定是我先执行完，但是加入join后，main主线程会等待子线程执行完毕后才执行");
	}
}
```

# Lock 

多线程和并发编程中使用 lock 接口的最大优势是它为读和写提供两个单独的锁，可以让你构建高性能数据结构，比如 `ConcurrentHashMap` 和条件阻塞。









# 守护线程

守护线程随着主线程的退出而退出，所以子线程有可能没执行完

```java
public class DaemonDemo {

    public static void main(String[] args) throws InterruptedException {
    	
        Daemon  d1 = new Daemon();
        Daemon  d2 = new Daemon();
        /**
         * 将两个线程设置为守护线程
         */
        d1.setDaemon(true);
        d2.setDaemon(true);

        d1.start();
        d2.start();

        Thread.sleep(2000);
        System.out.println("主线程正在执行");
    }
}

class Daemon extends Thread {

    @Override
    public void run() {
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println("子线程执行");
    }
}
```







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



# 线程顺序执行

```java

public class ThreadTest {

	public static void main(String[] args) {

		final Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("call t1");
			}
		});

		final Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//引用t1线程，等待t1线程执行完
					t1.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("call t2");
			}
		});

		Thread t3 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					//引用t2线程，等待t2线程执行完
					t2.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("call t3");
			}
		});
		t3.start();// 这里三个线程的启动顺序可以任意，大家可以试下！
		t2.start();
		t1.start();
	}
}
```



# addShutdownHook

当下列情况出现时被调用

1. 程序正常退出
2. 使用System.exit()
3. 终端使用Ctrl+C触发的中断
4. 系统关闭
5. OutOfMemory宕机
6. 使用Kill pid命令干掉进程（注：在使用kill -9 pid时，是不会被调用的）

```java
package com.audition;

public class TestRuntimeShutdownHook {
	 public static void main(String[] args) {  
		  
	        Thread shutdownHookOne = new Thread() {  
	            public void run() {  
	                System.out.println("shutdownHook one...");  
	            }  
	        };  
	        Thread shutdownHookTwo = new Thread() {  
	            public void run() {  
	                System.out.println("shutdownHook two...");  
	            }  
	        }; 
	        Runtime.getRuntime().addShutdownHook(shutdownHookOne);  
	        Runtime.getRuntime().addShutdownHook(shutdownHookTwo);  
	        new Thread() {  
	            public void run() {  
	                try {  
	                    Thread.sleep(1000);  
	                } catch (InterruptedException e) {  
	                    e.printStackTrace();  
	                }  
	                System.out.println("thread one doing something...");  
	            }  
	        }.start();  
	  
	        new Thread() {  
	            public void run() {  
	                try {  
	                    Thread.sleep(2000);  
	                } catch (InterruptedException e) {  
	                    e.printStackTrace();  
	                }  
	                System.out.println("thread two doing something...");  
	            }  
	        }.start();  
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



## Java线程池



Executors.newCachedThreadPool()

Executors.newFixedThreadPool()

都是调用的

```
new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>())
```

`线程池`：顾名思义，用一个池子装载多个线程，使用池子去管理多个线程。

`问题来源`：应用大量通过new Thread()方法创建执行时间短的线程，较大的消耗系统资源并且系统的响应速度变慢。

`解决办法`：使用线程池管理短时间执行完毕的大量线程，通过重用已存在的线程，降低线程创建和销毁造成的消耗，提高系统响应速度。

Executors静态方法：创建线程池



| 方法                             | 描述                                                         | 返回对象                 |
| -------------------------------- | ------------------------------------------------------------ | ------------------------ |
| newCachedThreadPool              | 创建有缓存功能的线程池， 无界线程池，可以进行自动线程回收    | ExecutorService          |
| newFixedThreadPool               | 创建固定大小的线程池                                         | ExecutorService          |
| newSingleThreadExecutor          | 创建单线程的线程池 。单个线程的线程池，即线程池中每次只有一个线程工作，单线程串行执行任务 | ExecutorService          |
| newScheduledThreadPool           | 创建固定大小的线程池，指定延迟                               | ScheduledExecutorService |
| newSingleThreadScheduledExecutor | 创建单线程的线程池，指定延迟                                 | ScheduledExecutorService |

线程池使用方法：

| 对象            |                        |                                      |
| --------------- | ---------------------- | ------------------------------------ |
| ExecutorService | submit(Runnable task)  | 提交任务 ，有返回值 返回future       |
| ExecutorService | execute(Runnable task) | 执行任务 ，没有返回值                |
| ExecutorService | shutdown               | 关闭线程池，但是会执行完已提交的任务 |
| ExecutorService | shutdownNow            | 关闭线程池，关闭所有任务             |
|                 |                        |                                      |



​       1、corePoolSize(线程池基本大小，核心线程数量) >= 0;【当前最大并行运行线程数】

　　2、maximumPoolSize(线程池最大大小) >= 1;【当前最大创建线程数】

　　3、keepAliveTime(线程存活保持时间) >= 0;【线程在线程池空闲时间超过这个时间，会被终止掉】

　　4、workQueue(任务队列)不能为空；【用于传输和保存等待执行任务的阻塞队列】

　　5、handler(线程饱和策略)不能为空。【当线程池和队列已满的处理策略】



 Executors类提供静态工厂方法默认参数

| 工厂方法                                 | corePoolSize | maximumPoolSize   | KeepAliveTime | workQueue           |
| ---------------------------------------- | ------------ | ----------------- | ------------- | ------------------- |
| nreCachedThreadPool()                    | 0            | Integer.MAX_VALUE | 60L           | SyschronousQueue    |
| newFixedThreadPool(int nThreads)         | nThreads     | nThreads          | 0             | LindedBlockingQueue |
| newSingleThreadExecutor()                | 1            | 1                 | 0             | LindedBlockingQueue |
| newScheduledThreadPool(int corePoolSize) | corePoolSize | Integer.MAX_VALUE | 0             | DelayedWorkQueue    |
| newSingleThreadScheduledExecutor()       | 1            | Integer.MAX_VALUE | 0             | DelayedWorkQueue    |





## 定时任务

方式一：

```java
ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(1);
		newScheduledThreadPool.scheduleAtFixedRate(command, initialDelay, period, unit)
```

方式二：

```java
ScheduledThreadPoolExecutor scheduledThreadPoolExecutor= new ScheduledThreadPoolExecutor(1);
		scheduledThreadPoolExecutor.scheduleAtFixedRate(command, initialDelay, period, unit)
```



## BlockingQueue

FIFO，poll后就从队列删除

因此如果你使用while(true)来获得队列元素，千万别用poll()，CPU会100%的. 



**offer(E e)**: 将给定的元素设置到队列中，如果设置成功返回true, 否则返回false. e的值不能为空，否则抛出空指针异常。

**offer(E e, long timeout, TimeUnit unit)**: 将给定元素在给定的时间内设置到队列中，如果设置成功返回true, 否则返回false.

**add(E e)**: 将给定元素设置到队列中，如果设置成功返回true, 否则抛出异常。如果是往限定了长度的队列中设置值，推荐使用offer()方法。

**put(E e)**: 将元素设置到队列中，如果队列中没有多余的空间，该方法会一直阻塞，直到队列中有多余的空间。

**take()**: 从队列中获取值，如果队列中没有值，线程会一直阻塞，直到队列中有值，并且该方法取得了该值。

**poll(long timeout, TimeUnit unit)**: 在给定的时间里，从队列中获取值，如果没有取到会抛出异常。

**remainingCapacity()**：获取队列中剩余的空间。

**remove(Object o)**: 从队列中移除指定的值。

**contains(Object o)**: 判断队列中是否拥有该值。

**drainTo(Collection c)**: 将队列中值，全部移除，并发设置到给定的集合中



```java
package com.audition;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class C extends B{
	
	public static void main(String[] args) {
	
		Queue quene = new ArrayBlockingQueue<>(10);
		quene.add("1234");
		quene.add("456");
		System.out.println(quene.size());
		System.out.println(quene.poll());
		System.out.println(quene.size());
		System.out.println(quene.poll());
		System.out.println(quene.size());
	}
}

2
1234
1
456
0
```

```java
package com.audition;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class QueueTest {
	
	public static void main(String[] args) {
	
		Queue quene = new ArrayBlockingQueue<>(10);
		System.out.println(quene.poll()); // null
		quene.remove(); //java.util.NoSuchElementException
	}
}
```



### SynchronousQueue

同步队列：直接提交策略

首先SynchronousQueue是`无界`的，也就是说他存数任务的能力是没有限制的，但是由于该Queue本身的特性，**在某次添加元素后必须等待其他线程取走后才能继续添加**。在这里不是核心线程便是新创建的线程，但是我们试想一样下，下面的场景

  SynchronousQueue是这样一种阻塞队列，其中每个 put 必须等待一个 take，反之亦然。同步队列没有任何内部容量，甚至连一个队列的容量都没有

```java
package com.audition;

import java.util.concurrent.SynchronousQueue;

public class ExecutorServiceTest {

	public static void main(String[] args) throws InterruptedException {
		SynchronousQueue<Integer> queue = new SynchronousQueue<Integer>();
	
		new Product(queue).start();
		new Customer(queue).start();
	}
	static class Product extends Thread{
		
		int rand = 0;
		
		SynchronousQueue<Integer> queue;
		public Product(SynchronousQueue<Integer> queue){
			this.queue = queue;
		}
		@Override
		public void run(){
			while(true){
				rand++;
				try {
					queue.put(rand);
					System.out.println("生产了一个产品："+rand);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	static class Customer extends Thread{
		SynchronousQueue<Integer> queue;
		public Customer(SynchronousQueue<Integer> queue){
			this.queue = queue;
		}
		@Override
		public void run(){
			while(true){
				try {
					Integer take = queue.take();
					System.out.println("消费了一个产品:"+take);
					System.out.println("------------------------------------------");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
```



### LinkedBlockingQueue

延迟 无界队列策略

### ArrayBlockingQueue

有界队列 阻塞队列



### ConcurrentLinkedQueue

由于我们的系统入队需求要远大于出队需求，可以用于秒杀场景



# Java 线程池运行状态

总线程数 = 排队线程数 + 活动线程数 + 执行完成的线程数。

```java
package com.audition;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceTest {
	
	private static ExecutorService es = new ThreadPoolExecutor(50, 100, 0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>(100000));

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 100000; i++) {
			es.execute(() -> {
				
			});
		}

		ThreadPoolExecutor tpe = ((ThreadPoolExecutor) es);

		while (true) {

			int queueSize = tpe.getQueue().size();
			System.out.println("当前排队线程数：" + queueSize);

			int activeCount = tpe.getActiveCount();
			System.out.println("当前活动线程数：" + activeCount);

			long completedTaskCount = tpe.getCompletedTaskCount();
			System.out.println("执行完成线程数：" + completedTaskCount);

			long taskCount = tpe.getTaskCount();
			System.out.println("总线程数：" + taskCount);

			Thread.sleep(3000);
		}
	}
}
```

```java
package com.audition;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceTest {

	static ExecutorService executorService = Executors.newFixedThreadPool(30);

	final static CountDownLatch latch = new CountDownLatch(1) {

		@Override
		public void await() throws InterruptedException {
			super.await();
			System.out.println("-----------all finished--------");
		}
	};

	public static void main(String[] args) throws InterruptedException {

		for (int i = 0; i < 100; i++) {
			executorService.submit(new CustomerThread());
		}
		isAllTaskFinished();
		latch.await();
	}

	public static void isAllTaskFinished() throws InterruptedException {

		ThreadPoolExecutor tpe = ((ThreadPoolExecutor) executorService);

		boolean isFinished = false;

		while (!isFinished) {

			int queueSize = tpe.getQueue().size();
			System.out.println("当前排队线程数：" + queueSize);
			int activeCount = tpe.getActiveCount();
			System.out.println("当前活动线程数：" + activeCount);
			long completedTaskCount = tpe.getCompletedTaskCount();
			System.out.println("执行完成线程数：" + completedTaskCount);
			long taskCount = tpe.getTaskCount();
			System.out.println("总线程数：" + taskCount);
			if (queueSize == 0 && activeCount == 0 && (taskCount == (queueSize + activeCount + completedTaskCount))) {
				latch.countDown();
				isFinished = true;
				System.out.println("======线程执行完毕======");
			}
		}
	}

	static class CustomerThread implements Runnable {

		@Override
		public void run() {

			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < 100; i++) {
				System.out.println("***");
			}
		}
	}
}
```



# StopWatch

有时我们在做开发的时候需要记录每个任务执行时间，或者记录一段代码执行时间，最简单的方法就是打印当前时间与执行完时间的差值，然后这样如果执行大量测试的话就很麻烦，并且不直观，如果想对执行的时间做进一步控制，则需要在程序中很多地方修改，目前spring-framework提供了一个StopWatch类可以做类似任务执行时间控制，也就是封装了一个对开始时间，结束时间记录操作的Java类，小例一则如下

```java
package com.example.stopwatch;

import org.springframework.util.StopWatch;


public class TestStopWatch {
    private void test() throws InterruptedException {
        StopWatch sw = new StopWatch();

        sw.start("起床");
        Thread.sleep(1000);
        sw.stop();

        sw.start("洗漱");
        Thread.sleep(2000);
        sw.stop();
        
       logger.info("耗时间：" + sw.getTotalTimeMillis());
        
        sw.start("锁门");
        Thread.sleep(500);
        sw.stop();

        System.out.println(sw.prettyPrint());
        System.out.println(sw.getTotalTimeMillis());
        System.out.println(sw.getLastTaskName());
        System.out.println(sw.getLastTaskInfo());
        System.out.println(sw.getTaskCount());
    }

    public static void main(String []argv) throws InterruptedException {
        TestStopWatch testStopWatch = new TestStopWatch();
        testStopWatch.test();
    }
}
```







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

http://www.runoob.com/design-pattern/design-pattern-tutorial.html

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



## 观察者设计模式

```java
//抽象观察者角色
public interface Watcher
{
    public void update(String str);

}
```

```java
//抽象主题角色，watched：被观察
public interface Watched
{
    public void addWatcher(Watcher watcher);

    public void removeWatcher(Watcher watcher);

    public void notifyWatchers(String str);

}
```

```java
//定义具体观察者
public class ConcreteWatcher implements Watcher
{

    @Override
    public void update(String str)
    {
        System.out.println(str);
    }

}
```

```java
import java.util.ArrayList;
import java.util.List;

public class ConcreteWatched implements Watched
{
    // 存放观察者
    private List<Watcher> list = new ArrayList<Watcher>();

    @Override
    public void addWatcher(Watcher watcher)
    {
        list.add(watcher);
    }

    @Override
    public void removeWatcher(Watcher watcher)
    {
        list.remove(watcher);
    }

    @Override
    public void notifyWatchers(String str)
    {
        // 自动调用实际上是主题进行调用的
        for (Watcher watcher : list)
        {
            watcher.update(str);
        }
    }

}

```

```java
public class Test
{
    public static void main(String[] args)
    {
        Watched girl = new ConcreteWatched();
        
        Watcher watcher1 = new ConcreteWatcher();
        Watcher watcher2 = new ConcreteWatcher();
        Watcher watcher3 = new ConcreteWatcher();
        
        girl.addWatcher(watcher1);
        girl.addWatcher(watcher2);
        girl.addWatcher(watcher3);
        
        girl.notifyWatchers("开心");
    }
}
```



观察者二

```java
import java.util.ArrayList;
import java.util.List;
public class Subject {
	private List<ITicketObserver> list = new ArrayList<ITicketObserver>();

	public void addObservers(ITicketObserver ticketObserver) {
		list.add(ticketObserver);
	}
	public void removeObservers(Object object) {
		list.remove(object);
	}
	public void change(String state) {
		System.out.println("观察者改变了！");
		for (ITicketObserver ticketObserver : list) {
			ticketObserver.doWork(state);
		}
	}
}
```

```java
public interface ITicketObserver {
	public void doWork(String state);
}
```

```java
public class MessageObservers implements ITicketObserver {
	@Override
	public void doWork(String state) {
		 System.out.println("短信操作开始！更新状态"+state);
	}
}
```

```java

public class LogObservers implements ITicketObserver {
 
	@Override
	public void doWork(String state) {
		 System.out.println("日志文件操作开始！更新状态"+state);
	}
}
```

```java
public class MainTest {
	public static void main(String[] args) {
		 Subject subject=new Subject();
		 
		 ITicketObserver logObservers=new LogObservers(); 
		 ITicketObserver messageObservers=new MessageObservers();
		 subject.addObservers(logObservers);
		 subject.addObservers(messageObservers);
		 subject.change("ok");
	}
}
```



## 回调

```java
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

## 单例（双重校验锁）

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

