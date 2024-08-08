package com.example.webclient.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.example.webclient.entity.Customer;

@Controller
@RequestMapping("/web/customers")
public class CustomerWebController {
	
	@Autowired
	RestTemplate restTemplate;
	
	@GetMapping("")
	public String getCustomers(Model model) throws URISyntaxException {
		URI uri = new URI("http://localhost:8080/customers");
		Customer[] customers = restTemplate.getForObject(uri, Customer[].class);
		model.addAttribute("customers", customers);
		return "customers";
	}
	
	@GetMapping("/create")
	public String getCreateCustomerForm(Model model) {
		Customer cust = new Customer();
		model.addAttribute("customer", cust);
		return "customer-form";
	}
	
	@PostMapping("")
	public String createCustomer(@ModelAttribute Customer customer) throws URISyntaxException {
		URI uri = new URI("http://localhost:8080/customers");
		restTemplate.postForObject(uri, customer, Customer.class);
		return "redirect:/web/customers";
	}
	
	@GetMapping("/update/{id}")
	public String editCustomerForm(@PathVariable long id, Model model) throws URISyntaxException {
		URI uri = new URI("http://localhost:8080/customers/" + id);
		Customer customer = restTemplate.getForObject(uri, Customer.class);
		model.addAttribute("customer", customer);
		return "customer-form";
	}
	
	@PostMapping("/update/{id}")
	public String editCustomer(@PathVariable long id, @ModelAttribute Customer customer) throws URISyntaxException {
		URI uri = new URI("http://localhost:8080/customers/" + id);
		restTemplate.put(uri, customer);
		return "redirect:/web/customers";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteCustomer(@PathVariable long id) throws URISyntaxException {
		URI uri = new URI("http://localhost:8080/customers/" + id);
		restTemplate.delete(uri);
		return "redirect:/web/customers";
	}
	
	@GetMapping("/purchase/{id}")
	public String purchase(@PathVariable long id, Model model) throws URISyntaxException {
		URI uri = new URI("http://localhost:8080/customers/" + id);
		Customer customer = restTemplate.getForObject(uri, Customer.class);
		model.addAttribute("customer", customer);
		model.addAttribute("totalSales", 0.0);
		return "purchase-form";
	}
	
	@PostMapping("/purchase")
	public String postPurchase(@RequestParam("totalSales") double purchase, @RequestParam("action") String action, @ModelAttribute Customer customer) throws URISyntaxException {
		URI uri;
		if ("purchase".equals(action)) {
			uri = new URI("http://localhost:8080/customers/purchase/" + customer.getId());
			uri = new URI(uri.toString() + "?purchase=" + purchase);
		} else {
			uri = new URI("http://localhost:8080/customers/purchase/credit/" + customer.getId());
			uri = new URI(uri.toString() + "?purchase=" + purchase);
		}
		//System.out.println("URI:" + uri.toString());
		//System.out.println("totalSales:" + purchase);
		restTemplate.put(uri, purchase);
		return "redirect:/web/customers";
	}
	
	@GetMapping("/payment/{id}")
	public String payment(@PathVariable long id, Model model) throws URISyntaxException {
		URI uri = new URI("http://localhost:8080/customers/" + id);
		Customer customer = restTemplate.getForObject(uri, Customer.class);
		model.addAttribute("customer", customer);
		return "payment-form";
	}
	
	@PostMapping("/payment")
	public String postPayment(@RequestParam("payment") double payment, @ModelAttribute Customer customer) throws URISyntaxException {
		URI uri;
		uri = new URI("http://localhost:8080/customers/payment/" + customer.getId());
		uri = new URI(uri.toString() + "?payment=" + payment);
		//System.out.println("URI:" + uri.toString());
		//System.out.println("totalSales:" + purchase);
		restTemplate.put(uri, null);
		return "redirect:/web/customers";
	}
	
}
