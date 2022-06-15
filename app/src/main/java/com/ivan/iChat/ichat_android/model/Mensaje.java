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


import java.io.Serializable;

public class Mensaje implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id = -1;
	private String contenido;
	private boolean propio;
	private String language;
	private final int sender;


	public Mensaje(String contenido, String language, int sender) {
		this.contenido = contenido;
		this.language = language;
		this.sender = sender;
	}

	public Mensaje(String contenido, boolean propio, String language, int sender) {
		this.contenido = contenido;
		this.propio = propio;
		this.language = language;
		this.sender = sender;
	}


	public int getId() { return id; }

	public void setId(int id) {
		if (this.id == -1)
			this.id = id;
	}

	public String getContenido() { return contenido; }

	public boolean isPropio() { return propio; }

	public String getLanguage() { return language; }

	public int getSender() { return sender; }

}
