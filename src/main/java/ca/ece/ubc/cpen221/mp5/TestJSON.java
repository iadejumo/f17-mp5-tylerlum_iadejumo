package ca.ece.ubc.cpen221.mp5;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class TestJSON {

	
	public static void main(String[] args) throws org.json.simple.parser.ParseException, FileNotFoundException, IOException {

		/*
		String fileName = "data\\users.json";
>>>>>>> 397dda50fc7cd5d707c1c90dba18dc886183184d
		BufferedReader br =null;
        JSONParser parser = new JSONParser();

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader(fileName));

            while ((sCurrentLine = br.readLine()) != null) {
                System.out.println("Record:\t" + sCurrentLine);

                Object obj;
                try {
                    obj = parser.parse(sCurrentLine);
                    JSONObject jsonObject = (JSONObject) obj;
                    
                    User r = new User(jsonObject);
                    //System.out.println("Categories:\t" + r.votes);
/*
                    String rel = (String) jsonObject.get("url");
                    System.out.println(rel);

                    Double start = (Double) jsonObject.get("longitude");
                    System.out.println(start);

                    Boolean end = (Boolean) jsonObject.get("open");
                    System.out.println(end);

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
 */
		String userFile = "data\\users.json";
		String restaurantFile = "data\\restaurants.json";
		String reviewFile = "data\\reviews.json";
				
		YelpDB yelpDB = new YelpDB(restaurantFile, reviewFile, userFile);
		
		System.out.println("Done");
		
    }
	
	public static void readJsonFile() {

        BufferedReader br =null;
        JSONParser parser = new JSONParser();

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader("C:/Users/tlum1/f17-mp5-tylerlum_iadejumo/data/restaurants.json"));

            while ((sCurrentLine = br.readLine()) != null) {
                System.out.println("Record:\t" + sCurrentLine);

                Object obj;
                try {
                    obj = parser.parse(sCurrentLine);
                    JSONObject jsonObject = (JSONObject) obj;

                    String rel = (String) jsonObject.get("rel");
                    System.out.println(rel);

                    String start = (String) jsonObject.get("start");
                    System.out.println(start);

                    String end = (String) jsonObject.get("end");
                    System.out.println(end);

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
