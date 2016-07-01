package mutators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import wyc.lang.Expr;
import wyc.lang.Stmt;
import wyc.lang.WhileyFile;
import wyc.lang.WhileyFile.Declaration;
import wyc.lang.WhileyFile.FunctionOrMethod;
import wycc.lang.Attribute;

public class ReturnMutator implements Mutator {

	@Override
	public List<Mutation> generate(WhileyFile file) {
		List<Mutation> rtn = new ArrayList<Mutation>();
		
		for (Declaration d : file.declarations){
			if (d instanceof FunctionOrMethod){
				FunctionOrMethod func = (FunctionOrMethod)d;
				
				rtn.addAll(insertReturn(func.statements));
			}
		}
		
		return rtn;
	}
	
	private Collection<? extends Mutation> insertReturn(List<Stmt> statements) {
		List<Mutation> list = new ArrayList<Mutation>();
		
		for (int i = 0; i < statements.size(); i++){
			Stmt statement = statements.get(i);
			
			//Recursively iterate through other blocks
			if (statement instanceof Stmt.While){
				Stmt.While stmt = (Stmt.While)statement;
				list.addAll(insertReturn(stmt.body));
			} else if (statement instanceof Stmt.IfElse){
				Stmt.IfElse stmt = (Stmt.IfElse)statement;
				list.addAll(insertReturn(stmt.trueBranch));
				list.addAll(insertReturn(stmt.falseBranch));
			} else if (statement instanceof Stmt.DoWhile){
				Stmt.DoWhile stmt = (Stmt.DoWhile)statement;
				list.addAll(insertReturn(stmt.body));
			} else if (statement instanceof Stmt.Switch){
				Stmt.Switch stmt = (Stmt.Switch)statement;
				
				for (int j = 0; j < stmt.cases.size(); j++){
					Stmt.Case c = stmt.cases.get(i);
					list.addAll(insertReturn(c.stmts));
				}
			}
			
			//Create a Return Mutation at this index
			list.add(new ReturnMutation(statements, i));
		}
		
		return list;
	}

	public class ReturnMutation implements Mutation {
		List<Stmt> list;
		int index;
		
		public ReturnMutation(List<Stmt> list, int index){
			this.list = list;
			this.index = index;
		}
		
		@Override
		public void apply() {
			Stmt.Return rtn = new Stmt.Return(new ArrayList<Expr>(), new ArrayList<Attribute>());
			
			list.add(index, rtn);
		}
		
	}
}
