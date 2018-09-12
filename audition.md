# 面试锦集

https://www.bilibili.com/read/cv558137?from=articleDetail

http://www.importnew.com/29562.html

# 1 、 Java支持多继承么

Java中类不支持多继承，只支持单继承（即一个类只有一个父类）。

一个类只有一个父类

一个子接口可以有多个父接口

# 2、接口和抽象类的区别是什么

抽象方法：`没有方法体的方法`。

接口中所有的方法隐含的都是抽象的。而`抽象类则可以同时包含抽象和非抽象的方法`。

类可以实现很多个接口，但是只能继承一个抽象类 

`类可以不实现抽象类和接口声明的所有方法，当然，在这种情况下，类也必须得声明成是抽象的`

抽象类可以在不提供接口方法实现的情况下实现接口

Java接口中声明的变量默认都是final的。抽象类可以包含非final的变量

`包含抽象方法的类一定是抽象类(接口也可以)`;

```
抽象类有构造方法，接口没有构造方法
抽象类只能单继承，接口可以多继承
抽象类可以有普通方法，接口中的所有方法都是抽象方法
接口的属性都是public static final修饰的，而抽象的不是

public interface Interfacetest {
	public static String a = "123";
	public static void main(String[] args) {
		a = "456";//The final field Interfacetest.a cannot be assigned
		System.out.println(a);
	}
}
```

有抽象方法的`类`必须被声明为抽象类，而抽象类未必要有抽象方法

| **参数**           | **抽象类**                                                   | **接口**                                                     |
| ------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 默认的方法实现     | 它可以有默认的方法实现                                       | 接口完全是抽象的。它根本不存在方法的实现                     |
| 实现               | 子类使用**extends**关键字来继承抽象类。如果子类不是抽象类的话，它需要提供抽象类中所有声明的方法的实现。 | 子类使用关键字**implements**来实现接口。它需要提供接口中所有声明的方法的实现 |
| 构造器             | 抽象类可以有构造器                                           | 接口不能有构造器                                             |
| 与正常Java类的区别 | 除了你不能实例化抽象类之外，它和普通Java类没有任何区别       | 接口是完全不同的类型                                         |
| 访问修饰符         | 抽象方法可以有**public**、**protected**和**default**这些修饰符 | 接口方法默认修饰符是**public**。你不可以使用其它修饰符。     |
| main方法           | 抽象方法可以有main方法并且我们可以运行它                     | 接口没有main方法，因此我们不能运行它。                       |
| 多继承             | 抽象方法可以继承一个类和实现多个接口                         | 接口只可以继承一个或多个其它接口                             |
| 速度               | 它比接口速度要快                                             | 接口是稍微有点慢的，因为它需要时间去寻找在类中实现的方法。   |
| 添加新方法         | 如果你往抽象类中添加新的方法，你可以给它提供默认的实现。因此你不需要改变你现在的代码。 | 如果你往接口中添加方法，那么你必须改变实现该接口的类。       |

# 3、用最有效率的方法计算2乘以8？

 2 << 3（左移3位相当于乘以2的3次方，右移3位相当于除以2的3次方）

# 4、 单例

```java
package com.audition;
public class Singleton {
    
	private Singleton() {
        
	}
	private static Singleton singleton;
	
	public static Singleton getInstance() {
		
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

# 5 、StringBuilder、StringBuffer的区别

StringBuffer    `线程安全`

StringBuilder   效率高  ，因为不用维护线程安全问题



1. StringBuilder相比StringBuffer效率更高，但多线程不安全；
2. 在单线程中字符串的频繁拼接使用StringBuilder效率更高，对于多线程使用StringBuffer则更安全；
3. 字符串简单操作时没必要使用上述两者，还是用String类型提高速度

# 6、Java集合框架的基础接口有哪些

Collection为集合层级的根接口

Set是一个不能包含重复元素的集合
List是一个有序集合，可以包含重复元素。你可以通过它的索引来访问任何元素。List更像长度动态变换的数组。 
Map是一个将key映射到value的对象.一个Map不能包含重复的key：每个key最多只能映射一个value。 
一些其它的接口有Queue、Dequeue、SortedSet、SortedMap和ListIterator。

# 7、Iterator和ListIterator的区别是什么

Iterator可用来遍历Set和List集合，但是ListIterator只能用来遍历List。 
Iterator对集合只能是前向遍历，ListIterator既可以前向也可以后向。 
ListIterator实现了Iterator接口，并包含其他的功能，比如：增加元素，替换元素，获取前一个和后一个元素的索引，等等。

```java
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class TestA {
	public static void main(String[] args) {

		List<String> a = new ArrayList<>();
		a.add("1");
		a.add("2");
		a.add("3");
		
		ListIterator<String> listIterator = a.listIterator(); 
		while(listIterator.hasNext()) {
			System.out.println(listIterator.next());//当执行next方法后，迭代器的指向位置后移
			listIterator.add("222");
		}
		System.out.println( a);
	}
}
[1, 222, 2, 222, 3, 222]
```

1、使用范围不同，iterator可以应用于所有的集合，Set、List和Map以及这些集合的子类型。而ListIterator只能用于List及其子类型。

2、ListIterator有add方法，可以向List中添加对象，而Iterator不能。

3、ListIterator和Iterator都有hasNext()和next()方法，可以实现顺序向后遍历，但是ListIterator有hasPrevious()和previous()方法，可以实现逆向遍历，但是iterator不可以。

4、ListIterator可以定位当前索引的位置，nextIndex()和previousIndex()可以实现。Iterator没有此功能。

5、都可以实现删除操作，但是ListIterator可以实现对象的修改，set()方法可以实现。Iterator仅能遍历，不能实现修改。

# 7、LinkedList和ArrayList区别

 LinkedList也像ArrayList一样实现了基本的接口，但是它执行某些从操作时比ArrayList更高效，但在随机访问方面要逊色一些。LinkedList中有一些方法虽然名字不同，但可以完成相同的操作，实现相同的功能，其中：

1、getFirst()和element()完全一样，它们都返回列表的头（第一个元素），并且不移除它，若List为空，则抛出NoSunchElementException。

​    peek()方法也是不移除且返回列表的第一个元素，不同之处是List为空时返回null。

2、remove()和removeFirst()也是一样的，移除并返回列表的第一个元素，若列表为空，抛出NoSunchElementException。

​    poll()也是移除并返回列表的第一个元素，若列表为空，返回null。

3、addFirst()、add()、addLast()相同，它们都将某个元素插入到列表的尾部。

4、removeLast()移除并返回列表的最后一个元素。



LinkedList和ArrayList的差别主要来自于Array和LinkedList数据结构的不同。如果你很熟悉Array和LinkedList，你很容易得出下面的结论：

1) 因为Array是基于索引(index)的数据结构，它使用索引在数组中搜索和读取数据是很快的。Array获取数据的时间复杂度是O(1),但是要删除数据却是开销很大的，因为这需要重排数组中的所有数据。

2) 相对于ArrayList，LinkedList插入是更快的。**因为LinkedList不像ArrayList一样，不需要改变数组的大小**，也不需要在数组装满的时候要将所有的数据重新装入一个新的数组，这是ArrayList最坏的一种情况，时间复杂度是O(n)，而LinkedList中插入或删除的时间复杂度仅为O(1)。ArrayList在插入数据时还需要更新索引（除了插入数组的尾部）。

3) 类似于插入数据，删除数据时，LinkedList也优于ArrayList。

4) LinkedList需要更多的内存，因为ArrayList的每个索引的位置是实际的数据，而LinkedList中的每个节点中存储的是实际的数据和前后节点的位置。



# 8、hashCode()和equals()方法有何重要性

（1）如果o1.equals(o2)，那么o1.hashCode() == o2.hashCode()总是为true的。 
（2）如果o1.hashCode() == o2.hashCode()，并不意味着o1.equals(o2)会为true。 

```
package com.audition;

public class HashCode {
	
