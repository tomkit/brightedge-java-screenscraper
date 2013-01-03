package Main;

import java.io.*;
import java.net.*;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains most of the content fetching and parsing logic
 * 
 * @author tomkit
 *
 */
public class Parser {
	
	private String urlString = "http://www.nextag.com";
	
	/**
	 * 
	 */
	public Parser() 
	{
		
	}
	
	/**
	 * Parsing logic to grab the information we want from the page
	 * 
	 * @param query
	 * @param page
	 * @return ResultsPage
	 */
	public ResultsPage getPage(String query, int page)
	{
		boolean totalResultsOnly = false;
		if(page <= 0)
		{
			totalResultsOnly = true;
		}
		
		int totalResults = getTotalResults(query);
		
		if (totalResultsOnly)
		{
			return new ResultsPage(totalResults, query, null, 0);
		}
		else
		{
			// get the actual content via our helpers
			URL queryURL = createQueryURL(query, page);
			String content = getURLContent(queryURL);
			
			// parsing logic in regex
			// NOTE: we shouldn't be using regex to parse HTML 
			// but see note below (I couldn't get HTML->XML and then use
			// xpath/xquery to work because the site had too many HTML errors)
			String regex = "<div[^c]*class=\"sr-info\"[^>]*>" + 						// key off of "sr-info" div class	
						   "[^>]+>(.*?)</a>" +											// to grab product name
						   ".*?<div[^c]*class=\"sr-info-description\"[^>]*>" +			// key off of "sr-info-description" div class
						   "(.*?)</div>" +												// to grab product description
						   ".*?<div[^c]*class=\"featuredSeller\"[^>]*>" +				// key off of "featuredSeller" div class
						   "(?:[^<]*<a[^c]*class=\"featuredSeller\"[^>]*>(.*?)</a>)*";	// to grab vendor name. TODO: We try to grab mor than 1 vendor here, but it's not working properly and only returning 1
			
			// compile and match the regex with the content
			Pattern pat = Pattern.compile(regex, Pattern.DOTALL | Pattern.UNIX_LINES);
			Matcher m = pat.matcher(content);
			
			int count = 0;
			ResultsPage resultsPage = null;
			Vector<OneProduct> products = new Vector<OneProduct>();
			while (m.find())
			{
				count++;
				String productName = sanitizeString(m.group(1));
				String description = sanitizeString(m.group(2));
				Vector<String> sellers = new Vector<String>();
				
				for (int i = 3; i <= m.groupCount(); i++)
				{
					sellers.add(sanitizeString(m.group(i)));
				}
	
				products.add(new OneProduct(productName, description, sellers));
				
			}
			resultsPage = new ResultsPage(totalResults, query, products, page);
			
			//System.out.println(count);
			return resultsPage;
		}
	}
	
	/**
	 * Helper function to grab the total number of results form a page
	 * 
	 * @param query
	 * @return int
	 */
	private int getTotalResults(String query)
	{
		// get the actual content via our helpers
		URL queryURL = createQueryURL(query, 1);
		String content = getURLContent(queryURL);
		
		// parsing logic in regex
		String regex = "<meta[^n]*name=\"DESCRIPTION\"[^c]*content=\"[^0-9]*([0-9,]+)\\s+";
		
		// compile and match the regex with the content
		Pattern pat = Pattern.compile(regex, Pattern.DOTALL | Pattern.UNIX_LINES);
		Matcher m = pat.matcher(content);
		
		int totalResults = 0;
		while (m.find())
		{
			totalResults = Integer.valueOf(m.group(1).replaceAll("[,]", ""));
		}
		
		return totalResults;
	}
	
	/**
	 * Removes empirically observed HTML tags in the string.
	 * We can generalize this down the road.
	 * Also removes spaces greater than 1
	 * 
	 * @param input
	 * @return String
	 */
	protected String sanitizeString(String input)
	{
		String output = input.replaceAll("[\n]", "")
							 .replaceAll("[\\s]{2,}", "\\s")
							 .replaceAll("<b>", "")
							 .replaceAll("</b>", "");
		
		return output;
	}
	
