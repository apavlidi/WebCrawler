package com.webcrawler.app.web;

import java.util.List;

public class UrlResponse {

private final String url;
private final List<String> links;

public UrlResponse(String url, List<String> links) {
	this.url = url;
	this.links = links;
}

public List<String> getLinks() {
	return links;
}

public String getUrl() {
	return url;
}

}
