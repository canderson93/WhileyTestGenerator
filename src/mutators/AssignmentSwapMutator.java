package mutators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wyc.io.WhileyFileLexer.Token;
import wyc.lang.Expr;
import wyc.lang.Stmt;
import wyc.lang.WhileyFile;
import wyc.lang.WhileyFile.Declaration;
import wyc.lang.WhileyFile.FunctionOrMethod;

/**
 * Swaps whiley assignment values with others
 * @author Carl
 *
 */
public class AssignmentSwapMutator implements Mutator {
	public final Type type;
	public final double probability; //probability it will perform the mutation
	
	public enum Type {
		SELF, //self-assigns variables
		RANDOM //randomly swaps variable assignments
	}
	
	public AssignmentSwapMutator(Type type, double probability) {
		this.type = type;
		this.probability = probability;
	}

	@Override
	public WhileyFile next(WhileyFile file) {
		
		//Drill down to the individual statements
		for (Declaration d : file.declarations){
			if (d instanceof FunctionOrMethod){
				FunctionOrMethod func = (FunctionOrMethod)d;
				
				Map<Integer, Stmt> map = findAssignments(func);
				swap(func, map);
			}
		}
		
		return file;
	}
	
	private void swap(FunctionOrMethod func, Map<Integer, Stmt> map){
		switch(type){
		case SELF:
			swapSelf(func, map);
			break;
		case RANDOM:
			break;
		}
	}
	
	/**
	 * Finds all the variable assignments in functions
	 * @param func
	 * @return
	 */
	private Map<Integer, Stmt> findAssignments(FunctionOrMethod func){
		Map<Integer, Stmt> map = new HashMap<Integer, Stmt>();
		
		for (int i = 0; i < func.statements.size(); i++){
			Stmt statement = func.statements.get(i);
			
			if (statement instanceof Stmt.VariableDeclaration){
				map.put(i, statement);
			}
		}
		
		return map;
	}
	
	private void swapSelf(FunctionOrMethod func, Map<Integer, Stmt> map){
		for (Integer i : map.keySet()){
			if (!doSwap()){ continue; }
			
			Stmt stmt = map.get(i);
			
			Stmt.VariableDeclaration dec = (Stmt.VariableDeclaration)stmt;
			dec.expr = new Expr.LocalVariable(dec.parameter.name, dec.expr.attributes());
		}
	}
	
	/**
	 * Checks whether this should go through with the swap
	 * @return
	 */
	private boolean doSwap(){
		return Math.random() < probability;
	}

}
