package sample;



import javafx.scene.image.ImageView;

import java.util.List;

public class Rock {


    private int id;
    private String name;
    private double density_Min;
    private double density_Max;
    private boolean ferromagnetic;
    private String description;
    private String type1;
    private String type2;
    List<MyImageView> imagesList;
    private MyImageView photo1;
    private MyImageView photo2;
    private MyImageView photo3;
    private MyImageView photo4;
    private MyImageView photo5;


    public Rock(String name, boolean ferromagnetic)
    {
        this.name = name;
        this.ferromagnetic = ferromagnetic;
    }


    public Rock(String name, double d_min, double d_max, boolean ferromagnetic,
                String description, String type1, String type2, List<MyImageView> images)
    {
        this.name = name;
        this.density_Min = d_min;
        this.density_Max = d_max;
        this.ferromagnetic= ferromagnetic;
        this.description = description;
        this.type1 = type1;
        this.type2 = type2;

        imagesList = images;



        if(images.size()>0)
            photo1 = images.get(0);

        if(images.size()>1)
            photo2 = images.get(1);

        if(images.size()>2)
            photo3 = images.get(2);

        if(images.size()>3)
            photo4 = images.get(3);

        if(images.size()>4)
            photo5 = images.get(4);


    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public String getType1()
    {
        return type1;
    }

    public String getType2()
    {
        return type2;
    }

    public double getDensity_Min()
    {
        return density_Min;
    }

    public double getDensity_Max()
    {
        return density_Max;
    }

    public boolean isFerromagnetic()
    {
        return ferromagnetic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPhoto1(MyImageView imageView)
    {
        this.photo1 = imageView;
    }

    public MyImageView getPhoto1()
    {
        return photo1;
    }
    public MyImageView getPhoto2()
    {
        return photo2;
    }
    public MyImageView getPhoto3()
    {
        return photo3;
    }
    public MyImageView getPhoto4()
    {
        return photo4;
    }
    public MyImageView getPhoto5()
    {
        return photo5;
    }

    public List<MyImageView> getImagesList() {
        return imagesList;
    }

    //
//    public void setPhoto2(ImageView imageView)
//    {
//        this.photo2 = imageView;
//    }
//
//    public void setPhoto3(ImageView imageView)
//    {
//        this.photo3 = imageView;
//    }
//
//    public void setPhoto4(ImageView imageView)
//    {
//        this.photo4 = imageView;
//    }
//
//    public void setPhoto5(ImageView imageView)
//    {
//        this.photo5 = imageView;
//    }


}
