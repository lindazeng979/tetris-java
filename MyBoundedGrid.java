import java.util.*;

/**
 * This class is my implementation
 * of a GridWorld style grid
 * that uses locations to place
 * objects into a grid. It contains
 * the necessary methods
 * to added, remove, and access
 * locations of the grid.
 * 
 * @author Linda Zeng
 * @version 3.5.23
 * @param <E>   the type of object the grid holds
 */
public class MyBoundedGrid<E>
{
    //2d array representing the grid
    private Object[][] grid;

    /**
     * Constructs an instance of
     * MyBoundedGrid with the given
     * number of rows and columns.
     * 
     * @param rows  the number of rows in the grid
     * @param cols  the number of columns in the grid
     */
    public MyBoundedGrid(int rows, int cols)
    {
        grid = new Object[rows][cols];
    }

    /**
     * Returns the number of rows in the grid.
     * 
     * @return the integer representing rows
     */
    public int getNumRows()
    {
        return grid.length;
    }

    /**
     * Returns the number of columns in the grid.
     * 
     * @return the integer representing columns
     */
    public int getNumCols()
    {
        return grid[0].length;
    }

    /**
     * Checks if a given location
     * is valid in the grid (it is
     * not null and inside the rows and columns)
     * 
     * @param loc   location to be checked
     * @return true if location is valid, false otherwise
     */
    public boolean isValid(Location loc)
    {
        if (loc != null)
            return (loc.getRow() >= 0 && loc.getRow() < grid.length && loc.getCol() >= 0 && loc.getCol() < grid[0].length);
        return false;
    }

    /**
     * Puts a given object
     * in a given location
     * in the grid and
     * returns the previous object. 
     * 
     * @precondition the location is valid
     * 
     * @param loc   a valid location in the grid
     *              to put the object
     * @param obj   the object to be put
     * @return the previous object in the location,
     *          or null if there was none
     */
    public E put(Location loc, E obj)
    {
        E prev = (E)grid[loc.getRow()][loc.getCol()];
        grid[loc.getRow()][loc.getCol()] = obj;
        return prev;
        
    }

    /**
     * Removes the object
     * at the given location
     * in the grid and
     * returns it.
     * 
     * @precondition the location is valid
     * 
     * @param loc   a valid location in the grid
     *              to remove the object
     * @return the object that was removed,
     *         null if there was none
     */
    public E remove(Location loc)
    {
        E prev = (E)grid[loc.getRow()][loc.getCol()];
        grid[loc.getRow()][loc.getCol()] = null;
        return prev;
    }

    /**
     * Gets the object
     * at the given location.
     * 
     * @precondition the location is valid
     * 
     * @param loc   a valid location in the grid
     *              to get the object at
     * @return the object at the location,
     *          null if there was none
     */
    public E get(Location loc)
    {
        return (E)grid[loc.getRow()][loc.getCol()];
    }

    /**
     * Goes through the grid
     * and checks for all nonnull,
     * occupied locations.
     * Returns a list of occupied locations.
     * 
     * @return an ArrayList of all the occupied
     *          locations in the grid.
     */
    public ArrayList<Location> getOccupiedLocations()
    {
        ArrayList<Location> res = new ArrayList<Location>();
        for (int i = getNumRows() - 1; i >= 0; i--)
        {
            for (int j = 0; j < getNumCols(); j++)
            {
                if (grid[i][j] != null)
                {
                    res.add(new Location(i, j));
                }
            }
        }
        return res;
    }
}
