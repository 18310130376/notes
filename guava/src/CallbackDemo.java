import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class CallbackDemo {
	public static void main(String[] args) {
		 LoadingCache<String,String> cache= CacheBuilder.newBuilder()
	               .maximumSize(100)
	               .expireAfterAccess(1, TimeUnit.SECONDS)
	               .build(new CacheLoader<String, String>() {
	                   @Override
	                   public String load(String key) throws Exception {
	                       return key;
	                   }
	               });;
	       cache.put("j","java");
	       cache.put("c","cpp");
	       cache.put("s","scala");
	       cache.put("g","go");
	       try {
	           System.out.println(cache.get("j"));
	           TimeUnit.SECONDS.sleep(2);
	           System.out.println(cache.get("s")); //输出s
	       } catch (ExecutionException | InterruptedException e) {
	           e.printStackTrace();
	       }
	   }
}
