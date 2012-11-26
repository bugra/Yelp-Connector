package com.github.bugra.yelp_connector;

import com.github.bugra.yelp_connector.MyWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.yelp.v2.Business;
import com.yelp.v2.YelpSearchResult;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.google.gson.Gson;

public class Yelper {
	private final String CONSUMER_KEY = "";
	private final String CONSUMER_SECRET = "";
	private final String TOKEN = "";
	private final String TOKEN_SECRET = "";
	
	private final String YELP_API_URL = "http://api.yelp.com/v2/search";
	private final String ERROR_MESSAGE_GSON = "Error in parsing Gson data!";
	
	@SuppressWarnings("unused")
	private String location;
	@SuppressWarnings("unused")
	private String category; // whether it is restaurant
	@SuppressWarnings("unused")
	private String term;     // whether it is fast-food, chinese, mexican and so on.
	@SuppressWarnings("unused")
	private String latitude;
	@SuppressWarnings("unused")
	private String longitude;
	@SuppressWarnings("unused")
	private String mode;     // Not given(both ranking and distance), 1 => by ranking, 2 => by distance
	public YelpSearchResult places; // return places 
	
	public Yelper(String location){
		this.location = location;
	}
	public Yelper(String location, String category){
		this.location = location;
		this.category = category;
	}
	public Yelper(String location, String category, String term){
		this.location = location;
		this.category = category;
		this.term = term;
	} 
	
	public YelpSearchResult getPlaces(String location){
		
		OAuthService service = new ServiceBuilder().provider(YelpV2API.class).apiKey(CONSUMER_KEY).apiSecret(CONSUMER_SECRET).build();
		Token accessToken = new Token(TOKEN, TOKEN_SECRET);
		OAuthRequest request = new OAuthRequest(Verb.GET, YELP_API_URL);
		
		request.addQuerystringParameter("location", location);
		
		service.signRequest(accessToken, request);
		Response response = request.send();
		String rawData = response.getBody();
		
		try {
			places = new Gson().fromJson(rawData, YelpSearchResult.class);
		} 
		catch(Exception e) {
			System.out.println(ERROR_MESSAGE_GSON);
			System.out.println(rawData);			
		}
		
		return places;
	}
	public YelpSearchResult getPlaces(String location, String category){
		
		OAuthService service = new ServiceBuilder().provider(YelpV2API.class).apiKey(CONSUMER_KEY).apiSecret(CONSUMER_SECRET).build();
		Token accessToken = new Token(TOKEN, TOKEN_SECRET);
		OAuthRequest request = new OAuthRequest(Verb.GET, YELP_API_URL);
		
		request.addQuerystringParameter("location", location);
		request.addQuerystringParameter("category", category);
		
		service.signRequest(accessToken, request);
		Response response = request.send();
		String rawData = response.getBody();
		
		try {
			places = new Gson().fromJson(rawData, YelpSearchResult.class);
		} 
		catch(Exception e) {
			System.out.println(ERROR_MESSAGE_GSON);
			System.out.println(rawData);			
		}
		
		return places;
	}
	
	public YelpSearchResult getPlaces(String location, String category, String term){
		
		OAuthService service = new ServiceBuilder().provider(YelpV2API.class).apiKey(CONSUMER_KEY).apiSecret(CONSUMER_SECRET).build();
		Token accessToken = new Token(TOKEN, TOKEN_SECRET);
		OAuthRequest request = new OAuthRequest(Verb.GET, YELP_API_URL);
		
		request.addQuerystringParameter("location", location);
		request.addQuerystringParameter("category", category);
		request.addQuerystringParameter("term", term);
		
		service.signRequest(accessToken, request);
		Response response = request.send();
		String rawData = response.getBody();
		
		try {
			places = new Gson().fromJson(rawData, YelpSearchResult.class);
		} 
		catch(Exception e) {
			System.out.println(ERROR_MESSAGE_GSON);
			System.out.println(rawData);			
		}
		return places;
	}
public YelpSearchResult getPlaces(String location, String category, String term, String mode){
		
		OAuthService service = new ServiceBuilder().provider(YelpV2API.class).apiKey(CONSUMER_KEY).apiSecret(CONSUMER_SECRET).build();
		Token accessToken = new Token(TOKEN, TOKEN_SECRET);
		OAuthRequest request = new OAuthRequest(Verb.GET, YELP_API_URL);
		
		request.addQuerystringParameter("location", location);
		request.addQuerystringParameter("category", category);
		request.addQuerystringParameter("term", term);
		request.addQuerystringParameter("sort", mode);
		
		service.signRequest(accessToken, request);
		Response response = request.send();
		String rawData = response.getBody();
		System.out.println(rawData);
		try {
			places = new Gson().fromJson(rawData, YelpSearchResult.class);
		} 
		catch(Exception e) {
			System.out.println(ERROR_MESSAGE_GSON);	
		}
		return places;
	}
	public static void printPlaces(YelpSearchResult places){
		System.out.println("Your search found " + places.getTotal() + " results.");
		System.out.println("Yelp returned " + places.getBusinesses().size() + " businesses.");
		System.out.println("Latitude of place is: " + places.getRegion().getCenter().getLatitude());
		System.out.println("Longitude of places is: " + places.getRegion().getCenter().getLongitude());
		System.out.println();

		for(Business biz : places.getBusinesses()) {
			System.out.println(biz.getName());
			System.out.println(biz.getId() +":"+ biz.getReviewCount() +":"+ biz.getSnippetText());
			for(String address : biz.getLocation().getAddress()) {					
				System.out.println("  " + address);
			}
			System.out.println("Distance to location is: " + biz.getDistance());
			System.out.println("Telephone number of the restaurant is: " + biz.getPhone());
			System.out.println("Restaurant gives service for following: " + biz.getCategories());
			System.out.println("  " + biz.getLocation().getCity());
			System.out.println(biz.getUrl());
			System.out.println("-----------------");
		}
	}
	public List<String> getRestaurantNames(YelpSearchResult places){
		List<String> restaurantNames = new ArrayList<String>();
		for(Business biz : places.getBusinesses()){
			restaurantNames.add(biz.getName());
		}
		return restaurantNames;
	}
	public List<String> getFoodTypes(YelpSearchResult places) throws NullPointerException{
		List<String> foodNames = new ArrayList<String>();
		for(Business biz : places.getBusinesses()){
			Iterator<List<String>> iter = biz.getCategories().iterator();
			while(iter.hasNext()){
				for (String elem : iter.next()){
					foodNames.add(elem);
				}
			}
		}
		return foodNames;
	}
	
	public static void main(String[] args) throws IOException {
		String location = "5 Metrotech Center, 11201";
		String category = "restaurants";
		String term = "mexican";
		String sort = "1"; 
		Yelper yelpObject = new Yelper(location, category, term);
		YelpSearchResult places = yelpObject.getPlaces(location, category, term, sort);
		printPlaces(places);
		
		List<String> restaurantNames = yelpObject.getRestaurantNames(places);
		List<String> foodTypes = yelpObject.getFoodTypes(places);
		
		String restaurantNamesFile = "restaurantNames.txt";
		MyWriter restaurantFile = new MyWriter(restaurantNamesFile);
		restaurantFile.writeStringList(restaurantNames);
		
		String foodTypesFile = "foodNames.txt";
		MyWriter foodFile = new MyWriter(foodTypesFile);
		foodFile.writeStringList(foodTypes);
		
		// FOR DEBUGGING
		for (String item : restaurantNames){
			System.out.println(item);
		}
		for (String item : foodTypes){
			System.out.println(item);
		}
		
	}
}
