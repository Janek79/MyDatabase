package podatabase;

import podatabase.exceptions.CannotBeNull;

public class User {
	private String username;
	private String password;
	
	public User(String username, String password) {
		if(username == null || password == null) {
			throw new CannotBeNull("Username and password");
		}
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}

	@Override
	public int hashCode() {
		return this.getUsername().length() + this.getPassword().length();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj instanceof User) {
			User us = (User)obj;
			return this.getUsername().equals(us.getUsername()) && this.password.equals(us.getPassword());
		} 
		
		return false;
	}
	
	
	
	
}
