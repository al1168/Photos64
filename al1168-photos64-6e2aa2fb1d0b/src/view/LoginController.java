/**
 * @author Alden Lu
 * @author Sam Lee
 */
package view;

import java.io.IOException;
import java.util.Iterator;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Persistence;
import model.User;

public class LoginController {
	static int userindex = -1;
	// careful static
	AlbumController alcontroller = null;
	@FXML
	private Button logLog;

	@FXML
	private TextField userText;

	@FXML
	private Button logQuit;
	
	/**
	 * logs in as inputted user if username exists in the system. username can be
	 * added in admin.
	 * 
	 * @param e
	 * @throws IOException
	 */
	@FXML
	void log_click(ActionEvent e) throws IOException {
		String UserName = userText.getText();
		Button b = (Button) e.getSource();
		if (b == logLog) {
			if (UserName.toLowerCase().trim().equals("admin")) {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/view/admin.fxml"));
				AnchorPane root = (AnchorPane) loader.load();
				Scene scene = new Scene(root);
				Stage primaryStage = (Stage) b.getScene().getWindow(); // gets the current stage
				primaryStage.setScene(scene);
				primaryStage.setResizable(false);
				primaryStage.show();
			} else {
				int i = 0;
				boolean userFound = false;
				// find see if user is in the data
				Iterator<User> userIterator = Persistence.userIterator();
				while (userIterator.hasNext()) {
					if (userIterator.next().toString().equals(UserName)) {
						userFound = true;
						userindex = i;
						break;

					}
					i++;
				}
				if (userFound) {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("/view/Album.fxml"));
					AnchorPane root = (AnchorPane) loader.load();
					alcontroller = loader.getController();
					alcontroller.start();
					Scene scene = new Scene(root);
					Stage primaryStage = (Stage) b.getScene().getWindow(); // gets the current stage
					primaryStage.setScene(scene);
					primaryStage.setResizable(false);
					primaryStage.show();
				} else {
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("User not found");
					error.setContentText("Username does not exist.");
					error.show();
					
					return;
				}
			}
		}
	}
	/**
	 * quits the program and saves progress.
	 * @param e
	 * @throws IOException
	 */
	@FXML
	void quit_click(ActionEvent e) throws IOException {
		Button b = (Button) e.getSource();
		if (b == logQuit) {
			Persistence.writeUsers();
			Platform.exit();
		}
	}

	public static int getUserindex() {
		// TODO Auto-generated method stud
		return userindex;
	}
}
