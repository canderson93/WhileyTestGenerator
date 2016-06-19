package mutators;

import java.util.List;

import wyc.io.WhileyFileLexer.Token;
import wyc.lang.WhileyFile;

/**
 * A mutator which does nothing.
 * This is intended to be used with more complicated template generators, which do not need further mutation.
 * @author Carl
 *
 */
public class EmptyMutator implements Mutator{

	@Override
	public WhileyFile next(WhileyFile tokens) {
		return tokens;
	}

}
