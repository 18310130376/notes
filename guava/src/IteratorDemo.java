import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;

public class IteratorDemo {
	public static void main(String[] args) {
		
		List<String> list = Lists.newArrayList("Apple","Pear","Peach","Banana");
		
		 Predicate<String> condition = new Predicate<String>() {
		     @Override
		     public boolean apply(String input) {
		         return ((String)input).startsWith("P");
		     }
		 };
		 boolean allIsStartsWithP = Iterators.all(list.iterator(), condition);
		 System.out.println("all result == " + allIsStartsWithP);
		 
		 allIsStartsWithP = Iterators.any(list.iterator(), condition);
		 System.out.println("all result == " + allIsStartsWithP);
		 
		 
		 Iterator<String> startPElements = Iterators.filter(list.iterator(), new Predicate<String>() {
	            @Override
	            public boolean apply(String input) {
	                return input.startsWith("A");
	            }
	        });
		 
		 while (startPElements.hasNext()) {
			System.out.println(startPElements.next());
		}
		 
		 int compare = Doubles.compare(22.00, 21.00);
		 System.out.println(compare);
		 
		 List<String> list0= Lists.newArrayList("moon","dad","refer","son");
	     Collection<String> palindromeList= Collections2.filter(list0, input -> {
	         return new StringBuilder(input).reverse().toString().equals(input); //找回文串
	     });
	     System.out.println(palindromeList);
	}

}
