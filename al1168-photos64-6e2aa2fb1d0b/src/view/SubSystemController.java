/**
 * @author Alden Lu
 * @author Sam Lee
 */
package view;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

import java.util.List;
import java.util.Optional;
import javafx.scene.control.ChoiceDialog;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Persistence;
import model.User;
import model.User.Album.Photo;

public class SubSystemController {
	AlbumController acontroller;
	private ObservableList<User.Album.Photo> obsList;
	@FXML
	private Button slideshow;

	@FXML
	private Button back;

	@FXML
	private Button logout;

	@FXML
	private Button quit;

	@FXML
	private Button addTag;

	@FXML
	private Button delTag;

	@FXML
	private Button addCap;

	@FXML
	private Button editCap;

	@FXML
	private Button addPhoto;

	@FXML
	private Button delPhoto;

	@FXML
	private Button movePhoto;

	@FXML
	private Button copyPhoto;

	@FXML
	private ImageView img;
	@FXML
	private ListView<User.Album.Photo> list;
	@FXML
	private Label dateLabel;
	@FXML
	private Label locationLabel;
	@FXML
	private Label personLabel;
	@FXML
	private Label captionLabel;
	/**
	 * logs out user and goes back to login page. progress is saved when this is clicked.
	 * @param e
	 * @throws IOException
	 */
	@FXML
	public void logClick(ActionEvent e) throws IOException {
		Button b = (Button) e.getSource();
		if (b == logout) {
			Persistence.writeUsers();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/login.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) b.getScene().getWindow(); // gets the current stage
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		}
	}

	SlideshowController controller;
	/**
	 * goes back to album subsystem.
	 * @param e
	 * @throws IOException
	 */
	@FXML
	void backClick(ActionEvent e) throws IOException {
		Button b = (Button) e.getSource();
		if (b == back) {
			Persistence.writeUsers();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/Album.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			acontroller = loader.getController();
			acontroller.start();
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) b.getScene().getWindow(); // gets the current stage
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
	void quitClicked(ActionEvent event) throws IOException {
		Button b = (Button) event.getSource();
		if (b == quit) {
			Persistence.writeUsers();
			Platform.exit();
		}
	}
	/**
	 * method to take in value of tag type(name)
	 * @param name
	 * @return
	 */
	public static String inputTagDialog(String name) {
		TextInputDialog inputdialog = new TextInputDialog();
		Optional<String> input;

		if (name == "person" || name == "location") {
			inputdialog.setTitle("Add tag");
			inputdialog.setHeaderText("Add " + name + " Tag");
			inputdialog.setContentText("Enter value: ");

			input = inputdialog.showAndWait();

			if (input.isPresent()) {
				if (input.get().isEmpty()) {
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("Empty input");
					error.setContentText("Please input valid tag.");
					error.show();

					return null;
				}
				return name + "%" + input.get().trim();
			}

		}
		return null;

	}
/**
 * tag selection to choose which type of tag is altered.
 * @param input
 * @return
 */
	public static String tagDialog(String input) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Tag");
		alert.setHeaderText("Tag Type");

		ButtonType person = new ButtonType("Person");
		ButtonType location = new ButtonType("Location");
		ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(person, location, cancel);

		if (input == "add") {
			alert.setContentText("Select name of tag you want to add.");
			Optional<ButtonType> choice = alert.showAndWait();
			if (choice.get() == person) {
				return inputTagDialog("person");
			} else if (choice.get() == location) {
				return inputTagDialog("location");
			} else {

			}
		} else if (input == "remove") {
			alert.setContentText("Select name of tag you want to delete.");
			Optional<ButtonType> choice = alert.showAndWait();
			if (choice.get() == person) {
				return inputTagDialog("person");
			} else if (choice.get() == location) {
				return inputTagDialog("location");
			} else {

			}
		}
		return null;

	}
/**
 * button to add tag to photo
 * @param e
 */
	@FXML
	void addT(ActionEvent e) {
		Button b = (Button) e.getSource();
		if (b == addTag) {
			String newtag = tagDialog("add");
			if (newtag != null) {
				User.Album.Photo photo = list.getSelectionModel().getSelectedItem();

				String[] tag = newtag.split("%");

				if (photo.addTag(tag[0], tag[1])) {
					show(photo);
				} else {
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("Duplicate Tag");
					error.setContentText("Tag already exists.");
					error.show();

					return;
				}
			}
		}

	}
