/*
	Hecho por:
		Eloy Guillermo Villad�niga M�rquez
		e
		Iv�n M�rquez Garc�a
		
	2� D.A.M.
	
	Pr�ctica "Chat Colectivo" - Programaci�n de Servicios y Procesos
	
	
	
	------------------------------- DESCRIPCI�N -------------------------------
	
	Este es el archivo para ejecutar la apliciaci�n gr�fica asociada a un
	servidor.
*/



package application;



import javafx.application.Application;

import java.io.IOException;

import java.net.URL;

import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;

import javafx.stage.Stage;

import controller.ControladorServidor;

import model.Servidor;



public class AplicacionServidor extends Application {

    @Override
    public void start(Stage stage) {
    	URL rutaRecurso = getClass().getResource("/view/VistaServidor.fxml");
    	FXMLLoader fxmlLoader = new FXMLLoader(rutaRecurso);
    	
    	Servidor servidor = new Servidor(1234);
    	
    	ControladorServidor cs = new ControladorServidor(servidor);
    	
    	fxmlLoader.setController(cs);
    	
		try {
			Scene scene = new Scene(fxmlLoader.load(), 530, 600);
			
			stage.setTitle("Servidor de eiChat");
			stage.setScene(scene);
	    	stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error al cargar la ventana");
		}
    }

    // M�todo para lanzar la aplicaci�n gr�fica
    public static void main(String[] args) {
        launch(args);
    }
}
