package com.example.demo.service;


import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;


@Service
public class CustomerService {
	
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private CustomerRepository customerRepository;

	public Optional<Customer> getCustomerByID(String id) {
		log.trace("Metodo Service getUserID");
		return customerRepository.getCustomer(id);
	}

	public Optional<Customer> createCustomer(Customer data) {
		log.trace("Metodo service createCustomer");
		customerRepository.save(data);
		return Optional.of(data);
	}

	
	public boolean deleteCustomer(String id) {

		log.trace("Metodo Service deleteCustomer");
		//User userDelete=customerRepository.getUSer(dni);
		if (!customerRepository.getCustomer(id).isPresent()) {
			return false;
		}
		customerRepository.deleteCustomer(id);
		return true;
	}

	public List<Customer> getListCustomers() {
		log.trace("Metodo Service getListCustomers");
		return customerRepository.getListCustomer();
	}
}
