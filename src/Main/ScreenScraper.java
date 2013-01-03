package Main;

/**
 * 
 * 
 * @author tomkit
 *
 */
public class ScreenScraper {
	
	Parser parser;
	Display display;

	ScreenScraper() 
	{
		parser = new Parser(); 
		display = new Display();
	}
	
	public void getResults(String query, int page)
	{
		ResultsPage resultsPage = parser.getPage(query, page);
		display.toConsole(resultsPage);
	}
}
