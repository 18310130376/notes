import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

public class CollectionsDemo {
	public static void main(String[] args) {
	       Set<Integer> set1= Sets.newHashSet(1,2,3,4,5);
	       Set<Integer> set2=Sets.newHashSet(3,4,5,6);
	       Sets.SetView<Integer> inter=Sets.intersection(set1,set2); //交集
	       System.out.println(inter);
	       Sets.SetView<Integer> diff=Sets.difference(set1,set2); //差集,在A中不在B中
	       System.out.println(diff);
	       Sets.SetView<Integer> union=Sets.union(set1,set2); //并集
	       System.out.println(union);
	       
	       
	       List<String> list = Lists.newArrayList();
	       Map<String,Object> map = Maps.newLinkedHashMap();

	       Set<String> copySet = Sets.newHashSet("alpha", "beta", "gamma");
	       List<String> theseElements = Lists.newArrayList("alpha", "beta", "gamma");
	       
	       
	   	List<String> exactly100 = Lists.newArrayListWithCapacity(100);
	   	List<String> approx100 = Lists.newArrayListWithExpectedSize(100);
	   	Set<String> approx100Set = Sets.newHashSetWithExpectedSize(100);
	   	
	   	
	   	List countUp = Ints.asList(1, 2, 3, 4, 5);
	   	List countDown = Lists.reverse(countUp); // {5, 4, 3, 2, 1}
	   	List<List> parts = Lists.partition(countUp, 2);//{{1,2}, {3,4}, {5}}

	   }
}
