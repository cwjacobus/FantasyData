package fantasydata;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class FantasyData {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			String URL = "https://api.fantasydata.net/v3/nfl/odds/json/GameOddsByWeek/2018/1?key=eea45baa72c64bc6bd003b511e9e36d0";
			URL obj = new URL(URL);
			HttpURLConnection con = (HttpURLConnection)obj.openConnection();
			int responseCode = con.getResponseCode();
			//System.out.println("Response Code : " + responseCode);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())); 
			JSONArray all = new JSONArray(in.readLine());
			in .close();
			for (int i = 0; i < all.length(); i++) {
				JSONObject game = all.getJSONObject(i);
				System.out.print(game.getString("AwayTeamName") + " at " + game.getString("HomeTeamName"));
				System.out.println(" (" + game.getString("DateTime") + ")");
				JSONArray preGameOddsArray = game.getJSONArray("PregameOdds");
				String ou = "O/U: ";;
				String spread = "Spd: ";
				for (int j = 0; j < preGameOddsArray.length(); j++) {
					JSONObject gameOdds = preGameOddsArray.getJSONObject(j);
					ou += formatNumber(gameOdds.getString("OverUnder")) + "(" + gameOdds.getString("Sportsbook") + ")"+ " ";
					spread += game.getString("HomeTeamName") + " " + formatSpread(gameOdds.getString("HomePointSpread")) + "(" + gameOdds.getString("Sportsbook") + ")"+ " ";
				}
				System.out.println(ou);
				System.out.println(spread + "\n");
			}
		 }
		catch (Exception e) {
            System.out.println(e.getMessage());
        }
	}
	
	private static String formatNumber(String oldNum) {
		String numPart1 = "";
		String numPart2 = "";
		String[] numArray = oldNum.split("\\.");
		numPart1 = numArray[0].replaceAll("-", ""); // Remove the -
		if (Integer.parseInt(numArray[1]) >= 4 && Integer.parseInt(numArray[1]) <= 7) {
			numPart2 = ".5";	
		}
		else if (Integer.parseInt(numArray[1]) >= 8){
			numPart1 = Integer.toString(Integer.parseInt(numPart1) + 1);
		}
		if (oldNum.indexOf("-") != -1) { // Put the - back on for favs
			numPart1 = "-" + numPart1;
		}
		return numPart1 + numPart2;
	}
	
	private static String formatSpread(String oldNum) { 
		String formattedNumber = formatNumber(oldNum);
		
		if (formattedNumber.indexOf("-") == -1) { // Add a + for dogs
			formattedNumber = "+" + formattedNumber;
		}
		return formattedNumber;
	}

}
