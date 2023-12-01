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
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.example.demo.model.BankAccount;

@Repository
public class bankAccountRepository {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	public BankAccount save(BankAccount bankAccount) {
		log.trace("Metodo SavebankAccount");
		dynamoDBMapper.save(bankAccount);
		return bankAccount;
	}

	public Optional<BankAccount> getBankAccount(String id) {
		log.trace("Metodo getbankAccount");
		BankAccount bankAccount = null;
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":id", new AttributeValue().withS(id));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("id = :id")
				.withExpressionAttributeValues(eav);
		List<BankAccount> bankAccountesult = dynamoDBMapper.scan(BankAccount.class, scanExpression);
		if (!bankAccountesult.isEmpty() && bankAccountesult.size() > 0) {
			bankAccount = bankAccountesult.get(0);
		}
		return Optional.ofNullable(bankAccount);
	}

	public String deleteBankAccount(String id) {
		log.trace("Metodo deletebankAccount");
		BankAccount bankAccount = null;
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":id", new AttributeValue().withS(id));

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("id = :id")
				.withExpressionAttributeValues(eav);

		List<BankAccount> bankAccountesult = dynamoDBMapper.scan(BankAccount.class, scanExpression);
		if (!bankAccountesult.isEmpty() && bankAccountesult.size() > 0) {
			bankAccount = bankAccountesult.get(0);
		}
		dynamoDBMapper.delete(bankAccount);
		return "bankAccount Delete";
	}

	public String updateBankAccount(String id, BankAccount bankAccount) {
		log.trace("Metodo UpdateBankAccount");
		dynamoDBMapper.save(bankAccount, new DynamoDBSaveExpression().withExpectedEntry("id",
				new ExpectedAttributeValue(new AttributeValue().withS(id))));
		return id;
	}
	
	public List<BankAccount> getListBankAccount(){
		log.trace("Metodo listbankAccounts");
		PaginatedList<BankAccount> resultsbankAccount= dynamoDBMapper.scan(BankAccount.class, new DynamoDBScanExpression());
		resultsbankAccount.loadAllResults();
		return resultsbankAccount;
	}
}
