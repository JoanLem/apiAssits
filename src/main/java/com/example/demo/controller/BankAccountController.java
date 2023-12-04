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
import com.example.demo.model.BankAccount;
import com.example.demo.service.BankAccountService;
import com.grandapp.response.GeneralResponse;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.OK;


import org.springframework.http.HttpStatus;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1")
public class BankAccountController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private BankAccountService banckAccountService;

	@RequestMapping(path = "/account", method = RequestMethod.POST)
	public ResponseEntity<BankAccount> saveAccount(@RequestBody BankAccount data) {
		log.trace("Entering create() with {}", data);
		return banckAccountService.createBanckAccount(data).map(newAccount -> new ResponseEntity<>(newAccount, OK))
				.orElse(new ResponseEntity<>(CONFLICT));
	}

	@RequestMapping(path = "/account/", method = RequestMethod.DELETE)
	public  ResponseEntity<Void> deleteBanckAccount(@RequestBody String id) {		
		return banckAccountService.deleteBanckAccount(id)?new ResponseEntity<>(HttpStatus.OK):new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
	@RequestMapping(path = "/account/{id}", method = RequestMethod.PUT)
	public  ResponseEntity<Void> updateBanckAccount(@PathVariable("id") String id, @RequestBody BankAccount data) {		
		return banckAccountService.updateBanckAccount(id,data)?new ResponseEntity<>(HttpStatus.OK):new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(path = "/account/{id}", method = RequestMethod.GET)
	public ResponseEntity<GeneralResponse<BankAccount>> BanckAccount(@PathVariable("id") String id){
		
		GeneralResponse<BankAccount> response = new GeneralResponse<>();
		HttpStatus status = null;
		try {
			log.trace("Iniciando GetAccount() con {}", id);
			BankAccount account=banckAccountService.getBanckAccount(id);
			response.setMessage("Consulta Exitosa!");
			response.setSuccess(true);
			response.setData(account);
			status = HttpStatus.OK;
			if(account.getId().isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			log.info("GetAccount() Ejecutado con Éxito " + account);

		} catch (Exception e) {

			response.setMessage(e.getMessage());
			response.setSuccess(false);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error("error generado " + e.getMessage());
		}
		return new ResponseEntity<>(response, status);
		
	}
	

}
