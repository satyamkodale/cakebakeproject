package com.sheryians.major.global;

// data which is accesible to all over web site 
// here we need cart all over web site
import java.util.*;

import com.sheryians.major.model.Product;

public class GlobalData {
	
	public static List<Product> cart;
	
	static 
	{
		cart = new ArrayList<Product>();
	}

}
