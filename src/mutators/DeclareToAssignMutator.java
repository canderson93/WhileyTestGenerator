package mutators;

import java.util.ArrayList;
import java.util.List;

import wyc.lang.Expr;
import wyc.lang.Expr.LVal;
import wyc.lang.WhileyFile.Declaration;
import wyc.lang.WhileyFile.FunctionOrMethod;
import wyc.lang.Stmt;
import wyc.lang.WhileyFile;
import wycc.lang.Attribute;

public class DeclareToAssignMutator implements Mutator{
	
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
				DeclareToAssignMutation mutagen = new DeclareToAssignMutation(func.statements, (Stmt.VariableDeclaration) statement);
				list.add(mutagen);
			}
		}
		
		return list;
	}
	
	protected class DeclareToAssignMutation implements Mutation {
		public List<Stmt> list;
		public Stmt.VariableDeclaration statement;
		
		public DeclareToAssignMutation(List<Stmt> list, Stmt.VariableDeclaration stmt){
			this.list = list;
			this.statement = stmt;
		}

		@Override
		public void apply() {
			//Find the index of the statement
			int index = list.indexOf(statement);
			
			//Create left and right lists for the Assignment Statement
			List<LVal> lval = new ArrayList<LVal>();
			List<Expr> rval = new ArrayList<Expr>();
			
			lval.add(new Expr.AssignedVariable(statement.parameter.name(), new ArrayList<Attribute>()));
			rval.add(statement.expr);
			
			//The ol' switcheroo
			list.remove(index);
			list.add(index, new Stmt.Assign(lval, rval, statement.attributes()));
		}
	}
}
