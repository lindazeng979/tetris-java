import java.awt.Color;
/**
 * This class Block encapsulates a Block abstraction
 * which can be placed into a Gridworld style grid.
 * It holds the color it will show up as, the grid,
 * and the location in the grid it is in. It
 * contains standard getters and setters
 * as well as methods to remove, put, and move
 * itself in grids.
 * 
 * @author Linda Zeng
 * @version 3.5.23
 */
public class Block
{
    //grid the block is in
    private MyBoundedGrid<Block> grid;
    //location in the grid the block is in
    private Location location;
    //color of the block
    private Color color;

	/**
     * Constructs a blue block, because
     * blue is the greatest color ever!
     * Initializes grid and location to null
     * because it has not
     * been put in a grid.
     */
    public Block()
    {
        color = Color.BLUE;
        grid = null;
        location = null;
    }

	/**
	 * Returns the color of the block.
     * 
     * @return the Color of the block
     */
    public Color getColor()
    {
        return color;
    }

	/**
	 * Sets the color of
     * the block to a given color.
     * 
     * @param newColor  color to set the block to
	 */
    public void setColor(Color newColor)
    {
        color = newColor;
    }
    
	/**
     * Returns the grid of blocks
     * the block is in and null
     * if the block is not in a grid
     * 
     * @return the bounded grid of blocks
     *          that the block is in, or null
     */
    public MyBoundedGrid<Block> getGrid()
    {
        return grid;
    }
    
	/**
	 * Returns the location of
     * the block in the grid
     * it is in; null if it is
     * not in a grid.
     * 
     * @return the Location of
     *         the block in the grid,
     *          or null
	 */
    public Location getLocation()
    {
        return location;
    }
    
	/**
	 * Removes the block from
     * the grid it is in,
     * updating both the location
     * in the grid and its own instance
     * variables to null.
     * Does nothing if the block is
     * not in a grid.
	 */
    public void removeSelfFromGrid()
    {
        if (grid != null)
        {
            grid.remove(location);
            location = null;
            grid = null;
        }
    }
    
	/**
	 * Puts the block in a given
     * grid at the given location,
     * updating the grid 
     * and its own instance variables'
     * information as well as the previous
     * block's instance variables'
     * information (removing the previous
     * block if there was one).
     * Does nothing if the grid is null, or if the
     * location is not valid/is null in the grid.
     * 
     * @param gr    the grid to be put in
     * @param loc   a valid location in the grid,
     *              where the block should be put  
	 */
    public void putSelfInGrid(MyBoundedGrid<Block> gr, Location loc)
    {
        if (gr != null && gr.isValid(loc))
        {
            if (gr.get(loc) != null)
            {
                gr.get(loc).removeSelfFromGrid();
            }
            gr.put(loc, this);
            grid = gr;
            location = loc;
        }
    }

    /**
	 * Moves the block
     * from its current location
     * in its grid to a given
     * new location. It updates
     * its own instance
     * variable and the grid's information
     * as well as the previous
     * block's instance variables'
     * information (removing the previous block
     * if there was one).
     * Does nothing if the new location
     * is its own location, if the block is
     * not in a grid, or if the
     * location is not valid/ is null in the grid.
     * 
     * @param newLocation   valid location in the
     *                      grid the block is in
	 */
    public void moveTo(Location newLocation)
    {
        if (grid != null && grid.isValid(location))
        {
            if (!newLocation.equals(location))
            {
                if (grid.get(newLocation) != null)
                {
                    grid.get(newLocation).removeSelfFromGrid();
                }
                grid.put(newLocation, grid.remove(location));
                location = newLocation;
            }
        }
    }

    /**
	 * Returns a string with the location
     * and color of this block
     * 
     * @return String representing the block
	 */
    public String toString()
    {
        return "Block[location=" + location + ",color=" + color + "]";
    }
}