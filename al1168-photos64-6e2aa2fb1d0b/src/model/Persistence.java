/**
 * @author Alden Lu
 * @author Sam Lee
 */
package model;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author Alden basically just to keep track of users
 */
public class Persistence {

	static List<User> users = new ArrayList<User>();// list of users

	public static final String storeDir = "data";
	public static final String storeFile = "Users.dat";

	/**
	 * 
	 * @param user adds user
	 */
	public static void addUser(User user) {
		users.add(user);
	}

	/**
	 * removes user at index i
	 * 
	 * @param i
	 * 
	 */
	public static void removeUser(int i) { // removes user at index i
		users.remove(i);
	}

	/**
	 * 
	 * @param i
	 * @return user returns a user at a given index
	 */
	public static User getUser(int i) { // return User @ index
		return users.get(i);
	}

	/**
	 * return interator for suer
	 * 
	 * @return Iterator<user>
	 */
	public static Iterator<User> userIterator() { // iterator to iterate through each user
		return users.iterator();
	}

	/**
	 * saves user and all its data
	 * 
	 * @throws IOException
	 */
	public static void writeUsers() throws IOException {

		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		os.writeObject(users);
		os.close();
	}

	@SuppressWarnings("unchecked")
	/**
	 * reads the serialized data
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void readUsers() throws IOException, ClassNotFoundException, ParseException {
		String fileName = "users.dat";

		File file = new File("users.dat");

		User.Album stockalbum = new User.Album("Stock album");
		User stock = new User("stock");

		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		String s = format.format(file.lastModified());
		Date date = format.parse(s);

		String[] stockimages = { "data/box.jpg", "data/cat.jpg", "data/catrainbow.jpg", "data/dice.jpg",
				"data/elephant.jpg", "data/lego.jpg", "data/plane.jpg" };
		String[] imgnames = { "box", "cat", "catrainbow", "dice", "elephant", "lego", "plane" };

		if (file.length() == 0) {
			users.add(stock);
			stock.addAlbum(stockalbum);
			for (int i = 0; i < stockimages.length; i++) {
				User.Album.Photo newphoto = new User.Album.Photo(imgnames[i], date, stockimages[i]);
				stockalbum.addPhotos(newphoto);
			}
		}
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
			users = (List<User>) is.readObject();

		} catch (EOFException f) {
		} catch (FileNotFoundException x) {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}