package com.example.webclient.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/web/customers")
public class CustomerWebController {
	
	@Autowired
	RestTemplate restTemplate;
	
	@GetMapping("/login")
	public String getLogin(Model model) {
		return "login";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
		session.setAttribute("username", username);
		session.setAttribute("password", password);
		return "redirect:/web/customers";
	}
	
	@GetMapping("")
	public String getCustomers(Model model, HttpSession session) throws URISyntaxException {
		String username = (String) session.getAttribute("username");
		String password = (String) session.getAttribute("password");
		if (username != null && password != null) {
			String auth = username + ":" + password;
			String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
			String authHeader = "Basic " + encodedAuth;
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", authHeader);
			HttpEntity<String> request = new HttpEntity<>(headers);
			URI uri = new URI("http://localhost:8080/customers");
			ResponseEntity<Customer[]> response = restTemplate.exchange(uri.toString(), HttpMethod.GET, request, Customer[].class);
			model.addAttribute("customers", response.getBody());
			return "customers";
		} else {
			return "redirect:/web/customers/login";
		}
	}
	
	@GetMapping("/create")
	public String getCreateCustomerForm(Model model) {
		Customer cust = new Customer();
		model.addAttribute("customer", cust);
		return "customer-form";
	}
	
	@PostMapping("")
	public String createCustomer(@ModelAttribute Customer customer, HttpSession session) throws URISyntaxException {
		customer.setUsername(customer.getName().toLowerCase());
		//customer.setPassword("password");
		URI uri = new URI("http://localhost:8080/customers");
		restTemplate.postForObject(uri, customer, Customer.class);
		return "redirect:/web/customers/login";
	}
	
	@GetMapping("/update/{id}")
	public String editCustomerForm(@PathVariable long id, Model model, HttpSession session) throws URISyntaxException {
		String username = (String) session.getAttribute("username");
		String password = (String) session.getAttribute("password");
		if (username != null && password != null) {
			String auth = username + ":" + password;
			String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
			String authHeader = "Basic " + encodedAuth;
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", authHeader);
			HttpEntity<String> request = new HttpEntity<>(headers);
			URI uri = new URI("http://localhost:8080/customers/" + id);
			ResponseEntity<Customer> resp = restTemplate.exchange(uri.toString(), HttpMethod.GET, request, Customer.class);
			model.addAttribute("customer", resp.getBody());
			return "customer-form";
		}
		else {
			return "redirect:/web/customers/login";
		}
	}
	
	@PostMapping("/update/{id}")
	public String editCustomer(@PathVariable long id, @ModelAttribute Customer customer, HttpSession session) throws URISyntaxException {
		String username = (String) session.getAttribute("username");
		String password = (String) session.getAttribute("password");
		if (username != null && password != null) {
			String auth = username + ":" + password;
			String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
			String authHeader = "Basic " + encodedAuth;
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", authHeader);
			HttpEntity<Customer> request = new HttpEntity<>(customer, headers);
			URI uri = new URI("http://localhost:8080/customers/" + id);
			System.out.println("uri:" + uri.toString());
			restTemplate.exchange(uri.toString(), HttpMethod.PUT, request, Customer.class);
			return "redirect:/web/customers/login";
		}
		else {
			return "redirect:/web/customers/login";
		}
	}
	
	@GetMapping("/delete/{id}")
	public String deleteCustomer(@PathVariable long id, HttpSession session) throws URISyntaxException {
		String username = (String) session.getAttribute("username");
		String password = (String) session.getAttribute("password");
		if (username != null && password != null) {
			String auth = username + ":" + password;
			String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
			String authHeader = "Basic " + encodedAuth;
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", authHeader);
			HttpEntity<String> request = new HttpEntity<>(headers);
			URI uri = new URI("http://localhost:8080/customers/" + id);
			restTemplate.exchange(uri.toString(), HttpMethod.DELETE, request, Object.class);
			return "redirect:/web/customers";
		}
		else {
			return "redirect:/web/customers/login";
		}
		
	}
	
	@GetMapping("/purchase/{id}")
	public String purchase(@PathVariable long id, Model model, HttpSession session) throws URISyntaxException {
		String username = (String) session.getAttribute("username");
		String password = (String) session.getAttribute("password");
		if (username != null && password != null) {
			String auth = username + ":" + password;
			String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
			String authHeader = "Basic " + encodedAuth;
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", authHeader);
			HttpEntity<String> request = new HttpEntity<>(headers);
			URI uri = new URI("http://localhost:8080/customers/" + id);
			ResponseEntity<Customer> customer = restTemplate.exchange(uri.toString(), HttpMethod.GET, request, Customer.class);
			model.addAttribute("customer", customer.getBody());
			model.addAttribute("totalSales", 0.0);
			return "purchase-form";
		} else {
			return "redirect:/web/customers/login";
		}
	}
	
	@PostMapping("/purchase")
	public String postPurchase(@RequestParam("totalSales") double purchase, @RequestParam("action") String action, @ModelAttribute Customer customer, HttpSession session) throws URISyntaxException {
		String username = (String) session.getAttribute("username");
		String password = (String) session.getAttribute("password");
		if (username != null && password != null) {
			String auth = username + ":" + password;
			String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
			String authHeader = "Basic " + encodedAuth;
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", authHeader);
			HttpEntity<String> request = new HttpEntity<>(headers);
			URI uri;
			if ("purchase".equals(action)) {
				uri = new URI("http://localhost:8080/customers/purchase/" + customer.getId());
				uri = new URI(uri.toString() + "?purchase=" + purchase);
			} else {
				uri = new URI("http://localhost:8080/customers/purchase/credit/" + customer.getId());
				uri = new URI(uri.toString() + "?purchase=" + purchase);
			}
			restTemplate.exchange(uri.toString(), HttpMethod.PUT, request, Customer.class);
			return "redirect:/web/customers";
		}
		else {
			return "redirect:/web/customers/login";
		}
	}
	
	@GetMapping("/payment/{id}")
	public String payment(@PathVariable long id, Model model, HttpSession session) throws URISyntaxException {
		String username = (String) session.getAttribute("username");
		String password = (String) session.getAttribute("password");
		if (username != null && password != null) {
			String auth = username + ":" + password;
			String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
			String authHeader = "Basic " + encodedAuth;
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", authHeader);
			HttpEntity<String> request = new HttpEntity<>(headers);
			URI uri = new URI("http://localhost:8080/customers/" + id);
			ResponseEntity<Customer> resp = restTemplate.exchange(uri.toString(), HttpMethod.GET, request, Customer.class);
			model.addAttribute("customer", resp.getBody());
			return "payment-form";
		}
		else {
			return "redirect:/web/customers/login";
		}
	}
	
	@PostMapping("/payment")
	public String postPayment(@RequestParam("payment") double payment, @ModelAttribute Customer customer, HttpSession session) throws URISyntaxException {
		String username = (String) session.getAttribute("username");
		String password = (String) session.getAttribute("password");
		if (username != null && password != null) {
			String auth = username + ":" + password;
			String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
			String authHeader = "Basic " + encodedAuth;
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", authHeader);
			HttpEntity<String> request = new HttpEntity<>(headers);
			URI uri;
			uri = new URI("http://localhost:8080/customers/payment/" + customer.getId());
			uri = new URI(uri.toString() + "?payment=" + payment);
			restTemplate.exchange(uri.toString(), HttpMethod.PUT, request, Customer.class);
		//System.out.println("URI:" + uri.toString());
		//System.out.println("totalSales:" + purchase);
			return "redirect:/web/customers";
		} else {
			return "redirect:/web/customers/login";
		}
	}
	
}
