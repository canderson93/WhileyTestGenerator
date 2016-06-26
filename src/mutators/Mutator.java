package mutators;

import java.util.List;

import wyc.lang.WhileyFile;

/**
 * A mutator, which generates a list of possible mutations for a whiley file
 * @author Carl
 *
 */
public interface Mutator {
	
	/**
	 * Generates a list of possible mutations from the Whiley File
	 */
	public List<Mutation> generate(WhileyFile file);
	
}
