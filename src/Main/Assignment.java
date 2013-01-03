package Main;

/**
 * Static class entry point for app.
 * 
 * KNOWN ISSUES annotated with TODO in code. Major issues listed here:
 * 1. I didn't get different page selection working: need to figure out how to do posts to forms in Java
 * 2. Only grabs the first vendor (something incorrect in regex - also see note about regex in comments)
 *  
 * 
 * @author tomkit
 *
 */
public class Assignment {

	/**
	 * Entry point to run this application from commandline or JUnit
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length < 1) 
		{
			System.out.println("Not enough arguments.");
		}
		else if (args.length > 2)
		{
			System.out.println("Too many arguments.");
		}
		else if (args.length == 1)
		{
			String query = args[0];
			
			ScreenScraper scraper = new ScreenScraper();
			scraper.getResults(query, 0);

		}
		else if (args.length == 2)
		{
			String query = args[0];
			int page = 1;
			
			if (args.length == 2) 
			{
				try 
				{
					page = Integer.valueOf(args[1]);
				}
				catch (NumberFormatException nfe) 
				{
					System.err.println("Bad second argument: not a number.");
					System.exit(0);
				}
			}
			
			ScreenScraper scraper = new ScreenScraper();
			scraper.getResults(query, page);

		}
	}
}
