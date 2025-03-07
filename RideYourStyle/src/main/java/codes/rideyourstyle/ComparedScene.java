package codes.rideyourstyle;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ComparedScene implements Initializable {

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
    void BackButton(ActionEvent ev) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RideYourStyle.class.getResource("Compare.fxml"));
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
    private JFXListView<String> car1list;

    @FXML
    private JFXListView<String> car2list;


    @FXML
    private ImageView compimg1;

    @FXML
    private ImageView compimg2;

    ObservableList<Vehicle> list = FXCollections.observableArrayList();
    static Vehicle car1 , car2;
    DataSingleton data = DataSingleton.getInstance();


    static class XCell1 extends ListCell<String> {
        HBox hbox = new HBox();
        Label label = new Label("(empty)");
        Pane pane = new Pane();
        Polygon arrow1 = new Polygon();
        String lastItem;
        public XCell1() {
            super();
            arrow1.getPoints().addAll(-12.0, -45.00001525878906, 8.79998779296875, -45.00001525878906, -1.5999755859375, -56.19999694824219);
            arrow1.setFill(Color.GREEN);
            hbox.getChildren().addAll(label, pane, arrow1);
            hbox.setAlignment(Pos.CENTER);
            HBox.setHgrow(pane, Priority.ALWAYS);
        }
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item,empty);
            if (empty) {
                setText(null);  // No text in label of super class
                lastItem = null;
                setGraphic(null);
            } else {
                lastItem = item;
                label.setText(item!=null ? item : "<null>");
                String[] ar1 = {car1.name,car1.transmissionType,car1.fuelType,car1.bodyType,car1.wheelSize,car1.convertible,car1.price,car1.stock,car2.name,
                        car2.transmissionType,car2.fuelType,car2.bodyType,car2.wheelSize,car2.convertible,car2.price,car2.stock};
                for (String s : ar1) {
                    if (lastItem.equals(s)) {
                        arrow1.setVisible(false);
                    }
                }
                String[] ar2 = {car1.engine,car1.enginePower,car1.topSpeed,car1.acceleration,car1.mileage,car1.fuelCapacity,car1.seatingCapacity,car1.doors,car1.rating};
                String[] ar3 = {car2.engine,car2.enginePower,car2.topSpeed,car2.acceleration,car2.mileage,car2.fuelCapacity,car2.seatingCapacity,car2.doors,car2.rating};
                for(int i = 0; i<ar2.length;i++){
                    if(lastItem.equals(ar2[i])){
                        if(lastItem.equals(car1.mileage)){
                            String[] splitCar1 = ar2[i].split(" ");
                            String[] splitCar2 = ar3[i].split(" ");
                            double avCar1 = ((Double.parseDouble(splitCar1[0])+Double.parseDouble(splitCar1[2]))*0.5);
                            double avCar2 = ((Double.parseDouble(splitCar2[0])+Double.parseDouble(splitCar2[2]))*0.5);
                            if(avCar1<avCar2){
                                arrow1.setRotate(180);
                                arrow1.setFill(Color.RED);

                            } else if (avCar1==avCar2) {
                                arrow1.setVisible(false);
                            }
                        }
                        else if(lastItem.equals(car1.acceleration)){
                            String[] splitCar1 = ar2[i].split("s");
                            String[] splitCar2 = ar3[i].split("s");
                            String car1acc = splitCar1[0];
                            String car2acc = splitCar2[0];
                            if(car1acc.equals(car2acc)){
                                arrow1.setVisible(false);
                            }
                            else if(Double.parseDouble(car1acc) >Double.parseDouble(car2acc)) {
                                arrow1.setRotate(180);
                                arrow1.setFill(Color.RED);

                            }
                        }
                        else if(lastItem.equals(car1.rating)){
                            if(ar2[i].equals(ar3[i])){
                                arrow1.setVisible(false);
                            }
                            else if(Double.parseDouble(ar2[i]) <Double.parseDouble(ar3[i])) {
                                arrow1.setRotate(180);
                                arrow1.setFill(Color.RED);

                            }
                        } else {
                            int i1 = ar2[i].compareTo(ar3[i]);
                            if (i1 == 0) {
                                arrow1.setVisible(false);
                            } else if (i1 < 0) {
                                arrow1.setRotate(180);
                                arrow1.setFill(Color.RED);

                            }
                        }
                    }
                }
                setGraphic(hbox);
            }

        }


    }
    static class XCell2 extends ListCell<String> {
        HBox hbox = new HBox();
        Label label = new Label("(empty)");
        Pane pane = new Pane();
        Polygon arrow1 = new Polygon();
        String lastItem;
        public XCell2() {
            super();
            arrow1.getPoints().addAll(-12.0, -45.00001525878906, 8.79998779296875, -45.00001525878906, -1.5999755859375, -56.19999694824219);
            arrow1.setFill(Color.GREEN);
            hbox.getChildren().addAll(label, pane, arrow1);
            hbox.setAlignment(Pos.CENTER);
            HBox.setHgrow(pane, Priority.ALWAYS);
        }
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item,empty);
            if (empty) {
                setText(null);  // No text in label of super class
                setGraphic(null);
            } else {
                lastItem = item;
                label.setText(lastItem);
                String[] ar1 = {car1.name,car1.transmissionType,car1.fuelType,car1.bodyType,car1.wheelSize,car1.convertible,car1.price,car1.stock,car2.name,
                        car2.transmissionType,car2.fuelType,car2.bodyType,car2.wheelSize,car2.convertible,car2.price,car2.stock};
                for (String s : ar1) {
                    if (lastItem.equals(s)) {
                        arrow1.setVisible(false);
                    }
                }
                String[] ar2 = {car1.engine,car1.enginePower,car1.topSpeed,car1.acceleration,car1.mileage,car1.fuelCapacity,car1.seatingCapacity,car1.doors,car1.rating};
                String[] ar3 = {car2.engine,car2.enginePower,car2.topSpeed,car2.acceleration,car2.mileage,car2.fuelCapacity,car2.seatingCapacity,car2.doors,car2.rating};
                for (int i = 0;i< ar3.length;i++){
                    if(lastItem.equals(ar3[i])) {
                        if (lastItem.equals(car2.mileage)) {
                            String[] splitCar1 = ar3[i].split(" ");
                            String[] splitCar2 = ar2[i].split(" ");
                            double avCar1 = ((Double.parseDouble(splitCar1[0]) + Double.parseDouble(splitCar1[2])) * 0.5);
                            double avCar2 = ((Double.parseDouble(splitCar2[0]) + Double.parseDouble(splitCar2[2])) * 0.5);
                            if (avCar1 < avCar2) {
                                arrow1.setRotate(180);
                                arrow1.setFill(Color.RED);

                            } else if (avCar1 == avCar2) {
                                arrow1.setVisible(false);
                            }
                        } else if (lastItem.equals(car2.acceleration)) {
                            String[] splitCar1 = ar2[i].split("s");
                            String[] splitCar2 = ar3[i].split("s");
                            String car1acc = splitCar1[0];
                            String car2acc = splitCar2[0];
                            if (car1acc.equals(car2acc)) {
                                arrow1.setVisible(false);
                            } else if (Double.parseDouble(car1acc) < Double.parseDouble(car2acc)) {
                                arrow1.setRotate(180);
                                arrow1.setFill(Color.RED);

                            }
                        } else if (lastItem.equals(car2.rating)) {
                            if (ar2[i].equals(ar3[i])) {
                                arrow1.setVisible(false);
                            } else if (Double.parseDouble(ar2[i]) > Double.parseDouble(ar3[i])) {
                                arrow1.setRotate(180);
                                arrow1.setFill(Color.RED);

                            }
                        } else {
                            int i1 = ar3[i].compareTo(ar2[i]);
                            if (i1 == 0) {
                                arrow1.setVisible(false);
                            } else if (i1 < 0) {
                                arrow1.setRotate(180);
                                arrow1.setFill(Color.RED);

                            }
                        }
                    }
                }
                setGraphic(hbox);
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String s1 = data.getCar1();
        String s2 = data.getCar2();
        list = RideYourStyle.allVehicles;

        for(int i = 0; i< RideYourStyle.allVehicles.size(); i++){
            if(list.get(i).name.equals(s1))
                car1 = list.get(i);
            else if(list.get(i).name.equals(s2))
                car2 = list.get(i);
        }
        ObservableList<String> list1 = FXCollections.observableArrayList();
        ObservableList<String> list2 = FXCollections.observableArrayList();
        list1.addAll(car1.name, car1.engine, car1.transmissionType, car1.enginePower, car1.topSpeed, car1.acceleration, car1.mileage, car1.fuelType, car1.fuelCapacity, car1.bodyType, car1.seatingCapacity, car1.doors, car1.wheelSize, car1.convertible, car1.rating, car1.price, car1.stock);
        list2.addAll(car2.name, car2.engine, car2.transmissionType, car2.enginePower, car2.topSpeed, car2.acceleration, car2.mileage, car2.fuelType, car2.fuelCapacity, car2.bodyType, car2.seatingCapacity, car2.doors, car2.wheelSize, car2.convertible, car2.rating, car2.price, car2.stock);
        for(int i=0;i<2;i++) {
            if(i==0) {
                car1list.setItems(list1);
                car1list.setCellFactory(param -> new XCell1());
            }
            if(i==1) {
                car2list.setItems(list2);
                car2list.setCellFactory(param -> new XCell2());
            }
        }
        InputStream is;
        try {
            is = car1.image.getBinaryStream();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        compimg1.setImage(new Image(is));

        InputStream is1;
        try {
            is1 = car2.image.getBinaryStream();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        compimg2.setImage(new Image(is1));
    }
}