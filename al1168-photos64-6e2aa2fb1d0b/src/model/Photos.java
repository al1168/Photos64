/**
 * @author Alden Lu
 * @author  Sam Lee
 */
package model;

import javafx.stage.Stage;
import view.AlbumController;
import view.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.AlbumController;

public class Photos extends Application {
	AlbumController controller;
	LoginController controller2;

	/**
	 * Application start method
	 */
	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
//		try {
//			FXMLLoader loader = new FXMLLoader();
//			loader.setLocation(getClass().getResource("/view/Album.fxml"));
//			AnchorPane root = (AnchorPane)loader.load();
//			controller = 
//					loader.getController();
//			controller.start();
//			Scene scene = new Scene(root);
//			stage.setScene(scene);
//			stage.setTitle("Photos");
//			stage.setResizable(false);
//			stage.show();
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
//		System.out.println("fuck  yu");
//	}

		try {
			Persistence.readUsers();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/login.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			controller2 = loader.getController();
			// controller2.start();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Login");
			stage.setResizable(false);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * runs app
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}