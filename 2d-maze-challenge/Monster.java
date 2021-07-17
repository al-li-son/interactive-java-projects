import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
public class Monster
{
	private int col;
	private int row;
	private int direction;
	private BufferedImage monster;

	public Monster(int col, int row, int w, int h, int shift)
	{
		this.col=col;
		this.row=row;
		direction=37;
		try
		{
			monster=ImageIO.read(new File("Monster.png"));
		}
		catch(IOException io)
		{
			System.err.println("File does not exist");
		}
	}
	public void move(Hero h, ArrayList<Wall> walls)
	{
		int tempRow=row;
		int tempCol=col;
		switch(direction)
		{
			//up
			case 38: tempRow--;
				break;
			//down
			case 40: tempRow++;
				break;
			//left
			case 37: tempCol--;
				break;
			//right
			case 39: tempCol++;
				break;
		}
		if(!intersects(tempCol,tempRow,walls))
		{
			col=tempCol;
			row=tempRow;
		}
		int change=(int)(Math.random()*2);
		if(change==0)
		{
			int rand=(int)(Math.random()*2);
			if(rand==0)
				rand--;
			if(direction==38 || direction==40){
				if(!intersects(col-rand,row,walls))
					direction=38-rand;
				else if(!intersects(col+rand,row,walls))
					direction=38+rand;
				else if(intersects(tempCol,tempRow,walls))
					if(direction==38)
						direction=40;
					else
						direction=38;
			}
			else{
				if(!intersects(col,row-rand,walls))
					direction=39-rand;
				else if(!intersects(col,row+rand,walls))
					direction=39+rand;
				else if(intersects(tempCol,tempRow,walls))
					if(direction==37)
						direction=39;
					else
						direction=37;
			}
		}
	}
	public boolean intersects(int x, int y, ArrayList<Wall> walls)
	{
		if(x<0 || y<0)
			return true;
		for(int z=0;z<walls.size();z++)
		{
			if(x==walls.get(z).getCol() && y==walls.get(z).getRow())
				return true;
		}
		return false;
	}
	public int getCol()
	{
		return col;
	}
	public int getRow()
	{
		return row;
	}
	public BufferedImage getImage()
	{
		return monster;
	}
}