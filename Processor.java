/**
 * Starter code for Processor - the class that processes images.
 * <p>
 * This class manipulated Java BufferedImages, which are effectively 2d arrays
 * of pixels. Each pixel is a single integer packed with 4 values inside it.
 * </p>
 * I have included two useful methods for dealing with bit-shift operators so
 * you don't have to. These methods are unpackPixel() and packagePixel() and do
 * exactly what they say - extract red, green, blue and alpha values out of an
 * int, and put the same four integers back into a special packed integer.
 *  
 * Kevin Lin:
 * Used cases to reduce code redundancy massively
 * added filters
 * Rotation of 90 degrees in possible, but picture might go out of the world
 * Flipping of images
 * Export of PNG images
 *  
 * @author Jordan Cohen, Kevin Lin
 * @version November 2013, June 2020
 */
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import greenfoot.*;
import javax.swing.JOptionPane;
import java.io.*;
public class Processor
{
    //defining flipping and rotational directions
    public static final int hor = 1;
    public static final int ver = -1;
    public static final int rR = 1;
    public static final int rL = -1;
    //defining an arrayList for storing images - undo function
    public static ArrayList<GreenfootImage> imgList = new ArrayList<GreenfootImage>();
   
    /**
     * Flips the image by horizontally or vertically, depending on the parameteres
     *
     * @param bi The buffered image to change
     * @param direction The direction of flipping vertical(1) or horizontal(-1)
     */
    public static void flipImage(BufferedImage bi, int direction){
        //get x and y size of the buffered image
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        // Temp image, to store pixels as we reverse everything
        BufferedImage newBi = new BufferedImage (xSize, ySize, 3);
        for(int y = 0; y < ySize; y++){
            for(int x = 0; x < xSize; x++){
                //using cases will reduce code
                switch(direction){
                    case -1:
                    newBi.setRGB(x, (ySize-1)-y, bi.getRGB(x,y));
                    break;
                    case 1:
                    newBi.setRGB((xSize - 1)-x, y, bi.getRGB(x,y));
                    break;
                }

            }
        }
        //set the buffered image to the new image
        for(int y = 0; y < ySize; y++){
            for(int x = 0; x < xSize; x++){
                bi.setRGB(x,y,newBi.getRGB(x,y));
            }
        }
    }

    /**
     * Rotates the image 90 degrees clockwise or counterclockwise, depending on direction parameteres
     *
     * @param bi The buffered image to change
     * @param direction The direction of rotation, clockwise(1) or counterclockwise(-1)
     *
     */
    public static GreenfootImage rotate90(BufferedImage bi, int direction){
        //get x and y size of the buffered image
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        // Temp image, to store pixels as we rotate everything
        BufferedImage newBi = new BufferedImage (ySize, xSize, 3);
        for(int x = 0; x < xSize; x++){
            for(int y = 0; y < ySize; y++){
                //using cases will reduce code 
                switch(direction){
                    case rR:
                    newBi.setRGB(y, (xSize-1)-x, bi.getRGB(x,y));
                    break;
                    case rL:
                    newBi.setRGB((ySize-1)-y, x, bi.getRGB(x,y));
                    break;
                }
            }
        }
        //return the newly created greenfoot image, using the method provided by Mr.Cohen
        return createGreenfootImageFromBI(newBi);

    }
    //method for converting BufferedImage to Greenfootimage provided by Mr.Cohen
    private static GreenfootImage createGreenfootImageFromBI (BufferedImage newBi){
        GreenfootImage returnImage = new GreenfootImage (newBi.getWidth(), newBi.getHeight());
        BufferedImage backingImage = returnImage.getAwtImage();
        Graphics2D backingGraphics = (Graphics2D)backingImage.getGraphics();
        backingGraphics.drawImage(newBi, null, 0, 0);
        return returnImage;
    }

