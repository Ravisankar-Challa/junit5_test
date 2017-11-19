package junit5_test.junit5;

public class App {
	
	public App(String name, String version, String author) {
		this.name = name;
		this.version = version;
		this.author = author;
	}
	private String name;
	private String version;
	private String author;
	
	public String getName() {
		return name;
	}
	public String getVersion() {
		return version;
	}
	public String getAuthor() {
		return author;
	}
	
	public static int divide(int a, int b) {
		return a/b;
	}
	
	public static int wordCount(String word) {
		return word.length();
	}
	
}
