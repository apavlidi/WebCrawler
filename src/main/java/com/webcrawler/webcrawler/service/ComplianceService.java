package com.webcrawler.webcrawler.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ComplianceService {
private final List<String> disallowedUrls;

public ComplianceService() {
	this.disallowedUrls = new ArrayList<>();
}


public List<String> retrieveDisallowedPages(String domainName) {
	try {
	domainName = removeTrailingSlashIfExists(domainName);

	URL url = new URL(domainName + "/robots.txt");
	BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	String inputLine = in.readLine();

	while (inputLine != null) {
		if (inputLine.contains("Disallow:")) {
		String[] disallowedUrl = inputLine.split(" ");

		disallowedUrls.add(domainName + disallowedUrl[1]);
		}

		inputLine = in.readLine();
	}
	in.close();
	} catch (Exception e) {
	e.printStackTrace();
	}

	return disallowedUrls;
}

private String removeTrailingSlashIfExists(String domainName) {
	if (domainName.endsWith("/")) {
	domainName = domainName.substring(0, domainName.length() - 1);
	}
	return domainName;
}


}
