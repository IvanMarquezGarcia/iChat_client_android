/*
	Hecho por:
		Eloy Guillermo Villad�niga M�rquez
		e
		Iv�n M�rquez Garc�a

	2� D.A.M.

	Pr�ctica "Chat Colectivo" - Programaci�n de Servicios y Procesos



	------------------------------- DESCRIPCI�N -------------------------------
	
	Representa a un mensaje del chat.
 */



package com.ivan.iChat.ichat_android.model;



public class Message {
	
	String content;
	boolean own;
	
	
	public Message(String content) {
		this(content, false);
	}
	
	public Message(String content, boolean own) {
		this.content = content;
		this.own = own;
	}

	
	public String getContent() {
		return content;
	}

	public boolean isOwn() {
		return own;
	}
	
}
