import java.util.ArrayList;
public class Maze
{
    private ArrayList<Wall> walls = new ArrayList<Wall>();
    private ArrayList<Item> items = new ArrayList<Item>();
    private int endX = 0;
    private int endY = 0;
    public Maze()
    {   
    }
    public Maze(ArrayList<Wall> walls)
    {
        this.walls = walls;
    }
    public ArrayList<Wall> getWalls()
    {
        return walls;
    }
    public void addWall(Location l)
    {
        walls.add(new Wall(l));
    }
    public boolean isWall(Location l)
    {
        for(int x=0; x<walls.size(); x++)
            if(walls.get(x).getLocation().equals(l))
                return true;
        return false;
    }
    public void destroyWall(Location l)
    {
        for(int x=0; x<walls.size(); x++)
            if(walls.get(x).getLocation().equals(l))
            {
                walls.remove(x);
                x--;
            }
    }
    public int itemIndexHere(Location l)
    {
        for(int x=0; x<items.size(); x++)
            if(items.get(x).getLocation().equals(l))
                return x;
        return -1;
    }
    public ArrayList<Item> getItems()
    {
        return items;
    }
    public Item getItem(int i)
    {
        return items.get(i);
    }
    public void addItem(Item i)
    {
        items.add(i);
    }
    public void pickUpItem(int i)
    {
        items.remove(i);
    }
    public void setEnd(int a, int b)
    {
        endX = a;
        endY = b;
    }
    public Location getEnd()
    {
        return new Location(endX, endY);
    }
}