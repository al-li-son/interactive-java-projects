import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
public class Item
{
	private int col;
	private int row;
	private BufferedImage image;
	private boolean pickedUp;
	public Item(int col, int row, String img)
	{
		this.col=col;
		this.row=row;
		pickedUp=false;
		try
		{
			image=ImageIO.read(new File(img+".png"));
		}
		catch(IOException io)
		{
			System.err.println("File does not exist");
		}
	}
	public int getCol()
	{
		return col;
	}
	public int getRow()
	{
		return row;
	}
	public void interact(boolean pickedUp)
	{
		this.pickedUp=pickedUp;
	}
	public boolean pickedUp()
	{
		return pickedUp;
	}
	public BufferedImage getImage()
	{
		return image;
	}
}