package nimesaInternAssignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

public class restApiCall {

	private static final String API_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22";
	private final static String response = fetchDataFromAPI();

	public static void main(String[] args) {
		displayMenu();
	}

	public static void displayMenu() {
		int choice = -1;
		do {
			System.out.println("\nWeather App Menu");
			System.out.println("1. Get weather");
			System.out.println("2. Get Wind Speed");
			System.out.println("3. Get Pressure");
			System.out.println("0. Exit");
			System.out.print("\nEnter your choice: ");

			try {

				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				choice = Integer.parseInt(reader.readLine());

				switch (choice) {
				case 1:
					System.out.print("Enter the date (yyyy-MM-dd): ");
					String inputDate = reader.readLine();
					String date = dateFormatter(inputDate);
					getWeatherForDate(date);
					break;
				case 2:
					System.out.print("Enter the date (yyyy-MM-dd): ");
					inputDate = reader.readLine();
					date = dateFormatter(inputDate);
					getWindSpeedForDate(date);
					break;
				case 3:
					System.out.print("Enter the date (yyyy-MM-dd): ");
					inputDate = reader.readLine();
					date = dateFormatter(inputDate);
					getPressureForDate(date);
					break;
				case 0:
					System.out.println("\nExiting the program.\n");
					break;
				default:
					System.out.println("\nInvalid choice. Please try again.\n");
					break;
				}
				
			} catch (Exception e) {
				System.out.println("Error: Enter a valid choice. " + e.getMessage());
			}

		} while (choice != 0);
	}

	public static void getWeatherForDate(String inputDate) throws Exception {
		if(response == null) {
			throw new Exception("\nResponse form API is null. Data cannot be fetched.");
		}
		String jsonData = response;
		JSONObject weatherData = findDataForDate(jsonData, inputDate);

		if (weatherData != null) {
			JSONObject mainData = weatherData.getJSONObject("main");
			double temperature = mainData.getDouble("temp");
			double tempMin = mainData.getDouble("temp_min");
			double tempMax = mainData.getDouble("temp_max");

			System.out.println("\nTemperature for " + inputDate + ": " + temperature + " °K");
			System.out.println("Minimum Temperature for " + inputDate + ": " + tempMin + " °K");
			System.out.println("Maximum Temperature for " + inputDate + ": " + tempMax + " °K");
		} else {
			System.out.println("\nWeather data not found for the input date.");
		}
	}

	public static void getWindSpeedForDate(String inputDate) throws Exception {
		if(response == null) {
			throw new Exception("\nResponse form API is null. Data cannot be fetched.");
		}
		String jsonData = response;
		JSONObject weatherData = findDataForDate(jsonData, inputDate);
		
		if (weatherData != null) {
	        JSONObject windData = weatherData.getJSONObject("wind");
	        double windSpeed = windData.getDouble("speed");

	        System.out.println("\nWind Speed for " + inputDate + ": " + windSpeed + " m/s");
	    } else {
	        System.out.println("\nWind speed data not found for the input date.");
	    }
	}

	public static void getPressureForDate(String inputDate) throws Exception {
		if(response == null) {
			throw new Exception("\nResponse form API is null. Data cannot be fetched.");
		}
		String jsonData = response;
		JSONObject weatherData = findDataForDate(jsonData, inputDate);
		
		if (weatherData != null) {
	        JSONObject mainData = weatherData.getJSONObject("main");
	        double pressure = mainData.getDouble("pressure");

	        System.out.println("\nPressure for " + inputDate + ": " + pressure + " hPa");
	    } else {
	        System.out.println("\nPressure data not found for the input date.");
	    }

	}

	public static String fetchDataFromAPI() {
		try {
			URL url = new URL(API_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();
			return response.toString();
		} catch (IOException e) {
			System.out.println("Error fetching data from API: " + e.getMessage());
			return null;
		}
	}

	public static JSONObject findDataForDate(String jsonData, String inputDate) {
		JSONObject jsonObject = new JSONObject(jsonData);
	    JSONArray listArray = jsonObject.getJSONArray("list");

	    for (int i = 0; i < listArray.length(); i++) {
	        JSONObject weatherData = listArray.getJSONObject(i);
	        String dt_txt = weatherData.getString("dt_txt").split(" ")[0]; // Get only the date part

	        if (dt_txt.equals(inputDate)) {
	            return weatherData;
	        }
	    }

	    return null;
	}

	public static String dateFormatter(String inputDate) throws Exception {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedDate = null;
		try {
			LocalDate parsedDate = LocalDate.parse(inputDate, formatter);

			// Converting the parsed date back to string
			formattedDate = parsedDate.format(formatter);
		} catch (Exception e) {
			System.out.println("\nEnter a valid date in given format. " + e.getMessage());
		}

		return formattedDate;
	}
}

