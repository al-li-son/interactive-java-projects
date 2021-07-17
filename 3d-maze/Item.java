import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Item
{
    private Location l;
    private String id;
    private BufferedImage img;
    private int durability;

    public Item(Location l, String id, BufferedImage img, int durability)
    {
        this.l = l;
        this.id = id;
        this.img = img;
        this.durability = durability;
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
    public String getID()
    {
        return id;
    }
    public BufferedImage getImage()
    {
        return img;
    }
    public int getDurability()
    {
        return durability;
    }
    public void damage(int a)
    {
        durability -= a;
    }
}