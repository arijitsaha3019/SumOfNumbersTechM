package com.att.rest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SumOfNumbersController {
	public static void main(String args[]) throws IOException, ParseException {

		Properties prop = new Properties();
		InputStream is = null;
		int totalNumbersCount = 0;
		long totalRecordSum = 0;
		int counter = 0;

		// Loading the property file and reading url as property
		is = new FileInputStream("config.properties");
		prop.load(is);
		URL url = new URL(prop.getProperty("url"));

		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		// set the request method and properties.
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");

		InputStream ip = con.getInputStream();
		BufferedReader br1 = new BufferedReader(new InputStreamReader(ip));

		// Print the response code
		// and response message from server.
		System.out.println("Response Code:" + con.getResponseCode());
		System.out.println("Response Message:" + con.getResponseMessage());

		StringBuilder response = new StringBuilder();
		String responseSingle = null;
		while ((responseSingle = br1.readLine()) != null) {
			response.append(responseSingle);
		}
		String responseString = response.toString();

		// 1. The rest endpoint will emit an array of
		// JSON documents, each JSON document will
		// be a complete flat (non nested) record.

		System.out.println("\n" + "Complete JSON String and Keys : ");
		System.out.println(responseString);

		// Utilizing JSON Parser to parse the complete JSON
		JSONParser parser = new JSONParser();
		Object parsedObj = parser.parse(responseString);

		JSONArray jsonArray = (JSONArray) parsedObj;
		System.out.println("\n" + "JSON Arrays: ");
		System.out.println(jsonArray);

		Iterator<JSONObject> iterator = jsonArray.iterator();
		System.out.println("\n" + "Individual JSON Objects : ");

		while (iterator.hasNext()) {
			System.out.println(++counter + ".");
			long individualRecordSum = 0;
			JSONObject jsonObject = (JSONObject) (iterator.next());
			System.out.println(jsonObject);

			// 2. For each document display all of the keys of the JSON on standard out
			System.out.println("Keys of the Document : ");
			for (Object key : jsonObject.keySet()) {
				String keyStr = (String) key;
				System.out.println(keyStr);

			}
			// end of solution for #2

			// 3. There will be a json array of integers with the key of "numbers",
			// sum all of the integers and display the sum on standard out, add
			// that sum to a running total for the program
			ArrayList<Long> listOfNumbers = new ArrayList<Long>();
			listOfNumbers = (ArrayList<Long>) jsonObject.get("numbers");
			Iterator<Long> numberIterator = listOfNumbers.iterator();
			while (numberIterator.hasNext()) {
				totalNumbersCount++;
				individualRecordSum = individualRecordSum + numberIterator.next();
			}

			System.out.println("Individual ISON Array Sum  : " + individualRecordSum + "\n");
			totalRecordSum = totalRecordSum + individualRecordSum;

		}

		System.out.println("Total Sum : " + totalRecordSum);
		// end of solution for #3

		// 4.Display the total of the integers that were summed for the execution
		System.out.println("Total Number of Integers : " + totalNumbersCount);

	}
}
