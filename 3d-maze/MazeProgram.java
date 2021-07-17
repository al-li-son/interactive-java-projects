import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Polygon;
import java.util.Date;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.GradientPaint;

public class MazeProgram extends JPanel implements KeyListener,MouseListener,Runnable
{
	//Screen variables
	JFrame frame;
	Thread thread;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int screenX = (int)(.51*screenSize.width);
	int screenY = (int)(.9*screenSize.height);
	int scale = 0;

	//Maze setup variables
	int fov = 5;
	Maze maze = new Maze();
	Explorer player;

	//Game control variables
	int level = 1;
	boolean gameOn = true;
	boolean paused = false;
	boolean endScreen = false;
	boolean menu = true;
	boolean levelChange = true;
	boolean wallDestroy = false;
	boolean movingF = false;
	boolean movingB = false;
	boolean mapOn = false;

	long initialTime = System.currentTimeMillis();
	long pausedElapsedTime = 0;

	long lvlinttime = System.currentTimeMillis();
	long lvlpsdelapsedtime = 0;

	//Display variables
	ArrayList<Polygon> leftWalls = new ArrayList<Polygon>();
	ArrayList<Polygon> rightWalls = new ArrayList<Polygon>();
	ArrayList<Polygon> frontWalls = new ArrayList<Polygon>();
	int ffWall = -1; //furthest front wall

	//Images
	BufferedImage battery;
	BufferedImage hammer;

