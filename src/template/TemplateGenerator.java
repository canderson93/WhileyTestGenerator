package template;

import wyc.lang.WhileyFile;

/**
 * An interface for generating lists of Lexer tokens for use with the Whiley Test Generator.
 * 
 * @author Carl
 *
 */
public interface TemplateGenerator {
	
	/**
	 * Produces a list of tokens representing a Whiley program
	 * @return list of tokens
	 */
	public WhileyFile generate();
	
	/**
	 * Return a new instance of the previously generated template
	 * @return
	 */
	public WhileyFile copy();
}
