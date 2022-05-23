/**
 * @author Alden Lu
 * @author Sam Lee
 */
package view;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Persistence;
import model.User;
import model.User.Album.Photo;

public class SlideshowController {
	SubSystemController controller ;
	static int currentIndex = 0;
	private ObservableList<User.Album.Photo> obsList;
	@FXML
	private Button slideReturn;
	@FXML
	private ImageView slideImg;

	@FXML
	private Button slideBack;

	@FXML
	private Button slideNext;

	@FXML
	private Button slideLog;

	@FXML
	private Button sldeQuit;
	/**
	 * shows previous photo
	 * @param e
	 */
	@FXML
	void backClick(ActionEvent e) {

		Button b = (Button) e.getSource();
		if (b == slideBack) {
			int backindex = currentIndex - 1;
			if (!(backindex >= 0)) {
				return;
			}
			File file = new File(obsList.get(backindex).getimgpath());
			Image display = new Image(file.toURI().toString());
			slideImg.setImage(display);
			currentIndex--;
		}
	}
	/**
	 * logs out user and leads back to login page. progress is saved.
	 * @param e
	 * @throws IOException
	 */
	@FXML
	void logClick(ActionEvent e) throws IOException {
		Button b = (Button) e.getSource();
    	if(b==slideLog) {
    	Persistence.writeUsers();
    	FXMLLoader loader = new FXMLLoader();		
		loader.setLocation(getClass().getResource("/view/login.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		Scene scene = new Scene(root);
		Stage primaryStage = (Stage) b.getScene().getWindow();  // gets the current stage 
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
    	}
	}
	/**
	 * shows next photo in slideshow.
	 * @param e
	 */
	@FXML
	void nextClick(ActionEvent e) {
		Button b = (Button) e.getSource();
		if (b == slideNext) {
			int nextindex = currentIndex + 1;
			if (!(nextindex < obsList.size())) {
				return;
			}
			File file = new File(obsList.get(nextindex).getimgpath());
			Image display = new Image(file.toURI().toString());
			slideImg.setImage(display);
			currentIndex++;
		}
	}
	/**
	 * exits the program and saves progress.
	 * @param e
	 */
	@FXML
	void quitClick(ActionEvent e) {
		Button b = (Button) e.getSource();
		if (b == sldeQuit) {
			Platform.exit();
		}
	}
	/**
	 * goes back to photo subsystem.
	 * @param e
	 * @throws IOException
	 */
	@FXML
	void returnClick(ActionEvent e) throws IOException {
		Button b = (Button) e.getSource();
    	if(b==slideReturn) {
    		Persistence.writeUsers();
    		FXMLLoader loader = new FXMLLoader();		
    		loader.setLocation(getClass().getResource("/view/Photos_SubSystem.fxml"));
    		AnchorPane root = (AnchorPane) loader.load();
    		controller =loader.getController();
    		controller.start();
    		Scene scene = new Scene(root);
    		Stage primaryStage = (Stage) b.getScene().getWindow();  // gets the current stage 
    		primaryStage.setScene(scene);
    		primaryStage.setResizable(false);
    		primaryStage.show();
    	}
	}
/**
 * displays nothing if there is no photo
 */
	public void Displaynull() {
		slideImg.setImage(null);
	}
/**
 * loads images from the album to start slideshow
 */
	public void load() {
		obsList = FXCollections.observableArrayList();
		Iterator<Photo> photoIterator = Persistence.getUser(LoginController.getUserindex())
				.getAlbum(AlbumController.currAlbumindex()).photoIter();
		while (photoIterator.hasNext()) {
			
			User.Album.Photo x = photoIterator.next();
			obsList.add(x);
		}
		if (!obsList.isEmpty()) {
			// dont display
			File file = new File(obsList.get(0).getimgpath());
			Image display = new Image(file.toURI().toString());
			slideImg.setImage(display);
		}
//    	File file = new;
//    	Image display = new Image(file.toURI().toString());
//		slideImg.setImage(display);
	}
}
