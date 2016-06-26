package mutators;

/**
 * A mutation, which is a possible action performed on a whiley file
 * @author Carl
 *
 */
public interface Mutation {
	
	/**
	 * Performs a mutation on the Whiley File
	 */
	public void apply();
}
