package codes.rideyourstyle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CompareController implements Initializable {
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

    String car1;
    String car2;
    @FXML
    private ChoiceBox<String> choicebox1;

    @FXML
    private ChoiceBox<String> choicebox2;
    DataSingleton data = DataSingleton.getInstance();
    @FXML
    void compareButton(ActionEvent ev) throws IOException {
            if(Objects.equals(car1, car2)){
                ImageView img = new ImageView( new Image("Mohtaram.png"));
                Stage stage = new Stage();
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.getChildren().add(img);
                Scene sc = new Scene(anchorPane,300,275);
                stage.setTitle("Bhaggatt");
                stage.setResizable(false);
                stage.setScene(sc);
                stage.show();
            }else {data.setCar1(car1);
                data.setCar2(car2);
                FXMLLoader fxmlLoader = new FXMLLoader(RideYourStyle.class.getResource("ComparedScene.fxml"));
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
    }
    ArrayList<String> carName = new ArrayList<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getDatabaseLink();
            Statement stmt = connectDB.createStatement();
            String query = "(select name from bentley)union" +
                    "(select name from bmw)union" +
                    "(select name from chevrolet)union" +
                    "(select name from mercedes)union" +
                    "(select name from porsche)union" +
                    "(select name from rollsroyce)";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                carName.add(rs.getString("name"));
            }
            choicebox1.getItems().addAll(carName);
            choicebox2.getItems().addAll(carName);
            choicebox1.setOnAction(e -> car1 = choicebox1.getValue());
            choicebox2.setOnAction(e -> car2 = choicebox2.getValue());
        }catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(FindCarController.class.getName()).log(Level.SEVERE,null,e);
        }

    }

}
