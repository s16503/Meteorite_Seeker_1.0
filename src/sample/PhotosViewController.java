package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotosViewController {

    @FXML
    ImageView bigPhoto;
    @FXML
    Button backButton;
    @FXML
    Button nextButton;

    List<ImageView> viewdPhotos = new ArrayList<>();
    int viewdPhotoIndex;

    public PhotosViewController()
    {
    }

    public void loadPhotos(List<ImageView> photos)
    {
        this.viewdPhotos = photos;
        this.bigPhoto.setImage(viewdPhotos.get(0).getImage());
        this.viewdPhotoIndex = 0;
    }

    // poprzednie zdjęcie z podglądu
    @FXML
    private void handleBackButton(ActionEvent event)
    {
        if (viewdPhotoIndex == 0)
        {
            bigPhoto.setImage(viewdPhotos.get(viewdPhotos.size()-1).getImage());
            viewdPhotoIndex = viewdPhotos.size()-1;
        }
        else
        {
            viewdPhotoIndex--;
            bigPhoto.setImage(viewdPhotos.get(viewdPhotoIndex).getImage());
        }
    }

    // następne zdjęcie z podglądu
    @FXML
    private void handleNextButton(ActionEvent event)
    {
        if (viewdPhotoIndex == viewdPhotos.size()-1)
        {
            viewdPhotoIndex = 0;
            bigPhoto.setImage(viewdPhotos.get(viewdPhotoIndex).getImage());
        }
        else
        {
            viewdPhotoIndex++;
            bigPhoto.setImage(viewdPhotos.get(viewdPhotoIndex).getImage());
        }
    }




    @FXML
    private void initialize()
    {

    }


}