	public MazeProgram()
	{
		if(screenX > screenY)
			scale = screenY/400;
		else
			scale = screenX/400;	
		try
		{
			battery=ImageIO.read(new File("battery.png"));
		}
		catch(IOException io)
		{
			System.err.println("File does not exist");
		}			
		try
		{
			hammer=ImageIO.read(new File("hammer.png"));
		}
		catch(IOException io)
		{
			System.err.println("File does not exist");
		}			

		setBoard();
		frame=new JFrame();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(screenX+13,screenY+36);
		frame.setVisible(true);
		frame.addKeyListener(this);
		thread = new Thread(this);
		thread.start();
		//this.addMouseListener(this); //in case you need mouse clicking
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(Color.BLACK);	//this will set the background color
		g.fillRect(0,0,screenX,screenY);  //since the screen size is 1000x800
									//it will fill the whole visible part
									//of the screen with a black rectangle

		//drawBoard here!

		Color custCyan = new Color(0, 160, 180);
		Color custBlue = new Color(0, 0, 130);
		//3D painting
		//Background (for floors and ceilings)
		g.setColor(custCyan);
		g.fillRect(0, 0, screenX, 70*fov);
		g.setColor(custBlue);
		g.fillRect(0, screenY-70*fov, screenX, 70*fov);

		//Ceiling
		GradientPaint ceilFade = new GradientPaint(screenX/2, 0, custCyan, screenX/2, fov*70, new Color(0, 40, 60));
		g2.setPaint(ceilFade);

		for(int x=0; x<fov; x++)
		{
			g2.fillPolygon(new Polygon(new int[]{0, screenX, screenX, 0}, new int[]{x*70, x*70, 70+x*70, 70+x*70}, 4));	
			g.setColor(Color.BLACK);
			g.drawPolygon(new Polygon(new int[]{x*70, screenX-x*70, screenX-70-x*70, 70+x*70},
									new int[]{x*70, x*70, 70+x*70, 70+x*70}, 4));	
			g2.setPaint(ceilFade);	
		}

		GradientPaint floorFade = new GradientPaint(screenX/2, screenY, custBlue, screenX/2, screenY-fov*70, new Color(0, 0, 50));
		g2.setPaint(floorFade);

		for(int x=0; x<fov; x++)
		{
			g2.fillPolygon(new Polygon(new int[]{0, screenX, screenX, 0}, new int[]{screenY-70-x*70, screenY-70-x*70, screenY-x*70, screenY-x*70}, 4));
			g.setColor(Color.BLACK);
			g.drawPolygon(new Polygon(new int[]{70+x*70, screenX-70-x*70, screenX-x*70, x*70},
									new int[]{screenY-70-x*70, screenY-70-x*70, screenY-x*70, screenY-x*70}, 4));
			g2.setPaint(floorFade);
		}

		//Openings
		int grayNum = 255;
		int darkenFactor = 220/fov;
		for(int x=0; x<fov; x++)
		{
			grayNum -= darkenFactor*x;
			if(grayNum < 0)
				grayNum = 0;
			g.setColor(new Color(grayNum, grayNum, grayNum));
			g.fillPolygon(new Polygon(new int[]{x*70, 70+x*70, 70+x*70, x*70},
									new int[]{70+x*70, 70+x*70, screenY-70-x*70, screenY-70-x*70}, 4));
			g.fillPolygon(new Polygon(new int[]{screenX-70-x*70, screenX-x*70, screenX-x*70, screenX-70-x*70},
									new int[]{70+x*70, 70+x*70, screenY-70-x*70, screenY-70-x*70}, 4));
			g.setColor(Color.BLACK);
			g.drawPolygon(new Polygon(new int[]{x*70, 70+x*70, 70+x*70, x*70},
									new int[]{70+x*70, 70+x*70, screenY-70-x*70, screenY-70-x*70}, 4));
			g.drawPolygon(new Polygon(new int[]{screenX-70-x*70, screenX-x*70, screenX-x*70, screenX-70-x*70},
									new int[]{70+x*70, 70+x*70, screenY-70-x*70, screenY-70-x*70}, 4));		
			grayNum = 255;	
		}

		GradientPaint hallFade = new GradientPaint(0, screenY/2, Color.WHITE, fov*70, screenY/2, new Color(79,79,79));
		g2.setPaint(hallFade);
		for(Polygon p : leftWalls)
		{
			g2.fillPolygon(p);
			g.setColor(Color.BLACK);
			g.drawPolygon(p);
			g2.setPaint(hallFade);			
		}

		hallFade = new GradientPaint(screenX-fov*70, screenY/2, new Color(79,79,79), screenX, screenY/2, Color.WHITE);
		g2.setPaint(hallFade);
		for(Polygon p : rightWalls)
		{
			g2.fillPolygon(p);
			g.setColor(Color.BLACK);
			g.drawPolygon(p);
			g2.setPaint(hallFade);
		}

		for(int x=0; x<frontWalls.size(); x++)
		{
			grayNum = 35 + darkenFactor + darkenFactor*x;
			if(grayNum > 255)
				grayNum = 255;
			g.setColor(new Color(grayNum, grayNum, grayNum));
			if(frontWalls.get(x)!=null)
			{
				g.fillPolygon(frontWalls.get(x));
				g.setColor(Color.BLACK);
				g.drawPolygon(frontWalls.get(x));
			}
		}	
		//checking for items
		for(int x=ffWall; x>=0; x--)
		{
			int itemIndex = maze.itemIndexHere(player.scan(0, x));
			if(itemIndex != -1)
			{
				Item temp = maze.getItem(itemIndex);
				if(temp.getID() == "battery")
					g.drawImage(temp.getImage(), screenX/2 - 200 + x*60, screenY/2 - 100 + x*30, 400 - x*120, 200 - x*60, null);
				else if(temp.getID() == "hammer")
					g.drawImage(temp.getImage(), screenX/2 - 150 + x*45, screenY/2 - 200 + x*60, 300 - x*90, 400 - x*120, null);
			}
		}			
		if(player.getInventory().size() > 0)
		{
			g.drawImage(hammer, 20, 20, 20+scale*60, 20+scale*80, null);
		}

		if(mapOn)
		{
			Color transparentBlack = new Color(0,0,0,127);
			g.setColor(transparentBlack);
			g.fillRect(0,0,screenX,screenY);

			ArrayList<Wall> walls = maze.getWalls();
			ArrayList<Item> items = maze.getItems();

			//2D Painting
			g.setColor(Color.WHITE);
			for(int x=0; x<walls.size(); x++)
			{
				int wallX = walls.get(x).getX();
				int wallY = walls.get(x).getY();
				g.fillRect(50+wallX*(scale*8), 50+wallY*(scale*8), (scale*8), (scale*8));
			}
			
			for(int x=0; x<items.size(); x++)
			{
				int itemX = items.get(x).getX();
				int itemY = items.get(x).getY();
				if(items.get(x).getID() == "battery")
				{
					g.setColor(Color.YELLOW);
					g.fillOval(50+itemX*(scale*8), 50+itemY*(scale*8), (scale*8), (scale*8));
				}
				else if(items.get(x).getID() == "hammer")
				{
					g.setColor(new Color(180, 72, 52));
					g.fillRect(50+itemX*(scale*8), 50+itemY*(scale*8), (scale*8), (scale*8));
				}
				
			}

			g.setColor(Color.RED);
			int playerX = player.getX();
			int playerY = player.getY();

			switch(player.getDirection())
			{
				case 0: 
					g.fillPolygon(new int[]{50+playerX*8*scale+4*scale, 50+playerX*8*scale, 50+playerX*8*scale+8*scale},
									new int[]{50+playerY*8*scale, 50+playerY*8*scale+8*scale, 50+playerY*8*scale+8*scale},3);
					break;
				case 1:
					g.fillPolygon(new int[]{50+playerX*8*scale, 50+playerX*8*scale, 50+playerX*8*scale+8*scale},
									new int[]{50+playerY*8*scale, 50+playerY*8*scale+8*scale, 50+playerY*8*scale+4*scale},3);
					break;
				case 2:
					g.fillPolygon(new int[]{50+playerX*8*scale, 50+playerX*8*scale+4*scale, 50+playerX*8*scale+8*scale},
									new int[]{50+playerY*8*scale, 50+playerY*8*scale+8*scale, 50+playerY*8*scale},3);
					break;
				case 3:
					g.fillPolygon(new int[]{50+playerX*8*scale+8*scale, 50+playerX*8*scale+8*scale, 50+playerX*8*scale},
									new int[]{50+playerY*8*scale, 50+playerY*8*scale+8*scale, 50+playerY*8*scale+4*scale},3);
					break;
			}
		}

		//Compass
		g.setColor(Color.WHITE);
		g.fillPolygon(new int[]{screenX-10-80, screenX-10-70, screenX-10-90}, new int[]{screenY-10-160, screenY-10-90, screenY-10-90}, 3);
		g.fillPolygon(new int[]{screenX-10-70, screenX-10, screenX-10-70}, new int[]{screenY-10-90, screenY-10-80, screenY-10-70}, 3);
		g.fillPolygon(new int[]{screenX-10-90, screenX-10-70, screenX-10-80}, new int[]{screenY-10-70, screenY-10-70, screenY-10}, 3);
		g.fillPolygon(new int[]{screenX-10-160, screenX-10-90, screenX-10-90}, new int[]{screenY-10-80, screenY-10-90, screenY-10-70}, 3);
		g.setColor(Color.RED);
		switch(player.getDirection())
		{
			case 0: g.fillPolygon(new int[]{screenX-10-80, screenX-10-70, screenX-10-90}, new int[]{screenY-10-160, screenY-10-90, screenY-10-90}, 3);
				break;
			case 1: g.fillPolygon(new int[]{screenX-10-70, screenX-10, screenX-10-70}, new int[]{screenY-10-90, screenY-10-80, screenY-10-70}, 3);
				break;
			case 2: g.fillPolygon(new int[]{screenX-10-90, screenX-10-70, screenX-10-80}, new int[]{screenY-10-70, screenY-10-70, screenY-10}, 3);
				break;
			case 3: g.fillPolygon(new int[]{screenX-10-160, screenX-10-90, screenX-10-90}, new int[]{screenY-10-80, screenY-10-90, screenY-10-70}, 3);
				break;
		}
		g.setColor(Color.BLACK);
		g.drawPolygon(new int[]{screenX-10-80, screenX-10-70, screenX-10-90}, new int[]{screenY-10-160, screenY-10-90, screenY-10-90}, 3);
		g.drawPolygon(new int[]{screenX-10-70, screenX-10, screenX-10-70}, new int[]{screenY-10-90, screenY-10-80, screenY-10-70}, 3);
		g.drawPolygon(new int[]{screenX-10-90, screenX-10-70, screenX-10-80}, new int[]{screenY-10-70, screenY-10-70, screenY-10}, 3);
		g.drawPolygon(new int[]{screenX-10-160, screenX-10-90, screenX-10-90}, new int[]{screenY-10-80, screenY-10-90, screenY-10-70}, 3);
		g.setColor(Color.GREEN);
		g.fillRect(screenX-10-90, screenY-10-90, 20, 20);

		if(levelChange)
		{
			Color transparentWhite = new Color(255,255,255,127);
			g.setColor(transparentWhite);
			g.fillRect(scale*50, screenY/2 - 100, screenX - scale*100, 210);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Impact", Font.BOLD, scale*230));
			g.drawString("LEVEL "+level, scale*50, screenY/2 + 100);	
		}

		if(menu)
		{
			Color transparentBlack = new Color(0,0,0,200);
			g.setColor(transparentBlack);
			g.fillRect(0,0,screenX,screenY);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Impact", Font.PLAIN, scale*50));
			g.drawString("INSTRUCTIONS:", 20, 20+scale*50);
			g.setFont(new Font("Impact", Font.PLAIN, scale*30));
			g.drawString("WASD/Arrow Keys = Move/turn", 20, 20+scale*80);		
			g.drawString("m/SPACE = minimap", 20, 20+scale*110);
			g.drawString("b = break block (w/ hammer)", 20, 20+scale*140);
			g.drawString("p = pause", 20, 20+scale*170);	
			g.drawString("i = close/open this menu", 20, 20+scale*200);	
			g.drawString("Every 30 seconds, your flashlight will get dimmer!", 20, 20+scale*230);	
			g.drawString("Find batteries for your flashlight (yellow circles in the map).", 20, 20+scale*260);
			g.drawString("If you pick up a hammer, break some blocks!", 20, 20+scale*290);
			g.drawString("There are 3 levels that get progressively harder, good luck!", 20, 20+scale*320);
		}
		
		if(paused)
		{
			Color transparentBlack = new Color(0,0,0,150);
			g.setColor(transparentBlack);
			g.fillRect(0,0,screenX,screenY);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Impact", Font.BOLD, scale*200));
			g.drawString("PAUSED", scale*50, screenY/2 + 50);				
		}

		if(endScreen)
		{
			g.setColor(Color.BLACK);
			g.fillRect(0,0,screenX,screenY);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Impact", Font.BOLD, scale*200));
			g.drawString("THE END", scale*50, screenY/2 + 100);	
		}
		//other commands that might come in handy
		//g.setFont("Times New Roman",Font.PLAIN,18);
				//you can also use Font.BOLD, Font.ITALIC, Font.BOLD|Font.Italic
		//g.drawOval(x,y,10,10);
		//g.fillRect(x,y,100,100);
		//g.fillOval(x,y,10,10);
	}
	public void run()
	{
		while(true)
		{
			if(gameOn)
			{
				//Movement
				if(movingF)
				{
					player.move(0, maze);
				}
				else if(movingB)
				{
					player.move(1, maze);
				}

				//Maze end
				if(player.getLocation().equals(maze.getEnd()))
				{
					if(level < 3)	
					{
						level++;
						maze = new Maze();
						setBoard();
						levelChange = true;
						lvlinttime = System.currentTimeMillis();
					}
					else
					{
						gameOn = false;
						endScreen = true;
					}
				}
				//pick up items
				int itemIndex = maze.itemIndexHere(player.getLocation());
				if(itemIndex != -1)
				{
					Item temp = maze.getItem(itemIndex);
					if(temp.getID() == "battery")
						fov = 5;
					else if(temp.getID() == "hammer")
						player.pickUp(temp);
					maze.pickUpItem(itemIndex);
				}	
				if(wallDestroy)
				{
					if(player.getInventory().size() > 0)
					{
						ArrayList<Item> inventory = player.getInventory();
						if(maze.isWall(player.scan(0, 0)))
						{
							maze.destroyWall(player.scan(0,0));
							player.damageTool(0, 1);
						}
					}
					wallDestroy = false;
				}

				//Flashlight timer
				long elapsedTime = System.currentTimeMillis() - initialTime;
				if(fov > 1 && elapsedTime > 30000)
				{
					fov--;
					initialTime = System.currentTimeMillis();
				}
				//Level change display timer
				elapsedTime = System.currentTimeMillis() - lvlinttime;
				if(levelChange && elapsedTime > 3000)
				{
					levelChange = false;
				}
			}
			try
			{
				thread.sleep(70);
			}
			catch(InterruptedException e)
			{
			}
			setWalls();
			repaint();
		}
	}

