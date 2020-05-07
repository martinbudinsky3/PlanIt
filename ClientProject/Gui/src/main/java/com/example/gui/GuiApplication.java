package com.example.gui;

import com.example.client.clients.UsersClient;
import com.example.gui.controllers.PlanItLoginController;
import com.example.gui.utils.UTF8Control;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class GuiApplication extends Application {

	public static void main(String[] args) {launch(args);}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Locale locale = Locale.getDefault();
		ResourceBundle bundle = ResourceBundle.getBundle("captions", locale, new UTF8Control());

		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItLogin.fxml"));
			PlanItLoginController planItLoginController = new PlanItLoginController(new UsersClient());
			fxmlLoader.setController(planItLoginController);
			fxmlLoader.setResources(bundle);
			AnchorPane rootPane = (AnchorPane) fxmlLoader.load();
			Scene newScene = new Scene(rootPane);
			newScene.getStylesheets().add(getClass().getClassLoader().getResource("css/styles.css").toExternalForm());
			primaryStage.setScene(newScene);
			primaryStage.setTitle("PlanIt");
			primaryStage.resizableProperty().setValue(false);
			primaryStage.show();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
