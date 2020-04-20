package com.example.gui;

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
			AnchorPane rootPane = (AnchorPane) fxmlLoader.load();
			Scene newScene = new Scene(rootPane, 600, 600);
			primaryStage.setScene(newScene);
			primaryStage.setTitle("PlanIt");
			primaryStage.show();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
