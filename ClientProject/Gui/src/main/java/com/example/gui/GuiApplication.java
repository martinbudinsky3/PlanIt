package com.example.gui;

import com.example.client.clients.EventsClient;
import com.example.client.clients.UsersClient;
import com.example.gui.controllers.PlanItEventDetailController;
import com.example.gui.controllers.PlanItLoginController;
import com.example.gui.controllers.PlanItMainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiApplication extends Application {

	public static void main(String[] args) {launch(args);}

	@Override
	public void start(Stage primaryStage) throws Exception {

		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItLogin.fxml"));
			PlanItLoginController planItLoginController = new PlanItLoginController(new UsersClient());
			fxmlLoader.setController(planItLoginController);
			AnchorPane rootPane = (AnchorPane) fxmlLoader.load();
			Scene newScene = new Scene(rootPane);
			primaryStage.setScene(newScene);
			primaryStage.setTitle("PlanIt");
			primaryStage.show();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
