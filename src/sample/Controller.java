package sample;

import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Controller {

    RocksDB dataBase;

    @FXML
    TableView tableView = new TableView();

    @FXML
    TableView resultTableView = new TableView();

    @FXML
    Button addButton;
    @FXML
    Button editButton;
    @FXML
    Button deleteButton;
    @FXML
    Button refreshButton;
    @FXML
    Button calcButton;
    @FXML
    TextField m1TextField;
    @FXML
    TextField m2TextField;
    @FXML
    ChoiceBox<Integer> waterTempChoiceBox;
    @FXML
    TextField densityResultTextField;

    // temp, density
    HashMap<Integer, Double> waterDensitiesMap;

    public Controller()
    {
    }


    @FXML
    private void handleCalcButton(ActionEvent event)
    {
        if(m1TextField.getText().length()>0 && m1TextField.getText().length()>0)
        {
            double m1 = Double.parseDouble(m1TextField.getText());
            double m2 = Double.parseDouble(m2TextField.getText());
            int waterTemp = waterTempChoiceBox.getValue();
            densityResultTextField.setText(String.valueOf(calcDensity(m1,m2,waterTemp)));
        }

        double density = Double.parseDouble(densityResultTextField.getText());

        try {
            resultTableView.getItems().addAll(dataBase.getByDensity(density));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private double calcDensity(double m1, double m2, int waterTemp)
    {
        double A = m1/m2;
        System.out.println("m1/m2= " + A);
        double density = A * waterDensitiesMap.get(waterTemp);
        System.out.println("calcualted density = " + density);

        return density;
    }

    @FXML
    private void handleAddButton(ActionEvent event)
    {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample\\AddDataWindow.fxml"));
            Parent root = loader.load();
            AddDataController addDataController = loader.getController();

            addDataController.setBaseController(this);


            Stage stage = new Stage();
            stage.setTitle("Dodawanie skały");
            stage.setScene(new Scene(root, 800, 500));
            stage.show();
            // Hide this current window (if this is what you want)
            //((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        refreshTable();
    }

    @FXML
    private void handleEditButton(ActionEvent event)
    {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample\\AddDataWindow.fxml"));
            Parent root = loader.load();
            AddDataController addDataController = loader.getController();
            addDataController.setBaseController(this);

            Rock rock = (Rock) tableView.getSelectionModel().getSelectedItem();
            addDataController.initEditData(rock);
            Stage stage = new Stage();
            stage.setTitle("Edycja skały");
            stage.setScene(new Scene(root, 800, 500));
            stage.show();
            // Hide this current window (if this is what you want)
            //((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handleDeleteButton(ActionEvent event)
    {
        Rock rock;
        try
        {
           rock = (Rock) tableView.getSelectionModel().getSelectedItem();
           dataBase.deleteData(rock.getId());
        }
        catch (NullPointerException ex)
        {
            System.out.println("[!]> Nie wybrano wiersza");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        refreshTable();
    }


    @FXML
    private void handleRefreshButton(ActionEvent event)
    {
        refreshTable();
    }

    public void refreshTable()
    {
        try {
           tableView.getItems().clear();
           tableView.getItems().addAll(dataBase.getAll());
        }
        catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showPhotosView(List<MyImageView> myImageViews)
    {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample\\photosWindow.fxml"));
            Parent root = loader.load();
            PhotosViewController photosViewController = loader.getController();

            List<ImageView> images = new ArrayList<>();
            for (MyImageView myImageView : myImageViews)
                images.add(new ImageView(myImageView.getOriginalSizeImage()));

            photosViewController.loadPhotos(images);


            Stage stage = new Stage();
            stage.setTitle("Podgląd");
            stage.setScene(new Scene(root, 1000, 800));
            stage.show();

            // Hide this current window (if this is what you want)
            //((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    private void initialize()
    {

       dataBase = new RocksDB();
       //dataBase.init();

       int temps[] = {17,18,19,20,21,22,23,24,25,26,27};
       double waterDensities[] = {0.998,0.998,0.998,0.998,0.998,0.997,0.997,0.997,0.997,0.996,0.996};

       waterDensitiesMap = new HashMap<>();
        for (int i = 0; i < temps.length; i++) {
            waterDensitiesMap.put(temps[i],waterDensities[i]);
        }

        waterTempChoiceBox.setItems(FXCollections.observableArrayList(waterDensitiesMap.keySet()));
        waterTempChoiceBox.setValue(20);

       // dataBase.init();


        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        resultTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // wyswietlanie w tebeli, definicja kolumn
        TableColumn<Integer, Rock> column0 = new TableColumn<>("Id");
        column0.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<String, Rock> column1 = new TableColumn<>("Nazwa");
        column1.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<String, Rock> column2 = new TableColumn<>("Gestosc_min");
        column2.setCellValueFactory(new PropertyValueFactory<>("density_Min"));

        TableColumn<String, Rock> column3 = new TableColumn<>("Gestosc_max");
        column3.setCellValueFactory(new PropertyValueFactory<>("density_Max"));

        TableColumn<String, Rock> column4 = new TableColumn<>("Ferromagnetyk");
        column4.setCellValueFactory(new PropertyValueFactory<>("ferromagnetic"));

        TableColumn<String, Rock> column5 = new TableColumn<>("*Rodzaj");
        column5.setCellValueFactory(new PropertyValueFactory<>("type1"));

        TableColumn<String, Rock> column6 = new TableColumn<>("*Rodzaj2");
        column6.setCellValueFactory(new PropertyValueFactory<>("type2"));

        TableColumn<String, Rock> column7 = new TableColumn<>("*Opis");
        column7.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Rock, MyImageView> column8 = new TableColumn<Rock, MyImageView>("Photo_1");
        column8.setCellValueFactory(new PropertyValueFactory<Rock, MyImageView>("photo1"));

        TableColumn<Rock, MyImageView> column9 = new TableColumn<>("Photo_2");
        column9.setCellValueFactory(new PropertyValueFactory<>("photo2"));

        TableColumn<Rock, MyImageView> column10 = new TableColumn<>("Photo_3");
        column10.setCellValueFactory(new PropertyValueFactory<>("photo3"));

        TableColumn<Rock, MyImageView> column11 = new TableColumn<>("Photo_4");
        column11.setCellValueFactory(new PropertyValueFactory<>("photo4"));

        TableColumn<Rock, MyImageView> column12 = new TableColumn<>("Photo_5");
        column12.setCellValueFactory(new PropertyValueFactory<>("photo5"));

        TableColumn columns[] = {column0,column1,column2,column3,column4,column5,column6
                                    ,column7,column8,column9,column10,column11,column12};


        for (int i = 0; i < columns.length; i++)
        {
            tableView.getColumns().add(columns[i]);
        }


        TableColumn<Integer, Rock> resultColumn0 = new TableColumn<>("Id");
        resultColumn0.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<String, Rock> resultColumn1 = new TableColumn<>("Nazwa");
        resultColumn1.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<String, Rock> resultColumn2 = new TableColumn<>("Gestosc_min");
        resultColumn2.setCellValueFactory(new PropertyValueFactory<>("density_Min"));

        TableColumn<String, Rock> resultColumn3 = new TableColumn<>("Gestosc_max");
        resultColumn3.setCellValueFactory(new PropertyValueFactory<>("density_Max"));

        TableColumn<String, Rock> resultColumn4 = new TableColumn<>("Ferromagnetyk");
        resultColumn4.setCellValueFactory(new PropertyValueFactory<>("ferromagnetic"));

        TableColumn<String, Rock> resultColumn5 = new TableColumn<>("*Rodzaj");
        resultColumn5.setCellValueFactory(new PropertyValueFactory<>("type1"));

        TableColumn<String, Rock> resultColumn6 = new TableColumn<>("*Rodzaj2");
        resultColumn6.setCellValueFactory(new PropertyValueFactory<>("type2"));

        TableColumn<String, Rock> resultColumn7 = new TableColumn<>("*Opis");
        resultColumn7.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Rock, MyImageView> resultColumn8 = new TableColumn<Rock, MyImageView>("Photo_1");
        resultColumn8.setCellValueFactory(new PropertyValueFactory<Rock, MyImageView>("photo1"));

        TableColumn<Rock, MyImageView> resultColumn9 = new TableColumn<>("Photo_2");
        resultColumn9.setCellValueFactory(new PropertyValueFactory<>("photo2"));

        TableColumn<Rock, MyImageView> resultColumn10 = new TableColumn<>("Photo_3");
        resultColumn10.setCellValueFactory(new PropertyValueFactory<>("photo3"));

        TableColumn<Rock, MyImageView> resultColumn11 = new TableColumn<>("Photo_4");
        resultColumn11.setCellValueFactory(new PropertyValueFactory<>("photo4"));

        TableColumn<Rock, MyImageView> resultColumn12 = new TableColumn<>("Photo_5");
        resultColumn12.setCellValueFactory(new PropertyValueFactory<>("photo5"));

        TableColumn resultColumns[] = {resultColumn0,resultColumn1,resultColumn2,resultColumn3,resultColumn4,
                                        resultColumn5,resultColumn6,resultColumn7,resultColumn8,resultColumn9,
                                                                resultColumn10,resultColumn11,resultColumn12};


        for (int i = 0; i < resultColumns.length; i++)
        {
            resultTableView.getColumns().add(resultColumns[i]);
        }



        // doulbe click -> otwarcie dużych zdjęć tej skały
        tableView.setRowFactory( tv -> {
            TableRow<Rock> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Rock rock = row.getItem();
                    System.out.println(rock.getName());

                    showPhotosView(rock.getImagesList());
                }
            });
            return row ;
        });

        resultTableView.setRowFactory( tv -> {
            TableRow<Rock> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Rock rock = row.getItem();
                    System.out.println(rock.getName());

                    showPhotosView(rock.getImagesList());
                }
            });
            return row ;
        });


        column0.setMinWidth(25);
        column0.setPrefWidth(25);
        column0.setMaxWidth(25);
        column8.setMinWidth(150);
        column9.setMinWidth(150);
        column10.setMinWidth(150);
        column11.setMinWidth(150);
        column12.setMinWidth(150);


        resultColumn0.setMinWidth(25);
        resultColumn0.setPrefWidth(25);
        resultColumn0.setMaxWidth(25);
        resultColumn8.setMinWidth(150);
        resultColumn9.setMinWidth(150);
        resultColumn10.setMinWidth(150);
        resultColumn11.setMinWidth(150);
        resultColumn12.setMinWidth(150);

        try {
            tableView.getItems().addAll(dataBase.getAll());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    ImageView getImageView(String path)
    {
        InputStream instream = null;
        try {
            instream = Files.newInputStream(Paths.get(path));
            BufferedImage im = ImageIO.read(instream);

            int w = 200;
            int h = 150;
            BufferedImage imOut = new BufferedImage(w,h,im.getType());
            Graphics2D g2d = imOut.createGraphics();
            g2d.drawImage(im, 0, 0, w, h, null);
            g2d.dispose();

            Image image = SwingFXUtils.toFXImage(imOut, null);

            ImageView imageView = new ImageView(image);
            return imageView;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;

    }

}