	public static void main(String[] args) {
		
		String a="123";
		String b="123";
		System.out.println(a.equals(b));
		System.out.println(a.hashCode());
		System.out.println(b.hashCode());
	}
}
true
48690
48690
```

# 9、HashMap和Hashtable有什么区别

1、HashMap是`非线程安全`的，HashTable是`线程安全`的。 
2、HashMap的键和值都允许有null值存在，而HashTable则不行。 
3、因为线程安全的问题，HashMap效率比HashTable的要高。 
4、Hashtable是同步的，而HashMap不是。因此，HashMap更适合于单线程环境，而Hashtable适合于多线程环境。 
一般现在`不建议用HashTable`, ①是HashTable是遗留类，内部实现很多没优化和冗余。②即使在多线程环境下，现在也有同步的ConcurrentHashMap替代，没有必要因为是多线程而用HashTable。

TreeMap : 有序的

# 10、Array和ArrayList有何区别？什么时候更适合用Array

Array可以容纳`基本类型和对象`，而ArrayList只能容纳对象。List<`Integr`> a = new ArrayList<>(); 
Array是指定大小的，而ArrayList大小是固定的。 
Array没有提供ArrayList那么多功能，比如addAll、removeAll和iterator等。尽管ArrayList明显是更好的选择，但也有些时候Array比较好用。 
（1）如果列表的`大小已经指定`，大部分情况下是存储和遍历它们。 
（2）对于遍历基本数据类型，尽管Collections使用自动装箱来减轻编码任务，在指定大小的基本类型的列表上工作也会变得很慢。 
（3）如果你要使用多维数组



# 11、java虚拟机包括什么

JVM：java虚拟机，运用硬件或软件手段实现的虚拟的计算机，

Java虚拟机包括：

寄存器

堆栈

处理器

# 12、 类与对象的关系

类是对象的抽象，对象是类的具体，类是对象的模板，对象是类的实例

就如同docker中的image和container的区别

# 13、Java中有几种数据类型

```
整形：byte,short,int,long
浮点型：float,double
字符型：char
布尔型：boolean
```

# 14、什么是拆装箱

```
拆箱：把包装类型转成基本数据类型
装箱：把基本数据类型转成包装类型
```

# 15、Java中的包装类都是那些

byte：Byte 

short：Short 

int：Integer 

long：Long 

float：Float 

double：Double 

char：Character

boolean：Boolean

# 16、Object类常用方法有那些

Equals
Hashcode
toString
wait
notify

notifyAll

getClass

# 17、java中是值传递引用传递

理论上说，java都是引用传递，对于基本数据类型，传递是值的副本，而不是值本身。对于对象类型，传递是对象的引用，当在一个方法操作操作参数的时候，其实操作的是引用所指向的对象

# 18、Final在java中的作用

Final可以修饰类，修饰方法，修饰变量。
修饰的类叫最终类。该类不能被继承。
修饰的方法不能被重写。
修饰的变量叫常量，常量必须初始化，一旦初始化后，常量的值不能发生改变。

# 19、 String str=”aaa”,与String str=new String(“aaa”)一样吗

不一样的。因为内存分配的方式不一样。
第一种，创建的”aaa”是常量，jvm都将其分配在常量池中。
第二种创建的是一个对象，jvm将其值分配在堆内存中。

# 20、String str=”aa”,String s=”bb”,String aa=aa+s;一种创建了几个对象

一共有两个引用，三个对象。因为”aa”与”bb”都是常量，常量的值不能改变，当执行字符串拼接时候，会创建一个新的常量是” aabbb”,有将其存到常量池中

# 21、抽象类可以使用final修饰吗

`不可以。定义抽象类就是让其他继承的，而final修饰类表示该类不能被继承，与抽象类的理念违背了`

# 22、普通类与抽象类有什么区别

普通类不能包含抽象方法，抽象类可以包含抽象方法
抽象类不能直接实例化，普通类可以直接实例化

# 23、接口有什么特点

`接口中声明全是public static final修饰的常量`
接口中所有方法都是抽象方法
接口是没有构造方法的
接口也不能直接实例化
接口可以多继承

# 24、Java中异常分为哪两种

编译时异常
运行时异常

# 25、在异常捕捉时，如果发生异常，那么try.catch.finally块外的return语句会执行吗

会执行，如果有finally，在finally之后被执行，如果没有finally，在catch之后被执行

# 26、Thow与thorws区别

Throw写在代码块内，throw后面跟的是一个具体的异常实例
Throw写在方法前面后面，throws后面跟的是异常类，异常类可以出现多个

# 27、除了使用new创建对象之外，还可以用什么方法创建对象

1. 采用new
2. 通过反射
3. 采用clone
4. 通过序列化机制

## 28、ArrayList与LinkedList有什么区别

ArrayList与LinkedList都实现了List接口。
ArrayList是线性表，底层是使用数组实现的，它在尾端插入和访问数据时效率较高， 
Linked是双向链表，他在中间插入或者头部插入时效率较高，在访问数据时效率较低



# 29、Array与ArrayList有什么不一样

Array与ArrayList都是用来存储数据的集合。ArrayList底层是使用数组实现的，但是arrayList对数组进行了封装和功能扩展，拥有许多原生数组没有的一些功能。我们可以理解成ArrayList是Array的一个升级版。

1. Array可以容纳基本类型和对象，而ArrayList只能容纳对象。
2. Array是指定大小的，而ArrayList大小是固定的(默认10)

# 30、JDBC操作的步骤

加载数据库驱动类
打开数据库连接
执行sql语句
处理返回结果
关闭资源

# 31、怎么在JDBC内调用一个存储过程

使用CallableStatement

# 32、字节流与字符流的区别

以字节为单位输入输出数据，字节流按照8位传输
以字符为单位输入输出数据，字符流按照16位传输

# 33、线程同步的方法

1. wait():让线程等待。将线程存储到一个线程池中。
2. notify()：唤醒被等待的线程。通常都唤醒线程池中的第一个。让被唤醒的线程处于临时阻塞状态。
3. notifyAll(): 唤醒所有的等待线程。将线程池中的所有线程都唤醒。

## 34、switch能否作用在byte, long上

可以用在byte上，但是不能用在long上。

在idk 1.7之前，switch只能支持byte, short, char, int或者其对应的封装类以及Enum类型。从idk 1.7之后switch开始支持String

# 35、short s1= 1; s1 = s1 + 1; 该段代码是否有错,有的话怎么改

有错误，short类型在进行运算时会自动提升为int类型，也就是说`s1+1`的运算结果是int类型。

short s1= 1; s1 += 1; 该段代码是否有错，有的话怎么改？

`+=操作符会自动对右边的表达式结果强转匹配左边的数据类型，所以没错。`

# 36、final有哪些用法

1.被final修饰的类不可以被继承 
2.被final修饰的方法不可以被重写 
3.被final修饰的变量不可以被改变。如果修饰引用，那么表示引用不可变，引用指向的内容可变。



# 37、java中int char,long各占多少字节

| 类型   | 位数 | 字节数 |
| ------ | ---- | ------ |
| short  | 2    | 16     |
| char   | 2    | 16     |
| int    | 4    | 32     |
| float  | 4    | 32     |
| long   | 8    | 64     |
| double | 8    | 64     |



# int 和 Integer 哪个会占用更多的内存

Integer 对象会占用更多的内存。Integer 是一个对象，需要存储对象的元数据。但是 int 是一个原始类型的数据，所以占用的空间更少





# 38、调用System.gc()会发生什么

通知GC开始工作，但是GC真正开始的时间不确定。

# 39、你了解守护线程吗？它和非守护线程有什么区别

程序运行完毕，jvm会等待非守护线程完成后关闭，但是jvm不会等待守护线程。守护线程最典型的例子就是GC线程。

# 40、什么是多线程上下文切换

多线程的上下文切换是指CPU控制权由一个已经正在运行的线程切换到另外一个就绪并等待获取CPU执行权的线程的过程。

# 41、Runnable和Callable的区别

Runnable接口中的run()方法的返回值是void，它做的事情只是纯粹地去执行run()方法中的代码而已；Callable接口中的call()方法是有返回值的，是一个泛型，`和Future、FutureTask`配合可以用来获取异步执行的结果。 
这其实是很有用的一个特性，因为多线程相比单线程更难、更复杂的一个重要原因就是因为多线程充满着未知性，某条线程是否执行了？某条线程执行了多久？某条线程执行的时候我们期望的数据是否已经赋值完毕？无法得知，我们能做的只是等待这条多线程的任务执行完毕而已。而Callable+Future/FutureTask却可以方便获取多线程运行的结果，可以在等待时间太长没获取到需要的数据的情况下取消该线程的任务。

# 42、wait()与sleep()的区别

- wait 方法和之前的 sleep 一样就是放弃 CPU 执行权，但是他和 sleep 不一样的地方是需 要等待另外一个持有相同锁的线程对其进行唤醒操作，并且 wait 方法必须有一个同步锁， 
- ​
- sleep()来自Thread类，wait()来自Object类。调用sleep()方法的过程中，线程`不会释放对象锁`。而 调用 `wait 方法线程会释放对象锁`
- sleep()睡眠后`出让`系统资源（CPU），wait让其他线程可以占用CPU
- sleep(milliseconds)需要指定一个睡眠时间，时间一到会`自动唤醒`.而wait()需要配合`notify()或者notifyAll()`使用

# 43、FutureTask是什么

这个其实前面有提到过，FutureTask表示一个异步运算的任务。FutureTask里面可以传入一个Callable的具体实现类，可以对这个异步运算的任务的结果进行等待获取、判断是否已经完成、取消任务等操作。当然，由于`FutureTask也是Runnable接口的实现类`，所以FutureTask也可以放入线程池中。

# 44、生产者消费者模型的作用是什么

（1）通过平衡生产者的生产能力和消费者的消费能力来提升整个系统的运行效率，这是生产者消费者模型最重要的作用。
（2）解耦，这是生产者消费者模型附带的作用，解耦意味着生产者和消费者之间的联系少，联系越少越可以独自发展而不需要收到相互的制约。

```java
//生产者
public class Producer implements Runnable{
   private final BlockingQueue<Integer> queue;

