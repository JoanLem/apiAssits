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
import com.example.demo.model.Transactional;


@Repository
public class TransactionalRepository {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	public Transactional save(Transactional Transaction) {
		log.trace("Metodo SaveTransaction");
		dynamoDBMapper.save(Transaction);
		return Transaction;
	}

	public Optional<Transactional> getTransaction(String id) {
		log.trace("Metodo getTransaction");
		Transactional Transaction = null;
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":id", new AttributeValue().withS(id));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("id_trx = :id")
				.withExpressionAttributeValues(eav);
		List<Transactional> Transactionesult = dynamoDBMapper.scan(Transactional.class, scanExpression);
		if (!Transactionesult.isEmpty() && Transactionesult.size() > 0) {
			Transaction = Transactionesult.get(0);
		}
		return Optional.ofNullable(Transaction);
	}

	public String deleteTransaction(String id) {
		log.trace("Metodo deleteTransaction");
		Transactional Transaction = null;
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":id", new AttributeValue().withS(id));

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("id_trx = :id")
				.withExpressionAttributeValues(eav);

		List<Transactional> Transactionesult = dynamoDBMapper.scan(Transactional.class, scanExpression);
		if (!Transactionesult.isEmpty() && Transactionesult.size() > 0) {
			Transaction = Transactionesult.get(0);
		}
		dynamoDBMapper.delete(Transaction);
		return "Transaction Delete";
	}

	public String update(String id, Transactional Transaction) {
		log.trace("Metodo UpdateTransaction");
		dynamoDBMapper.save(Transaction, new DynamoDBSaveExpression().withExpectedEntry("id_trx",
				new ExpectedAttributeValue(new AttributeValue().withS(id))));
		return id;
	}
	
	public List<Transactional> getListTransaction(){
		log.trace("Metodo listTransactions");
		PaginatedList<Transactional> resultsTransaction= dynamoDBMapper.scan(Transactional.class, new DynamoDBScanExpression());
		resultsTransaction.loadAllResults();
		return resultsTransaction;
	}
}