	/**
	 * Helper function to manipulate the given URL in the constructor to work
	 * for the given query and page number
	 * 
	 * @param query
	 * @param page
	 * @return URL
	 */
	private URL createQueryURL(String query, int page)
	{
		URL url = null;
		
		if (query.equals("") || query == null)
		{
			try 
			{
				url = new URL(query);
			}
			catch (MalformedURLException mue)
			{
				System.err.println("Can't convert query to URL: " + query);
			}
			
			return url;
		}
		
		// TODO: add additional sanity checks to the string: query
		query = query.replaceAll("[\\s]+", "-");
		
		// http://www.nextag.com has some kind of RESTful approach for search queries page 1
		if (page == 1)
		{
			
			String newUrlString = this.urlString + "/" + query + "/search-html";

			try
			{
				url = new URL(newUrlString);
			}
			catch (MalformedURLException mue)
			{
				System.err.println("Couldn't create the query URL: " + newUrlString);
			}
		}
		else if (page > 1)
		{
			// TODO: figure out how to post html forms in Java. 
			// when going to second page, the RESTful URL seems to be
			// some kind of hash based on the query term
		}
		
		return url;
	}
	
	/**
	 * Helper function to grab content from a given URL
	 * 
	 * @param url
	 * @return String
	 */
	private String getURLContent(URL url)
	{
		URLConnection con = null;	
		
		// try to get a connection
		try
		{
			con = url.openConnection();
		}
		catch (IOException ioe)
		{
			System.err.println("Could not open connect to URL: " + url);
		}
		
		// try to get the page's encoding
		String encoding = con.getContentEncoding();		
		if (encoding == null)
		{
			encoding = "UTF-8";
		}
		
		// try to read the page into a reader
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new InputStreamReader(con.getInputStream(), encoding));
		}
		catch (UnsupportedEncodingException uee)
		{
			System.err.println("Bad encoding when trying to read from URL: " + this.urlString);
		}
		catch (IOException ioe)
		{
			System.err.println("Could not read from URL: " + this.urlString);
		}
		
		// put the content from the reader into a string
		StringBuilder sb = new StringBuilder(20000); // empirically this a bit larger than what is on a results page to be safer
		try
		{
			String line;
			while ((line = br.readLine()) != null)
			{
				sb.append(line);
				sb.append('\n');
			}
		}
		catch (IOException ioe)
		{
			System.err.println("Could not read a line from URL: " + this.urlString);
		}
		finally
		{
			// cleanup
			try
			{
				br.close();
			}
			catch (IOException ioe)
			{
				System.err.println("Couldn't clenaup buffered read.");
			}
		}
		
		return sb.toString();

	}
	
	// NOTE: attempted to use a HTML->XML sanitizer but the website's HTML had too many errors
	/*
	public ResultsPage getPage(String query, int page)
	{
		XPath xpath = XPathFactory.newInstance().newXPath();
		String totalResultsExpr = getTotalResultsExpr();
		Reader webPageReader = getURLContent();
		//String webPage = getURLContent();
		//InputSource inputSource = new InputSource(webPageReader);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(300000);
		
		FileInputStream fileStream = null;
		try
		{
			fileStream = new FileInputStream(new File("src/Main/test2.xml"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		// Convert HTML to XML
		Tidy tidy = new Tidy();
		tidy.setXHTML(true);
		tidy.parse(fileStream, outputStream);
		//Document tidyDoc = tidy.parseDOM(webPageReader, null);
		ByteArrayInputStream tidyIn = new ByteArrayInputStream(outputStream.toByteArray());
		
//		// try to make output stream --> input stream more efficient later
//		
//		//PipedInputStream in = new PipedInputStream();
//		new Thread(
//				new Runnable() {
//					public void run() {
//						class1
//					}
//				}
		
		
		DocumentBuilder builder = null;
		Document document = null;
		try
		{
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			//builder.setEntityResolver(null);
			document = builder.parse(tidyIn);
		}
		catch (ParserConfigurationException pce)
		{
			System.err.println("Couldn't get instance of DocumentBuilder.");
		}
		catch (IOException ioe)
		{
			System.err.println("Couldn't parse inputSource and create document.");
			ioe.printStackTrace();
		}
		catch (SAXException se)
		{
			se.printStackTrace();
		}
		
		
		//InputSource inputSource = new InputSource("src/Main/test.xml");
		
		try
		{
			xpath.evaluate(totalResultsExpr, document, XPathConstants.NODE);
		}
		catch (XPathExpressionException xee)
		{
			System.err.println("Bad XPath expression: " + totalResultsExpr);
			xee.printStackTrace();
		}
		
		
		// cleanup
		try
		{
			webPageReader.close();
			outputStream.close();
		}
		catch (IOException ioe)
		{
			System.err.println("Couldn't close streams.");
		}
		
		
		return new ResultsPage();
	}
	*/

}
