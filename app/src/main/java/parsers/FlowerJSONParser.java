package parsers;

import java.util.ArrayList;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.Flower;

public class FlowerJSONParser {
	public static List<Flower> parseFeed(String content) {
		try {
			JSONArray ar = new JSONArray(content);
			List<Flower> flowerList = new ArrayList<>();
			for (int i = 0; i < ar.length(); i++) {
				JSONObject obj = ar.getJSONObject(i);
				Flower flower = new Flower();
				flower.setName(obj.getString("name"));
				flowerList.add(flower);
			}
			
			return flowerList;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
