package com.example.demo.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.BankAccount;
import com.example.demo.repository.bankAccountRepository;

@Service
public class BankAccountService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private bankAccountRepository bankAccountRepository;

	public Optional<BankAccount> getBanckAccountsByID(String id) {
		log.trace("Metodo Service getBanckAccountsByID");
		return bankAccountRepository.getBankAccount(id);
	}

	public Optional<BankAccount> createBanckAccount(BankAccount data) {
		log.trace("Metodo service createBanckAccount");
		bankAccountRepository.save(data);
		return Optional.of(data);
	}

	public boolean deleteBanckAccount(String id) {

		log.trace("Metodo Service deleteBanckAccount");
		// User userDelete=bankAccountRepository.getUSer(dni);
		if (!bankAccountRepository.getBankAccount(id).isPresent()) {
			return false;
		}
		bankAccountRepository.deleteBankAccount(id);
		return true;
	}

	public boolean updateBanckAccount(String id, BankAccount data) {
		try {
			log.trace("Metodo Service updateBanckAccount");
			if (!bankAccountRepository.getBankAccount(id).isPresent()) {
				return false;
			}
			bankAccountRepository.updateBankAccount(id, data);
			return true;

		} catch (Exception e) {
			 log.error("Error al actualizar la cuenta bancaria.", e);
		        throw new RuntimeException("Error al actualizar la cuenta bancaria.", e);

		}
	}

	public BankAccount getBanckAccount(String id) {
		log.trace("Metodo Service getListBanckAccounts");
		return bankAccountRepository.getBanckAccount(id);
	}
}
