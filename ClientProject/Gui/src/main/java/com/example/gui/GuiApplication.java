package com.example.gui;

import com.example.client.clients.EventsClient;
import com.example.gui.controllers.PlanItMainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class GuiApplication extends Application {

	public static void main(String[] args) {launch(args);}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItMainWindow.fxml"));
			PlanItMainWindowController planItMainWindowController = new PlanItMainWindowController(new EventsClient());
			fxmlLoader.setController(planItMainWindowController);
			AnchorPane rootPane = (AnchorPane) fxmlLoader.load();
			Scene newScene = new Scene(rootPane, 1213, 630);
//			newScene.getStylesheets().add(getClass().getClassLoader().getResource("css/styles.css").toExternalForm()); // not working
			primaryStage.setScene(newScene);
			primaryStage.setTitle("PlanIt");
			primaryStage.show();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
