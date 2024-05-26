import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {

    private static final String API_KEY = "914d2470669b15e97ba6bde5"; // Replace with your actual API key
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the base currency (e.g., USD): ");
        String baseCurrency = scanner.next().toUpperCase();

        System.out.print("Enter the target currency (e.g., EUR): ");
        String targetCurrency = scanner.next().toUpperCase();

        System.out.print("Enter the amount to convert: ");
        double amount = scanner.nextDouble();

        double exchangeRate = fetchExchangeRate(baseCurrency, targetCurrency);
        if (exchangeRate == -1) {
            System.out.println("Failed to fetch exchange rate. Please try again.");
        } else {
            double convertedAmount = amount * exchangeRate;
            System.out.printf("Converted Amount: %.2f %s%n", convertedAmount, targetCurrency);
        }

        scanner.close();
    }

    private static double fetchExchangeRate(String baseCurrency, String targetCurrency) {
        String urlString = API_URL + API_KEY + "/latest/" + baseCurrency;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Extract exchange rate from JSON response
                String jsonResponse = response.toString();
                return parseExchangeRate(jsonResponse, targetCurrency);
            } else {
                System.err.println("Error fetching exchange rate: " + connection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static double parseExchangeRate(String jsonResponse, String targetCurrency) {
        // Simple JSON parsing to extract the exchange rate for the target currency
        String searchKey = "\"" + targetCurrency + "\":";
        int startIndex = jsonResponse.indexOf(searchKey) + searchKey.length();
        int endIndex = jsonResponse.indexOf(",", startIndex);
        if (startIndex > -1 && endIndex > -1) {
            String exchangeRateString = jsonResponse.substring(startIndex, endIndex).trim();
            return Double.parseDouble(exchangeRateString);
        }
        return -1;
    }
}
