import java.util.*;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
/*import javax.swing.JOptionPane;
import java.lang.IllegalStateException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;*/


/**
 * This class represents
 * a game of Tetris,
 * which contains of falling
 * tetrads that are movable
 * and rotateable by keyboard presses.
 * The goal
 * is to fit the tetrads
 * from bottom to top
 * so that rows are
 * completely full of blocks.
 * Then, the rows will be cleared,
 * and the game is lost
 * when the rows all fill up
 * but not enough to be cleared.
 * 
 * Add ons: this game
 * includes music, grid lines,
 * a score based on cleared rows,
 * a way to hard drop blocks,
 * and loss detection with a message.
 * 
 * @author Linda Zeng
 * @version 3.17.23
 */
public class Tetris implements ArrowListener {

    //grid to play in
    private MyBoundedGrid<Block> grid;
    //display for the game
    private BlockDisplay display;
    //active falling block
    private Tetrad activeTetrad;
    //score in game
    //which is number of rows cleared
    private int score;

    /**
     * Constructs a new Tetris
     * with a grid of 20 by 10
     * and the first active Tetrad
     * displayed. Instantiates
     * the score as 0 in the title.
     */
    public Tetris()
    {
        grid = new MyBoundedGrid(20, 10);
        display = new BlockDisplay(grid);
        display.setArrowListener(this);
        score = 0;
        display.setTitle("Tetris (Score: " + score + ")");
        activeTetrad = new Tetrad(grid);
        display.showBlocks();
    }

    /**
     * Rotates the tetrad
     * when the up arrow
     * is pressed.
     */
    public void upPressed()
    {
        activeTetrad.rotate();
        display.showBlocks();
    }

    /**
     * Moves the tetrad down 1
     */
    public void downPressed()
    {
        activeTetrad.translate(1, 0);
        display.showBlocks();
    }

    /**
     * Moves the tetrad left 1
     */
    public void leftPressed()
    {
        activeTetrad.translate(0, -1);
        display.showBlocks();
    }

    /**
     * Moves the tetrad right 1
     */
    public void rightPressed()
    {
        activeTetrad.translate(0, 1);
        display.showBlocks();
    }

    /**
     * Drops the tetrad to
     * the bottommost empty
     * spots below it
     */
    public void spacePressed()
    {
        boolean flag = true;
        while (flag)
        {
            flag = activeTetrad.translate(1, 0);
            display.showBlocks();
        }
    }

    /**
     * Plays a game of tetris,
     * generates tetrads falling
     * every second
     * and clearing completed rows
     * until user has lost.
     * Plays music. Stops the music
     * and creates a message
     * when game is lost.
     */
    public void play()
    {
        Clip c = playMusic("Tetris.wav");
        try
        {
            boolean flag = true;
            while (flag)
            {
                Thread.sleep(1000);
                if (!activeTetrad.translate(1,0))
                {
                    clearCompletedRows();
                    display.showBlocks();
                    if (hasLost())
                    {
                        flag = false;
                    }
                    else
                    {
                        activeTetrad = new Tetrad(grid);
                    }
                }
                display.showBlocks();
            }
            //handles loss when top row is filled
            //System.out.println("You lost!");
            c.stop();
            display.showLoss();
        }
        //catches exception when tries to move a block down that isn't there
        //because block was not placed (no empty slots)
        catch (Exception e) 
        {
            //System.out.println("You lost!");
            System.out.println(e.toString());
            c.stop();
            display.showLoss();
        }
    }

    /**
     * Checks if a given row is completed.
     * 
     * @precondition 0 <= row < number of rows
     * @postcondition returns true if every cell
     *                  in given row is occupied,
     *                  false otherwise
     * @param row   the row to be checked
     * @return  true if the row is completed,
     *          false otherwise
     */
    private boolean isCompletedRow(int row)
    {
        for (int i = 0; i < grid.getNumCols(); i++)
        {
            if (grid.get(new Location(row, i)) == null)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Clears a given row,
     * removing blocks from
     * all locations in the row
     * and moving blocks above it
     * down.
     * 
     * @precondition 0 <= row < number of rows
     *               given row is full of blocks
     * @postcondition every block in the given
     *                row has been removed, and
     *                every block above row has 
     *                been moved down one row
     * @param row   row to be cleared
     */
    private void clearRow(int row)
    {
        for (int i = 0; i < grid.getNumCols(); i++)
        {
            grid.get(new Location(row, i)).removeSelfFromGrid();
        }
        /*for (int i = row - 1; i >= 0; i--)
        {
            for (int j = 0; j < grid.getNumCols(); j++)
            {
                Location cur = new Location(i, j);
                if (grid.get(cur) != null)
                {
                    grid.get(cur).removeSelfFromGrid();
                    grid.get(cur).putSelfInGrid(grid, new Location(i + 1, j));
                }
            }
        }*/
        List<Location> occupiedLoc = grid.getOccupiedLocations();
        for (int i = 0; i < occupiedLoc.size(); i++)
        {
            Location cur = occupiedLoc.get(i);
            Block block = grid.get(cur);
            if (cur.getRow() < row)
            {
                block.removeSelfFromGrid();
                block.putSelfInGrid(grid, new Location(cur.getRow() + 1, cur.getCol()));
            }
        }
    }

    /**
     * Clears all completed
     * rows in Tetris game.
     * Increments the score
     * and displays it in the title
     * each time a row is cleared.
     */
    private void clearCompletedRows()
    {
        for (int i = 0; i < grid.getNumRows(); i++)
        {
            if (isCompletedRow(i))
            {
                clearRow(i);
                score++;
                display.setTitle("Tetris (Score: " + score + ")");
            }
        }
    }

    /**
     * Determines one way
     * the game may be lost:
     * if the blocks have
     * reached but not filled
     * the top row.
     * 
     * @return true if it lost,
     *          false otherwise
     */
    private boolean hasLost()
    {
        if (!isCompletedRow(0))
        {
            for (int i = 0; i < grid.getNumCols(); i++)
            {
                if (grid.get(new Location(0, i)) != null)
                {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * Plays music in
     * the audio file
     * of the given path
     * continuously,
     * and returns the clip
     * that is playing.
     * 
     * @param filepath  the path/name of the audio file
     * @return the Clip that is playing
     */
    private Clip playMusic(String filepath)
    {
        try
        {
            File path = new File(filepath);
        
            if (path.exists())
            {
                AudioInputStream input = AudioSystem.getAudioInputStream(path);
                Clip clip = AudioSystem.getClip();
                clip.open(input);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                return clip;
            }
            else
            {
                System.out.println("No music file found.");
                return null;
            }
        }
        /*catch (IllegalStateException e)
        {
            System.out.println("a");
        }
        catch (IOException e)
        {
            System.out.println("b");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("c");
        }
        catch (LineUnavailableException e)
        {
            System.out.println("d");
        }
        catch (SecurityException e)
        {
            System.out.println("e");
        }
        catch (UnsupportedAudioFileException e)
        {
            System.out.println("f");
        }*/
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Main method that is automatically run,
     * which creates a new Tetris game
     * and begins to play.
     * 
     * @param args  default java arguments
     */
    public static void main(String[] args)
    {
        Tetris t = new Tetris();
        t.play();
    }
}
