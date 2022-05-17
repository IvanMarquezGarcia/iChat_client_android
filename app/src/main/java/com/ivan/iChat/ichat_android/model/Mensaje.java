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



public class Mensaje {
	
	String contenido;
	boolean propio;
	
	
	public Mensaje(String contenido) {
		this(contenido, false);
	}
	
	public Mensaje(String contenido, boolean propio) {
		this.contenido = contenido;
		this.propio = propio;
	}

	
	public String getContenido() {
		return contenido;
	}

	public boolean isPropio() {
		return propio;
	}
	
}
