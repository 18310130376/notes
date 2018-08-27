import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;

public class GuavaFiles {
	public static void main(String[] args) throws IOException {

		/**写文件***/
		String contents = "123";
		String fileName = "C:\\Users\\789\\Desktop\\logs\\a.log";

		final File newFile = new File(fileName);
		try {
			Files.write(contents.getBytes(), newFile);
		} catch (IOException fileIoEx) {
			System.err.println("ERROR trying to write to file '" + fileName + "' - " + fileIoEx.toString());
		}
		
		
		/**读取文件内容(小文件)**/
		 String testFilePath = "C:\\Users\\789\\Desktop\\dubbo-governance.log";
	     File testFile = new File(testFilePath);
	     List<String> lines = Files.readLines(testFile, Charsets.UTF_8);
	     for (String line : lines) {
	       //  System.out.println(line);
	     }
	     
	     
	     testFilePath = "C:\\Users\\789\\Desktop\\dubbo-governance.log";
	     testFile = new File(testFilePath);
	     CounterLine counter = new CounterLine();
	    // Files.readLines(testFile, Charsets.UTF_8, counter);
	     String string = Files.toString(testFile, Charsets.UTF_8);/**返回整个文件内容*/
	    // System.out.println(string);
	     
	     
	     /***复制  移动文件**/
	     File sourceFile = new File("C:\\Users\\789\\Desktop\\dubbo-governance.log");
	     File targetFile = new File("C:\\Users\\789\\Desktop\\logs\\a.log");
	     //Files.copy(sourceFile, targetFile);
	     
	     
	     sourceFile = new File("C:\\Users\\789\\Desktop\\logs\\a.log");
	     targetFile = new File("C:\\Users\\789\\Desktop\\logs\\a.txt");
	     Files.move(sourceFile, targetFile);/**会删除a.log文件，内容拷贝到a.txt**/
	     
	     /***比较内容是否完全一致**/
	     boolean sameFile = Files.equal(sourceFile, targetFile);
	     
	     Files.touch(sourceFile);//方法创建或者更新文件的时间戳
	     Files.createTempDir();//创建临时目录
	     Files.createParentDirs(sourceFile);//创建父级目录
	     Files.getFileExtension("C:\\Users\\789\\Desktop\\logs\\a.log");//获得文件的扩展名
	     Files.getNameWithoutExtension("C:\\Users\\789\\Desktop\\logs\\a.log");//获得不带扩展名的文件名
	     
	     
	    File file = new File("F:/prd/kw/olay1.txt");
		ImmutableList<String> lineList = Files.asCharSource(file, Charsets.UTF_8).readLines();
		for (String line : lineList) {
		  System.out.println(line);
		}
		
		File f = new File("F:/prd/kw/ok.txt");
		//Copy the data from a URL to a file
		Resources.asByteSource(new URL("http://ifeve.com/google-guava-io/")).copyTo(Files.asByteSink(f));
		
		/**获取文件hash**/
		file = new File("F:/prd/kw/olay1.txt");
		HashCode hash = Files.asByteSource(file).hash(Hashing.sha1());
		System.out.println(hash);
	}
	
	
	static class CounterLine implements LineProcessor<String> {
	    private int rowNum = 0;
	    private String line;
	    
	   @Override
	    public boolean processLine(String line) throws IOException {
	        rowNum ++;
	        this.line = line;
	        return true;
	    }

	    @Override
	    public String getResult() {
	        return line;
	    }
	}
}