/**
 * button to delete tag of photo
 * @param e
 */
	@FXML
	void deleteT(ActionEvent e) {
		Button b = (Button) e.getSource();
		if (b == delTag) {
			String Tagdelete = tagDialog("remove");
			if (Tagdelete != null) {
				User.Album.Photo photo = list.getSelectionModel().getSelectedItem();

				String[] tag = Tagdelete.split("%");

				if (photo.removeTag(tag[0], tag[1])) {
					show(photo);
				} else {
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("Nonexistant Tag");
					error.setContentText("Tag does not exists.");
					error.show();

					return;
				}
			}
		}
	}
/**
 * Button to copy photo to another album.
 * @param e
 * @throws IOException
 */
	@FXML
	void CopyPH(ActionEvent e) throws IOException {
		Button b = (Button) e.getSource();
		Photo selectedPhoto = list.getSelectionModel().getSelectedItem();
		Photo cloned = null;
		List<String> albumz = new ArrayList<>();
		if (b == copyPhoto) {
			Iterator<User.Album> albumInter = Persistence.getUser(LoginController.userindex).alIter();
			Iterator<Photo> phit = SubSystemController.getopened().photoIter();
			while (phit.hasNext()) {
				Photo curr = phit.next();
				if (curr.equals(selectedPhoto)) {
					cloned = new Photo(curr.getName(), curr.getDate(), curr.getimgpath());
				}
			}
			while (albumInter.hasNext()) {
				albumz.add(albumInter.next().getAlbum_name());
			}
			ChoiceDialog<String> movechoices = new ChoiceDialog<String>(albumz.get(0), albumz);
			Optional<String> result;
			movechoices.setTitle("copy a Photo");
			movechoices.setHeaderText("Select album to copy your photo");
			movechoices.setContentText("Album:");
			result = movechoices.showAndWait();
			if (result.isPresent()) {
				String d = result.get() + "";
			
				Persistence.getUser(LoginController.getUserindex())
						.getAlbum(Persistence.getUser(LoginController.userindex).indexofAlbum(d)).addPhotos(cloned);
			}
		}
		Persistence.writeUsers();
		// Object cloned = cloneThroughSerialize (someObject);
	}
/**
 * button to move photo to another album.
 * @param e
 * @throws IOException
 */
	@FXML
	void movePH(ActionEvent e) throws IOException {
		Button b = (Button) e.getSource();
		Photo selectedPhoto = list.getSelectionModel().getSelectedItem();
		Photo cloned = null;
		List<String> albumz = new ArrayList<>();
		if (b == movePhoto) {
			Iterator<User.Album> albumInter = Persistence.getUser(LoginController.userindex).alIter();
			Iterator<Photo> a = SubSystemController.getopened().photoIter();
			int i = 0;
			while (a.hasNext()) {
				Photo curr = a.next();
				if (curr.equals(selectedPhoto)) {
					cloned = new Photo(curr.getName(), curr.getDate(), curr.getimgpath());
					SubSystemController.getopened().deletePhotos(i);
					obsList.remove(i);
					break;
				}
				i++;
			}
			while (albumInter.hasNext()) {
				albumz.add(albumInter.next().getAlbum_name());
			}
			ChoiceDialog<String> movechoices = new ChoiceDialog<String>(albumz.get(0), albumz);
			Optional<String> result;
			movechoices.setTitle("Move a Photo");
			movechoices.setHeaderText("Choose an album to move your photo to.");
			movechoices.setContentText("Albums:");
			result = movechoices.showAndWait();
			if (result.isPresent()) {
				String d = result.get() + "";
				
				Persistence.getUser(LoginController.getUserindex())
						.getAlbum(Persistence.getUser(LoginController.userindex).indexofAlbum(d)).addPhotos(cloned);

			}
		}
		Persistence.writeUsers();
		if (obsList.size() > 0) {
			list.getSelectionModel().select(0);
			show(list.getSelectionModel().getSelectedItem());
		} else {
			img.setImage(null);
		}
	}
/**
 * add photo to album.
 * @param e
 * @throws ParseException
 */
	@FXML
	void addPH(ActionEvent e) throws ParseException {
		Button b = (Button) e.getSource();
		if (b == addPhoto) {
			FileChooser addPhoto = new FileChooser();
			Chooser(addPhoto);
			File file = addPhoto.showOpenDialog(null);
			if (file == null)
				return;
			Image display = new Image(file.toURI().toString());
			String fileName = file.getName();
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			Date date = format.parse(format.format(file.lastModified()));
			
			Photo photo = new Photo(fileName, date, file.getPath());
			obsList.add(photo);
			getopened().addPhotos(photo);
			show(photo);
			img.setImage(display);
			list.getSelectionModel().select(obsList.size() - 1);
		}
	}
