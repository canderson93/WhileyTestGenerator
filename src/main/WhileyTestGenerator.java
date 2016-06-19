package main;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import wyc.io.WhileyFileLexer;
import wyc.io.WhileyFileLexer.Token;
import wyc.io.WhileyFileParser;
import wyc.io.WhileyFilePrinter;
import wyc.lang.WhileyFile;

import mutators.*;
import template.*;

public class WhileyTestGenerator {
	TemplateGenerator template;
	Mutator mutator;
	
	int numTests = 1;
	
	public WhileyTestGenerator(String[] args){
		template = new FileTemplate("BoolAssign_Valid_5.whiley");
		mutator = new AssignmentSwapMutator(AssignmentSwapMutator.Type.SELF, 1);
	}
	
	/**
	 * Starts the test generator
	 */
	public void start(){
		WhileyFile file = template.generate();
		int test = 0;
		
		while (test < numTests){
			//Pass in same reference so mutators can mutate their own output
			mutator.next(file);
			
			printFile("BoolAssign_Valid_5.whiley", file, System.out);
			test++;
		}
	}
	
	private static void printFile(String filename, WhileyFile file, OutputStream stream){
		WhileyFilePrinter printer = new WhileyFilePrinter(stream);
		printer.print(file);
	}
	
	public static void main(String[] args) {
		WhileyTestGenerator generator = new WhileyTestGenerator(args);
		generator.start();
	}
}
