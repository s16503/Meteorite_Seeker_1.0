package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyImageView extends ImageView {


    private int id;
    boolean selected;
    public static final Image DEFAULT_IMAGE = getFormatImage("images\\image-empty-icon.png");
    private Path imagePath;
    private Image originalSizeImage;

    public MyImageView() {
        super();
        this.setImage(DEFAULT_IMAGE);
        this.selected = false;
    }

    public MyImageView(String url) {
        super(url);
    }

    public MyImageView(Image image) {
        super(image);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        if (selected == false)
            this.setImage(DEFAULT_IMAGE);

        this.selected = selected;
    }

    public void setOriginalSizeImage(Image image)
    {
        this.originalSizeImage = image;
    }

    public Image getOriginalSizeImage()
    {
        return originalSizeImage;
    }

    public void setDBId(int id) {
        this.id = id;
    }

    public int getDBId() {
        return id;
    }

    static Image getFormatImage(String path) {
        InputStream instream = null;
        try {
            instream = Files.newInputStream(Paths.get(path));
            BufferedImage im = ImageIO.read(instream);

            int w = 200;
            int h = 150;
            BufferedImage imOut = new BufferedImage(w, h, im.getType());
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

    public void setImagePath(Path imagePath) {
        this.imagePath = imagePath;
    }

    public Path getImagePath() {
        return imagePath;
    }
}
