package com.example.demo.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.example.demo.model.Customer;


@Repository
public class CustomerRepository {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	public Customer save(Customer customer) {
		log.trace("Metodo SaveCustomer");
		dynamoDBMapper.save(customer);
		return customer;
	}

	public Optional<Customer> getCustomer(String id) {
		log.trace("Metodo getCustomer");
		Customer customer = null;
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":id", new AttributeValue().withS(id));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("id_customer = :id")
				.withExpressionAttributeValues(eav);
		List<Customer> Customeresult = dynamoDBMapper.scan(Customer.class, scanExpression);
		if (!Customeresult.isEmpty() && Customeresult.size() > 0) {
			customer = Customeresult.get(0);
		}
		return Optional.ofNullable(customer);
	}

	public String deleteCustomer(String id) {
		log.trace("Metodo deleteCustomer");
		Customer Customer = null;
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":id", new AttributeValue().withS(id));

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("id = :id")
				.withExpressionAttributeValues(eav);

		List<Customer> Customeresult = dynamoDBMapper.scan(Customer.class, scanExpression);
		if (!Customeresult.isEmpty() && Customeresult.size() > 0) {
			Customer = Customeresult.get(0);
		}
		dynamoDBMapper.delete(Customer);
		return "Customer Delete";
	}

	public String update(String id, Customer Customer) {
		log.trace("Metodo UpdateCustomer");
		dynamoDBMapper.save(Customer, new DynamoDBSaveExpression().withExpectedEntry("id",
				new ExpectedAttributeValue(new AttributeValue().withS(id))));
		return id;
	}
	
	public List<Customer> getListCustomer(){
		log.trace("Metodo listCustomers");
		PaginatedList<Customer> resultsCustomer= dynamoDBMapper.scan(Customer.class, new DynamoDBScanExpression());
		resultsCustomer.loadAllResults();
		return resultsCustomer;
	}
}
