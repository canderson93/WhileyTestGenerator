package main;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import wyc.io.WhileyFilePrinter;
import wyc.lang.WhileyFile;
import mutators.*;
import template.*;

public class WhileyTestGenerator {
	private final TemplateGenerator template;
	private final Collection<Mutator> mutators;

	private int numTests = 1; // number of tests to generate
	private int testRate = 1; // number of mutations to perform per test

	private WhileyTestGenerator() { // default for testing
		mutators = new ArrayList<Mutator>();

		template = new FileTemplate("BoolAssign_Valid_5.whiley");
		mutators.add(new SelfAssignmentMutator());
	}

	/**
	 * Creates an instance of the WhileyTestGenerator. This generator takes a
	 * template generator to create
	 * 
	 * @param template
	 * @param mutator
	 * @param tests
	 * @param rate
	 */
	public WhileyTestGenerator(int tests, int rate, TemplateGenerator template,
			Mutator... mutators) {
		this.template = template;
		this.mutators = Arrays.asList(mutators);

		numTests = tests;
		testRate = rate;
	}

	/**
	 * Starts the test generator, printing to System.out
	 */
	public void start() {
		this.start(System.out);
	}

	/**
	 * Starts the test generator
	 */
	public void start(OutputStream stream) {
		for (int test = 0; test < numTests; test++) {
			WhileyFile file = doTest();
			print(file, stream);
		}
	}

	private WhileyFile doTest() {
		WhileyFile file = template.generate(); // src
		List<Mutation> mutations = new ArrayList<Mutation>();
		
		Random rand = new Random(System.currentTimeMillis());

		// Populate the list of mutations
		for (Mutator m : mutators) {
			mutations.addAll(m.generate(file));
		}

		// Perform the mutations for this test
		for (int i = 0; i < testRate; i++) {
			int index = rand.nextInt(mutations.size());
			
			mutations.get(index).apply();
			i++;
		}
		
		return file;
	}

	private void print(WhileyFile file, OutputStream stream) {
		WhileyFilePrinter printer = new WhileyFilePrinter(stream);
		printer.print(file);
	}

	public static void main(String[] args) {
		WhileyTestGenerator generator = new WhileyTestGenerator();
		generator.start();
	}
}