   public Producer(BlockingQueue q){
       this.queue=q;
   }

   @Override
   public void run() {
       try {
           while (true){
               Thread.sleep(1000);//模拟耗时
               queue.put(produce());
           }
       }catch (InterruptedException e){

       }
   }

   private int produce() {
       int n=new Random().nextInt(10000);
       System.out.println("Thread:" + Thread.currentThread().getId() + " produce:" + n);
       return n;
   }
}
//消费者
public class Consumer implements Runnable {
   private final BlockingQueue<Integer> queue;

   public Consumer(BlockingQueue q){
       this.queue=q;
   }

   @Override
   public void run() {
       while (true){
           try {
               Thread.sleep(2000);//模拟耗时
               consume(queue.take());
           }catch (InterruptedException e){

           }

       }
   }

   private void consume(Integer n) {
       System.out.println("Thread:" + Thread.currentThread().getId() + " consume:" + n);

   }
}
//测试
public class Main {

   public static void main(String[] args) {
       BlockingQueue<Integer> queue=new ArrayBlockingQueue<Integer>(100);
       Producer p=new Producer(queue);
       Consumer c1=new Consumer(queue);
       Consumer c2=new Consumer(queue);

       new Thread(p).start();
       new Thread(c1).start();
       new Thread(c2).start();
   }
}
```



# 45、如果你提交任务时，线程池队列已满，这时会发生什么

LinkedBlockingQueue：如果你使用的LinkedBlockingQueue，也就是无界队列的话，没关系，继续添加任务到阻塞队列中等待执行，因为LinkedBlockingQueue可以近乎认为是一个无穷大的队列，可以无限存放任务；

ArrayBlockingQueue：如果你使用的是有界队列比方说ArrayBlockingQueue的话，任务首先会被添加到ArrayBlockingQueue中，ArrayBlockingQueue满了，则会使用拒绝策略RejectedExecutionHandler处理满了的任务，默认是AbortPolicy。

# 46、CyclicBarrier和CountDownLatch区别

这两个类非常类似，都在java.util.concurrent下，都可以用来表示代码运行到某个点上，二者的区别在于：

- CyclicBarrier的某个线程运行到某个点上之后，该线程即`停止运行`，直到所有的线程都到达了这个点，所有线程才重新运行；CountDownLatch则不是，某线程运行到某个点上之后，只是给某个数值-1而已，`该线程继续运行。`子线程countDown后继续向下运行，主线程在await处等待计数器为0时向下运行
- CyclicBarrier只能唤起一个任务，CountDownLatch可以唤起多个任务
- CyclicBarrier可重用，CountDownLatch不可重用，计数值为0该CountDownLatch就不可再用了。

CountDownLatch主线程等待计数器归零，子线程countDown计数器减去1后继续向下

CyclicBarrier所有子线程在await处等待，直到所有的子线程都到了await时所有子线程继续向下执行

# 47、你有哪些多线程开发良好的实践

1. 给线程命名
2. 最小化同步范围
3. 优先使用volatile
4. 尽可能使用更高层次的并发工具而非wait和notify()来实现线程通信,如BlockingQueue,Semeaphore
5. 优先使用并发容器而非同步容器.
6. 考虑使用线程池

# 48 ArrayList和LinkedList的区别

最明显的区别是 ArrrayList底层的数据结构是数组，支持随机访问，而 LinkedList 的底层数据结构是双向循环链表，不支持随机访问。使用下标访问一个元素，ArrayList 的时间复杂度是 O(1)，而 LinkedList 是 O(n)。



# 49、Math.round(11.5) 等于多少？Math.round(-11.5)等于多少

Math.round(11.5)的返回值是12，Math.round(-11.5)的返回值是-11。四舍五入的原理是在参数上加0.5然后进行下取整。



# 50、两个对象值相同(x.equals(y) == true)，但却可有不同的hash code，这句话对不对

不对，如果两个对象x和y满足x.equals(y) == true，它们的哈希码（hash code）应当相同。Java对于eqauls方法和hashCode方法是这样规定的：(1)如果两个对象相同（equals方法返回true），那么它们的hashCode值一定要相同；(2)如果两个对象的hashCode相同，它们并不一定相同。



# 51、是否可以继承String类

String 类是final类，不可以被继承。

# 52、打印昨天的当前时刻

Calendar cal = Calendar.getInstance();
cal.add(Calendar.DATE, -1);
System.out.println(cal.getTime());

java8：

LocalDateTime today = LocalDateTime.now();
LocalDateTime yesterday = today.minusDays(1);



# 53、JSP有哪些内置对象？作用分别是什么

- request：封装客户端的请求，其中包含来自GET或POST请求的参数； 
- response：封装服务器对客户端的响应； 
- pageContext：通过该对象可以获取其他对象； 
- session：封装用户会话的对象； 
- application：封装服务器运行环境的对象； 
- out：输出服务器响应的输出流对象； 
- config：Web应用的配置对象； 
- page：JSP页面本身（相当于Java程序中的this）； 
- exception：封装页面抛出异常的对象。



# 54  MyBatis中使用#和$书写占位符有什么区别

1 #是将传入的值当做字符串的形式，eg:select id,name,age from student where id =#{id},当前端把id值1，传入到后台的时候，就相当于 select id,name,age from student where id ='1'.

 2 $是将传入的数据直接显示生成sql语句，eg:select id,name,age from student where id =${id},当前端把id值1，传入到后台的时候，就相当于 select id,name,age from student where id = 1.

 3 使用#可以很大程度上防止sql注入。(语句的拼接)

 4 但是如果使用在order by 中就需要使用 $.

 5 在大多数情况下还是经常使用#，但在不同情况下必须使用$. 

我觉得#与的区别最大在于：#{} 传入值时，sql解析时，参数是带引号的，而${}穿入值，sql解析时，参数是不带引号的。

一 : 理解mybatis中 $与#

​    在mybatis中的$与#都是在sql中动态的传入参数。

​    eg:select id,name,age from student where name=#{name}  这个name是动态的，可变的。当你传入什么样的值，就会根据你传入的值执行sql语句。

二:使用$与#

{}: 解析为一个 JDBC 预编译语句（prepared statement）的参数标记符，一个 #{ } 被解析为一个参数占位符 。

   ${}: 仅仅为一个纯碎的 string 替换，在动态 SQL 解析阶段将会进行变量替换。

  name-->cy

 eg:  select id,name,age from student where name=#{name}   -- name='cy'

​        select id,name,age from student where name=${name}    -- name=cy

# 56  解释一下MyBatis中命名空间（namespace）的作用

在大型项目中，可能存在大量的SQL语句，这时候为每个SQL语句起一个唯一的标识（ID）就变得并不容易了。为了解决这个问题，在MyBatis中，可以为每个映射文件起一个唯一的命名空间，这样定义在这个映射文件中的每个SQL语句就成了定义在这个命名空间中的一个ID。只要我们能够保证每个命名空间中这个ID是唯一的，即使在不同映射文件中的语句ID相同，也不会再产生冲突了

# 57 、你用过的网站前端优化的技术有哪些

​      ① 浏览器访问优化： 

- 减少HTTP请求数量：合并CSS、合并JavaScript、合并图片（CSS Sprite） ，必要的合并接口
- 使用浏览器缓存：通过设置HTTP响应头中的Cache-Control和Expires属性，将CSS、JavaScript、图片等在浏览器中缓存，当这些静态资源需要更新时，可以更新HTML文件中的引用来让浏览器重新请求新的资源 
- 启用压缩 
- CSS前置，JavaScript后置 
- 减少Cookie传输 
- ② CDN加速：CDN（Content Distribute Network）的本质仍然是缓存，将数据缓存在离用户最近的地方，CDN通常部署在网络运营商的机房，不仅可以提升响应速度，还可以减少应用服务器的压力。当然，CDN缓存的通常都是静态资源。 
  ③ 反向代理：反向代理相当于应用服务器的一个门面，可以保护网站的安全性，也可以实现负载均衡的功能，当然最重要的是它缓存了用户访问的热点资源，可以直接从反向代理将某些内容返回给用户浏览器。

# 58、你使用过的应用服务器优化技术有哪些

① 分布式缓存：缓存的本质就是内存中的哈希表，如果设计一个优质的哈希函数，那么理论上哈希表读写的渐近时间复杂度为O(1)。缓存主要用来存放那些读写比很高、变化很少的数据，这样应用程序读取数据时先到缓存中读取，如果没有或者数据已经失效再去访问数据库或文件系统，并根据拟定的规则将数据写入缓存。对网站数据的访问也符合二八定律（Pareto分布，幂律分布），即80%的访问都集中在20%的数据上，如果能够将这20%的数据缓存起来，那么系统的性能将得到显著的改善。当然，使用缓存需要解决以下几个问题： 

- 频繁修改的数据； 
- 数据不一致与脏读； 
- 缓存雪崩（可以采用分布式缓存服务器集群加以解决，[memcached](http://memcached.org/)是广泛采用的解决方案）； 
- 缓存预热； 
- 缓存穿透（恶意持续请求不存在的数据）。 
  ② 异步操作：可以使用消息队列将调用异步化，通过异步处理将短时间高并发产生的事件消息存储在消息队列中，从而起到削峰作用。电商网站在进行促销活动时，可以将用户的订单请求存入消息队列，这样可以抵御大量的并发订单请求对系统和数据库的冲击。目前，绝大多数的电商网站即便不进行促销活动，订单系统都采用了消息队列来处理。 
  ③ 使用集群。 
  ④ 代码优化： 
- 多线程：基于Java的Web开发基本上都通过多线程的方式响应用户的并发请求，使用多线程技术在编程上要解决线程安全问题，主要可以考虑以下几个方面：A. 将对象设计为无状态对象（这和面向对象的编程观点是矛盾的，在面向对象的世界中被视为不良设计），这样就不会存在并发访问时对象状态不一致的问题。B. 在方法内部创建对象，这样对象由进入方法的线程创建，不会出现多个线程访问同一对象的问题。使用ThreadLocal将对象与线程绑定也是很好的做法，这一点在前面已经探讨过了。C. 对资源进行并发访问时应当使用合理的锁机制。 
- 非阻塞I/O： 使用单线程和非阻塞I/O是目前公认的比多线程的方式更能充分发挥服务器性能的应用模式，基于Node.js构建的服务器就采用了这样的方式。Java在JDK 1.4中就引入了NIO（Non-blocking I/O）,在Servlet 3规范中又引入了异步Servlet的概念，这些都为在服务器端采用非阻塞I/O提供了必要的基础。 
- 资源复用：资源复用主要有两种方式，一是单例，二是对象池，我们使用的数据库连接池、线程池都是对象池化技术，这是典型的用空间换取时间的策略，另一方面也实现对资源的复用，从而避免了不必要的创建和释放资源所带来的开销

# 59、Collection 和 Collections的区别

Collection是集合类的上级接口，继承与他的接口主要有Set 和List.
Collections是针对集合类的一个帮助类，他提供一系列静态方法实现对各种集合的搜索、排序、线程安全化等操作。

List<Object> emptyList = Collections.EMPTY_LIST;

# 60 、接口是否可继承接口? 抽象类是否可实现(implements)接口? 抽象类是否可继承实体类(concrete class)

接口可以继承接口。抽象类可以实现(implements)接口，抽象类是否可继承实体类，但前提是实体类必须有明确的构造函数。

# 61、sql  decode  sign

select max_limit ,decode(sign(max_limit-60),-1,'fail','pass') as mark from t_activity_bonus

等价于

select (case when max_limit>60  then 'fail'  else 'pass '  end)  as mark  from  t_activity_bonus

查询ID重复三次以上的记录

select * from(select * ,count(ID) as count from t_activity_bonus group by ID) T where T.count>3



# 62、zookeeper面试题

## 1.ZooKeeper是什么？

ZooKeeper是一个**分布式**的，开放源码的分布式**应用程序协调服务**，是Google的Chubby一个开源的实现，它是**集群的管理者**，**监视着集群中各个节点的状态根据节点提交的反馈进行下一步合理操作**。最终，将简单易用的接口和性能高效、功能稳定的系统提供给用户。
客户端的**读请求**可以被集群中的**任意一台机器处理**，如果读请求在节点上注册了监听器，这个监听器也是由所连接的zookeeper机器来处理。对于**写请求**，这些请求会同**时发给其他zookeeper机器并且达成一致后，请求才会返回成功**。因此，随着**zookeeper的集群机器增多，读请求的吞吐会提高但是写请求的吞吐会下降**。
有序性是zookeeper中非常重要的一个特性，所有的**更新都是全局有序的**，每个更新都有一个**唯一的时间戳**，这个时间戳称为**zxid（Zookeeper Transaction Id）**。而**读请求只会相对于更新有序**，也就是读请求的返回结果中会带有这个**zookeeper最新的zxid**。

## 2.ZooKeeper提供了什么？

1、**文件系统**
2、**通知机制**

## 3.Zookeeper文件系统

Zookeeper提供一个多层级的节点命名空间（节点称为znode）。与文件系统不同的是，这些节点**都可以设置关联的数据**，而文件系统中只有文件节点可以存放数据而目录节点不行。Zookeeper为了保证高吞吐和低延迟，在内存中维护了这个树状的目录结构，这种特性使得Zookeeper**不能用于存放大量的数据**，每个节点的存放数据上限为**1M**。

## 4.四种类型的znode

1、**PERSISTENT-持久化目录节点** 
客户端与zookeeper断开连接后，该节点依旧存在 
2、**PERSISTENT_SEQUENTIAL-持久化顺序编号目录节点**
客户端与zookeeper断开连接后，该节点依旧存在，只是Zookeeper给该节点名称进行顺序编号 
3、**EPHEMERAL-临时目录节点**
客户端与zookeeper断开连接后，该节点被删除 
4、**EPHEMERAL_SEQUENTIAL-临时顺序编号目录节点**
客户端与zookeeper断开连接后，该节点被删除，只是Zookeeper给该节点名称进行顺序编号
![clipboard.png](https://segmentfault.com/img/bV8Xel?w=371&h=463)

## 5.Zookeeper通知机制

client端会对某个znode建立一个**watcher事件**，当该znode发生变化时，这些client会收到zk的通知，然后client可以根据znode变化来做出业务上的改变等。

## 6.Zookeeper做了什么？

1、命名服务
2、配置管理
3、集群管理
4、分布式锁
5、队列管理

## 7.zk的命名服务（文件系统）

命名服务是指通过指定的名字来**获取资源**或者**服务的地址**，利用zk创建一个全局的路径，即是**唯一**的路径，这个路径就可以作为一个名字，指向集群中的集群，提供的服务的地址，或者一个远程的对象等等。

## 8.zk的配置管理（文件系统、通知机制）

程序分布式的部署在不同的机器上，将程序的配置信息放在zk的**znode**下，当有配置发生改变时，也就是znode发生变化时，可以通过改变zk中某个目录节点的内容，利用**watcher**通知给各个客户端，从而更改配置。

## 9.Zookeeper集群管理（文件系统、通知机制）

所谓集群管理无在乎两点：**是否有机器退出和加入、选举master**。 
对于第一点，所有机器约定在父目录下**创建临时目录节点**，然后监听`父目录节点的子节点`变化消息。一旦有机器挂掉，该机器与 zookeeper的连接断开，其所创建的临时目录节点被删除，**所有其他机器都收到通知：某个兄弟目录被删除**，于是，所有人都知道：它上船了。
新机器加入也是类似，**所有机器收到通知：新兄弟目录加入**，highcount又有了，对于第二点，我们稍微改变一下，**所有机器创建临时顺序编号目录节点，每次选取编号最小的机器作为master就好**。

## 10.Zookeeper分布式锁（文件系统、通知机制）

有了zookeeper的一致性文件系统，锁的问题变得容易。锁服务可以分为两类，一个是**保持独占**，另一个是**控制时序**。 
对于第一类，我们将zookeeper上的一个**znode看作是一把锁**，通过createznode的方式来实现。所有客户端都去创建 /distribute_lock 节点，最终成功创建的那个客户端也即拥有了这把锁。用完删除掉自己创建的distribute_lock 节点就释放出锁。 
对于第二类， /distribute_lock 已经预先存在，所有客户端在它下面创建临时顺序编号目录节点，和选master一样，**编号最小的获得锁**，用完删除，依次方便。

## 11.获取分布式锁的流程

![clipboard.png](https://segmentfault.com/img/bV8WZ1?w=605&h=483)
在获取分布式锁的时候在locker节点下创建临时顺序节点，释放锁的时候删除该临时节点。客户端调用createNode方法在locker下创建临时`顺序节点`，
然后调用getChildren(“locker”)来获取locker下面的所有子节点，注意此时不用设置任何Watcher。客户端获取到所有的子节点path之后，如果发现自己创建的节点在所有创建的子节点序号最小，那么就认为该客户端获取到了锁。如果发现自己创建的节点并非locker所有子节点中最小的，说明自己还没有获取到锁，此时客户端需要找到**比自己小的那个节点**，然后对其调用**exist()**方法，同时对其注册事件监听器。之后，让这个被关注的节点删除，则客户端的Watcher会收到相应通知，此时再次判断自己创建的节点是否是locker子节点中序号最小的，如果是则获取到了锁，如果不是则重复以上步骤继续获取到比自己小的一个节点并注册监听。当前这个过程中还需要许多的逻辑判断。
![clipboard.png](https://segmentfault.com/img/bV8WVn?w=665&h=355)

代码的实现主要是基于互斥锁，获取分布式锁的重点逻辑在于**BaseDistributedLock**，实现了基于Zookeeper实现分布式锁的细节。

## 12.Zookeeper队列管理（文件系统、通知机制）

两种类型的队列：
1、同步队列，当一个队列的成员都聚齐时，这个队列才可用，否则一直等待所有成员到达。 
2、队列按照 FIFO 方式进行入队和出队操作。 
第一类，在约定目录下创建临时目录节点，监听节点数目是否是我们要求的数目。 
第二类，和分布式锁服务中的控制时序场景基本原理一致，入列有编号，出列按编号。在特定的目录下创建**PERSISTENT_SEQUENTIAL**节点，创建成功时**Watcher**通知等待的队列，队列删除**序列号最小的节点**用以消费。此场景下Zookeeper的znode用于消息存储，znode存储的数据就是消息队列中的消息内容，SEQUENTIAL序列号就是消息的编号，按序取出即可。由于创建的节点是持久化的，所以**不必担心队列消息的丢失问题**。

## 13.Zookeeper数据复制

Zookeeper作为一个集群提供一致的数据服务，自然，它要在**所有机器间**做数据复制。数据复制的好处： 
1、容错：一个节点出错，不致于让整个系统停止工作，别的节点可以接管它的工作； 
2、提高系统的扩展能力 ：把负载分布到多个节点上，或者增加节点来提高系统的负载能力； 
3、提高性能：让**客户端本地访问就近的节点，提高用户访问速度**。

从客户端读写访问的透明度来看，数据复制集群系统分下面两种： 
1、**写主**(WriteMaster) ：对数据的**修改提交给指定的节点**。读无此限制，可以读取任何一个节点。这种情况下客户端需要对读与写进行区别，俗称**读写分离**； 
2、**写任意**(Write Any)：对数据的**修改可提交给任意的节点**，跟读一样。这种情况下，客户端对集群节点的角色与变化透明。

对zookeeper来说，它采用的方式是**写任意**。通过增加机器，它的读吞吐能力和响应能力扩展性非常好，而写，随着机器的增多吞吐能力肯定下降（这也是它建立observer的原因），而响应能力则取决于具体实现方式，是**延迟复制保持最终一致性**，还是**立即复制快速响应**。

## 14.Zookeeper工作原理

Zookeeper 的核心是**原子广播**，这个机制保证了**各个Server之间的同步**。实现这个机制的协议叫做**Zab协议**。Zab协议有两种模式，它们分别是**恢复模式（选主）**和**广播模式（同步）**。当服务启动或者在领导者崩溃后，Zab就进入了恢复模式，当领导者被选举出来，且大多数Server完成了和 leader的状态同步以后，恢复模式就结束了。状态同步保证了leader和Server具有相同的系统状态。

## 15.zookeeper是如何保证事务的顺序一致性的？

zookeeper采用了**递增的事务Id**来标识，所有的proposal（提议）都在被提出的时候加上了zxid，zxid实际上是一个64位的数字，高32位是epoch（时期; 纪元; 世; 新时代）用来标识leader是否发生改变，如果有新的leader产生出来，epoch会自增，**低32位用来递增计数**。当新产生proposal的时候，会依据数据库的两阶段过程，首先会向其他的server发出事务执行请求，如果超过半数的机器都能执行并且能够成功，那么就会开始执行。

## 16.Zookeeper 下 Server工作状态

每个Server在工作过程中有三种状态： 
LOOKING：当前Server**不知道leader是谁**，正在搜寻
LEADING：当前Server即为选举出来的leader
FOLLOWING：leader已经选举出来，当前Server与之同步

## 17.zookeeper是如何选取主leader的？

当leader崩溃或者leader失去大多数的follower，这时zk进入恢复模式，恢复模式需要重新选举出一个新的leader，让所有的Server都恢复到一个正确的状态。Zk的选举算法有两种：一种是基于basic paxos实现的，另外一种是基于fast paxos算法实现的。系统默认的选举算法为**fast paxos**。

1、Zookeeper选主流程(basic paxos)
（1）选举线程由当前Server发起选举的线程担任，其主要功能是对投票结果进行统计，并选出推荐的Server； 
（2）选举线程首先向所有Server发起一次询问(包括自己)； 
（3）选举线程收到回复后，验证是否是自己发起的询问(验证zxid是否一致)，然后获取对方的id(myid)，并存储到当前询问对象列表中，最后获取对方提议的leader相关信息(id,zxid)，并将这些信息存储到当次选举的投票记录表中； 
（4）收到所有Server回复以后，就计算出zxid最大的那个Server，并将这个Server相关信息设置成下一次要投票的Server； 
（5）线程将当前zxid最大的Server设置为当前Server要推荐的Leader，如果此时获胜的Server获得n/2 + 1的Server票数，设置当前推荐的leader为获胜的Server，将根据获胜的Server相关信息设置自己的状态，否则，继续这个过程，直到leader被选举出来。 通过流程分析我们可以得出：要使Leader获得多数Server的支持，则Server总数必须是奇数2n+1，且存活的Server的数目不得少于n+1. 每个Server启动后都会重复以上流程。在恢复模式下，如果是刚从崩溃状态恢复的或者刚启动的server还会从磁盘快照中恢复数据和会话信息，zk会记录事务日志并定期进行快照，方便在恢复时进行状态恢复。
![clipboard.png](https://segmentfault.com/img/bV8XeP?w=357&h=791)

2、Zookeeper选主流程(basic paxos)
fast paxos流程是在选举过程中，某Server首先向所有Server提议自己要成为leader，当其它Server收到提议以后，解决epoch和 zxid的冲突，并接受对方的提议，然后向对方发送接受提议完成的消息，重复这个流程，最后一定能选举出Leader。
![clipboard.png](https://segmentfault.com/img/bV8XeR?w=533&h=451)

## 18.Zookeeper同步流程

选完Leader以后，zk就进入状态同步过程。 
1、Leader等待server连接； 
2、Follower连接leader，将最大的zxid发送给leader； 
3、Leader根据follower的zxid确定同步点； 
4、完成同步后通知follower 已经成为uptodate状态； 
5、Follower收到uptodate消息后，又可以重新接受client的请求进行服务了。
![clipboard.png](https://segmentfault.com/img/bV8Xag?w=393&h=155)

## 19.分布式通知和协调

对于系统调度来说：操作人员发送通知实际是通过控制台**改变某个节点的状态**，**然后zk将这些变化发送给注册了这个节点的watcher的所有客户端**。
对于执行情况汇报：每个工作进程都在某个目录下**创建一个临时节点**。**并携带工作的进度数据**，这样**汇总的进程可以监控目录子节点的变化获得工作进度的实时的全局情况**。

## 20.机器中为什么会有leader？

在分布式环境中，有些业务逻辑只需要集群中的某一台机器进行执行，**其他的机器可以共享这个结果**，这样可以大大**减少重复计算**，**提高性能**，于是就需要进行leader选举。

## 21.zk节点宕机如何处理？

Zookeeper本身也是集群，推荐配置不少于3个服务器。Zookeeper自身也要保证当一个节点宕机时，其他节点会继续提供服务。
如果是一个Follower宕机，还有2台服务器提供访问，因为Zookeeper上的数据是有多个副本的，数据并不会丢失；
如果是一个Leader宕机，Zookeeper会选举出新的Leader。
ZK集群的机制是只要超过半数的节点正常，集群就能正常提供服务。只有在ZK节点挂得太多，只剩一半或不到一半节点能工作，集群才失效。
所以
3个节点的cluster可以挂掉1个节点(leader可以得到2票>1.5)
2个节点的cluster就不能挂掉任何1个节点了(leader可以得到1票<=1)

## 22.zookeeper负载均衡和nginx负载均衡区别

zk的负载均衡是可以调控，nginx只是能调权重，其他需要可控的都需要自己写插件；但是nginx的吞吐量比zk大很多，应该说按业务选择用哪种方式。

## 23.zookeeper watch机制

Watch机制官方声明：一个Watch事件是一个一次性的触发器，当被设置了Watch的数据发生了改变的时候，则服务器将这个改变发送给设置了Watch的客户端，以便通知它们。
Zookeeper机制的特点：
1、一次性触发数据发生改变时，一个watcher event会被发送到client，但是client**只会收到一次这样的信息**。
2、watcher event异步发送watcher的通知事件从server发送到client是**异步**的，这就存在一个问题，不同的客户端和服务器之间通过socket进行通信，由于**网络延迟或其他因素导致客户端在不通的时刻监听到事件**，由于Zookeeper本身提供了**ordering guarantee，即客户端监听事件后，才会感知它所监视znode发生了变化**。所以我们使用Zookeeper不能期望能够监控到节点每次的变化。Zookeeper**只能保证最终的一致性，而无法保证强一致性**。
3、数据监视Zookeeper有数据监视和子数据监视getdata() and exists()设置数据监视，getchildren()设置了子节点监视。
4、注册watcher **getData、exists、getChildren**
5、触发watcher **create、delete、setData**
6、**setData()**会触发znode上设置的data watch（如果set成功的话）。一个成功的**create()** 操作会触发被创建的znode上的数据watch，以及其父节点上的child watch。而一个成功的**delete()**操作将会同时触发一个znode的data watch和child watch（因为这样就没有子节点了），同时也会触发其父节点的child watch。
7、当一个客户端**连接到一个新的服务器上**时，watch将会被以任意会话事件触发。当**与一个服务器失去连接**的时候，是无法接收到watch的。而当client**重新连接**时，如果需要的话，所有先前注册过的watch，都会被重新注册。通常这是完全透明的。只有在一个特殊情况下，**watch可能会丢失**：`对于一个未创建的znode的exist watch，如果在客户端断开连接期间被创建了，并且随后在客户端连接上之前又删除了，这种情况下，这个watch事件可能会被丢失。`



8、Watch是轻量级的，其实就是本地JVM的**Callback**，服务器端只是存了是否有设置了Watcher的布尔类型



## 24 、dubbo--zookeeper面试中问题解答





# 25、redis面试总结

## （1）什么是redis

[Redis](http://lib.csdn.net/base/redis) 是一个基于内存的高性能分布式key-value[数据库](http://lib.csdn.net/base/mysql)。

## （2）Redis支持的数据类型

Redis通过Key-Value的单值不同类型来区分, 以下是支持的类型: 

String 

List

Set 求交集、并集 

Sorted Set  

hash

## （3）为什么redis需要把所有数据放到内存中

Redis为了达到最快的读写速度将数据都读到内存中，并通过`异步的方式将数据写入磁盘`。所以redis具有快速和数据持久化的特征。如果不将数据放在内存中，磁盘I/O速度为严重影响redis的性能。在内存越来越便宜的今天，redis将会越来越受欢迎。 如果设置了最大使用的内存，则数据已有记录数达到内存限值后不能继续插入新值。 

## （4）读写分离模型

redis支持主从的模式。原则：`Master会将数据同步到slave，而slave不会将数据同步到master。Slave启动时会连接master来同步数据`。

这是一个典型的分布式读写分离模型。我们可以利用master来插入数据，slave提供检索服务。这样可以有效减少单个机器的并发访问数量



通过增加Slave DB的数量，读的性能可以线性增长。为了避免Master DB的单点故障，集群一般都会采用两台Master DB做双机热备，所以整个集群的读和写的可用性都非常高。 读写分离[架构](http://lib.csdn.net/base/architecture)的缺陷在于，不管是Master还是Slave，每个节点都必须保存完整的数据，如果在数据量很大的情况下，集群的扩展能力还是受限于单个节点的存储能力，而且对于Write-intensive类型的应用，读写分离架构并不适合。 

## （5）Redis的回收策略

redis 内存数据集大小上升到一定大小的时候，就会施行数据淘汰策略（回收策略）。redis 提供 6种数据淘汰策略 

- volatile-lru：从已设置过期时间的数据集（server.db[i].expires）中挑选`最近最少使用`的数据淘汰
- volatile-ttl：从已设置过期时间的数据集（server.db[i].expires）中挑选`将要过期`的数据淘汰
- volatile-random：从已设置过期时间的数据集（server.db[i].expires）中`任意`选择数据淘汰
- allkeys-lru：从数据集（server.db[i].dict）中挑选`最近最少使用`的数据淘汰
- allkeys-random：从数据集（server.db[i].dict）中`任意`选择数据淘汰
- no-enviction（驱逐）：禁止驱逐数据

## （6）使用redis有哪些好处

 (1) 速度快，因为数据存在内存中，类似于HashMap，HashMap的优势就是查找和操作的时间复杂度都是O(1)     (2) 支持丰富数据类型，支持string，list，set，sorted set，hash     

(3) 支持事务，操作都是原子性，所谓的原子性就是对数据的更改要么全部执行，要么全部不执行     

(4) 丰富的特性：可用于缓存，消息，按key设置过期时间，过期后将会自动删除 



## （7）redis相比memcached有哪些优势？ 

(1) memcached所有的值均是简单的字符串，redis作为其替代者，支持更为丰富的数据类型  　　　

(2) redis的速度比memcached快很多 (3) redis可以持久化其数据 



# 26 Java 中的 TreeMap 是采用什么树实现的

Java 中的 TreeMap 是使用红黑树实现的。



# 27 ArrayList 和 HashMap 的默认大小是多数

ArrayList 的默认大小是 10 个元素

HashMap 的默认大小是16个元素



# 28 GET和POST 的区别

|                  | GET                                                          | POST                                                         |
| ---------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 后退按钮/刷新    | 无害                                                         | 数据会被重新提交（浏览器应该告知用户数据会被重新提交）。     |
| 书签             | 可收藏为书签                                                 | 不可收藏为书签                                               |
| 缓存             | 能被缓存                                                     | 不能缓存                                                     |
| 编码类型         | application/x-www-form-urlencoded                            | application/x-www-form-urlencoded 或 multipart/form-data。为二进制数据使用多重编码。 |
| 历史             | 参数保留在浏览器历史中。                                     | 参数不会保存在浏览器历史中。                                 |
| 对数据长度的限制 | 是的。当发送数据时，GET 方法向 URL 添加数据；URL 的长度是受限制的（URL 的最大长度是 2048 个字符）。 | 无限制。                                                     |
| 对数据类型的限制 | 只允许 ASCII 字符。                                          | 没有限制。也允许二进制数据。                                 |
| 安全性           | 与 POST 相比，GET 的安全性较差，因为所发送的数据是 URL 的一部分。在发送密码或其他敏感信息时绝不要使用 GET ！ | POST 比 GET 更安全，因为参数不会被保存在浏览器历史或 web 服务器日志中。 |
| 可见性           | 数据在 URL 中对所有人都是可见的。                            | 数据不会显示在 URL 中。                                      |

# 29 一条sql执行过长的时间，如何优化

1、查看sql是否涉及多表的联表或者子查询，如果有，看是否能进行业务拆分，相关字段冗余或者合并成临时表（业务和算法的优化）

2、涉及链表的查询，是否能进行分表查询，单表查询之后的结果进行字段整合

3、如果以上两种都不能操作，非要链表查询，那么考虑对相对应的查询条件做索引。加快查询速度

4、针对数量大的表进行历史表分离（如交易流水表）

5、数据库主从分离，读写分离，降低读写针对同一表同时的压力，至于主从同步，mysql有自带的binlog实现 主从同步

6、explain分析sql语句，查看执行计划，分析索引是否用上，分析扫描行数等等

7、查看mysql执行日志，看看是否有其他方面的问题

个人理解：从根本上来说，查询慢是占用mysql内存比较多，那么可以从这方面去酌手考虑



# 30 Mysql的事物隔离级别

Mysql的事物隔离级别 其实跟 Spring的事物隔离级别一样，都是

1、Read Uncommitted（读取未提交内容），

 2、Read Committed（读取提交内容），

3、Repeatable Read（可重读），

4、Serializable（可串行化）

# 31 数据库事务是什么,并分别说明ACID

事务,是指作为单个逻辑工作单元执行的一系列操作.事务处理可以确保除非事务性单元内的所有操作都成功完成,否则不会永久更新面向数据的资源.通过将一组相关操作组合为一个要么全部成功要么全部失败的单元,可以简化错误恢复并使应用程序更加可靠. 
事务的四个特性ACID,分别表示：

原子性(atomicity):一个事务或者完全发生,或者完全不发生; 
一致性(consistency):事务把数据库从一个一致状态转变到另一个状态; 
隔离性(isolation):在事务提交之前,其他事务觉察不到事务的影响; 
持久性(durability):一旦事务提交,它是永久的.



# 32 Dubbo中zookeeper做注册中心，如果注册中心集群都挂掉，发布者和订阅者之间还能通信么

可以的，启动dubbo时，消费者会从zk拉取注册的生产者的地址接口等数据，缓存在本地。每次调用时，按照本地存储的地址进行调用

默认缓存位置：`C:\Users\789\.dubbo\dubbo-registry-127.0.0.1.cache`

 更改：dubbo.registry.file=/tomcat/soa/dubbo-registry.properties



# 33 Dubbo在安全机制方面是如何解决的

Dubbo通过Token令牌防止用户绕过注册中心直连，然后在注册中心上管理授权。Dubbo还提供服务黑白名单，来控制服务所允许的调用方。

# 34 zookeeper是如何保证事务的顺序一致性的 

zookeeper采用了递增的事务Id来标识，所有的proposal都在被提出的时候加上了zxid，zxid实际上是一个64位的数字，高32位是epoch用来标识leader是否发生改变，如果有新的leader产生出来，epoch会自增，低32位用来递增计数。当新产生proposal的时候，会依据数据库的两阶段过程，首先会向其他的server发出事务执行请求，如果超过半数的机器都能执行并且能够成功，那么就会开始执行 

# 35 默认使用什么序列化框架，你知道的还有哪些

默认使用Hessian序列化，还有Duddo、FastJson、Java自带序列化。

# 36  dubbo服务提供者能实现失效踢出是什么原理

服务失效踢出基于zookeeper的临时节点原理。

# 37   画一画服务注册与发现的流程图

![img](https://images2018.cnblogs.com/blog/1254220/201806/1254220-20180612095859810-1306765019.png)



# 38  springboot+springcloud相关面试题

## 一 、什么是springboot

用来简化spring应用的初始搭建以及开发过程 使用特定的方式来进行配置（properties或yml文件） 

​      创建独立的spring引用程序 main方法运行

​       嵌入的Tomcat 无需部署war文件

​       简化maven配置

​       自动配置spring添加对应功能starter自动化配置

​      简化spring配置，用javaConfig配置

​      约定优于配置，比如包接口，配置结构，属性文件读取

​      微服务监控

## 二、springboot常用的starter有哪些

 spring-boot-starter-web 嵌入tomcat和web开发需要servlet与jsp支持
 spring-boot-starter-data-jpa 数据库支持
 spring-boot-starter-data-redis redis数据库支持
 spring-boot-starter-data-solr solr支持
 mybatis-spring-boot-starter 第三方的mybatis集成starter

 spring-boot-starter-dubbo

## 三 、springboot自动配置的原理

在spring程序main方法中 添加@SpringBootApplication或者@EnableAutoConfiguration

会自动去maven中读取每个starter中的spring.factories文件  该文件里配置了所有需要被创建spring容器中的bean

## 四  springboot读取配置文件的方式

springboot默认读取配置文件为application.properties或者是application.yml



## 五 springboot如何添加【修改代码】自动重启功能

  添加开发者工具集=====spring-boot-devtools

## 六 什么是微服务

分布式：分散压力。

微服务：分散能力。



 以前的模式是 所有的代码在同一个工程中 部署在同一个服务器中 同一个项目的不同模块不同功能互相抢占资源

​        微服务 将工程根据不同的业务规则拆分成微服务 微服务部署在不同的机器上 服务之间进行相互调用

​        Java微服务的框架有 dubbo（只能用来做微服务），spring cloud（提供了服务的发现，断路器等）



## 七 springcloud如何实现服务的注册和发现

服务在发布时 指定对应的服务名（服务名包括了IP地址和端口） 将服务注册到注册中心（eureka或者zookeeper）

​        这一过程是springcloud自动实现 只需要在main方法添加@EnableDisCoveryClient  同一个服务修改端口就可以启动多个实例

​        调用方法：传递服务名称通过注册中心获取所有的可用实例 通过负载均衡策略调用（ribbon和feign）对应的服务



## 八 ribbon和feign区别

 Ribbon添加maven依赖 spring-starter-ribbon 使用@RibbonClient(value="服务名称") 使用RestTemplate调用远程服务对应的方法

feign添加maven依赖 spring-starter-feign 服务提供方提供对外接口 调用方使用 在接口上使用@FeignClient("指定服务名")



Ribbon和Feign都是用于调用其他服务的，不过方式不同。

​        1.启动类使用的注解不同，Ribbon用的是@RibbonClient，Feign用的是@EnableFeignClients。

​        2.服务的指定位置不同，Ribbon是在@RibbonClient注解上声明，Feign则是在定义抽象方法的接口中使用@FeignClient声明。

​        3.调用方式不同，Ribbon需要自己构建http请求，模拟http请求然后使用RestTemplate发送给其他服务，步骤相当繁琐。

​        Feign则是在Ribbon的基础上进行了一次改进，采用接口的方式，将需要调用的其他服务的方法定义成抽象方法即可，

​        不需要自己构建http请求。不过要注意的是抽象方法的注解、方法签名要和提供服务的方法完全一致。



## 九 springcloud断路器的作用

 当一个服务调用另一个服务由于网络原因或者自身原因出现问题时 调用者就会等待被调用者的响应 当更多的服务请求到这些资源时

​                导致更多的请求等待 这样就会发生连锁效应（雪崩效应） 断路器就是解决这一问题

​                断路器有完全打开状态

​                        一定时间内 达到一定的次数无法调用 并且多次检测没有恢复的迹象 断路器完全打开，那么下次请求就不会请求到该服务

​                半开

​                    短时间内 有恢复迹象 断路器会将部分请求发给该服务 当能正常调用时 断路器关闭

​                关闭

​                  当服务一直处于正常状态 能正常调用 断路器关闭

## 十  微服务技术栈有哪些



| **微服务条目**                           | **落地技术**                                                 | **备注** |
| ---------------------------------------- | ------------------------------------------------------------ | -------- |
| 服务开发                                 | SpringBoot、Spring、SpringMVC                                |          |
| 服务配置与管理                           | Netflix公司的Archaius、阿里的Diamond等                       |          |
| 服务注册与发现                           | Eureka、Consul、Zookeeper等                                  |          |
| 服务调用                                 | Rest、RPC、GRP C                                             |          |
| 服务熔断器                               | Hystrix、Envoy等                                             |          |
| 负载均衡                                 | Ribbon、Nginx等                                              |          |
| 服务接口调用（客户端调用服务的简化工具） | Feign等                                                      |          |
| 消息队列                                 | Kafka、RabbitMQ、ActiveMQ等                                  |          |
| 服务配置中心管理                         | SpringCloudConfig、Cherf等                                   |          |
| 服务路由（API网关）                      | Zuul等                                                       |          |
| 服务监控                                 | Zabbix、Nagios、Metrics、Spectator等                         |          |
| 全链路追踪                               | Zipkin、Brave、Kubernetes等                                  |          |
| 服务部署                                 | Docker、OpenStack、Kubernetes等                              |          |
| 数据流操作开发包                         | SpringCloud Stream(封装与Redis、Rabbit、Kafka等发送接收消息) |          |
| 事件消息总线                             | Spring Cloud Bus                                             |          |

 

# 38、 Java线程池中submit() 和 execute()方法有什么区别？

两个方法都可以向线程池提交任务，execute()方法的返回类型是void，它定义在Executor接口中, 而submit()方法可以返回持有计算结果的Future对象，它定义在ExecutorService接口中，它扩展了Executor接口，其它线程池类像ThreadPoolExecutor和ScheduledThreadPoolExecutor都有这些方法。

http://www.cnblogs.com/king-garden/p/5672853.html



# 39、获取分布式锁的总体思路

 在获取分布式锁的时候在locker节点下创建临时顺序节点，释放锁的时候删除该临时节点。客户端调用createNode方法在locker下创建临时顺序节点，

然后调用getChildren(“locker”)来获取locker下面的所有子节点，注意此时不用设置任何Watcher。客户端获取到所有的子节点path之后，如果发现自己在之

前创建的子节点序号最小，那么就认为该客户端获取到了锁。如果发现自己创建的节点并非locker所有子节点中最小的，说明自己还没有获取到锁，

此时客户端需要找到比自己小的那个节点，然后对其调用exist()方法，同时对其注册事件监听器。之后，让这个被关注的节点删除，则客户端的Watcher会

收到相应通知，此时再次判断自己创建的节点是否是locker子节点中序号最小的，如皋是则获取到了锁，如果不是则重复以上步骤继续获取到比自己小的一个

节点并注册监听。当前这个过程中还需要许多的逻辑判断。



# Kafka面试题

1、请说明什么是Apache Kafka?

Apache Kafka是由Apache开发的一种发布订阅消息系统，它是一个分布式的、分区的和重复的日志服务。

2、请说明什么是传统的消息传递方法?

传统的消息传递方法包括两种：

- 排队：在队列中，一组用户可以从服务器中读取消息，每条消息都发送给其中一个人。
- 发布-订阅：在这个模型中，消息被广播给所有的用户。

3、请说明Kafka相对传统技术有什么优势?

Apache Kafka与传统的消息传递技术相比优势之处在于：

快速:单一的Kafka代理可以处理成千上万的客户端，每秒处理数兆字节的读写操作。

可伸缩:在一组机器上对数据进行分区和简化，以支持更大的数据

持久:消息是持久性的，并在集群中进行复制，以防止数据丢失。

设计:它提供了容错保证和持久性

4、在Kafka中broker的意义是什么?

在Kafka集群中，broker术语用于引用服务器。

5、Kafka服务器能接收到的最大信息是多少?

Kafka服务器可以接收到的消息的最大大小是1000000字节。

6、解释Kafka的Zookeeper是什么?我们可以在没有Zookeeper的情况下使用Kafka吗?

Zookeeper是一个开放源码的、高性能的协调服务，它用于Kafka的分布式应用。

不，不可能越过Zookeeper，直接联系Kafka broker。一旦Zookeeper停止工作，它就不能服务客户端请求。

- Zookeeper主要用于在集群中不同节点之间进行通信
- 在Kafka中，它被用于提交偏移量，因此如果节点在任何情况下都失败了，它都可以从之前提交的偏移量中获取
- 除此之外，它还执行其他活动，如: leader检测、分布式同步、配置管理、识别新节点何时离开或连接、集群、节点实时状态等等。

7、解释Kafka的用户如何消费信息?

在Kafka中传递消息是通过使用sendfile API完成的。它支持将字节从套接口转移到磁盘，通过内核空间保存副本，并在内核用户之间调用内核。

8、解释如何提高远程用户的吞吐量?

如果用户位于与broker不同的数据中心，则可能需要调优套接口缓冲区大小，以对长网络延迟进行摊销。

9、解释一下，在数据制作过程中，你如何能从Kafka得到准确的信息?

在数据中，为了精确地获得Kafka的消息，你必须遵循两件事: 在数据消耗期间避免重复，在数据生产过程中避免重复。

这里有两种方法，可以在数据生成时准确地获得一个语义:

- 每个分区使用一个单独的写入器，每当你发现一个网络错误，检查该分区中的最后一条消息，以查看您的最后一次写入是否成功
- 在消息中包含一个主键(UUID或其他)，并在用户中进行反复制

10、解释如何减少ISR中的扰动?broker什么时候离开ISR?

ISR是一组与leaders完全同步的消息副本，也就是说ISR中包含了所有提交的消息。ISR应该总是包含所有的副本，直到出现真正的故障。如果一个副本从leader中脱离出来，将会从ISR中删除。

11、Kafka为什么需要复制?

Kafka的信息复制确保了任何已发布的消息不会丢失，并且可以在机器错误、程序错误或更常见些的软件升级中使用。

12、如果副本在ISR中停留了很长时间表明什么?

如果一个副本在ISR中保留了很长一段时间，那么它就表明，跟踪器无法像在leader收集数据那样快速地获取数据。

13、请说明如果首选的副本不在ISR中会发生什么?

如果首选的副本不在ISR中，控制器将无法将leadership转移到首选的副本。

14、有可能在生产后发生消息偏移吗?

在大多数队列系统中，作为生产者的类无法做到这一点，它的作用是触发并忘记消息。broker将完成剩下的工作，比如使用id进行适当的元数据处理、偏移量等。

作为消息的用户，你可以从Kafka broker中获得补偿。如果你注视SimpleConsumer类，你会注意到它会获取包括偏移量作为列表的MultiFetchResponse对象。此外，当你对Kafka消息进行迭代时，你会拥有包括偏移量和消息发送的MessageAndOffset对象。





# 40 HashMap和ConcurrentHashMap的区别

1、HashMap不是线程安全的，而ConcurrentHashMap是线程安全的。

2、ConcurrentHashMap采用锁分段技术，将整个Hash桶进行了分段segment，也就是将这个大的数组分成了几个小的片段segment，而且每个小的片段segment上面都有锁存在，那么在插入元素的时候就需要先找到应该插入到哪一个片段segment，然后再在这个片段上面进行插入，而且这里还需要获取segment锁。

3、ConcurrentHashMap让锁的粒度更精细一些，并发性能更好。



# 41 JVM的内存结构

根据 JVM 规范，JVM 内存共分为虚拟机栈、堆、方法区、程序计数器、本地方法栈五个部分。

**1、Java虚拟机栈：**

线程私有；每个方法在执行的时候会创建一个栈帧，存储了局部变量表，操作数栈，动态连接，方法返回地址等；每个方法从调用到执行完毕，对应一个栈帧在虚拟机栈中的入栈和出栈。

**2、堆：**

线程共享；被所有线程共享的一块内存区域，在虚拟机启动时创建，用于存放对象实例。

**3、方法区：**

线程共享；被所有线程共享的一块内存区域；用于存储已被虚拟机加载的类信息，常量，静态变量等。

**4、程序计数器：**

线程私有；是当前线程所执行的字节码的行号指示器，每条线程都要有一个独立的程序计数器，这类内存也称为“线程私有”的内存。

**5、本地方法栈：**

线程私有；主要为虚拟机使用到的Native方法服务



# 42 强引用，软引用和弱引用的区别

**强引用：**

只有这个引用被释放之后，对象才会被释放掉，只要引用存在，垃圾回收器永远不会回收，这是最常见的New出来的对象。

**软引用：**

内存溢出之前通过代码回收的引用。软引用主要用户实现类似缓存的功能，在内存足够的情况下直接通过软引用取值，无需从繁忙的真实来源查询数据，提升速度；当内存不足时，自动删除这部分缓存数据，从真正的来源查询这些数据。

**弱引用：**

第二次垃圾回收时回收的引用，短时间内通过弱引用取对应的数据，可以取到，当执行过第二次垃圾回收时，将返回null。弱引用主要用于监控对象是否已经被垃圾回收器标记为即将回收的垃圾，可以通过弱引用的isEnQueued方法返回对象是否被垃圾回收器标记。

## 43 悲观锁和乐观锁的区别，怎么实现

悲观锁：一段执行逻辑加上悲观锁,不同线程同时执行时,只能有一个线程执行,其他的线程在入口处等待,直到锁被释放。

乐观锁：一段执行逻辑加上乐观锁,不同线程同时执行时,可以同时进入执行,在最后更新数据的时候要检查这些数据是否被其他线程修改了(版本和执行初是否相同),没有修改则进行更新,否则放弃本次操作。

https://blog.csdn.net/L_BestCoder/article/details/79298417

# 44 Java内存泄露的问题调查定位：jmap，jstack的使用等等



# 45 在数据库中查询语句速度很慢，如何优化？

1.建索引
2.减少表之间的关联
3.优化sql，尽量让sql很快定位数据，不要让sql做全表查询，应该走索引,把数据 量大的表排在前面
4.简化查询字段，没用的字段不要，尽量返回少量数据，也就是避免select *
5.尽量用PreparedStatement来查询，不要用Statement

6.用EXISTS替代IN、用NOT EXISTS替代NOT IN

7 应尽量避免在 where 子句中对字段进行 null 值判断,否则索引失效

  应尽量避免在 where 子句中使用!=或<>操作符,否则索引失效

  in 和 not in 也要慎用，否则会导致全表扫描,否则索引失效

  select id from t where name like '%abc' 索引失效， 'abc%'才会使用索引

  应尽量避免在 where 子句中对字段进行表达式操作,否则索引失效



# 46.count(column) 和 count(*) 是不一样的

count(column) 是表示结果集中有多少个column字段不为空的记录
count(*) 是表示整个结果集有多少条记录

 如果使用主键（主键肯定不为空的），则count(id)和 count(*) 效果一样

# 47.delete 和trunc区别

trunc是截断表，将表所有数据删除，释放表空间。
不可以回滚。
 delete：删除表数据，不释放表空间，可以回滚。



# 48、ConcurrentHashMap与HashMap

ConcurrentHashMap与HashMap相比，有以下不同点

- ConcurrentHashMap线程安全，而HashMap非线程安全
- HashMap允许Key和Value为null，而ConcurrentHashMap不允许
- HashMap不允许通过Iterator遍历的同时通过HashMap修改，而ConcurrentHashMap允许该行为，并且该更新对后续的遍历可见

```java
ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
map.put(null,null);//Exception in thread "main" java.lang.NullPointerException
System.out.println(map);
```



# 49 、为什么wait()和notify()属于Object类

简单说：因为synchronized中的这把锁可以是任意对象，所以任意对象都可以调用wait()和notify()；所以wait和notify属于Object。

专业说：因为这些方法在操作同步线程时，都必须要标识它们操作线程的锁，只有同一个锁上的被等待线程，可以被同一个锁上的notify唤醒，不可以对不同锁中的线程进行唤醒。

也就是说，等待和唤醒必须是同一个锁。而锁可以是任意对象，所以可以被任意对象调用的方法是定义在object类中。



# 50 、CountDownLatch    CyclicBarrier区别

| CountDownLatch                                               | CyclicBarrier                                                |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| 减计数方式                                                   | 加计数方式                                                   |
| 计算为0时释放所有等待的线程                                  | 计数达到指定值时释放所有等待线程                             |
| 计数为0时，无法重置                                          | 计数达到指定值时，计数置为0重新开始                          |
| 调用countDown()方法计数减一，调用await()方法只进行阻塞，对计数没任何影响 | 调用await()方法计数加1，若加1后的值不等于构造方法的值，则线程阻塞 |
| 不可重复利用                                                 | 可重复利用                                                   |

# 51、实现多线程的3种方式

1、继承Thread类

看jdk源码可以发现，Thread类其实是实现了Runnable接口的一个实例，继承Thread类后需要重写run方法并通过start方法启动线程。

继承Thread类耦合性太强了，因为java只能单继承，所以不利于扩展。

2、实现Runnable接口

通过实现Runnable接口并重写run方法，并把Runnable实例传给Thread对象，Thread的start方法调用run方法再通过调用Runnable实例的run方法启动线程。

所以如果一个类继承了另外一个父类，此时要实现多线程就不能通过继承Thread的类实现。

3、实现Callable接口

通过实现Callable接口并重写call方法，并把Callable实例传给FutureTask对象，再把FutureTask对象传给Thread对象。它与Thread、Runnable最大的不同是Callable能返回一个异步处理的结果Future对象并能抛出异常，而其他两种不能。