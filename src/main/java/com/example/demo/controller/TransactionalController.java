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

import com.example.demo.model.Transactional;
import com.example.demo.service.TransactionalService;
import com.grandapp.response.GeneralResponse;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.springframework.http.HttpStatus;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1")
public class TransactionalController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private TransactionalService transactionalService;

	@RequestMapping(path = "/transactional", method = RequestMethod.POST)
	private ResponseEntity<GeneralResponse<Transactional>> save(@RequestBody Transactional data) throws Exception {
		GeneralResponse<Transactional> response = new GeneralResponse<>();
		HttpStatus status = null;
		try {
			log.trace("Iniciando saveTransactional() con {}", data);
			Transactional cliente = transactionalService.createTransactional(data).orElse(null);
			response.setMessage("Transaccion Exitosa!");
			response.setSuccess(true);
			response.setData(cliente);
			status = HttpStatus.OK;
			log.info("saveTransactional() Ejecutado con Éxito " + cliente);

		} catch (Exception e) {

			response.setMessage(e.getMessage());
			response.setSuccess(false);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error("error generado " + e.getMessage());
		}
		return new ResponseEntity<>(response, status);
	}

		
	@RequestMapping(path = "/transactional/{id}", method = RequestMethod.GET)
	public ResponseEntity<Transactional> getTransactional(@PathVariable("id") String id) {
		return transactionalService.getTransactionalByID(id)
				.map(newTransactionalData -> new ResponseEntity<>(newTransactionalData, OK))
				.orElse(new ResponseEntity<>(CONFLICT));
	}

	@RequestMapping(path = "/transactional/", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteTransactional(@RequestBody String id) {
		return transactionalService.deleteTransactional(id) ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "/transactional", method = RequestMethod.GET)
	public ResponseEntity<List<Transactional>> listTransactionals() {
		List<Transactional> lstTransactional = transactionalService.getListTransactionals();
		if (lstTransactional.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(lstTransactional, OK);
	}

}
