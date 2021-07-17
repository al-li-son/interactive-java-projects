import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
public class Lava
{
	private int col;
	private int row;
	private int level;
	private BufferedImage lava;

	public Lava(int col, int row, int level)
	{
		this.col=col;
		this.row=row;
		this.level=level;
		try
		{
			lava=ImageIO.read(new File("Lava.png"));
		}
		catch(IOException io)
		{
			System.err.println("File does not exist");
		}
	}
	public boolean intersects(int x, int y, ArrayList<Wall> walls, ArrayList<Lava> lava)
	{
		if(x<0 || y<0)
			return true;
		for(Wall w: walls)
		{
			if((!(w instanceof StartWall) && x==w.getCol() && y==w.getRow()))
				return true;
		}
		for(Lava l: lava)
		{
			if(x==l.getCol() && y==l.getRow())
				return true;
		}
		return false;
	}
	public ArrayList<Integer> getAdjacentLocations(int c, int r, ArrayList<Wall> walls, ArrayList<Lava> lava)
	{
		//right=2 down=3 left=4
		ArrayList<Integer> locs=new ArrayList<Integer>();
		if(!intersects(c,r-1,walls,lava))
			locs.add(1);
		if(!intersects(c+1,r,walls,lava))
			locs.add(2);
		if(!intersects(c,r+1,walls,lava))
			locs.add(3);
		if(!intersects(c-1,r,walls,lava))
			locs.add(4);
		return locs;
	}
	public void setLevel(int l)
	{
		level=l;
	}
	public int getCol()
	{
		return col;
	}
	public int getRow()
	{
		return row;
	}
	public int getLevel()
	{
		return level;
	}
	public BufferedImage getImage()
	{
		return lava;
	}
}