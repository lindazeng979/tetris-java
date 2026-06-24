import java.awt.Color;
import java.util.concurrent.Semaphore;

/**
 * This class represents
 * a block of four in a Tetris
 * game. It is may come in
 * a variety of shapes and colors.
 * This class contains methods
 * to put the block in the grid
 * and to translate and spin the Tetrad.
 *
 * @author Linda Zeng
 * @version 3.17.23
 */
public class Tetrad {
    
    //array of length 4 representing each block
    //in the tetrad
    Block[] blocks;
    //the grid the tetrad is in
    MyBoundedGrid<Block> grid;
    //helps minimize error
    private Semaphore lock;

    /**
     * Constructs a Tetrad
     * in a given grid,
     * placing the tetrad
     * in the top middle of the
     * grid and instantiating
     * all four blocks in the array,
     * with the first element being
     * the middle. It randomly
     * chooses a color and corresponding
     * shape.
     * Adds Tetrad to the grid as long
     * as starting locations have not
     * been taken.
     * 
     * @param gr grid to put the tetrad in
     */
    public Tetrad(MyBoundedGrid<Block> gr)
    {
        grid = gr;
        blocks = new Block[4];
        lock = new Semaphore(1, true);
        Location[] locs = new Location[4];
        int iniR = 0;
        int iniC = 5;
        int color = (int)(Math.random() * 7);
        Color c = Color.RED;
        if (color == 0)
        {
            c = Color.RED;
            locs[1] = new Location(iniR, iniC);
            locs[0] = new Location(iniR + 1, iniC);
            locs[2] = new Location(iniR + 2, iniC);
            locs[3] = new Location(iniR + 3, iniC);
        }
        if (color == 1)
        {
            c = Color.GRAY;
            locs[0] = new Location(iniR, iniC);
            locs[1] = new Location(iniR, iniC - 1);
            locs[2] = new Location(iniR, iniC + 1);
            locs[3] = new Location(iniR + 1, iniC);
        }
        if (color == 2)
        {
            c = Color.CYAN;
            locs[0] = new Location(iniR, iniC);
            locs[1] = new Location(iniR, iniC - 1);
            locs[2] = new Location(iniR + 1, iniC - 1);
            locs[3] = new Location(iniR + 1, iniC);
        }
        if (color == 3)
        {
            c = Color.YELLOW;
            locs[1] = new Location(iniR, iniC - 1);
            locs[0] = new Location(iniR + 1, iniC - 1);
            locs[2] = new Location(iniR + 2, iniC - 1);
            locs[3] = new Location(iniR + 2, iniC);
        }
        if (color == 4)
        {
            c = Color.MAGENTA;
            locs[1] = new Location(iniR, iniC);
            locs[0] = new Location(iniR + 1, iniC);
            locs[2] = new Location(iniR + 2, iniC);
            locs[3] = new Location(iniR + 2, iniC - 1);
        }
        if (color == 5)
        {
            c = Color.BLUE;
            locs[0] = new Location(iniR, iniC);
            locs[1] = new Location(iniR + 1, iniC);
            locs[2] = new Location(iniR + 1, iniC - 1);
            locs[3] = new Location(iniR, iniC + 1);
        }
        if (color == 6)
        {
            c = Color.GREEN;
            locs[0] = new Location(iniR, iniC);
            locs[1] = new Location(iniR + 1, iniC);
            locs[2] = new Location(iniR, iniC - 1);
            locs[3] = new Location(iniR + 1, iniC + 1);
        }
        for (int i = 0; i < 4; i++)
        {
            blocks[i] = new Block();
            blocks[i].setColor(c);
        }
        if (areEmpty(grid, locs)) //todo
            addToLocations(grid, locs);
    }

