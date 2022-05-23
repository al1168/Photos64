/**
 * 
 * @author Alden Lu
 *	@author  Sam Lee
 */
package model;

import java.io.Serializable;

public class Tag implements Serializable {
	private String name;
	private String value;

	public Tag(String name, String value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * returns the tag name
	 * 
	 * @return String
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * returns the value whether its a person o location tag
	 * 
	 * @return Stringg
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * sets the tagname
	 * 
	 * @param newname
	 */
	public void setName(String newname) {
		this.name = newname;
	}

	/**
	 * set the value (location,person)
	 * 
	 * @param newvalue
	 */
	public void setValue(String newvalue) {
		this.value = newvalue;
	}

	public String toString() {
		return getName() + "," + getValue();
	}

	public boolean equalTo(String name, String value) {
		return this.name.compareTo(name) == 0 && this.value.compareTo(value) == 0;
	}

}
