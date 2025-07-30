package com.samiksha.ui.home.pojo;

import com.google.gson.annotations.SerializedName;

public class SignOutResponsePOJO{

	@SerializedName("message")
	private String message;

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"SignOutResponsePOJO{" + 
			"message = '" + message + '\'' + 
			"}";
		}
}