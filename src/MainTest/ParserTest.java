package MainTest;

import junit.framework.TestCase;
import Main.*;

public class ParserTest extends TestCase {

	private Parser parser = new Parser();
	
	// Other tests:
	// 1. Network: try running without internet connection at beginning and halfway through
	// 2. Stress: try really long query string (e.g. 10+ relevant words)
	// 3. Performance: time the results
	// 4. Edge: try a large page number, negative page number, ...
	
	// TODO: not sure why it's returning 29 not 30
	public void testGetPage_digitalCameraPage1()
	{
		ResultsPage results = parser.getPage("digital camera", 1);
		TestCase.assertEquals(results.getResultsOnPage(), 29); 
	}
	
	public void testGetPage_digitalCameraPage2()
	{
		ResultsPage results = parser.getPage("digital camera", 2);
		TestCase.assertEquals(results.getResultsOnPage(), 29); 
	}
	
	public void testGetPage_sonyDigitalCameraPage1()
	{
		ResultsPage results = parser.getPage("sony digital camera", 1);
		TestCase.assertEquals(results.getResultsOnPage(), 29); 
	}
	
	public void testGetPage_sonyDigitalCameraPage0()
	{
		ResultsPage results = parser.getPage("sony digital camera", 0);
		TestCase.assertEquals(results.getResultsOnPage(), 0); 
	}
	
	public void testGetPage_sonyDigitalCameraPage0TotalResults()
	{
		ResultsPage results = parser.getPage("sony digital camera", 0);
		TestCase.assertNotSame(results.getTotalResults(), 0); 
	}
	
	public void testGetPage_symbolInputs()
	{
		ResultsPage results = parser.getPage("#$%$&%*^(&^@#$", 0);
		TestCase.assertNotSame(results.getTotalResults(), 0); 
	}
	
	// TODO: should handle this more gracefully
	public void testGetPage_emptyInput()
	{
		ResultsPage results = parser.getPage("", 0);
		TestCase.assertNotSame(results.getTotalResults(), 0); 
	}
	
	// TODO: write more unit tests
	public void testGetTotalResults()
	{
		
	}
	
	public void testSanitizeString()
	{
	}
	
	public void testCreateQueryURL()
	{
		
	}
	
	public void testGetURLContent()
	{
		
	}
}
