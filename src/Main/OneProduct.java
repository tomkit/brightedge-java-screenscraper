package Main;

import java.util.Vector;

/**
 * OO class for one specific product.
 * Simple getter/setter class
 * 
 * @author tomkit
 *
 */
public class OneProduct {
	private String productName;
	private String description;
	private Vector<String> sellers;
	
	OneProduct(String productName, String description, Vector<String> sellers)
	{
		this.productName = productName;
		this.description = description;
		this.sellers = sellers;
	}
	
	public String getProductName()
	{
		return this.productName;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public Vector<String> getVendors()
	{
		return this.sellers;
	}
}
