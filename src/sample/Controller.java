package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    RocksDB dataBase;

    @FXML
    TableView tableView = new TableView();

    @FXML
    Button addButton;
    @FXML
    Button editButton;
    @FXML
    Button deleteButton;
    @FXML
    Button refreshButton;



    public Controller()
    {
    }

    @FXML
    private void handleAddButton(ActionEvent event)
    {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("sample\\AddDataWindow.fxml"));
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
        System.out.println(">>> TEST OK");

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

    private void refreshTable()
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

       // dataBase.init();


        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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



        tableView.getColumns().add(column0);
        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        tableView.getColumns().add(column3);
        tableView.getColumns().add(column4);
        tableView.getColumns().add(column5);
        tableView.getColumns().add(column6);
        tableView.getColumns().add(column7);
        tableView.getColumns().add(column8);
        tableView.getColumns().add(column9);
        tableView.getColumns().add(column10);
        tableView.getColumns().add(column11);
        tableView.getColumns().add(column12);



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


        column0.setMinWidth(25);
        column0.setPrefWidth(25);
        column0.setMaxWidth(25);
        column8.setMinWidth(150);
        column9.setMinWidth(150);
        column10.setMinWidth(150);
        column11.setMinWidth(150);
        column12.setMinWidth(150);

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
