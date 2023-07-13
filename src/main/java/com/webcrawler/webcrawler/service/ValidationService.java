package com.webcrawler.webcrawler.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {


public List<String> validate(String url) {
	ArrayList<String> errorMessages = new ArrayList<>();

	if(isNotValidUrl(url)){
	errorMessages.add(String.format("The url:%s provided is invalid",url));
	}

	return errorMessages;
}


public static boolean isNotValidUrl(String url) {
	String regex = "^(https)://([\\w.-]+)(:\\d+)?(/\\S*)?$";
	Pattern pattern = Pattern.compile(regex);
	Matcher matcher = pattern.matcher(url);
	return !matcher.matches();
}

}
