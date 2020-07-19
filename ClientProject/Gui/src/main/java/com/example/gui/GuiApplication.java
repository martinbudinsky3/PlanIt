package com.example.gui;

import com.example.client.clients.UsersClient;
import com.example.gui.controllers.PlanItLoginController;
import com.example.gui.utils.UTF8Control;
import com.example.gui.utils.WindowsCreator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

/** Main class. "PlanItLogin" window is displayed when the application is opened*/
public class GuiApplication extends Application {

	public static void main(String[] args) {launch(args);}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Locale locale = Locale.getDefault();
		ResourceBundle bundle = ResourceBundle.getBundle("captions", locale, new UTF8Control());
		WindowsCreator windowsCreator = new WindowsCreator();

		windowsCreator.createLoginWindow(bundle, primaryStage, new UsersClient());
	}
}
