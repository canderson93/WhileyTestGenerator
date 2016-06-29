package mutators;

import java.util.ArrayList;
import java.util.List;

import wyc.lang.Stmt;
import wyc.lang.WhileyFile;
import wyc.lang.WhileyFile.Declaration;
import wyc.lang.WhileyFile.FunctionOrMethod;

public class AssignmentRemovalMutator implements Mutator {

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
				AssignmentRemovalMutation mutagen = new AssignmentRemovalMutation(func.statements, (Stmt.VariableDeclaration) statement);
				list.add(mutagen);
			}
		}
		
		return list;
	}
	
	protected class AssignmentRemovalMutation implements Mutation {
		protected List<Stmt> list;
		protected Stmt.VariableDeclaration statement;
		
		public AssignmentRemovalMutation(List<Stmt> list, Stmt.VariableDeclaration statement){
			this.list = list;
			this.statement = statement;
		}
		
		@Override
		public void apply() {
			list.remove(statement);
		}
	}
}
