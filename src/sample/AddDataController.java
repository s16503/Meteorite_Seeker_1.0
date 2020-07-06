package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AddDataController {


    @FXML
    Button addPhotosButton;

    @FXML
    Button addButton;

    @FXML
    Button cancelButton;

    @FXML
    TextField nameTextField;

    @FXML
    TextField density1TextField;

    @FXML
    TextField density2TextField;

    @FXML
    TextField type1TextField;

    @FXML
    TextField type2TextField;

    @FXML
    TextArea descTextArea;

    @FXML
    CheckBox ferromagneticCheckbox;



    @FXML
    MyImageView imageViewSlot1;
    @FXML
    MyImageView imageViewSlot2;
    @FXML
    MyImageView imageViewSlot3;
    @FXML
    MyImageView imageViewSlot4;
    @FXML
    MyImageView imageViewSlot5;

    //todo
//    @FXML
//    ArrayList<ImageView> imageViewsList;


    @FXML
    Label imageLabel1;
    @FXML
    Label imageLabel2;
    @FXML
    Label imageLabel3;
    @FXML
    Label imageLabel4;
    @FXML
    Label imageLabel5;

    RocksDB dataBase = new RocksDB();


    //zatwierdzzenie dodawanych danych
    @FXML
    private void handleAddButton(ActionEvent event)
    {
        String name = nameTextField.getText();
        boolean isFerromagnetic = ferromagneticCheckbox.isSelected();
        double density1 = Double.parseDouble(density1TextField.getText());
        double density2 = Double.parseDouble(density2TextField.getText());
        String desc = descTextArea.getText();
        String type1 = type1TextField.getText();
        String type2 = type2TextField.getText();


        List<ImageView> images = new ArrayList<>();

        if (imageViewSlot1.isSelected())
            images.add(imageViewSlot1);
        if (imageViewSlot2.isSelected())
            images.add(imageViewSlot2);
        if (imageViewSlot3.isSelected())
            images.add(imageViewSlot3);
        if (imageViewSlot4.isSelected())
            images.add(imageViewSlot4);
        if (imageViewSlot5.isSelected())
            images.add(imageViewSlot5);


        try {
            dataBase.addData(new Rock(name,density1,density2,isFerromagnetic,desc,type1,type2,images));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @FXML
    private void handleCancelButton(ActionEvent event)
    {

    }

    // wybór zdjęć, foto umieszczane w wolnych slotach
    @FXML
    private void handleAddPhotosButton(ActionEvent event)
    {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybór zdjęć");
        List<File> list = fileChooser.showOpenMultipleDialog(new Stage());



        for(File file : list)
        {
            if (!imageViewSlot1.isSelected())
            {
                imageLabel1.setText(file.getName());
                imageViewSlot1.setImage(getImage(file.getPath()));
                imageViewSlot1.setSelected(true);
            }
            else
            if (!imageViewSlot2.isSelected())
            {
                imageLabel2.setText(file.getName());
                imageViewSlot2.setImage(getImage(file.getPath()));
                imageViewSlot2.setSelected(true);
            }
            else
            if (!imageViewSlot3.isSelected())
            {
                imageLabel3.setText(file.getName());
                imageViewSlot3.setImage(getImage(file.getPath()));
                imageViewSlot3.setSelected(true);
            }
            else
            if (!imageViewSlot4.isSelected())
            {
                imageLabel4.setText(file.getName());
                imageViewSlot4.setImage(getImage(file.getPath()));
                imageViewSlot4.setSelected(true);
            }
            else
            if (!imageViewSlot5.isSelected())
            {
                imageLabel5.setText(file.getName());
                imageViewSlot5.setImage(getImage(file.getPath()));
                imageViewSlot5.setSelected(true);
            }

        }


    }

    Image getImage(String path)
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


            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;

    }






    @FXML
    private void initialize()
    {

//        imageViewSlot1.setImage(getImage("images\\image-empty-icon.png"));
//        imageViewSlot2.setImage(getImage("images\\image-empty-icon.png"));
//        imageViewSlot3.setImage(getImage("images\\image-empty-icon.png"));
//        imageViewSlot4.setImage(getImage("images\\image-empty-icon.png"));
//        imageViewSlot5.setImage(getImage("images\\image-empty-icon.png"));


        // po kliknięciu na zdjęcie jest ono usuwane

        //todo
//        for (ImageView imageView : imageViewsList)
//        {
//            imageView.setOnMouseClicked(event ->
//            {
//                imageView.setImage(null);
//            });
//        }

        imageViewSlot1.setOnMouseClicked(event ->
        {

            imageViewSlot1.setSelected(false);
            imageLabel1.setText("1) ...");

//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setTitle("Wybór zdjęcia");
//            try
//            {
//                imageViewSlot1.setImage(getImageView(fileChooser.showOpenDialog(new Stage()).getPath()));
//            }catch (NullPointerException ex)
//            {
//                imageViewSlot1.setImage(null);
//            }

        });

        imageViewSlot2.setOnMouseClicked(event ->
        {
            imageViewSlot2.setSelected(false);
            imageLabel2.setText("2) ...");
        });

        imageViewSlot3.setOnMouseClicked(event ->
        {
            imageViewSlot3.setSelected(false);
            imageLabel3.setText("3) ...");
        });

        imageViewSlot4.setOnMouseClicked(event ->
        {
            imageViewSlot4.setSelected(false);
            imageLabel4.setText("4) ...");
        });

        imageViewSlot5.setOnMouseClicked(event ->
        {
            imageViewSlot5.setSelected(false);
            imageLabel5.setText("5) ...");
        });
    }
}
