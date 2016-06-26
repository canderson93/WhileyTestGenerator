package mutators;

import java.util.ArrayList;
import java.util.List;

import wyc.lang.Expr;
import wyc.lang.Stmt;
import wyc.lang.WhileyFile;
import wyc.lang.WhileyFile.Declaration;
import wyc.lang.WhileyFile.FunctionOrMethod;

/**
 * Swaps Whiley assignment values with others
 * @author Carl
 *
 */
public class SelfAssignmentMutator implements Mutator {

	@Override
	public List<Mutation> generate(WhileyFile file) {
		List<Mutation> rtn = new ArrayList<Mutation>();
		
		for (Declaration d : file.declarations){
			if (d instanceof FunctionOrMethod){
				FunctionOrMethod func = (FunctionOrMethod)d;
				
				rtn.addAll(findAssignments(func));
			}
		}
		
		return rtn;
	}
	
	/**
	 * Finds all the variable assignments in functions
	 * @param func
	 * @return
	 */
	private List<Mutation> findAssignments(FunctionOrMethod func){
		List<Mutation> list = new ArrayList<Mutation>();
		
		for (int i = 0; i < func.statements.size(); i++){
			Stmt statement = func.statements.get(i);
			
			if (statement instanceof Stmt.VariableDeclaration){
				SelfAssignmentMutation mutagen = new SelfAssignmentMutation((Stmt.VariableDeclaration) statement);
				list.add(mutagen);
			}
		}
		
		return list;
	}
	
	/**
	 * Causes a variable assignments to assign themselves
	 *
	 */
	protected class SelfAssignmentMutation implements Mutation{
		protected Stmt.VariableDeclaration statement;
		
		public SelfAssignmentMutation(Stmt.VariableDeclaration statement){
			this.statement = statement;
		}
		
		@Override
		public void apply() {
			statement.expr = new Expr.LocalVariable(statement.parameter.name, statement.expr.attributes());
		}
		
	}
}
