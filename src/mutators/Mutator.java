package mutators;

import java.util.List;

import wyc.io.WhileyFileLexer.Token;
import wyc.lang.WhileyFile;

public interface Mutator {
	
	/**
	 * Generates the next test
	 */
	public WhileyFile next(WhileyFile tokens);
	
}
