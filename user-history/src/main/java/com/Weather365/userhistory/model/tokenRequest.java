package com.Weather365.userhistory.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class tokenRequest{

	@SerializedName("token")
	private String token;

	public String getToken(){
		return token;
	}
}