package Main;

import java.util.Vector;

/**
 * Pretty formatting
 * 
 * @author tomkit
 *
 */
public class Display {

	public void toConsole(ResultsPage resultsPage)
	{
		if (resultsPage == null)
		{
			System.out.println("No results from query.");
		}
		else if (resultsPage.getPageNum() == 0)
		{
			System.out.println("======================");
			System.out.println("Query: " + resultsPage.getQuery());
			System.out.println("Total Results: " + resultsPage.getTotalResults());
			System.out.println("======================");
		}
		else 
		{
			System.out.println("======================");
			System.out.println("Query: " + resultsPage.getQuery());
			System.out.println("Total Results: " + resultsPage.getTotalResults());
			
			for (int i = 0; i < resultsPage.getResultsOnPage(); i++)
			{
				System.out.println("----------------------");
				System.out.println("Product Name: " + resultsPage.getProductName(i));
				System.out.println("Description: " + resultsPage.getDescription(i));
				
				Vector<String> vendors = resultsPage.getVendors(i);
				for (String vendor : vendors)
				{
					System.out.println("Vendor: " + vendor);
				}
			}
			System.out.println("======================");
		}
	}
}
