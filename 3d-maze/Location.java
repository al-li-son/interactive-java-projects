public class Location
{
    private int x;
    private int y;
    public Location(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    public void addX(int a)
    {
        x+=a;
    }
    public void addY(int a)
    {
        y+=a;
    }
    public void setX(int a)
    {
        x = a;
    }
    public void setY(int a)
    {
        y = a;
    }
    public void set(int a, int b)
    {
        x = a;
        y = b;
    }
    public boolean equals(Location a)
    {
        if(x == a.getX() && y == a.getY())
            return true;
        return false;
    }
}