import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import kotlin.jvm.internal.StringCompanionObject;

public class GuavaTest {

	public static void main(String[] args) {

		/** 判空 **/
		String input = "0";
		boolean isNullOrEmpty = Strings.isNullOrEmpty(input);
		System.out.println(isNullOrEmpty);

		/** 公共的前缀 后缀 **/
		String a = "com.jd.coo.Hello";
		String b = "com.jd.coo.Hi";
		String ourCommonPrefix = Strings.commonPrefix(a, b);
		System.out.println(ourCommonPrefix);

		String c = "com.google.Hello";
		String d = "com.jd.Hello";
		String ourSuffix = Strings.commonSuffix(c, d);
		System.out.println(ourSuffix);

		/** 补全字符串 **/
		int minLength = 4;
		String padEndResult = Strings.padEnd("123", minLength, '0');
		System.out.println(padEndResult);

		String padStartResult = Strings.padStart("1", minLength, '0');
		System.out.println(padStartResult);

		/** 使用Splitter类来拆分字符串 */
		Iterable<String> splitResults = Splitter.onPattern("[,;]").trimResults().omitEmptyStrings()// 表示忽略空字符串，split方法会执行拆分操作,trimResults():去掉子串中的空格
				.split("hello,word,,,;世界;水平");
		for (String item : splitResults) {
			System.out.println(item);
		}
		
		
		   System.out.println(Splitter.on(",").limit(2).trimResults().split(" a,  b,  c,  d"));//[ a, b, c,d]
	       System.out.println(Splitter.fixedLength(2).split("1 2 3"));//[1 2,  3]
	       System.out.println(Splitter.on(" ").omitEmptyStrings().splitToList("1  2 3"));
	       System.out.println(Splitter.on(",").omitEmptyStrings().split("1,,,,2,,,3"));//[1, 2, 3]
	       System.out.println(Splitter.on(" ").trimResults().split("1 2 3")); //[1, 2, 3],默认的连接符是,
	       System.out.println(Splitter.on(";").withKeyValueSeparator(":").split("a:1;b:2;c:3"));//{a=1, b=2, c=3}
		
		
		
	       String text = "1-2-3-4-5-6";
	       List<String> list = Splitter.on("-").splitToList(text);
		
	       String result = Joiner.on("-").join(list);
	       System.out.println(result);
	       

		/***二次拆分*****/
		String toSplitString = "a=b&c=d;e=f,g=h";
		Map<String, String> kvs = Splitter.onPattern("[,;&]").withKeyValueSeparator('=').split(toSplitString);
		for (Map.Entry<String, String> entry : kvs.entrySet()) {
			System.out.println(String.format("%s=%s", entry.getKey(), entry.getValue()));
		}
		
		/**数组合并字符串**/
		String joinResult = Joiner.on("&").join(new String[]{"hello","world"});
		System.out.println(joinResult);
		
		List<String> list2 = new ArrayList<>();
		
		list2.add("1");
		list2.add("2");
		list2.add(null);
		joinResult = Joiner.on(" ").skipNulls().join(list2);
		System.out.println(joinResult);
		
		
		 StringBuilder sb=new StringBuilder();
		 sb.append("123");
		 Joiner.on(",").skipNulls().appendTo(sb,"Hello","guava");
	     System.out.println(sb);
		
	     System.out.println(Joiner.on(",").useForNull("0").join(1,null,3));
		
		/***Map转为key value字符串****/
		Map<String,String> map = new HashMap<String,String>();
		map.put("a", "b");
		map.put("c", "d");
		String mapJoinResult = Joiner.on(",").withKeyValueSeparator("=").join(map);
		System.out.println(mapJoinResult);
		
		/**字符串重复N次**/
		String repeat = Strings.repeat("a",5);
		System.out.println(repeat);
		
		
		/**************对象操作****************/
		boolean aEqualsB = Objects.equal("aaa","aaa");
		System.out.println(aEqualsB);
		
		/**toString*/
		Objects.toStringHelper(GuavaTest.class)
		.add("id", "123")
		.add("name", "wk")
		.add("age", 12)
		.omitNullValues().toString();//omitNullValues()方法来指定忽略空值
	}
}
