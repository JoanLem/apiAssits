package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.BankAccount;
import com.example.demo.model.Transactional;
import com.example.demo.repository.TransactionalRepository;


@Service
public class TransactionalService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private TransactionalRepository transactionalRepository;

	@Autowired
	private BankAccountService bankAccountService;

	public Optional<Transactional> getTransactionalByID(String id) {
		log.trace("Metodo Service getUserID");
		return transactionalRepository.getTransaction(id);
	}

	public Optional<Transactional> createTransactional(Transactional data) throws Exception {
		log.trace("Metodo service createTransactional");
		validateTransaction(data);
		
		generateTransaction(data);
		
		data.setTime((LocalDateTime.now().toString()));
		transactionalRepository.save(data);
		return Optional.of(data);
	}

	private void validateTransaction(Transactional data) throws Exception {
		BankAccount origin = bankAccountService.getBanckAccountsByID(data.getCustomerOrigin())
				.orElseThrow(() -> new Exception("Cuenta de origen no encontrada"));

		if (!origin.isStatus()) {
			throw new Exception("Cuenta de origen inactiva");
		}

		BigDecimal amountOrigin = (BigDecimal) origin.getAmount();

		if (amountOrigin.compareTo(data.getAmount()) == -1) {
			throw new Exception("Saldo insuficiente en la cuenta de origen");
		}

		BankAccount destination = bankAccountService.getBanckAccountsByID(data.getCustomerDestination())
				.orElseThrow(() -> new Exception("Cuenta de destino no encontrada"));

		if (!destination.isStatus()) {
			throw new Exception("Cuenta de destino inactiva");
		}
	}
	
	private void generateTransaction(Transactional data)throws Exception { 
		discountAmountOrigin(data);
		addAmountDestination(data);
		
	};
	
	
	private void discountAmountOrigin(Transactional data)throws Exception { 
		
		BankAccount origin = bankAccountService.getBanckAccountsByID(data.getCustomerOrigin())
				.orElseThrow(() -> new Exception("Cuenta no encontrada"));

		BankAccount Accountupdate = origin;
		Accountupdate.setAmount(origin.getAmount().subtract(data.getAmount()));
		
		bankAccountService.updateBanckAccount(data.getCustomerOrigin(), Accountupdate);
	};
	
	
	//Este metodo funcionara de igual forma cuando realices una consignacion directa a una cuenta de destino.
	private void  addAmountDestination(Transactional data) throws Exception{
		
		BankAccount destination = bankAccountService.getBanckAccountsByID(data.getCustomerDestination())
				.orElseThrow(() -> new Exception("Cuenta no encontrada"));
		
		BankAccount Accountupdate = destination;
		Accountupdate.setAmount(destination.getAmount().add(data.getAmount()));
		
		bankAccountService.updateBanckAccount(data.getCustomerDestination(), Accountupdate);	
	}
	

	public boolean deleteTransactional(String id) {

		log.trace("Metodo Service deleteTransactional");
		// User userDelete=TransactionalRepository.getUSer(dni);
		if (!transactionalRepository.getTransaction(id).isPresent()) {
			return false;
		}
		transactionalRepository.deleteTransaction(id);
		return true;
	}

	public List<Transactional> getListTransactionals() {
		log.trace("Metodo Service getListTransactionals");
		return transactionalRepository.getListTransaction();
	}
}
