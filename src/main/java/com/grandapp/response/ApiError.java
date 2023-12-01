package com.grandapp.response;

import java.io.Serializable;
/*
 * @autor Joan Lemus
 */

public class ApiError implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	
	/**
	 * Mensaje de error mostrado al usuario
	 */
	private String messageUser;

	/**
	 * Logs en consola
	 */
	private String messageDeveloper;

	/**
	 * Codigo del error
	 */
	private String code;

	/**
	 * Constructor
	 */
	public ApiError() {
	}

	public ApiError(String messageUser, String messageDeveloper, String code) {
		super();
		this.messageUser = messageUser;
		this.messageDeveloper = messageDeveloper;
		this.code = code;
	}

	public String getMessageUser() {
		return messageUser;
	}

	public void setMessageUser(String messageUser) {
		this.messageUser = messageUser;
	}

	public String getMessageDeveloper() {
		return messageDeveloper;
	}

	public void setMessageDeveloper(String messageDeveloper) {
		this.messageDeveloper = messageDeveloper;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "ApiError [messageUser=" + messageUser + ", messageDeveloper=" + messageDeveloper + ", code=" + code
				+ "]";
	}
	 
	
	

}
