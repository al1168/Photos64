/**
 * 
 * @author Alden Lu
 *	@author  Sam Lee
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javafx.scene.image.Image;

/**
 * User Class
 */
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private String Username;

	private List<Album> albums = new ArrayList<Album>();

	/**
	 * User Constructor
	 * 
	 * @param username
	 */
	public User(String username) {
		this.Username = username;
	}

	/**
	 * returns the Album at that index
	 * 
	 * @param index
	 * @return Album
	 */
	public User.Album getAlbum(int index) {
		return albums.get(index);
	}

	/**
	 * return Username
	 * 
	 * @return String
	 */
	public String getUsername() {
		return Username;
	}

	/**
	 * set the username
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		this.Username = username;
	}

//	
	public String toString() {
		return getUsername();
	}

	/**
	 * reuturn album Iterator of the user
	 * 
	 * @return Iterator<Album>
	 */
	public Iterator<Album> alIter() {
		return albums.iterator();
	}

	/**
	 * adds a new Album to the album list
	 * 
	 * @param newAl
	 */
	public void addAlbum(Album newAl) {
		albums.add(newAl);
	}

	/**
	 * removes the album based on index
	 * 
	 * @param index
	 */
	public void deleteAlbum(int index) {
		albums.remove(index);
	}

	/**
	 * returns a index based on the name of the album
	 * 
	 * @param alname
	 * @return int
	 */
	public int indexofAlbum(String alname) {
		int i = 0;
		System.out.println(albums);
		for (Album x : albums) {
			System.out.println(x.getAlbum_name());
			if (x.getAlbum_name().equals(alname)) {
				System.out.println(x.getAlbum_name());
				return i;
			}
			i++;
		}
		return -1;
	}

	/**
	 * given an index and a name , it will change album name at that index
	 * 
	 * @param index
	 * @param name
	 */
	public void editname(int index, String name) {
		albums.get(index).setAlbum_name(name);
	}

	/**
	 * returns if the album exist or not based on the name
	 * 
	 * @param textbox
	 * @return boolean
	 */
	public boolean alexist(String textbox) {
		// TODO Auto-generated method stub
		// boolean that check for album name dups
		for (Album x : albums) {
			if (x.getAlbum_name().equals(textbox)) {
				return true;
			}
		}
		return false;
	}

	public static class Album implements Serializable {
		private String Album_name;
		private int numPhotos = 0;
		private String daterange = "";
		private List<Photo> photos = new ArrayList<Photo>(); // list of photos in the album

		/**
		 * Constructor fields: album_name
		 * 
		 * @param album_name
		 */
		public Album(String album_name) {
			Album_name = album_name;

		}

		/**
		 * addPhoto to album
		 * 
		 * @param x
		 */
		public void addPhotos(Photo x) {
			photos.add(x);
			numPhotos++;
		}

		/**
		 * @param index delete photo at given index
		 */
		public void deletePhotos(int index) {
			photos.remove(index);
			numPhotos--;
		}

		/**
		 * 
		 * @return String return album name
		 */
		public String getAlbum_name() {
			return Album_name;
		}

		/**
		 * @param album_name setAlbumname
		 */
		public void setAlbum_name(String album_name) {
			Album_name = album_name;
		}

		/**
		 * @return return getNumPhotos
		 */
		public int getNumPhotos() {
			return numPhotos;
		}

		/**
		 * 
		 * @param numPhotos
		 */
		public void setNumPhotos(int numPhotos) {
			this.numPhotos = numPhotos;
		}

		/**
		 * gets date range
		 * 
		 * @return
		 */
		public String getDaterange() {
			return daterange;
		}

		/**
		 * 
		 * @param daterange
		 */
		public void setDaterange(String daterange) {
			this.daterange = daterange;
		}

		/**
		 * 
		 * @return List
		 */
		public List<Photo> getPhotos() {
			return photos;
		}

		/**
		 * @param photos set photos
		 */
		public void setPhotos(List<Photo> photos) {
			this.photos = photos;
		}

		/**
		 * @return Iterator<User.Album.Photo> return iterator of photos in album
		 */
		public Iterator<User.Album.Photo> photoIter() {
			return photos.iterator();
		}

		public static class Photo implements Serializable {
			private String name;
			private Date date;
			private String caption = "";
			private String imgpath;
			private List<Tag> tags = new ArrayList<Tag>();

			/**
			 * 
			 * @param name
			 * @param date
			 * @param x    Constructor for photo
			 */
			public Photo(String name, Date date, String x) {
				this.name = name;
				this.date = date;
				this.imgpath = x;
			}

			/**
			 * @return Stirng image path of photo
			 */
			public String getimgpath() {
				return imgpath;
			}

			/**
			 * @param x set the path
			 */
			public void setimgpath(String x) {
				this.imgpath = x;
			}

			/**
			 * @param name
			 * @param value
			 * @return boolean adds tag
			 */
			public boolean addTag(String name, String value) {
				for (Iterator<Tag> tag = tags.iterator(); tag.hasNext();) {

					Tag x = tag.next();
					if (x.equalTo(name, value)) {
						return false;
					}
				}
				tags.add(new Tag(name, value));
				return true;
			}

			/**
			 * remove tag
			 * 
			 * @param name
			 * @param value
			 * @return boolean
			 */
			public boolean removeTag(String name, String value) {
				for (Iterator<Tag> tag = tags.iterator(); tag.hasNext();) {
					Tag x = tag.next();
					if (x.equalTo(name, value)) {
						tag.remove();
						return true;
					}
				}
				return false;
			}

			/**
			 * @param name
			 * @return String gets the tags
			 */
			public String getTags(String name) {
				StringBuilder tagslist = new StringBuilder();

				for (Iterator<Tag> x = tags.iterator(); x.hasNext();) {
					Tag tag = x.next();
					String curtag[] = tag.toString().split(",");
					if (curtag[0].equals(name)) {
						tagslist.append(curtag[1] + ", ");
					}

				}
				String tagsliststring = tagslist.toString();
				if (tagsliststring.length() > 0) {
					tagsliststring = tagsliststring.substring(0, tagsliststring.length() - 2);
				}
				return tagsliststring;
			}

			/**
			 * @return Stringg gets name of photo
			 */
			public String getName() {
				return name;
			}

			/**
			 * @param name setName
			 */
			public void setName(String name) {
				this.name = name;
			}

			/**
			 * @return Date return date of photo
			 */
			public Date getDate() {
				return date;
			}

			/**
			 * @param date set date
			 */
			public void setDate(Date date) {
				this.date = date;
			}

			/**
			 * @return String gets the caption of photo
			 */
			public String getCaption() {
				return caption;
			}

			/**
			 * @param caption sets the caption
			 */
			public void setCaption(String caption) {
				this.caption = caption;
			}

			/**
			 * 
			 * @return List<Tag>
			 */
			public List<Tag> getPhotoTags() {
				return tags;
			}

			/**
			 * @return Iterator<Tag> returns an iterator for Tags
			 */
			public Iterator<Tag> TagIter() {
				return tags.iterator();
			}

			/**
			 * @return String returns the name
			 */
			@Override
			public String toString() {
				return this.name;
			}

			/**
			 * 
			 * @param x
			 * @return boolean compares 2 photos to check if they are equal
			 */
			public boolean equals(Photo x) {
				if (this.name == x.getName() && this.imgpath == x.getimgpath()) {
					return true;
				}
				return false;
			}
		}
	}
}
