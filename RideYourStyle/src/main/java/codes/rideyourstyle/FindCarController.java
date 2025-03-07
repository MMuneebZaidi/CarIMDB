package codes.rideyourstyle;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.*;


public class FindCarController implements Initializable {
    @FXML
    void HomeButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RideYourStyle.class.getResource("Main.fxml"));
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        Scene scene;
        if (stage.isMaximized()) {
            scene = new Scene(fxmlLoader.load(), screenSize.getWidth(), screenSize.getHeight());
        } else {
            scene = new Scene(fxmlLoader.load());
        }
        stage.setScene(scene);
    }
    @FXML
    void backButton(ActionEvent ev) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RideYourStyle.class.getResource("Main.fxml"));
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Stage stage = (Stage) (((Node) ev.getSource()).getScene().getWindow());
        Scene scene;
        if (stage.isMaximized()) {
            scene = new Scene(fxmlLoader.load(), screenSize.getWidth(), screenSize.getHeight());
        } else {
            scene = new Scene(fxmlLoader.load());
        }
        stage.setScene(scene);
    }
    @FXML
    private ChoiceBox<String> Engine ;
    @FXML
    private ChoiceBox<String> BodyType ;
    @FXML
    private JFXSlider minPriceSlider;
    @FXML
    private JFXSlider maxPriceSlider;
    @FXML
    private Label minPriceLabel;
    @FXML
    private Label maxPriceLabel;
    @FXML
    private JFXListView<String> vehicleListView;



    int minPrice=10000000;
    int maxPrice=30000000;
    ObservableList<Vehicle> allVehicles = FXCollections.observableArrayList();

    String[] engineRanges = {"ALL","1500 cc - 2999 cc","3000 cc - 4499 cc","4500 cc - 5999 cc","6000 cc - 7499 cc"};
    String[] bodyType = {"ALL","Sedan","SUV","Coupe"};
    public static String colorToHex(Color color) {
        return String.format("#%02X%02X%02X", (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        allVehicles = RideYourStyle.allVehicles;

        Engine.getItems().addAll(engineRanges);
        BodyType.getItems().addAll(bodyType);

        minPriceSlider.setStyle("-fx-custom-color : ORANGE;");
        maxPriceSlider.setStyle("-fx-custom-color : ORANGE;");

        minPriceSlider.valueProperty().addListener((observableValue, number, t1) -> {
            if(minPriceSlider.isFocused()){
                Color imageColor = Color.RED.interpolate(Color.ORANGE,
                        minPriceSlider.getValue() / 100);
                minPriceSlider.setStyle("-fx-custom-color : "+colorToHex(imageColor)+";");
            }
            minPrice = (int) minPriceSlider.getValue();
            minPriceLabel.setText(Integer.toString(minPrice));
        });
        maxPriceSlider.valueProperty().addListener((observableValue, number, t1) -> {
            if(maxPriceSlider.isFocused()){
                Color imageColor = Color.RED.interpolate(Color.ORANGE,
                        maxPriceSlider.getValue() / 100);
                maxPriceSlider.setStyle("-fx-custom-color : "+colorToHex(imageColor)+";");
            }
            maxPrice = (int) maxPriceSlider.getValue();
            maxPriceLabel.setText(Integer.toString(maxPrice));
        });

        Engine.getSelectionModel().selectFirst();
        BodyType.getSelectionModel().selectFirst();

        Engine.valueProperty().addListener(((observableValue, s, t1) -> searchButton()));
        BodyType.valueProperty().addListener(((observableValue, s, t1) -> searchButton()));

        minPriceSlider.setOnMouseReleased(mouseEvent -> searchButton());
        maxPriceSlider.setOnMouseReleased(mouseEvent -> searchButton());
        searchButton();
    }
    ArrayList<String> extractedNames = new ArrayList<>();
    ObservableList<Vehicle> extractedVehicles = FXCollections.observableArrayList();

    int minEngine;
    int maxEngine;

    public void clearAll(){

        extractedVehicles.clear();
        extractedNames.clear();
        vehicleListView.getItems().clear();
    }

    public void applyFilters(){

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
        extractedVehicles.removeIf(vehicle -> !(Integer.parseInt(vehicle.engine) >= minEngine && Integer.parseInt(vehicle.engine) <= maxEngine));
        switch (BodyType.getValue()) {
            case "Sedan" -> extractedVehicles.removeIf(vehicle -> !(Objects.equals(vehicle.bodyType, "Sedan")));
            case "SUV" -> extractedVehicles.removeIf(vehicle -> !(Objects.equals(vehicle.bodyType, "SUV")));
            case "Coupe" -> extractedVehicles.removeIf(vehicle -> !(Objects.equals(vehicle.bodyType, "Coupe")));
            default -> {}
        }
        extractedVehicles.removeIf(vehicle -> !(Integer.parseInt(vehicle.price) >= minPrice && Integer.parseInt(vehicle.price) <= maxPrice));

    }

    public void sortCars(){

        for(int i=0;i<extractedVehicles.size();i++) {
            for (int j = 0; j < extractedVehicles.size(); j++) {
                if (Double.parseDouble(extractedVehicles.get(i).rating) > Double.parseDouble(extractedVehicles.get(j).rating)){
                    Collections.swap(extractedVehicles, j, i);
                }
            }
        }
    }
    String car;
    CarDataSingleton data = CarDataSingleton.getInstance();
    void setCar(Vehicle vehicle){
        car = vehicle.name;
    }
    public static String FXMLSelector;
    @FXML
    void searchButton(){

        clearAll();

        extractedVehicles.addAll(allVehicles);

        applyFilters();
        if(!Objects.equals(Engine.getValue(), "ALL")){
            sortCars();
        }


        for (Vehicle vehicle : extractedVehicles){
            extractedNames.add(vehicle.name);
        }
        if(extractedNames.isEmpty()){
            vehicleListView.getItems().add("No Item Found");
        }

        vehicleListView.getItems().addAll(extractedNames);

        vehicleListView.setOnMouseClicked(event -> {
            FXMLSelector="Finding";
            for(Vehicle vehicle : extractedVehicles){
                if(Objects.equals(vehicleListView.getSelectionModel().getSelectedItem(), vehicle.name)){
                    setCar(vehicle);
                    data.setVehicle(car);
                    FXMLLoader fxmlLoader1 = new FXMLLoader(RideYourStyle.class.getResource("CarDetail.fxml"));
                    Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
                    Stage stage = (Stage) vehicleListView.getScene().getWindow();
                    Scene scene;
                    try {
                        if (stage.isMaximized()) {
                            scene = new Scene(fxmlLoader1.load(), screenSize.getWidth(), screenSize.getHeight());
                        } else {
                            scene = new Scene(fxmlLoader1.load());
                        }                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setScene(scene);
                }
            }
        });
    }

}