	public void setBoard()
	{
		//choose your maze design

		//pre-fill maze array here

		File name = new File("maze"+level+".txt");
		try
		{
			BufferedReader input = new BufferedReader(new FileReader(name));
			String text;
			int y=0;
			while( (text=input.readLine())!= null)
			{
				for(int x=0; x<text.length(); x++)
				{
					if(text.charAt(x) == '#')
						maze.addWall(new Location(x,y));
					else if(text.charAt(x) == 's')
						player = new Explorer(new Location(x,y), 2);
					else if(text.charAt(x) == 'e')
						maze.setEnd(x, y);
					else if(text.charAt(x) == 'b')
						maze.addItem(new Item(new Location(x,y), "battery", battery, 1));
					else if(text.charAt(x) == 'h')
						maze.addItem(new Item(new Location(x,y), "hammer", hammer, 5));
				}
				y++;
			}
		}
		catch (IOException io)
		{
			System.err.println("File error");
		}

		setWalls();
	}

	public void setWalls()
	{
		leftWalls = new ArrayList<Polygon>();
		rightWalls = new ArrayList<Polygon>();
		frontWalls = new ArrayList<Polygon>();
		//checking left and right walls
		for(int x=0; x<fov; x++)
		{
			if(maze.isWall(player.scan(1, x)) || player.scan(1, x).getX() < 0 || player.scan(1, x).getY() < 0)
				leftWalls.add(new Polygon(new int[]{x*70, 70+x*70, 70+x*70, x*70}, 
										 new int[]{x*70, 70+x*70, screenY-70-x*70, screenY-x*70}, 4));
			if(maze.isWall(player.scan(2, x)) || player.scan(2, x).getX() < 0 || player.scan(2, x).getY() < 0)
				rightWalls.add(new Polygon(new int[]{screenX-70-x*70, screenX-x*70, screenX-x*70, screenX-70-x*70},
										 new int[]{70+x*70, x*70, screenY-x*70, screenY-70-x*70}, 4));
		}
		//checking walls in front
		ffWall = -1;
		for(int x=fov-1; x>=0; x--)
		{
			if(maze.isWall(player.scan(0, x)) || player.scan(0, x).getX() < 0 || player.scan(0, x).getY() < 0)
			{
				frontWalls.add(new Polygon(new int[]{70+x*70, screenX-70-x*70, screenX-70-x*70, 70+x*70}, 
											new int[]{70+x*70, 70+x*70, screenY-70-x*70, screenY-70-x*70}, 4));
				ffWall = x;
			}
			else
				frontWalls.add(null);
		}
	}