    /**
     * Change to image to gray scale
     * @param bi The BufferedImage (passed by reference) to change
     * @param change Different image process 1 - GrayScale  2 - Negative  3 - warmth    4 - colder tones  5 - sepia    6 - red crush    7 - muted   8 - kodak summer
     */
    public static void filters(BufferedImage bi, int change){
        //get x and y dimension of the image
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();
        //going through the image, nested for loops since its like a 2d array
        for(int x = 0; x < xSize; x++){
            for(int y = 0; y < ySize; y++){
                int rgb = bi.getRGB(x,y);
                //getting rgb values to alter
                int[] rgbValues = unpackPixel(rgb);
                int alpha = rgbValues[0];
                int red = rgbValues[1];
                int green = rgbValues[2];
                int blue = rgbValues[3];
                
                //using cases will massively reduce code redundancy
                switch(change){

                    //grayscale, average of rgb values for each color
                    case 1:
                    red = (int)(red + green + blue)/3;
                    green = (int)(red + green + blue)/3;
                    blue = (int)(red + green + blue)/3;
                    break;
                    //negative, 255-x
                    case 2:
                    red = 255 - red;
                    green = 255 - green;
                    blue = 255 - blue;
                    break;
                    //warmer tones, increase red, reduce others
                    case 3:
                    if (blue >= 1 && blue <= 254)blue --;
                    if (red >= 1 && red <= 254)red+= 2;
                    if (green >= 1 && green <= 254)green--;
                    break;

                    //colder tones, increase bluem reduce others
                    case 4:
                    if (blue >= 1 && blue <= 254)blue += 2;
                    if (red >= 1 && red <= 254)red --;
                    if (green >= 1 && green <= 254)green--;
                    break;
                    //sepia, formula from https://www.geeksforgeeks.org/image-procesing-java-set-6-colored-image-sepia-image-conversion/
                    case 5:
                    red = (int)(0.393*red + 0.796*green + 0.189*blue);
                    blue = (int)(0.349*red + 0.686*green + 0.168*blue);
                    green = (int)(0.272*red + 0.534*green + 0.131*blue);
                    break;
                    //red crush, own formula
                    case 6:
                    red = (int)(0.353*red + 0.196*green + 0.299*blue);
                    blue = (int)(0.3*red + 0.206*green + 0.168*blue);
                    green = (int)(0.172*red + 0.224*green + 0.131*blue);
                    break;
                    //muted, own formula
                    case 7:
                    red = (int)(0.353*red + 0.196*green + 0.299*blue);
                    blue = (int)(0.5*red + 0.236*green + 0.568*blue);
                    green = (int)(0.572*red + 0.424*green + 0.131*blue);
                    break;
                    //kodak summer, own formula
                    case 8:
                    red = (int)(0.453*red + 0.696*green + 0.219*blue);
                    blue = (int)(0.334*red + 0.236*green + 0.568*blue);
                    green = (int)(0.372*red + 0.424*green + 0.231*blue);

                    break;
                }
                //limit to max
                if(red >= 255) red=254;
                if(blue >= 255) blue = 254;
                if(green >= 255) green = 254;
                //pack off the colours into one int
                int newColour  = packagePixel(red, green, blue, alpha);
                //set the color
                bi.setRGB(x,y,newColour);

            }
        }

    }

    private static int undoC = 0;
    /**
     * Stores images into an greenfoot arrayList
     * @param bi BufferedImage to store to arraylist of images
     * 
     */
    public static void storeImage(BufferedImage bi){
        imgList.add(createGreenfootImageFromBI(bi));
        undoC = 0;
    }

    /**
     * Undo the latest change to the image
     * this method was a more straight forward and easier to complete the task in my opinion
     */
    public static GreenfootImage undo(){
        undoC ++;
        GreenfootImage bi = imgList.get(imgList.size()-undoC);
        
        return bi;
    }
    
    /**
     * Export modified image as PNG file
     * @param bi The bufferedImage to export
     */
    public static void exportPNG(BufferedImage bi){
        String fileName = JOptionPane.showInputDialog("Input file name (no extension)");
        try{
            fileName += ".png";
            File f = new File (fileName);
            ImageIO.write(bi, "png", f); // need to do some imports
        }catch(IOException e){
            throw new RuntimeException("Unexpected error writing image");
        }   
    }

    /**
     * Takes in an rgb value - the kind that is returned from BufferedImage's
     * getRGB() method - and returns 4 integers for easy manipulation.
     *
     * By Jordan Cohen
     * Version 0.2
     *
     * @param rgbaValue The value of a single pixel as an integer, representing<br>
     *                  8 bits for red, green and blue and 8 bits for alpha:<br>
     *                  <pre>alpha   red     green   blue</pre>
     *                  <pre>00000000000000000000000000000000</pre>
     * @return int[4]   Array containing 4 shorter ints<br>
     *                  <pre>0       1       2       3</pre>
     *                  <pre>alpha   red     green   blue</pre>
     */
    public static int[] unpackPixel (int rgbaValue){
        int[] unpackedValues = new int[4];
        // alpha
        unpackedValues[0] = (rgbaValue >> 24) & 0xFF;
        // red
        unpackedValues[1] = (rgbaValue >> 16) & 0xFF;
        // green
        unpackedValues[2] = (rgbaValue >>  8) & 0xFF;
        // blue
        unpackedValues[3] = (rgbaValue) & 0xFF;

        return unpackedValues;
    }

    /**
     * Takes in a red, green, blue and alpha integer and uses bit-shifting
     * to package all of the data into a single integer.
     *
     * @param   int red value (0-255)
     * @param   int green value (0-255)
     * @param   int blue value (0-255)
     * @param   int alpha value (0-255)
     *
     * @return int  Integer representing 32 bit integer pixel ready
     *              for BufferedImage
     */
    public static int packagePixel (int r, int g, int b, int a) {
        int newRGB = (a << 24) | (r << 16) | (g << 8) | b;
        return newRGB;
    }
}
