import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import java.io.*;

public class Hero
{
	private int col;
	private int row;
	private int lives;
	private int health;
	private boolean died;
	private int w;
	private int h;
	private int shift;

	private Clip death;
	private BufferedImage Standing1;
	private BufferedImage Standing2;
	private BufferedImage Up;
	private BufferedImage Down;
	private BufferedImage Right;
	private BufferedImage Left;
	private BufferedImage hero;

	public Hero(int col, int row, int w, int h, int shift)
	{
		this.col=col;
		this.row=row;
		this.w=w;
		this.h=h;
		this.shift=shift;
		lives=2;
		health=5;
		died=false;
		try
		{
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("death.wav"));
			death = AudioSystem.getClip();
			death.open(audioStream);
		}
		catch(LineUnavailableException lue){}
		catch(UnsupportedAudioFileException uafe){}
		catch(IOException ioe){System.out.println("File does not exist");}
		try
		{
			Standing1=ImageIO.read(new File("Standing1.png"));
		}
		catch(IOException io)
		{
			System.err.println("File does not exist");
		}
		try
		{
			Standing2=ImageIO.read(new File("Standing2.png"));
		}
		catch(IOException io)
		{
			System.err.println("File does not exist");
		}
		try
		{
			Up=ImageIO.read(new File("Up.png"));
		}
		catch(IOException io)
		{
			System.err.println("File does not exist");
		}
		try
		{
			Down=ImageIO.read(new File("Down.png"));
		}
		catch(IOException io)
		{
			System.err.println("File does not exist");
		}
		try
		{
			Left=ImageIO.read(new File("Left.png"));
		}
		catch(IOException io)
		{
			System.err.println("File does not exist");
		}
		try
		{
			Right=ImageIO.read(new File("Right.png"));
		}
		catch(IOException io)
		{
			System.err.println("File does not exist");
		}
		hero=Standing1;
	}
	public void move(int button, ArrayList<Wall> walls)
	{
		int tempCol=col;
		int tempRow=row;
		switch(button)
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
		if(!intersects(tempCol,tempRow,walls)){
			col=tempCol;
			row=tempRow;
		}
	}
	public boolean intersects(int x, int y, ArrayList<Wall> walls)
	{
		if(x<0 || y<0)
			return true;
		for(Wall w: walls)
		{
			if((!(w instanceof StartWall) && x==w.getCol() && y==w.getRow()))
			{
				if(w instanceof EndWall)
					if(((EndWall)(w)).unlocked())
						return false;
				return true;
			}
		}
		return false;
	}
	public boolean touchingLava(int x, int y, ArrayList<Lava> lava)
	{
		if(x<0 || y<0)
			return true;
		for(Lava l: lava)
			if(x==l.getCol() && y==l.getRow())
				return true;
		return false;
	}
	public void kill()
	{
		lives--;
	}
	public void damage()
	{
		health--;
		if(health<=0)
		{
			lives--;
			died=true;
			death.start();
		}
	}
	public boolean hasDied()
	{
		return died;
	}
	public void setDeath(boolean d)
	{
		died=d;
	}
	public void respawn(ArrayList<Wall> walls, ArrayList<Lava> lava, int totCols, int totRows)
	{
		int tempCol;
		int tempRow;
		do
		{
			tempCol=(int)(Math.random()*totCols);
			tempRow=(int)(Math.random()*totRows);
		}while(intersects(tempCol,tempRow,walls) || touchingLava(tempCol,tempRow,lava));
		col=tempCol;
		row=tempRow;
		health=5;
		died=false;
	}
	public void regenerate()
	{
		lives++;
	}
	public int getLives()
	{
		return lives;
	}
	public int getHealth()
	{
		return health;
	}
	public int getCol()
	{
		return col;
	}
	public int getRow()
	{
		return row;
	}
	public void setImage(String a)
	{
		switch(a)
		{
			case "stand": hero=Standing1;
				break;
			case "stand left": hero=Standing2;
				break;
			case "up": hero=Up;
				break;
			case "down": hero=Down;
				break;
			case "left": hero=Left;
				break;
			case "right": hero=Right;
				break;
		}
	}
	public BufferedImage getImage()
	{
		return hero;
	}
	public Ellipse2D getEllipse()
	{
		return new Ellipse2D.Double(col*w+shift,row*h+shift,w,h);
	}
}