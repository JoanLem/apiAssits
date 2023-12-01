package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import com.grandapp.response.GeneralResponse;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.springframework.http.HttpStatus;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1")
public class CustomerController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private CustomerService customerService;

	@RequestMapping(path = "/customer", method = RequestMethod.POST)
	private ResponseEntity<GeneralResponse<Customer>> save(@RequestBody Customer data) throws Exception {
		GeneralResponse<Customer> response = new GeneralResponse<>();
		HttpStatus status = null;
		try {
			log.trace("Iniciando saveCustomer() con {}", data);
			Customer cliente = customerService.createCustomer(data).orElse(null);
			response.setMessage("Ejecucion Exitosa!");
			response.setSuccess(true);
			response.setData(cliente);
			status = HttpStatus.OK;
			log.info("saveCustomer() Ejecutado con Éxito " + cliente);
			
		} catch (Exception e) {
			
			response.setMessage(e.getMessage());
			response.setSuccess(false);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error("error generado " + e.getMessage());
		}
		return new ResponseEntity<>(response, status);
	}

	@RequestMapping(path = "/customer/{id}", method = RequestMethod.GET)
	public ResponseEntity<Customer> getCustomer(@PathVariable("id") String id) {
		return customerService.getCustomerByID(id).map(newCustomerData -> new ResponseEntity<>(newCustomerData, OK))
				.orElse(new ResponseEntity<>(CONFLICT));
	}

	@RequestMapping(path = "/customer/", method = RequestMethod.DELETE)
	public  ResponseEntity<Void> deleteCustomer(@RequestBody String id) {		
		return customerService.deleteCustomer(id)?new ResponseEntity<>(HttpStatus.OK):new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(path = "/customer", method = RequestMethod.GET)
	public ResponseEntity<List<Customer>> listCustomers(){
		List<Customer> lstCustomer=customerService.getListCustomers();
		if(lstCustomer.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(lstCustomer,OK);
	}


}