/**
 * delete photo from album.
 * @param e
 */
	@FXML
	void deletePH(ActionEvent e) {
		Button b = (Button) e.getSource();
		if (b == delPhoto) {
			// User.Album.Photo removed = list.getSelectionModel().getSelectedItem();
			User.Album user = getopened();
			int index = list.getSelectionModel().getSelectedIndex();
			user.deletePhotos(index);
			obsList.remove(index);
		}
	}
/**
 * slideshow of current album.
 * @param e
 * @throws IOException
 */
	@FXML
	void slideshowClicked(ActionEvent e) throws IOException {
		Button b = (Button) e.getSource();
		if (b == slideshow) {
			Persistence.writeUsers();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/slideshow.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			controller = loader.getController();
			controller.load();
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) b.getScene().getWindow(); // gets the current stage
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		}
	}

	private static void Chooser(final FileChooser file) {
		file.setTitle("CHOOSE A PHOTO");
		file.setInitialDirectory(new File(System.getProperty("user.home")));
		file.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.*"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));

	}
/*
 * Edit caption when caption exists
 */
	@FXML
	void editC(ActionEvent e) {
		Button b = (Button) e.getSource();
		User.Album.Photo photo = list.getSelectionModel().getSelectedItem();
		if (b == editCap && !photo.getCaption().isEmpty()) {
			TextInputDialog dialog = new TextInputDialog();
			String caption;

			dialog.setTitle("Enter Caption");
			dialog.setHeaderText("Caption");
			dialog.setContentText("Enter Caption:");

			caption = dialog.showAndWait().get();

			photo.setCaption(caption);
			show(photo);

		} else {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("No caption set.");
			error.setContentText("Add a caption first to edit.");
			error.show();

			return;
		}
	}
/**
 * add caption to image if there is no caption.
 * @param e
 */
	@FXML
	void addC(ActionEvent e) {
		Button b = (Button) e.getSource();
		User.Album.Photo photo = list.getSelectionModel().getSelectedItem();
		if (b == addCap && photo.getCaption().isEmpty()) {
			TextInputDialog dialog = new TextInputDialog();
			String caption;

			dialog.setTitle("Enter Caption");
			dialog.setHeaderText("Caption");
			dialog.setContentText("Enter Caption:");

			caption = dialog.showAndWait().get();

			if (caption.isEmpty()) {
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Empty Input");
				error.setContentText("Enter valid caption.");
				error.show();

				return;
			}

			photo.setCaption(caption);
			show(photo);

		} else {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Caption already exists!");
			error.setContentText("There is already a caption for this photo.");
			error.show();

			return;
		}
	}
/**
 * initializes photo subsystem.
 */
	void start() {
		obsList = FXCollections.observableArrayList();
		Iterator<Photo> photoIterator = Persistence.getUser(LoginController.getUserindex())
				.getAlbum(AlbumController.currAlbumindex()).photoIter();
		while (photoIterator.hasNext()) {
			User.Album.Photo x = photoIterator.next();
			obsList.add(x);
		}
		list.setItems(obsList);
		if (obsList.size() > 0) {
			list.getSelectionModel().select(0);
			show(list.getSelectionModel().getSelectedItem());
		}
		try {
			list.getSelectionModel().selectedItemProperty().addListener((v, oldval, newval) -> show(newval));
		} catch (NullPointerException e) {

		}

	}
/**
 * returns the currently opened album.
 * @return
 */
	public static User.Album getopened() {
		return Persistence.getUser(LoginController.getUserindex()).getAlbum(AlbumController.OpenedIndex);
	}
/**
 * show image on photo subsystem and refresh the tags, captions, etc.
 * @param newval
 */
	private void show(Photo newval) {
		// TODO Auto-generated method stub
		if (newval == null) {
			img.setImage(null);

			return;
		}
		User.Album.Photo photo = list.getSelectionModel().getSelectedItem();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");


		dateLabel.setText(format.format(newval.getDate()));

		if (!list.getSelectionModel().isEmpty() && !photo.getPhotoTags().isEmpty()) {
			locationLabel.setText(photo.getTags("location"));
			personLabel.setText(photo.getTags("person"));
			captionLabel.setText(photo.getCaption());
		} else if (!list.getSelectionModel().isEmpty() && photo.getPhotoTags().isEmpty()) {
			locationLabel.setText("");
			personLabel.setText("");
		}

		File file = new File(newval.getimgpath());
		Image display = new Image(file.toURI().toString());
		img.setImage(display);
	}

}
