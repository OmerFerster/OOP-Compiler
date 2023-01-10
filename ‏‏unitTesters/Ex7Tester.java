import java.io.*;
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;
import oop.ex6.main.Sjavac;

@RunWith(Parameterized.class)
public class Ex7Tester {
	/**
	 * The location of the test files
	 * TODO change to oop home dir
	 */
	private static final String LOCAL_DIR = System.getProperty("user.dir");
	private static final String TEST_INPUT_DIR = LOCAL_DIR.concat("/../tests/"); 
            //"/cs/course/current/oop/scripts/ex6/tests/"; 
			//"/cs/course/current/oop/Grading/ex6/tests/";	
			//"/home/noa/oop_ex6/local_temp/tests/";

    private static final String TEST_SOL_DIR = LOCAL_DIR.concat("/../presubmission_sjavac_tests.txt");
	//private static final String TEST_SOL_DIR = "/cs/course/current/oop/Grading/ex6/all_sjavac_tests.txt";
    //private static final String TEST_SOL_DIR = "/cs/course/current/oop/scripts/ex6/sjavac_tests.txt";

	
	private String retVal;
	private String numOfTest;
	private String desc;
	/**
	 * A constructor that receives a test file input and reads its lines.
	 * @param fileName
	 * @throws FileNotFoundException 
	 */
	public Ex7Tester(String fileName) throws FileNotFoundException {
		
		String[] parts = fileName.split(" ");
		numOfTest = parts[0].substring(4,7);
		desc = parts[2];
		retVal = parts[1];
	}

	/**
	 * Read test files from test directory
	 * @return a collection of arrays containing a single string - the current test file
	 * @throws IOException 
	 */
	@SuppressWarnings("resource")
	@Parameterized.Parameters
	public static Collection<String[]> readTests() throws IOException  {
		List<String[]> tests = new LinkedList<>();
		
		FileReader fileReader = null;
		BufferedReader br = null;
		
		try {
			fileReader = new FileReader(new File(TEST_SOL_DIR));
			br = new BufferedReader(fileReader);			
		} catch (IOException e) {
				fail("Problem reading "+TEST_SOL_DIR);
		}
		 
		 String line = null;
		 // if no more lines the readLine() returns null
		 while ((line = br.readLine()) != null) {
			 if (!line.equals(""))
				 tests.add(new String[] {line.toString()});
		 }
		return tests;
	}

	/**
	 * Run a set of tests on the main class of MyFileScript with different args input
	 * @param 
	 * @throws IOException 
	 */
	@Test
	public void runTests() throws IOException {
		         
		PrintStream default_out = System.out;

		File file = new File("tmp.txt"); 
		FileOutputStream fos = new FileOutputStream(file);
		PrintStream ps = new PrintStream(fos);
		
		//catch the errors of school solution - should not print them.
		File file1 = new File("tmp1.txt"); 
		FileOutputStream fos1 = new FileOutputStream(file1);
		PrintStream ps1 = new PrintStream(fos1);
	
		String str = new String(TEST_INPUT_DIR+"test"+numOfTest+".sjava");
		String[] args = {str};

		System.out.println("Testing " + numOfTest);

		System.setOut(ps);	
		System.setErr(ps1);	
			
		
		//call to Sjavac
		try{
			Sjavac.main(args);
		}
		catch(Exception e){
			System.setOut(default_out);	
			fail("You don't catch an exception in test number " + numOfTest);
			
		}
	
		ps.close();
		ps1.close();
		
		System.setOut(default_out);	
		
		compareResults();
	}

	private void compareResults() throws IOException {
		
        File student = new File("tmp.txt");// Student output
        FileReader fR_student = new FileReader(student);
        BufferedReader reader1 = new BufferedReader(fR_student);

        String line1 = null;       
    	line1 = reader1.readLine();
    	if((line1 == null)) {
            fail("Please print your result to System.out");
    	}
		else{
			assertEquals(desc + " problem in test number:"+numOfTest,retVal,line1);
		}

			reader1.close();
		}

}
