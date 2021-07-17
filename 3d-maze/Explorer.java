import java.util.ArrayList;

public class Explorer
{
    private Location l;
    private int direction; //0:north, 1:east, 2:south, 3:west
    private ArrayList<Item> inventory;

    public Explorer()
    {
        l = new Location(0,0);
        direction = 0;
    }
    public Explorer(Location l, int direction)
    {
        this.l = l;
        this.direction = direction;
        inventory = new ArrayList<Item>();
    }
    public Location getLocation()
    {
        return l;
    }
    public int getX()
    {
        return l.getX();
    }
    public int getY()
    {
        return l.getY();
    }
    public int getDirection()
    {
        return direction;
    }
    public void move(int a, Maze m)
    {
        int tempX = l.getX();
        int tempY = l.getY();
        if(a==0)
        {
            switch(direction)
            {
                case 0: l.addY(-1);
                    break;
                case 1: l.addX(1);
                    break;
                case 2: l.addY(1);
                    break;
                case 3: l.addX(-1);
                    break;
            }
        }
        else if(a==1)
        {
            switch(direction)
            {
                case 0: l.addY(1);
                    break;
                case 1: l.addX(-1);
                    break;
                case 2: l.addY(-1);
                    break;
                case 3: l.addX(1);
                    break;
            }            
        }
        if(m.isWall(l) || (l.getX() < 0 || l.getY() < 0))
        {
            l.setX(tempX);
            l.setY(tempY);
        }
    }
    public Location scan(int a, int b)
    {
        //values of a (determines which wall is being checked): 0-front 1-left 2-right
        //values of b determine how far ahead to look
        Location leftWall = new Location(0,0);
        Location frontWall = new Location(0,0);
        Location rightWall = new Location(0,0);

        switch(direction)
        {
            case 0: leftWall.set(getX()-1, getY()-b);
                    frontWall.set(getX(), getY()-b-1);
                    rightWall.set(getX()+1, getY()-b);
                break;
            case 1: leftWall.set(getX()+b, getY()-1);
                    frontWall.set(getX()+b+1, getY());
                    rightWall.set(getX()+b, getY()+1);                    
                break;
            case 2: leftWall.set(getX()+1, getY()+b);
                    frontWall.set(getX(), getY()+b+1);
                    rightWall.set(getX()-1, getY()+b);
                break;
            case 3: leftWall.set(getX()-b, getY()+1);
                    frontWall.set(getX()-b-1, getY());
                    rightWall.set(getX()-b, getY()-1);             
                break;
        }
        switch(a)
        {
            case 0: return frontWall;
            case 1: return leftWall;
            case 2: return rightWall;
        }
        return new Location(0,0);
    }
    public void turn(int a) //0:right, 1:left
    {
        if(a == 0 && direction != 3)
            direction++;
        else if(a == 0 && direction == 3)
            direction = 0;
        else if(a == 1 && direction != 0)
            direction--;
        else
            direction = 3;
    }
    public void pickUp(Item a)
    {
        inventory.add(a);
    }
    public ArrayList<Item> getInventory()
    {
        return inventory;
    }
    public void damageTool(int a, int b) //a = tool index in inventory array, b = amount of damage
    {
        inventory.get(a).damage(b);
        if(inventory.get(a).getDurability() <= 0)
            inventory.remove(a);
    }
}