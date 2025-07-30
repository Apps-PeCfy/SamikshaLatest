package com.samiksha.ui.drawer.faqs.POJO;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class FAQResponsePOJO{

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("message")
	private String message;

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"FAQResponsePOJO{" + 
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}


	public class DataItem{

		@SerializedName("question")
		private String question;

		@SerializedName("answer")
		private String answer;

		@SerializedName("id")
		private int id;

		public void setQuestion(String question){
			this.question = question;
		}

		public String getQuestion(){
			return question;
		}

		public void setAnswer(String answer){
			this.answer = answer;
		}

		public String getAnswer(){
			return answer;
		}

		public void setId(int id){
			this.id = id;
		}

		public int getId(){
			return id;
		}

		@Override
		public String toString(){
			return
					"DataItem{" +
							"question = '" + question + '\'' +
							",answer = '" + answer + '\'' +
							",id = '" + id + '\'' +
							"}";
		}
	}
}