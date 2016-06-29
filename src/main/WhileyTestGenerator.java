package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import wyc.io.WhileyFilePrinter;
import wyc.lang.WhileyFile;
import wycc.lang.SyntaxError;
import mutators.*;
import template.*;

/**
 * Iterates over a folder of source files, and applies mutations to them
 * 
 * @author Carl
 *
 */
public class WhileyTestGenerator {
	private Collection<Mutator> mutators;

	// private int testRate = 1; // number of mutations to perform per test

	/**
	 * Creates an instance of the WhileyTestGenerator. This generator takes a
	 * template generator to create
	 * 
	 * @param template
	 * @param mutator
	 * @param tests
	 * @param rate
	 */
	public WhileyTestGenerator(Mutator... mutators) {
		this.mutators = Arrays.asList(mutators);
	}

	public void iterate(String root, String target) {
		File file = new File(root);
		iterate(file, target);
	}

	/**
	 * Iterate over a sub directory, and generate tests
	 * 
	 * @param root
	 */
	public void iterate(File root, String target) {
		for (File f : root.listFiles()) {
			if (f.isDirectory()) {
				iterate(f, target);
				continue;
			}

			TemplateGenerator template = new FileTemplate(f.getAbsolutePath());
			List<WhileyFile> output = null;
			try {
				output = generateTests(template);
			} catch (SyntaxError e){
				System.err.println("Error parsing "+f.getName()+": "+e.getMessage());
				continue;
			}

			int index = 0;

			// Write all the files to the filesystem
			for (WhileyFile out : output) {
				FileOutputStream stream = null;
				try {
					//Create and write out the file stream
					stream = new FileOutputStream(target + "/" + index + "_" + f.getName());
					print(out, stream);
					
					index += 1;
				} catch (FileNotFoundException e) {
					System.err.println(e.getMessage());
				} catch (RuntimeException e) {
					System.err.println(e.getMessage());
				} finally {
					//Close the stream
					if (stream != null){
						try {
							stream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	/**
	 * Generate a list of test files
	 * 
	 * @return
	 */
	private List<WhileyFile> generateTests(TemplateGenerator template) {
		WhileyFile base = template.generate(); // src
		List<WhileyFile> outputs = new ArrayList<WhileyFile>();
		int mutations = generateMutation(base).size(); // Get the maximum number
														// of mutations

		for (int i = 0; i < mutations; i++) {
			// Sadly, we have to get a new instance of WhileyFile so that
			// we can apply each mutation in isolation.
			// Since mutations are applied to a specific file instance, we then
			// have to regenerate them as well.
			// TODO: Work out a better way of doing this
			WhileyFile file = template.copy();
			List<Mutation> muts = generateMutation(file);

			muts.get(i).apply();
			outputs.add(file);
		}

		return outputs;
	}

	/**
	 * Generates a list of mutations from a file
	 * 
	 * @param file
	 * @return
	 */
	private List<Mutation> generateMutation(WhileyFile file) {
		List<Mutation> mutations = new ArrayList<Mutation>();
		// Populate the list of mutations
		for (Mutator m : mutators) {
			mutations.addAll(m.generate(file));
		}

		return mutations;
	}

	private void print(WhileyFile file, OutputStream stream) {
		WhileyFilePrinter printer = new WhileyFilePrinter(stream);
		printer.print(file);
	}

	public static void main(String[] args) {
		WhileyTestGenerator generator = new WhileyTestGenerator(
				new SelfAssignmentMutator(),
				new AssignmentRemovalMutator());
		generator.iterate("tests", "output");
	}
}
