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
	
	
}
