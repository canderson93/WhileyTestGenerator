package template;

import java.io.IOException;

import wyc.io.WhileyFileLexer;
import wyc.io.WhileyFileParser;
import wyc.lang.WhileyFile;

/**
 * Lexes a Whiley file for use as a template
 * @author Carl
 *
 */
public class FileTemplate implements TemplateGenerator {
	String filename;

	public FileTemplate(String filename){
		this.filename = filename;
	}
	
	@Override
	public WhileyFile generate() {
		return parseFile();
	}
	
	/**
	 * Compiles the file
	 * @return
	 */
	private WhileyFile parseFile() {
		try {
			WhileyFileLexer lexer = new WhileyFileLexer(filename);
			WhileyFileParser parser = new WhileyFileParser(filename, lexer.scan());
			
			return parser.read();

		} catch (IOException e) {
			System.err.println("Error reading file:" + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	public WhileyFile copy() {
		return parseFile();
	}

}
