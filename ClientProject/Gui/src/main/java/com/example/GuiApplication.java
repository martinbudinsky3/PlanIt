package com.example;

import com.example.client.clients.UsersClient;
import com.example.utils.UTF8Control;
import com.example.utils.WindowsCreator;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/** Main class. "PlanItLogin" window is displayed when the application is opened*/
public class GuiApplication extends Application {
	private static final Logger logger = LoggerFactory.getLogger(GuiApplication.class);

	public static void main(String[] args) {launch(args);}

	@Override
	public void start(Stage primaryStage) {

		Locale locale = Locale.getDefault();
		ResourceBundle bundle = ResourceBundle.getBundle("captions", locale, new UTF8Control());
		WindowsCreator windowsCreator = new WindowsCreator();
		windowsCreator.createLoginWindow(bundle, primaryStage);
	}
}
