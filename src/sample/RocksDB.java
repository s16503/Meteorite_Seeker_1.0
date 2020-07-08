package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RocksDB {
    public final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver"; //"com.mysql.cj.jdbc.Driver";
    public String jdbc_url;
    Connection connection;

    public RocksDB()
    {
        jdbc_url = "jdbc:derby:DataBase;create=true"; //"jdbc:mysql://188.68.232.122:3306/Janek?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(jdbc_url);   //"Janek","jupiter"
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void init()
    {
        try
        {


            connection.createStatement().execute("DROP TABLE Photo");
            connection.createStatement().execute("DROP TABLE Rock");

            connection.createStatement().execute(
                            "CREATE TABLE Photo(\n" +
                            "    Id_Photo integer  NOT NULL,\n" +
                            "    Rock_Id_Rock integer  NOT NULL,\n" +
                            "    Photo_File mediumblob NOT NULL,\n" +           // blob(10M)
                            "    CONSTRAINT Photo_pk PRIMARY KEY (Id_Photo)\n" +
                            ") \n" );


            connection.createStatement().execute(
                            "CREATE TABLE Rock(\n" +
                            "    Id_Rock integer  NOT NULL,\n" +
                            "    Name varchar(50)  NOT NULL,\n" +
                            "    Density_Min double  NOT NULL,\n" +
                            "    Density_Max double  NOT NULL,\n" +
                            "    Ferromagnetic integer  ,\n" +
                            "    Description varchar(300)  ,\n" +
                            "    Type1 varchar(50)  ,\n" +
                            "    Type2 varchar(50)  ,\n" +
                            "    CONSTRAINT Rock_pk PRIMARY KEY (Id_Rock)\n" +
                            ") \n" );


            connection.createStatement().execute(
                            "-- foreign keys\n" +
                            "-- Reference: Photo_Rock (table: Photo)\n" +
                            "ALTER TABLE Photo ADD CONSTRAINT Photo_Rock\n" +
                            "    FOREIGN KEY (Rock_Id_Rock)\n" +
                            "    REFERENCES Rock (Id_Rock)\n" +
                            "\n" +
                            "-- End of file."
            );

        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public List<Rock> getAll() throws SQLException, IOException {
        List<Rock> list = new ArrayList<>();

        ResultSet resultSet;
        resultSet = connection
                .createStatement()
                .executeQuery(
                        "SELECT * FROM Rock"
                );

        while (resultSet.next())
        {
            int idRock = resultSet.getInt("Id_Rock");

            System.out.println(">>>>>>>>>>>>ID ROCK = "+ idRock);
            ResultSet photosResultSet;
            photosResultSet = connection
                    .createStatement()
                    .executeQuery(
                            "SELECT Photo_File, Id_Photo FROM Photo WHERE Rock_Id_Rock =" + idRock
                    );
            List<MyImageView> images = new ArrayList<>();
            while(photosResultSet.next())
            {
                Blob b=photosResultSet.getBlob(1);
                byte barr[]=b.getBytes(1,(int)b.length());


                ByteArrayInputStream bis = new ByteArrayInputStream(barr);
                BufferedImage bufferedImage = ImageIO.read(bis);
                Image img = SwingFXUtils.toFXImage(bufferedImage, null);

                MyImageView myImageView = new MyImageView();
                myImageView.setOriginalSizeImage(img);

                int w = 200;
                int h = 150;

                BufferedImage imOut = new BufferedImage(w,h,bufferedImage.getType());
                Graphics2D g2d = imOut.createGraphics();
                g2d.drawImage(bufferedImage, 0, 0, w, h, null);
                g2d.dispose();
                Image image = SwingFXUtils.toFXImage(imOut, null);
                myImageView.setImage(image);

                myImageView.setDBId(photosResultSet.getInt("Id_Photo"));

                images.add(myImageView);
            }

            boolean ferromagnetyk = resultSet.getInt("Ferromagnetic") == 1 ? true : false;
            Rock rock = new Rock(

                    resultSet.getString("name"),
                    resultSet.getDouble("Density_Min"),
                    resultSet.getDouble("Density_Max"),
                    ferromagnetyk,
                    resultSet.getString("Description"),
                    resultSet.getString("Type1"),
                    resultSet.getString("Type2"),
                    images
            );

            rock.setId(resultSet.getInt("Id_Rock"));
            list.add(rock);
        }

        return list;
    }


    public List<Rock> getByDensity(double density) throws SQLException, IOException {
        List<Rock> list = new ArrayList<>();

        ResultSet resultSet;
        resultSet = connection
                .createStatement()
                .executeQuery(
                        "SELECT * FROM Rock WHERE Density_Min <= "+density+"AND Density_Max >= "+density
                );

        while (resultSet.next())
        {
            int idRock = resultSet.getInt("Id_Rock");

            ResultSet photosResultSet;
            photosResultSet = connection
                    .createStatement()
                    .executeQuery(
                            "SELECT Photo_File, Id_Photo FROM Photo WHERE Rock_Id_Rock =" + idRock
                    );
            List<MyImageView> images = new ArrayList<>();
            while(photosResultSet.next())
            {
                Blob b=photosResultSet.getBlob(1);
                byte barr[]=b.getBytes(1,(int)b.length());


                ByteArrayInputStream bis = new ByteArrayInputStream(barr);
                BufferedImage bufferedImage = ImageIO.read(bis);
                Image img = SwingFXUtils.toFXImage(bufferedImage, null);

                MyImageView myImageView = new MyImageView();
                myImageView.setOriginalSizeImage(img);

                int w = 200;
                int h = 150;

                BufferedImage imOut = new BufferedImage(w,h,bufferedImage.getType());
                Graphics2D g2d = imOut.createGraphics();
                g2d.drawImage(bufferedImage, 0, 0, w, h, null);
                g2d.dispose();
                Image image = SwingFXUtils.toFXImage(imOut, null);
                myImageView.setImage(image);

                myImageView.setDBId(photosResultSet.getInt("Id_Photo"));

                images.add(myImageView);
            }

            boolean ferromagnetyk = resultSet.getInt("Ferromagnetic") == 1 ? true : false;
            Rock rock = new Rock(

                    resultSet.getString("name"),
                    resultSet.getDouble("Density_Min"),
                    resultSet.getDouble("Density_Max"),
                    ferromagnetyk,
                    resultSet.getString("Description"),
                    resultSet.getString("Type1"),
                    resultSet.getString("Type2"),
                    images
            );

            rock.setId(resultSet.getInt("Id_Rock"));
            list.add(rock);
        }

        return list;
    }

    public void addData(Rock rock) throws SQLException, IOException {

        System.out.println("> Dodawanie skały ...");
        ResultSet resultSet;
        resultSet = connection
                .createStatement()
                .executeQuery(
                "SELECT max(Id_Rock) as maxId FROM Rock"
        );

        int nextId;
        if(resultSet.next())
        {
            nextId = resultSet.getInt("maxId")+1;
        }
        else
            nextId = 1;

        System.out.println("> Nowa skała o id = "  + nextId);



        int ferromagnetic = rock.isFerromagnetic() ? 1 : 0;


        connection.createStatement().execute(
                "INSERT INTO Rock VALUES(" +
                        ""+nextId+",'"+rock.getName()+"',"+rock.getDensity_Min()+","+rock.getDensity_Max()+","+ferromagnetic+",'"
                        +rock.getDescription() + "','" + rock.getType1() + "','" + rock.getType2()+"')"
        );

        System.out.println("dodano.");

        System.out.println("Dodawanie zdjęć ...");
        //photos
        resultSet = connection
                .createStatement()
                .executeQuery(
                        "SELECT max(Id_Photo) as maxId FROM Photo"
                );

        int nextPhotoId;
        if (resultSet.next())
        {
            nextPhotoId = resultSet.getInt("maxId")+1;
        }
        else
            nextPhotoId = 1;

        for (MyImageView imageView : rock.imagesList)
        {
            String DML = "INSERT INTO Photo VALUES (?, ?, ?)";
            InputStream instream = Files.newInputStream(imageView.getImagePath());
            PreparedStatement pstmnt = connection.prepareStatement(DML);
            pstmnt.setInt(1, nextPhotoId);
            pstmnt.setInt(2, nextId);
            pstmnt.setBinaryStream(3, instream);
            pstmnt.executeUpdate();
            nextPhotoId++;
        }

        System.out.println("dodano.");
        System.out.println("success");


    }

    public void updateData(Rock rock) throws SQLException, IOException {
        System.out.println("Aktualizowanie skały o id: " + rock.getId());

        int ferromagnetic = rock.isFerromagnetic() ? 1:0;

        PreparedStatement update = connection.prepareStatement
                ("UPDATE Rock SET name = ?, Density_Min = ?, Density_Max = ?, Ferromagnetic = ?, Description = ?," +
                        "Type1 = ?, Type2 = ? WHERE Id_Rock = ?");


        update.setString(1,rock.getName());
        update.setDouble(2,rock.getDensity_Min());
        update.setDouble(3,rock.getDensity_Max());
        update.setInt(4,ferromagnetic);
        update.setString(5,rock.getDescription());
        update.setString(6,rock.getType1());
        update.setString(7,rock.getType2());
        update.setInt(8,rock.getId());

        update.executeUpdate();


        //photos
        ResultSet resultSet;
        resultSet = connection
                .createStatement()
                .executeQuery(
                        "SELECT max(Id_Photo) as maxId FROM Photo"
                );

        int nextPhotoId;
        if (resultSet.next())
        {
            nextPhotoId = resultSet.getInt("maxId")+1;
        }
        else
            nextPhotoId = 1;

        for (MyImageView imageView : rock.imagesList)
        {
            String DML = "INSERT INTO Photo VALUES (?, ?, ?)";
            InputStream instream = Files.newInputStream(imageView.getImagePath());
            PreparedStatement pstmnt = connection.prepareStatement(DML);
            pstmnt.setInt(1, nextPhotoId);
            pstmnt.setInt(2, rock.getId());
            pstmnt.setBinaryStream(3, instream);
            pstmnt.executeUpdate();
            nextPhotoId++;
        }

        System.out.println("Aktualizowano pomyślnie.");
    }

    public void deleteData(int id) throws SQLException {
        System.out.println("Usuwanie skały id: " + id);

        connection.createStatement().execute(
                "DELETE FROM Rock WHERE Id_Rock ="+id
        );

        connection.createStatement().execute(
                "DELETE FROM Photo WHERE Rock_Id_Rock ="+id
        );

        System.out.println("Usunięto pomyślnie");

    }


    public void deletePhoto(int id) throws SQLException {
        System.out.println("Usuwanie zdjecia id: " + id);

        connection.createStatement().execute(
                "DELETE FROM Photo WHERE Id_Photo ="+id
        );

        System.out.println("Usunięto pomyślnie");
    }


}
