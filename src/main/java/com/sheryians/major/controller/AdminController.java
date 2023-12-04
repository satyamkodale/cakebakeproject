package com.sheryians.major.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sheryians.major.dto.ProductDTO;
import com.sheryians.major.model.Category;
import com.sheryians.major.model.Product;
import com.sheryians.major.service.CategoryService;
import com.sheryians.major.service.ProductService;

@Controller
public class AdminController {
	
	// category routes 
	

	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;
	
	//user.dir returns current working directory
	public static String uploadDir=System.getProperty("user.dir")+"/src/main/resources/static/productImages";
	
//	@RequestMapping("/")
//	public String start() 
//	{
//		return "index";
//	}
	@GetMapping("/admin")
	public String adminHome() 
	{
		return "adminHome";
	}
	@GetMapping("/admin/categories")
	public String getCategories(Model model) 
	{
		model.addAttribute("categories",categoryService.getAllCategory());
		return "categories";
	}
	@GetMapping("/admin/categories/add")
	public String getCategoriesAdd(Model model) 
	{
		model.addAttribute("category",new Category());
		return "categoriesAdd";
	}
	@PostMapping("/admin/categories/add")
	public String postCategoriesAdd(@ModelAttribute("category") Category category) 
	{
		categoryService.addCategory(category);
		return "redirect:/admin/categories";
	}
	
	//when you are working with view we have only two options Get and Post mapping 
	// get ,post ,put delete are for rest api
	@GetMapping("/admin/categories/delete/{id}")
	public String deleteCategory(@PathVariable int id)
	{
		categoryService.removeCategoryById(id);
		return "redirect:/admin/categories";
	}
	
	@GetMapping("/admin/categories/update/{id}")
	public String updateCategory(@PathVariable int id,Model model)
	{
		Optional<Category> category=categoryService.getCategoryById(id);
		if(category.isPresent()) 
		{
			model.addAttribute("category",category.get());
			return "categoriesAdd";
		}
		else 
		{
			return "404";
		}
	}
	
	
	// products  routes 
	@GetMapping("/admin/products")
	public String Products(Model model) 
	{
		model.addAttribute("products",productService.getAllProduct());
		return "products";
	}
	
	@GetMapping("/admin/products/add")
	public String productAddGet(Model model) 
	{
		model.addAttribute("productDTO",new ProductDTO());
		model.addAttribute("categories",categoryService.getAllCategory());
		return "productsAdd";
	}
	
	//request param is used to fetch value based of name of field 
	
	// MultipartFile is used to any type of file form frontend to backend
	//here we are using sepratly dto obj and @request param bcoz 
	//those who are dirctly we can store we are storing in productDTO
	
	
	@PostMapping("/admin/products/add")
	public String productAddPost(@ModelAttribute("productDTO") ProductDTO productDTO,
			@RequestParam("productImage") MultipartFile file,
			@RequestParam("imgName") String imgName)throws IOException 
	{
		//we are here saving the product via product dto objj 
		//so we need to convert product dto ---> product
		Product product = new Product();
		product.setId(productDTO.getId());
		product.setName(productDTO.getName());
		//here we need the category obj and productdto returns only id so 
		product.setCategory(categoryService.getCategoryById(productDTO.getCategoryId()).get());
		product.setPrice(productDTO.getPrice());
		product.setWeight(productDTO.getWeight());
		product.setDescription(productDTO.getDescription());
		
		//here we are only storing the name of img in database and actual img is stored in static folder
		//but storing img name name is directly is not a good 
		//option so we are storing "uuid" instead of storing name to db
		//128 bit num used for tokens
		String imageUUID;
		// img input is not complusary
		//so 
		//if file is not empty
		if(!file.isEmpty()) 
		{
			imageUUID=file.getOriginalFilename();
			//we have to store img in  static / productImages
			
			// to get path we are using Path.nio
			
			Path fileNameAndPath = Paths.get(uploadDir,imageUUID);
			// saving file to specified path
			Files.write(fileNameAndPath,file.getBytes());
		
		}
		else 
		{
			imageUUID=imgName;
		}
		
		product.setImageName(imageUUID);
		productService.addProduct(product);
		
		return "redirect:/admin/products";
	} 
	
	
	@GetMapping("/admin/product/delete/{id}")
	public String deleteProduct(@PathVariable long id)
	{
		productService.removeProductById(id);
		return "redirect:/admin/products";
	}
	
	@GetMapping("/admin/product/update/{id}")
	public String updateProductGet(@PathVariable long id,Model model) 
	{
		Product product = productService.getProductById(id).get();
		
		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(product.getId());
		productDTO.setName(product.getName());
		productDTO.setCategoryId(product.getCategory().getId());
		productDTO.setPrice(product.getPrice());
		productDTO.setWeight(product.getWeight());
		productDTO.setDescription(product.getDescription());
		productDTO.setImageName(product.getImageName());
		
		model.addAttribute("categories",categoryService.getAllCategory());
		model.addAttribute("productDTO",productDTO);
		
		return "productsAdd";
	}
	
	
	
}
