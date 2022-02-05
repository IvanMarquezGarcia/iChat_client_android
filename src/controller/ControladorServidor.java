/*
	Hecho por:
		Eloy Guillermo Villad�niga M�rquez
		e
		Iv�n M�rquez Garc�a
		
	2� D.A.M.
	
	Pr�ctica "Chat Colectivo" - Programaci�n de Servicios y Procesos
	
	
	
	------------------------------- DESCRIPCI�N ------------------------------- MEJORAR DESCRIPCI�N
	
	Controlador de la vista VistaServidor.fxml.
*/



package controller;



import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

import javafx.scene.input.MouseEvent;

import javafx.scene.layout.VBox;

import javafx.scene.text.Text;

import model.Servidor;



public class ControladorServidor {

	Servidor servidor;
	
	
	
	public ControladorServidor(Servidor s) {
		this.servidor = s;
	}
	
	

	 @FXML
	    private Button conectar_button;

	    @FXML
	    private Button desconectar_button;

	    @FXML
	    private Text info_Text;

	    @FXML
	    private ScrollPane mensajes_scrollPane;

	    @FXML
	    private TextArea mensajes_textArea;

	    @FXML
	    private VBox root_VBox;
	    
	    
	    
	    @FXML
		public void initialize() {	    	
	    	this.servidor.setTextArea(mensajes_textArea);
		}
	    
	    

	    @FXML
	    void apagar(MouseEvent event) {
	    	servidor.desconectar();
	    }

	    @FXML
	    void arrancar(MouseEvent event) {
	    	servidor.arrancar();
	    }

}
