package core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class SoSImage {

    private static final HashMap<String, SoSImage> strToImage = new HashMap<>();
    // << Field: image >>
    private BufferedImage image;
    // FilePath
    private String filePath;
    // The number of the objects which refer to the image
    private int refCount = 0;

    private SoSImage() {

    }

    public static SoSImage create(String filePath) {
        SoSImage image = null;

        if (strToImage.containsKey(filePath)) {
            image = strToImage.get(filePath);
        } else {
            image = new SoSImage();
            image.setFilePath(filePath);
            try {
                //this works for running in intellij
                image.setImage(ImageIO.read(new File(filePath)));
            } catch (Exception e) {

                //during jar, images are copied to root.
                //if you unzip the packaged jar, you can see images are in root folder.
                //split for example "engine/resources/bridgehead.png" into "bridgehead.png
                String newFileName = (filePath.split("/"))[2];
                System.out.println(newFileName);

                try {
                    image.setImage(ImageIO.read(SoSImage.class.getClassLoader().getResource(newFileName)));
                } catch (Exception exception) {
                    if (exception.getClass() != IllegalArgumentException.class) {
                        exception.printStackTrace();
                    }
                }

            }
            strToImage.put(filePath, image);
        }

        if (image != null) {
            image.refCount++;
        }

        return image;
    }

    public void clear() {
        refCount--;
        if (refCount <= 0) {
            strToImage.remove(this);
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getRefCount() {
        return refCount;
    }

    // << Method: clear >>
    // 정리 코드 작성
    //public void clear() { }
    // << Method: clear >>
}
