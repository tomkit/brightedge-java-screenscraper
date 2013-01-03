package MainTest;

import Main.*;
import junit.framework.TestCase;

public class AssignmentTest extends TestCase {
	
	public void testArguments_3()
	{
		String[] args = {"digital", "camera", "1"};
		Assignment.main(args);
	}
	
	public void testArguments_2()
	{
		String[] args = {"digital camera", "1"};
		Assignment.main(args);
	}
	
	public void testArguments_1()
	{
		String[] args = {"digital camera"};
		Assignment.main(args);
	}
	
	public void testArguments_0()
	{
		String[] args = {};
		Assignment.main(args);
	}
}
