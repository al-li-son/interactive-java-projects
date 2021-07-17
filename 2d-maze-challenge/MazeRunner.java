import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class MazeRunner extends JPanel implements KeyListener , Runnable
{
	//setup
	JFrame frame;
	Thread thread;
	boolean gameOn;
	int scale=5;
	boolean countdown=false;
	int count=1;
	int counter=-50;
	boolean paused;
	boolean startMenu;
	boolean lost;
	boolean gameOver;
	Clip damage;
	Clip gotKey;
	Clip oneUpSound;
	Clip gameWon;
	Clip gameLost;
	Clip MiiTheme;

	//maze stuff
	ArrayList<Wall> walls;
	StartWall start;
	EndWall end;
	int spawnCol;
	int spawnRow;
	int keyCol;
	int keyRow;
	int totCols;
	int totRows;
	Item key;
	Item oneUp;
	BufferedImage heart;
	//ArrayList<Lava> lava;
	LavaFlow lava;

	//peeps
	Hero blub;
	ArrayList<Monster> mobs;

	//movement
	boolean up;
	boolean down;
	boolean right;
	boolean left;

	public MazeRunner()
	{
		walls=new ArrayList<Wall>();
		createMaze();
		blub=new Hero(start.getCol(),start.getRow(),3*scale,3*scale,10*scale);
		mobs=new ArrayList<Monster>();
		lava=new LavaFlow(start.getCol(),start.getRow());
		for(int x=0;x<5;x++)
			mobs.add(new Monster(spawnCol+(int)(Math.random()*6)-3,spawnRow+(int)(Math.random()*6)-3,3*scale,3*scale,10*scale));
		try
		{
			heart=ImageIO.read(new File("Heart.png"));
		}
		catch(IOException io){System.err.println("File does not exist");}
		try
		{
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("damage.wav"));
			damage = AudioSystem.getClip();
			damage.open(audioStream);
		}
		catch(LineUnavailableException lue){}
		catch(UnsupportedAudioFileException uafe){}
		catch(IOException ioe){System.out.println("File does not exist");}
		try
		{
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("gotKey.wav"));
			gotKey = AudioSystem.getClip();
			gotKey.open(audioStream);
		}
		catch(LineUnavailableException lue){}
		catch(UnsupportedAudioFileException uafe){}
		catch(IOException ioe){System.out.println("File does not exist");}
		try
		{
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("oneUpSound.wav"));
			oneUpSound = AudioSystem.getClip();
			oneUpSound.open(audioStream);
		}
		catch(LineUnavailableException lue){}
		catch(UnsupportedAudioFileException uafe){}
		catch(IOException ioe){System.out.println("File does not exist");}
		try
		{
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("gameWon.wav"));
			gameWon = AudioSystem.getClip();
			gameWon.open(audioStream);
		}
		catch(LineUnavailableException lue){}
		catch(UnsupportedAudioFileException uafe){}
		catch(IOException ioe){System.out.println("File does not exist");}
		try
		{
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("gameLost.wav"));
			gameLost = AudioSystem.getClip();
			gameLost.open(audioStream);
		}
		catch(LineUnavailableException lue){}
		catch(UnsupportedAudioFileException uafe){}
		catch(IOException ioe){System.out.println("File does not exist");}
		try
		{
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("MiiTheme.wav"));
			MiiTheme = AudioSystem.getClip();
			MiiTheme.open(audioStream);
		}
		catch(LineUnavailableException lue){}
		catch(UnsupportedAudioFileException uafe){}
		catch(IOException ioe){System.out.println("File does not exist");}

		frame=new JFrame("Atari Blub");
		frame.add(this);
		frame.setSize(250*scale,118*scale);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.addKeyListener(this);
		thread=new Thread(this);
		paused=false;
		gameOn=false;
		startMenu=true;
		lost=false;
		gameOver=false;
		thread.start();
		MiiTheme.loop(Clip.LOOP_CONTINUOUSLY);
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setColor(Color.BLACK);
		g2.fillRect(0,0,250*scale,115*scale);

		for(Wall w: walls)
		{
			g2.setColor(Color.lightGray);
			g2.fill(w.getRectangle());
		}

		for(int x=0; x<lava.getLavaFlow().size(); x++)
		{
			Lava l=lava.getLavaFlow().get(x);
			g2.drawImage(l.getImage(),l.getCol()*scale*3+scale*10,l.getRow()*scale*3+scale*10,3*scale,(int)(0.6*l.getLevel()*scale),null);
		}

		g2.setColor(new Color(160,82,45));
		g2.fill(start.getRectangle());
		g2.fill(end.getRectangle());
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(5));
		g2.draw(blub.getEllipse());
		g2.drawImage(blub.getImage(),blub.getCol()*scale*3+scale*10,blub.getRow()*scale*3+scale*10,(int)(3.7*scale),(int)(3.7*scale),null);
		g2.setColor(Color.GREEN);
		for(Monster m:mobs)
			g2.drawImage(m.getImage(),m.getCol()*scale*3+scale*10,m.getRow()*scale*3+scale*10,3*scale,3*scale,null);

		if(!key.pickedUp())
			g2.drawImage(key.getImage(),key.getCol()*scale*3+scale*10,key.getRow()*scale*3+scale*10,5*scale,3*scale,null);
		if(!oneUp.pickedUp())
			g2.drawImage(oneUp.getImage(),oneUp.getCol()*scale*3+scale*10,oneUp.getRow()*scale*3+scale*10,4*scale,4*scale,null);

		for(int x=0;x<blub.getLives();x++)
			g2.drawImage(heart,x*8*scale+2*scale,(int)(.8*scale),8*scale,8*scale,null);

		g2.setColor(Color.GREEN);
		for(int x=0;x<blub.getHealth();x++)
			g2.fillRect(180*scale+x*10*scale,3*scale,10*scale,4*scale);
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(10));
		g2.drawRoundRect(180*scale,3*scale,50*scale,4*scale,2*scale,2*scale);
		g2.setFont(new Font("Agency FB",Font.BOLD,7*scale));
		g2.drawString("HEALTH:",159*scale,7*scale);

		if(countdown)
		{
			g2.setFont(new Font("Agency FB",Font.BOLD,20*scale));
			g2.drawString(""+count,120*scale,62*scale);
		}

		if(startMenu)
		{
			g2.setColor(Color.WHITE);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.9f));
			g2.fillRect(0,0,250*scale,115*scale);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
			g2.setFont(new Font("Agency FB",Font.BOLD,25*scale));
			g2.setColor(Color.BLUE);
			g2.drawString("ATARI BLUB",70*scale,25*scale);
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("Berlin Sans FB",Font.BOLD,8*scale));
			g2.drawString("Grab the key and make it",8*scale,41*scale);
			g2.drawString("to the end of the maze!",8*scale,49*scale);
			g2.drawString("But don't touch the lava,",8*scale,57*scale);
			g2.drawString("and the ghosts do NOT",8*scale,65*scale);
			g2.drawString("want a high five",8*scale,73*scale);
			g2.setFont(new Font("Berlin Sans FB",Font.BOLD,10*scale));
			g2.drawString("CONTROLS",135*scale,43*scale);
			g2.setFont(new Font("Berlin Sans FB",Font.BOLD,8*scale));
			g2.drawString("Movement: Arrow Keys",135*scale,53*scale);
			g2.drawString("Pause: p",135*scale,63*scale);
			g2.drawString("Menu: m",135*scale,73*scale);
			g2.setColor(Color.darkGray);
			g2.fillRoundRect(68*scale,83*scale,100*scale,24*scale,5*scale,5*scale);
			g2.setFont(new Font("Agency FB",Font.BOLD,16*scale));
			g2.setColor(Color.GREEN);
			g2.drawString("Press \"m\" to exit",71*scale,100*scale);
		}

		if(paused)
		{
			g2.setColor(Color.WHITE);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.8f));
			g2.fillRect(0,0,250*scale,115*scale);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
			g2.setFont(new Font("Agency FB",Font.BOLD,50*scale));
			g2.setColor(Color.BLACK);
			g2.drawString("PAUSED",55*scale,67*scale);
			g2.setFont(new Font("Agency FB",Font.BOLD,11*scale));
			g2.setColor(Color.darkGray);
			g2.drawString("Press 'p' to continue or 'r' to restart",52*scale,78*scale);
		}

		if(gameOver)
		{
			g2.setColor(Color.WHITE);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.8f));
			g2.fillRect(0,0,250*scale,115*scale);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
			g2.setFont(new Font("Agency FB",Font.BOLD,20*scale));
			g2.setColor(Color.BLACK);
			g2.drawString("Press 'r' to restart",58*scale,75*scale);
			g2.setFont(new Font("Agency FB",Font.BOLD,40*scale));
			if(lost)
			{
				g2.drawString("GAME OVER",46*scale,52*scale);
			}
			else
			{
				g2.drawString("YOU WON!",54*scale,52*scale);
			}
		}
	}
	public void run()
	{
		while(true)
		{
			if(gameOn)
			{
				if(up)
					blub.move(38,walls);
				if(down)
					blub.move(40,walls);
				if(left)
					blub.move(37,walls);
				if(right)
					blub.move(39,walls);
				boolean died=false;
				for(Monster m:mobs){
					m.move(blub,walls);
					if(m.getCol()==blub.getCol() && m.getRow()==blub.getRow())
					{
						blub.damage();
						damage.start();
						damage.setFramePosition(0);
					}
				}
				if(blub.touchingLava(blub.getCol(),blub.getRow(),lava.getLavaFlow()) && blub.getLives()>0)
				{
					blub.damage();
					damage.start();
					damage.setFramePosition(0);
				}
				if(blub.hasDied() && blub.getLives()>0)
				{
					countdown=true;
					for(int x=3;x>=1;x--)
					{
						count=x;

						try
						{
							thread.sleep(1000);
						}
						catch(InterruptedException e)
						{
						}
						repaint();
					}
					countdown=false;
					blub.respawn(walls,lava.getLavaFlow(),totCols,totRows);
					blub.setDeath(false);
					key.interact(false);
					end.lock();
				}
				else if(blub.getLives()<=0)
				{
					gameOn=false;
					lost=true;
					gameOver=true;
					gameLost.start();
					gameLost.setFramePosition(0);
				}
				if(!key.pickedUp() && blub.getCol()==key.getCol() && blub.getRow()==key.getRow())
				{
					key.interact(true);
					end.unlock();
					gotKey.start();
					gotKey.setFramePosition(0);
				}
				if(!oneUp.pickedUp() && blub.getCol()==oneUp.getCol() && blub.getRow()==oneUp.getRow())
				{
					oneUp.interact(true);
					blub.regenerate();
					oneUpSound.start();
					oneUpSound.setFramePosition(0);
				}
				if(blub.getCol()==end.getCol() && blub.getRow()==end.getRow() && end.unlocked())
				{
					gameOn=false;
					gameOver=true;
					gameWon.start();
					gameWon.setFramePosition(0);
				}

				if(counter>0 && counter%12==0)
				{
					lava.move(walls);
					counter=0;
				}
				counter++;
			}
			try
			{
				thread.sleep(70);
			}
			catch(InterruptedException e)
			{
			}
			repaint();

		}
	}
	public void keyPressed(KeyEvent e)
	{
		//System.out.println(e.getKeyCode());
		switch(e.getKeyCode())
		{
			//up
			case 38: up=true;
					 blub.setImage("up");
				break;
			//down
			case 40: down=true;
					 blub.setImage("down");
				break;
			//left
			case 37: left=true;
					 blub.setImage("left");
				break;
			//right
			case 39: right=true;
					 blub.setImage("right");
				break;
			//p
			case 80: gameOn=!gameOn;
					 paused=!paused;
				break;
			//m
			case 77: startMenu=!startMenu;
					 gameOn=!gameOn;
				break;
			//r
			case 82: if(paused || gameOver)
						reset();
				break;
		}
	}
	public void keyReleased(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			//up
			case 38: up=false;
					 blub.setImage("stand");
				break;
			//down
			case 40: down=false;;
					 blub.setImage("stand");
				break;
			//left
			case 37: left=false;
					 blub.setImage("stand left");
				break;
			//right
			case 39: right=false;
					 blub.setImage("stand");
				break;
		}
	}
	public void createMaze()
	{
		File name = new File("Arena.txt");
		try
		{
			BufferedReader input = new BufferedReader(new FileReader(name));
			String text;
			int row=0;
			while((text=input.readLine())!=null)
			{
				totCols=text.length();
				for(int col=0;col<text.length();col++){
					if(text.charAt(col)=='#')
						walls.add(new Wall(col,row,3*scale,3*scale,10*scale));
					if(text.charAt(col)=='s')
					{
						start = new StartWall(col,row,3*scale,3*scale,10*scale);
						walls.add(start);
					}
					if(text.charAt(col)=='e')
					{
						end = new EndWall(col,row,3*scale,3*scale,10*scale);
						walls.add(end);
					}
					if(text.charAt(col)=='*')
					{
						spawnCol=col;
						spawnRow=row;
					}
					if(text.charAt(col)=='k')
					{
						key=new Item(col,row,"Key");
					}
					if(text.charAt(col)=='h')
					{
						oneUp=new Item(col,row,"1UP");
					}
				}
				row++;
			}
			totRows=row;

		}
		catch(IOException io)
		{
			System.err.println("File does not exist");
		}
	}
	public void reset()
	{
		walls=new ArrayList<Wall>();
		createMaze();
		blub=new Hero(start.getCol(),start.getRow(),3*scale,3*scale,10*scale);
		mobs=new ArrayList<Monster>();
		lava=new LavaFlow(start.getCol(),start.getRow());
		for(int x=0;x<5;x++)
			mobs.add(new Monster(spawnCol+(int)(Math.random()*6)-3,spawnRow+(int)(Math.random()*6)-3,3*scale,3*scale,10*scale));
		counter=-100;
		paused=false;
		gameOn=false;
		startMenu=true;
		gameOver=false;
		lost=false;
	}
	public void keyTyped(KeyEvent e)
	{
	}
	public static void main(String[] args)
	{
		MazeRunner app=new MazeRunner();
	}
}