import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.File;
import javax.swing.JOptionPane;
/**
 * Starter code for Image Manipulation Array Assignment.
 *
 * The class Processor contains all of the code to actually perform
 * transformation. The rest of the classes serve to support that
 * capability. This World allows the user to choose transformations
 * and open files.
 * Kevin Lin:
 * Redesigned UI for simple use, the utilization of simple button reduces complexit
 * Added rotation of images, flipping of images, as well as filters effects
 * Added function to export as png and undo
 * 
 * Used cases to reduce code massively
 * 
 *
 * @author Jordan Cohen, Kevin Lin
 * @version November 2013, June 2020
 */
public class Background extends World
{
    // Constants:
    private final String STARTING_FILE = "pond.jpg";

    // Objects and Variables:
    private ImageHolder image;

    private TextButton blueButton, hRevButton, vRevButton, rRotButton, lRotButton,  openFile;
    private TextButton rotation, flipping;
    private TextButton warmth, warmthp, warmthm;
    private TextButton filters, grayscale, negative,redCrush, sepia, muted, kodakSummer;
    private TextButton undo;
    private TextButton exportPng;
    private int currCount, totalCount;

    private String fileName;

    /**
     * Constructor for objects of class Background.
     *
     */
    public Background()
    {
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 600, 1);

        // Initialize buttons and the image
        image = new ImageHolder(STARTING_FILE);
        warmth = new TextButton(" [Warmth] ");
        warmthm = new TextButton(" [-] ");
        warmthp = new TextButton(" [+] ");
        
        flipping = new TextButton(" [Flipping] ");
        vRevButton = new TextButton(" [⬆]");
        hRevButton = new TextButton(" [⬅]");
        
        rotation = new TextButton(" [Rotation] ");
        rRotButton = new TextButton(" [↪] ");
        lRotButton = new TextButton(" [↩] ");
        
        filters = new TextButton( " [    Filters    ] " );
        grayscale = new TextButton(" Grayscale ");
        negative = new TextButton(" Negative ");
        redCrush = new TextButton(" Red Crush ");
        sepia = new TextButton(" Sepia ");
        muted = new TextButton(" Muted ");
        kodakSummer = new TextButton( " Kodak Summer " );
        

        
        undo = new TextButton(" [Undo] ");
        openFile = new TextButton(" [ Import: " + STARTING_FILE + " ] ");
        exportPng = new TextButton(" [Export as PNG] ");
        
        
        addObject (image, 330, 310);
        
        addObject (exportPng, 320, 560);
        // Add objects to the screen
        
        //undo
        addObject (undo, 710, 560);
        
        
        //warmth portion
        addObject (warmth, 710, 40);
        addObject (warmthm, 685, 70);
        addObject (warmthp, 735, 70);
        //rotation
        addObject (rotation, 710, 120);
        addObject (rRotButton, 685, 150);
        addObject (lRotButton, 735, 150);
        //flipping
        addObject (flipping, 710, 190);
        addObject (hRevButton, 685, 220);
        addObject (vRevButton, 735, 220);
        //filters
        addObject (filters, 710, 270);
        addObject (grayscale, 710, 300);
        addObject (negative, 710, 330);
        addObject (redCrush, 710, 360);
        addObject (sepia, 710, 390);
        addObject (muted, 710, 420);
        addObject (kodakSummer, 710, 450);
        //file opening
        addObject (openFile, 400, 50);

        
        prepare();
        //store the base image in the arraylist
        Processor.storeImage(image.getBufferedImage());
    }

    /**
     * Act() method just checks for mouse input
     */
    public void act ()
    {
        checkMouse();
    }

    /**
     * Check for user clicking on a button
     */
    private void checkMouse ()
    {
        // Avoid excess mouse checks - only check mouse if somethething is clicked.

        if (Greenfoot.mouseClicked(null))
        {
            
            if (Greenfoot.mouseClicked(warmthm)){
                Processor.storeImage(image.getBufferedImage());
                Processor.filters(image.getBufferedImage(), 4);
            }else if (Greenfoot.mouseClicked(hRevButton)){
                Processor.storeImage(image.getBufferedImage());
                Processor.flipImage(image.getBufferedImage(), 1);
            }else if(Greenfoot.mouseClicked(vRevButton)){
                Processor.storeImage(image.getBufferedImage());
                Processor.flipImage(image.getBufferedImage(),-1);
            }else if (Greenfoot.mouseClicked(rRotButton)){
                Processor.storeImage(image.getBufferedImage());
                image.setImage(Processor.rotate90(image.getBufferedImage(), 1));
            }else if(Greenfoot.mouseClicked(lRotButton)){
                Processor.storeImage(image.getBufferedImage());
                image.setImage(Processor.rotate90(image.getBufferedImage(), -1));
            }else if(Greenfoot.mouseClicked(grayscale)){
                Processor.storeImage(image.getBufferedImage());
                Processor.filters(image.getBufferedImage(), 1);
            }else if(Greenfoot.mouseClicked(negative)){
                Processor.storeImage(image.getBufferedImage());
                Processor.filters(image.getBufferedImage(), 2);
            }else if(Greenfoot.mouseClicked(redCrush)){
                Processor.storeImage(image.getBufferedImage());
                Processor.filters(image.getBufferedImage(), 6);
            }else if(Greenfoot.mouseClicked(sepia)){
                Processor.storeImage(image.getBufferedImage());
                Processor.filters(image.getBufferedImage(), 5);
            }else if(Greenfoot.mouseClicked(muted)){
                Processor.storeImage(image.getBufferedImage());
                Processor.filters(image.getBufferedImage(), 7);
            }else if(Greenfoot.mouseClicked(kodakSummer)){
                Processor.storeImage(image.getBufferedImage());
                Processor.filters(image.getBufferedImage(), 8);
            }else if(Greenfoot.mouseClicked(warmthp)){
                Processor.storeImage(image.getBufferedImage());
                Processor.filters(image.getBufferedImage(),3);
            }else if(Greenfoot.mouseClicked(undo)){
                image.setImage(Processor.undo());
            }else if (Greenfoot.mouseClicked(openFile)){
                openFile ();
            }else if (Greenfoot.mouseClicked(exportPng)){
                Processor.exportPNG(image.getBufferedImage());
            }
        }
        
    }

    /**
     * Allows the user to open a new image file.
     */
    private void openFile ()
    {
        // Use a JOptionPane to get file name from user
        String fileName = JOptionPane.showInputDialog("Please input a file name with extension");

        // If the file opening operation is successful, update the text in the open file button
        if (image.openFile (fileName))
        {
            String display = " [ Open File: " + fileName + " ] ";
            openFile.update (display);
        }

    }

    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        
    }
}
