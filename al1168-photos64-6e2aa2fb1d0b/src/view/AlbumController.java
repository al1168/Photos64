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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Persistence;
import model.User;
import model.User.Album;

public class AlbumController {

	int userIndex = -1;
	SubSystemController subcontroller = null;
	static int OpenedIndex = -1;
	@FXML
	private TextField txtbox;
	@FXML
	private TableView<User.Album> Album_tab;

	@FXML
	private Button Album_add;

	@FXML
	private Button Album_edit;

	@FXML
	private Button Album_Log;

	@FXML
	private TableColumn<User.Album, String> tabName;

	@FXML
	private TableColumn<User.Album, String> tabNum;

	@FXML
	private TableColumn<User.Album, String> tabDate;
	@FXML
	private Button Album_quit;

	@FXML
	private Button Album_delete;

	@FXML
	private Button Album_open;

	private ObservableList<User.Album> obsList;

	/**
	 * adds album of inputted name if the name does not exist
	 * 
	 * @param e
	 */
	@FXML
	void alAdd(ActionEvent e) {
		Button b = (Button) e.getSource();
		if (b == Album_add) {
			String textbox = txtbox.getText();
			boolean exist = Persistence.getUser(userIndex).alexist(textbox);
			if (exist) {
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Existing album name");
				error.setContentText("Existing album name");
				error.show();

				return;
			}
			if (textbox.isEmpty()) {
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Input Empty");
				error.setContentText("Album name is empty.");
				error.show();

				return;
			}
			User.Album newAl = new User.Album(textbox);
			Persistence.getUser(userIndex).addAlbum(newAl);
			obsList.add(newAl);
			tabName.setCellValueFactory(new PropertyValueFactory<User.Album, String>("Album_name"));
			tabNum.setCellValueFactory(new PropertyValueFactory<User.Album, String>("numPhotos"));
			tabDate.setCellValueFactory(new PropertyValueFactory<User.Album, String>("daterange"));
			Album_tab.setItems(obsList);

		}

	}

	/**
	 * deletes album from the list of albums.
	 * 
	 * @param e
	 */
	@FXML
	void alDel(ActionEvent e) {
		if (obsList.isEmpty())
			return;
		String AlbumName = Album_tab.getSelectionModel().getSelectedItem().getAlbum_name();
		int selectindex = Album_tab.getSelectionModel().getSelectedIndex();
		Button b = (Button) e.getSource();
		if (b == Album_delete) {
			int index = Persistence.getUser(userIndex).indexofAlbum(AlbumName);
			Persistence.getUser(userIndex).deleteAlbum(index);
			obsList.remove(selectindex);

			if (obsList.size() == selectindex) {
				Album_tab.getSelectionModel().select(selectindex--);
			} else if (obsList.isEmpty()) {
				return;
			} else {
				Album_tab.getSelectionModel().select(selectindex);
			}
		}

	}

	/**
	 * edits the name of album.
	 * 
	 * @param e
	 */
	@FXML
	void alEd(ActionEvent e) {
		if (obsList.isEmpty())
			return;
		int selIndex = Album_tab.getSelectionModel().getSelectedIndex();
		String origin = Album_tab.getSelectionModel().getSelectedItem().getAlbum_name();
		TextInputDialog editbox = new TextInputDialog();
		editbox.setTitle("ENTER NEW NAME");
		editbox.setHeaderText("Please enter a new name");
		editbox.setContentText("Name");
		Optional<String> newname = editbox.showAndWait();
		if (newname.isEmpty() || newname.get().equals("")) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Invalid");
			error.setContentText("Must Enter Album Name");
			error.show();
		}
		User u = Persistence.getUser(userIndex);
		int index = u.indexofAlbum(origin);
		u.editname(index, newname.get());
		obsList.get(index).setAlbum_name(newname.get());
		Album_tab.getColumns().get(selIndex).setVisible(false); // refresh columns to display new value
		Album_tab.getColumns().get(selIndex).setVisible(true);

	}

	/**
	 * logs out and saves progress. goes back to login page.
	 * 
	 * @param e
	 * @throws IOException
	 */
	@FXML
	void alLog(ActionEvent e) throws IOException {
		Button b = (Button) e.getSource();
		if (b == Album_Log) {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/login.fxml"));
			AnchorPane root = (AnchorPane) loader.load();

			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) Album_Log.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		}

	}

	/**
	 * open album to go to photo subsystem of the selected album.
	 * 
	 * @param e
	 * @throws IOException
	 */
	@FXML
	void alOpen(ActionEvent e) throws IOException {
		if (obsList.isEmpty())
			return;
		Button b = (Button) e.getSource();
		if (b == Album_open) {
			OpenedIndex = Album_tab.getSelectionModel().getSelectedIndex();
			// make sure it exist?
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/Photos_SubSystem.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			subcontroller = loader.getController();
			subcontroller.start();
			// loader.getController();
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) b.getScene().getWindow(); // gets the current stage
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		}
	}

	public static int currAlbumindex() {
		return OpenedIndex;
	}

	/**
	 * quits the program and saves the progress.
	 * 
	 * @param e
	 * @throws IOException
	 */
	@FXML
	void alQuit(ActionEvent e) throws IOException {
		Button b = (Button) e.getSource();
		if (b == Album_quit) {
			Persistence.writeUsers();
			Platform.exit();
		}
	}

	/**
	 * initializes album subsystem.
	 * 
	 * @throws IOException
	 */
	public void start() throws IOException {
		// TODO Auto-generated method stub
		// Album_tab.getSelectionModel().select(0);
		userIndex = LoginController.getUserindex();
		obsList = FXCollections.observableArrayList();
		Iterator<User.Album> albumIter = Persistence.getUser(userIndex).alIter();
		int i = 0;
		while (albumIter.hasNext()) {
			obsList.add(albumIter.next());
		}
		Album_tab.setItems(obsList);
		tabName.setCellValueFactory(new PropertyValueFactory<User.Album, String>("Album_name"));
		tabNum.setCellValueFactory(new PropertyValueFactory<User.Album, String>("numPhotos"));
		tabDate.setCellValueFactory(new PropertyValueFactory<User.Album, String>("daterange"));

	}

}