    /**
     * Adds the blocks
     * to the grid
     * at the given locations.
     * 
     * @precondition blocks are not in any grid;
     *               locs.length = 4. locations are empty
     * @postcondition the locations of blocks match
     *                locs, and blocks have been put in grid   
     * @param grid  grid in which blocks are added
     * @param locs  locations for blocks to be added
     */
    private void addToLocations(MyBoundedGrid<Block> grid, Location[] locs)
    {
        for (int i = 0; i < 4; i++)
        {
            blocks[i].putSelfInGrid(grid, locs[i]);
        }
    }

    /**
     * Removes the blocks
     * from their current locations
     * in the grid.
     * 
     * @precondition the blocks are in the grid
     * @return an array representing their
     *          old locations
     */
    private Location[] removeBlocks()
    {
        Location[] locs = new Location[4];
        for (int i = 0; i < 4; i++)
        {
            locs[i] = blocks[i].getLocation();
            blocks[i].removeSelfFromGrid();
        }
        return locs;
    }

    /**
     * Checks if the given locations
     * in the grid are valid and empty.
     * 
     * @param grid grid on which locations are checked
     * @param locs array of grid locations to be checked
     * @return true if the locations
     *          are valid and empty,
     *          and false otherwise
     */
    private boolean areEmpty(MyBoundedGrid<Block> grid, Location[] locs)
    {
        for (int i = 0; i < 4; i++)
        {
            if (!(grid.isValid(locs[i]) && grid.get(locs[i]) == null))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Translates the tetrad
     * by a given number of rows
     * and columns, and returns
     * true.
     * Does not do anything if
     * the new location is not
     * empty or not valid, and
     * returns false.
     * 
     * @param deltaRow  number of rows
     *                  to be shifted right by
     * @param deltaCol  number of cols
     *                  to be shifted down by 
     * @return true if the tetrad is translated,
     *          false otherwise
     */
    public boolean translate(int deltaRow, int deltaCol)
    {
        try
        {
            lock.acquire();
            Location[] locs = removeBlocks();
            Location[] locs2 = new Location[4];
            for (int i = 0; i < 4; i++)
            {
                locs2[i] = new Location(locs[i].getRow() + deltaRow, locs[i].getCol() + deltaCol);
            }
            if (areEmpty(grid, locs2))
            {
                for (int i = 0; i < 4; i++)
                {
                    blocks[i].putSelfInGrid(grid, locs2[i]);
                }
                return true;
            }
            else
            {
                for (int i = 0; i < 4; i++)
                {
                    blocks[i].putSelfInGrid(grid, locs[i]);
                }
                return false;
            }
        }
        catch (InterruptedException e)
        {
            //did not modify the tetrad
            return false;
        }
        finally
        {
            lock.release();
        }
    }

    /**
     * Rotates the tetrad clockwise 90 degrees
     * around its middle block and returns
     * true.
     * Does not do anything if
     * its new location is not
     * empty or not valid, and
     * returns false.
     * The cyan doesn't rotate,
     * but still returns true.
     * 
     * @return true if the tetrad is rotated,
     *          false otherwise
     */
    public boolean rotate()
    {
        try
        {
            lock.acquire();
            if (blocks[0].getColor().equals(Color.CYAN))
            {
                return true;
            }
            Location[] locs = removeBlocks();
            Location[] locs2 = new Location[4];
            for (int i = 0; i < 4; i++)
            {
                int newRow = locs[0].getRow() - locs[0].getCol() + locs[i].getCol();
                int newCol = locs[0].getRow() + locs[0].getCol() - locs[i].getRow();
                locs2[i] = new Location(newRow, newCol);
            }
            if (areEmpty(grid, locs2))
            {
                for (int i = 0; i < 4; i++)
                {
                    blocks[i].putSelfInGrid(grid, locs2[i]);
                }
                return true;
            }
            else
            {
                for (int i = 0; i < 4; i++)
                {
                    blocks[i].putSelfInGrid(grid, locs[i]);
                }
                return false;
            }
        }
        catch (InterruptedException e)
        {
            //did not modify the tetrad
            return false;
        }
        finally
        {
            lock.release();
        }
    }

}
