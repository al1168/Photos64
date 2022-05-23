/**
 * @author Alden Lu
 * @author Sam Lee
 */
package view;

import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Persistence;
import model.User;

public class AdminController {

	@FXML
	private ListView<User> listView;
	private ObservableList<User> obsUser;

	@FXML
	private Button adAdd, adDelete, adEdit, adQuit, adLogOut;

	@FXML
	private TextField userText;
	/**
	 * Initializes admin subsystem when logged in as admin.
	 */
	public void initialize() {

		obsUser = FXCollections.observableArrayList();

		Iterator<User> userIterator = Persistence.userIterator();

		while (userIterator.hasNext()) {
			obsUser.add(userIterator.next());
		}

		listView.setItems(obsUser);

		if (!obsUser.isEmpty()) {
			listView.getSelectionModel().select(0);
		}
	}
	/**
	 * add button to add username to the program. Added usernames can be used to log in.
	 * @param event
	 */
	@FXML
	public void addClick(ActionEvent event) {

		Button b = (Button) event.getSource();
		if (b == adAdd) {
			String username = userText.getText().trim(); // take textbox input as new username

			// empty is not valid
			if (username.isEmpty()) {
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Input Empty");
				error.setContentText("Username is empty.");
				error.show();

				return;
			}
			// space is not valid
			String[] spaceCheck = username.split(" ");
			if (spaceCheck.length > 1) {
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Invalid input");
				error.setContentText("Username contains space.");
				error.show();

				return;
			}
			// check for duplicates
			for (User x : obsUser) {
				if (username.contentEquals(x.toString())) {
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("Invalid input");
					error.setContentText("Name already exists.");
					error.show();

					return;
				}
			}

			User newUser = new User(username);
			obsUser.add(newUser);
			Persistence.addUser(newUser);
			listView.getSelectionModel().select(newUser);
			userText.clear();
		}
	}
	/**
	 * Deletes username and their information on the program.
	 * @param event
	 */
	@FXML
	public void delClick(ActionEvent event) {
		Button b = (Button) event.getSource();
		if (b == adDelete) {

			if (obsUser.isEmpty()) {
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Empty List");
				error.setContentText("No user to delete.");
				error.show();

				return;
			}
			User selected = listView.getSelectionModel().getSelectedItem();

			int i = 0;

			for (User x : obsUser) {
				if (selected.toString().equals(x.toString())) {
					break;
				}
				i++;
			}

			obsUser.remove(i);
			Persistence.removeUser(i);
			if (obsUser.size() == i) {
				listView.getSelectionModel().clearAndSelect(--i);
			} else {
				listView.getSelectionModel().clearAndSelect(i);
			}
		}
	}
	/**
	 * Edit username of selected user.
	 * @param event
	 */
	@FXML
	void editClick(ActionEvent event) {
		Button b = (Button) event.getSource();
		if (b == adEdit) {
			if (obsUser.isEmpty()) {
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Empty List");
				error.setContentText("No user to edit.");
				error.show();

				return;
			}

			TextInputDialog editDialog = new TextInputDialog();
			editDialog.setTitle("Edit Username");
			editDialog.setHeaderText("Edit Username");
			editDialog.setContentText("Username: ");
			String input = editDialog.showAndWait().get();
			if (input.isEmpty()) {
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Empty input");
				error.setContentText("Please enter valid username.");
				error.show();

				return;
			}
			
			Iterator<User> userIterator = Persistence.userIterator();
			
			while (userIterator.hasNext()) {
				if (input.equals(userIterator.next().toString())) {
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("Existing username");
					error.setContentText("Existing username");
					error.show();

					return;
				}
			}
			
			int i = listView.getSelectionModel().getSelectedIndex();
			User user = listView.getSelectionModel().getSelectedItem();
			user.setUsername(input);
			obsUser.get(i).setUsername(input);
			
			listView.setItems(null);
			listView.setItems(obsUser);
			listView.getSelectionModel().clearAndSelect(i);
		}
	}
	/**
	 * Logs out user and leads back to login screen. All progress is saved.
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void logoutClick(ActionEvent event) throws IOException {
		Button b = (Button) event.getSource();
		if (b == adLogOut) {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/login.fxml"));
			AnchorPane root = (AnchorPane) loader.load();

			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) adLogOut.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		}

	}
	/**
	 * Quits the program and saves all the object to users.dat
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void quitClick(ActionEvent event) throws IOException {
		Button b = (Button) event.getSource();
		if (b == adQuit) {
			Persistence.writeUsers();
			Platform.exit();
		}
	}

}
