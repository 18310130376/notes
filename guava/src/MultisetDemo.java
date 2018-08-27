import com.google.common.base.Splitter;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

@SuppressWarnings("all")
public class MultisetDemo {
	public static void main(String[] args) {
		
        Multiset multiset = HashMultiset.create();
        String sentences = "this is a story,there is a good girl in the story.";
        Iterable<String> words = Splitter.onPattern("[^a-z]").omitEmptyStrings().trimResults().split(sentences);
        for (String word : words) {
            multiset.add(word);
        }

        for (Object element : multiset.elementSet()) {
            System.out.println((String)element + ":" + multiset.count(element));
        }
    }
}