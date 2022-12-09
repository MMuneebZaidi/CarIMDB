package codes.rideyourstyle;
import com.mysql.cj.util.StringInspector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FindCarController implements Initializable {
    @FXML
    void backButton(ActionEvent ev) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RideYouStyle.class.getResource("Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
        Stage stage = (Stage) (((Node)ev.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private ChoiceBox<String> Engine ;
    @FXML
    private ChoiceBox<String> BodyType ;
    @FXML
    private Slider minPriceSlider;
    @FXML
    private Slider maxPriceSlider;
    @FXML
    private Label minPriceLabel;
    @FXML
    private Label maxPriceLabel;
    @FXML
    private ListView<String> vehicleListView;


    int minPrice;
    int maxPrice;
    ObservableList<Vehicle> allVehicles = FXCollections.observableArrayList();

    String[] engineRanges = {"None","1500 cc - 2999 cc","3000 cc - 4499 cc","4500 cc - 5999 cc","6000 cc - 7499 cc"};
    String[] bodyType = {"Sadan","SUV","Coupe"};
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDatabaseLink();
        Engine.getItems().addAll(engineRanges);
        BodyType.getItems().addAll(bodyType);

        String query;

        for(int i=1;i<=6;i++){
            if(i==1) {
                query = "SELECT * FROM bentley";
            }else if (i==2){
                query = "SELECT * FROM bmw";
            }else if (i==3){
                query = "SELECT * FROM chevrolet";
            }else if (i==4){
                query = "SELECT * FROM mercedes";
            }else if (i==5){
                query = "SELECT * FROM porsche";
            }else {
                query = "SELECT * FROM rollsroyce";
            }
            try{
                Statement stm = connectDB.createStatement();
                ResultSet output = stm.executeQuery(query);

                while (output.next()){

                    String name = output.getString("Name");
                    String engine = output.getString("Engine");
                    String transmissionType = output.getString("Transmission Type");
                    String enginePower = output.getString("Engine Power");
                    String topSpeed = output.getString("Top Speed");
                    String acceleration = output.getString("Acceleration");
                    String mileage = output.getString("Mileage");
                    String fuelType = output.getString("Fuel Type");
                    String fuelCapacity = output.getString("Fuel Tank");
                    String bodyType = output.getString("Body Type");
                    String seatingCapacity = output.getString("Seating Capacity");
                    String doors = output.getString("Doors");
                    String wheelSize = output.getString("Wheel Size");
                    String convertible = output.getString("Convertible");
                    String rating = output.getString("Rating");
                    String price = output.getString("Price");
                    String stock = output.getString("Stock");

                    allVehicles.add(new Vehicle(name,  engine,  transmissionType,  enginePower,  topSpeed,  acceleration,  mileage,  fuelType,  bodyType,  price,  rating,  seatingCapacity,  convertible,  doors,  wheelSize,  fuelCapacity, stock));

                }
            }catch (Exception e){
                Logger.getLogger(FindCarController.class.getName()).log(Level.SEVERE,null,e);
            }
        }
//        vehicleListView.getItems().addAll(allVehiclesName);
//        minPriceSlider.valueProperty().addListener((observableValue, number, t1) -> {
//            minPrice = (int) minPriceSlider.getValue();
//            minPriceLabel.setText(Integer.toString(minPrice));
//        });
//        maxPriceSlider.valueProperty().addListener((observableValue, number, t1) -> {
//            maxPrice = (int) maxPriceSlider.getValue();
//            maxPriceLabel.setText(Integer.toString(maxPrice));
//        });

    }

    @FXML
    void searchButton(ActionEvent ev){
        int minEngine;
        int maxEngine;
        switch (Engine.getValue()) {
            case "1500 cc - 2999 cc" -> {
                minEngine = 1500;
                maxEngine = 2999;
            }
            case "3000 cc - 4499 cc" -> {
                minEngine = 3000;
                maxEngine = 4499;
            }
            case "4500 cc - 5999 cc" -> {
                minEngine = 4500;
                maxEngine = 5999;
            }
            case "6000 cc - 7499 cc" -> {
                minEngine = 6000;
                maxEngine = 7499;
            }
            default -> {
                minEngine = 1500;
                maxEngine = 7499;
            }
        }
    }
}



