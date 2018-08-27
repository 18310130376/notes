import java.util.Collection;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class MutliMapTest {  
    public static void main(String... args) {  
  Multimap<String, String> myMultimap = ArrayListMultimap.create();  
    
  // 添加键值对
  myMultimap.put("Fruits", "Bannana"); 
 //给Fruits元素添加另一个元素 
  myMultimap.put("Fruits", "Apple");  
  myMultimap.put("Fruits", "Pear");  
  myMultimap.put("Vegetables", "Carrot");  
    
  // 获得multimap的size
  int size = myMultimap.size();  
  System.out.println(size);  // 4  
    
  // 获得Fruits对应的所有的值
  Collection<String> fruits = myMultimap.get("Fruits");  
  System.out.println(fruits); // [Bannana, Apple, Pear]  
    
  Collection<String> vegetables = myMultimap.get("Vegetables");  
  System.out.println(vegetables); // [Carrot]  
    
  //遍历Mutlimap  
  for(String value : myMultimap.values()) {  
   System.out.println(value);  
  }  
    
  // Removing a single value  
  myMultimap.remove("Fruits","Pear");  
  System.out.println(myMultimap.get("Fruits")); // [Bannana, Pear]  
    
  // Remove all values for a key  
  myMultimap.removeAll("Fruits");  
  System.out.println(myMultimap.get("Fruits")); // [] (Empty Collection!)  
}  
}