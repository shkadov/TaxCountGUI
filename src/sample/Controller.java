package sample;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.net.URL;
import java.net.URLConnection;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField sumTextField;

    @FXML
    private TextField commercialCourseTextField;


    @FXML
    private TextField dateTextField;

    @FXML
    private Button countButton;

    @FXML
    private TextArea resultTextArea;


    @FXML
    void initialize() {

        countButton.setOnAction(event -> {
            Double sumValue = Double.parseDouble(sumTextField.getText().trim());
            Double courseValue = Double.parseDouble(commercialCourseTextField.getText().trim());
            Integer dateValue = Integer.parseInt(dateTextField.getText().trim());



            if (!sumValue.equals("") && !courseValue.equals("") && !dateValue.equals("")) {
                countTaxes(sumValue, courseValue, dateValue);
            } else {
                resultTextArea.setText("Please pay attention");
            }

        });



        resultTextArea.setEditable(false);

    }


    private void countTaxes(Double sumValue, Double courseValue, Integer dateValue) {

        class NbuCourse {


            public double getJson() {
                try {

                    JSONParser parser = new JSONParser();

                    double rate = 0;
                    URL url = new URL("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?valcode=USD&date=" + dateValue + "&json");
                    URLConnection urlconn = url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        JSONArray a = (JSONArray) parser.parse(inputLine);

                        for (Object o : a) {
                            JSONObject tutorials = (JSONObject) o;

                            rate = (Double) tutorials.get("rate");

                        }

                    }
                    in.close();
                    return rate;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;

            }
        }


                NbuCourse q = new NbuCourse();
                double value = q.getJson();
                String formattedvalue = String.format("%.2f", value);
                double taxrate = ((sumValue / 2 * courseValue) + (sumValue / 2 * value)) / 100 * 5;
                String formattedTaxRate = String.format("%.2f", taxrate);
                resultTextArea.setText("Taxes are: " + formattedTaxRate + " + 819.06" + "\nCurrent NBU exchange course: " + formattedvalue);



    }


}