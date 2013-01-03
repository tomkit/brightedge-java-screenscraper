package Main;

import java.util.Vector;

/**
 * OO class for a page.
 * Getter/setters.
 * 
 * @author tomkit
 *
 */
public class ResultsPage {

	private int totalResults;
	private String query;
	private Vector<OneProduct> products;
	private int pageNum;
	private int resultsOnPage;
	
	ResultsPage()
	{
		
	}
	
	ResultsPage(int totalResults,
				String query,
				Vector<OneProduct> products,
			    int pageNum)
	{
		this.totalResults = totalResults;
		this.query = query;
		this.products = products;
		this.pageNum = pageNum;
		
		if (products == null)
		{
			this.resultsOnPage = 0;
		}
		else
		{
			this.resultsOnPage = products.size();
		}
		
	}
	
	public int getTotalResults()
	{
		return this.totalResults;
	}
	
	public String getQuery()
	{
		return this.query;
	}
	
	public String getProductName(int index)
	{
		return this.products.get(index).getProductName();
	}
	
	public String getDescription(int index)
	{
		return this.products.get(index).getDescription();
	}
	
	public Vector<String> getVendors(int index)
	{
		return this.products.get(index).getVendors();
	}
	
	public int getPageNum()
	{
		return this.pageNum;
	}
	
	public int getResultsOnPage()
	{
		return this.resultsOnPage;
	}
}
