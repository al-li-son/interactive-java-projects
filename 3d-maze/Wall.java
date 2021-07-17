public class Wall
{
    private Location l;
    public Wall(Location l)
    {
        this.l = l;
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
}