	public void keyPressed(KeyEvent e)
	{
		//System.out.println(e.getKeyCode());
		switch(e.getKeyCode())
		{
			case 80: //p:pause
			{
				if(gameOn)
				{
					pausedElapsedTime = System.currentTimeMillis() - initialTime;
					lvlpsdelapsedtime = System.currentTimeMillis() - lvlinttime;
				}
				else
				{
					initialTime = System.currentTimeMillis() - pausedElapsedTime;
					lvlinttime = System.currentTimeMillis() - lvlpsdelapsedtime;
				}
				gameOn = !gameOn; 
				paused = !paused;
			}
				break;
			case 73: menu = !menu; //i to bring up menu
				break;
			case 38: //up arrow
			case 87: movingF = true; //w
				break;
			//case 40: //back arrow
			//case 83: movingB = true;//s
				//break;
			case 37: //left arrow
			case 65: 
				if(gameOn)
					player.turn(1); //a
				break;
			case 39: //right arrow
			case 68: 
				if(gameOn)
					player.turn(0); //d
				break;
			case 77: //m
			case 32: mapOn = true; //space to pull up map
				break;
			case 66: wallDestroy = true; //b to break wall
				break;
		}
	}
	public void keyReleased(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case 38: 
			case 87: movingF = false;
				break;
			//case 40: 
			//case 83: movingB = false;
				//break;
			case 77: 
			case 32: mapOn = false;
				break;
		}
	}
	public void keyTyped(KeyEvent e)
	{
	}
	public void mouseClicked(MouseEvent e)
	{
	}
	public void mousePressed(MouseEvent e)
	{		
	}
	public void mouseReleased(MouseEvent e)
	{
	}
	public void mouseEntered(MouseEvent e)
	{
	}
	public void mouseExited(MouseEvent e)
	{
	}
	public static void main(String args[])
	{
		MazeProgram app=new MazeProgram();
	}
}