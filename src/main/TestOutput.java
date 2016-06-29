package main;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import wyc.WycMain;
import wyjc.WyjcMain;
import wyjc.util.WyjcBuildTask;

@RunWith(value = Parameterized.class)
public class TestOutput {
	String parameter;
	
	public TestOutput(String name, String dir){
		this.parameter = dir;
	}

	@Parameters ( name = "{0}" )
	/**
	 * Gets the JUnit parameters by reading the contents of the output folder
	 * @return
	 */
	public static Collection<String[]> data(){
		List<String[]> rtn = new ArrayList<String[]>();
		
		File folder = new File("output");
		
		//Populate the list of files
		for (File f : folder.listFiles(/*filter*/)){
			rtn.add(new String[]{f.getName(), f.getAbsolutePath()});
		}
		
		return rtn;
	}
	
	@Test
	public void compile() throws IOException {
		String [] args = {
				"-wd", "output",
				parameter
			};
		
		int r = new WyjcMain(new WyjcBuildTask(), WyjcMain.DEFAULT_OPTIONS).run(args);
		
		if (r != WycMain.SYNTAX_ERROR){
			fail("Syntax error expected");
		}
	}

}
