//Allison Li
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.Ellipse2D;
public class PodRacerCode extends JPanel implements KeyListener,Runnable
{
	private int xpos;
	private int ypos;
	private JFrame frame;
	private Thread t;
	private boolean gameOn;
	private Font f;
	private Color color;
	int changeX=0;
	int width=50;
	int[][] canyon=new int[80][50];
	int level=1;
	int steps=0;
	int points=0;
	int toNext=50;
	int mid=0;
	int counter=0;
	boolean timerStart=false;
	int timer=3;
	boolean gameEnd=false;

	//powerups
	boolean powerUp=false;
	int powNum=0;
	int powTime=100;
	int ptMod=1;
	int widthMod=0;
	int spdMod=0;

	public PodRacerCode()
	{
		frame=new JFrame();
		gameOn=false;
		f=new Font("TIMES NEW ROMAN",Font.PLAIN,50);
		frame.addKeyListener(this);
		frame.add(this);
		frame.setSize(800,539);
		wallSetup();
		xpos=mid;
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		t=new Thread(this);
		t.start();
		ypos=45;
	}
	public void wallSetup(){
		int lftwall=(int)(Math.random()*16)+10;
		canyon[lftwall][0]=1;
		canyon[lftwall+50][0]=1;
		for(int y=1;y<50;y++){
			int temp=(int)(Math.random()*3)-1;
			lftwall+=temp;
			if(lftwall<29 && lftwall>5){
				canyon[lftwall][y]=1;
				canyon[lftwall+50][y]=1;
				if(y%10==0)
					canyon[lftwall+(int)(Math.random()*48)+1][y]=1;
				if(y==45)
					mid=lftwall+25;
			}
			else
				y--;
		}
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setPaint(Color.RED);
		g2d.fillRect(0,0,800,500);
		g2d.setColor(Color.BLACK);
		if(!gameEnd){
			for(int y=0;y<50;y++){
				int temp=0;
				for(int x=0;x<80;x++)
					if(canyon[x][y]==1)
						temp++;
				int wall=0;
				for(int x=0;x<80;x++){
					g2d.setColor(Color.BLACK);
					if(canyon[x][y]==1){
						if(wall==0){
							g.fillRect(0,y*10,x*10,10);
							wall++;
						}
						else if(wall==1){
							if(temp==2)
								g.fillRect(x*10,y*10,(79-x)*10,10);
							else if(temp==3){
								g.fillRect(x*10,y*10,10,10);
								wall++;
							}
						}
						else
							g.fillRect(x*10,y*10,(79-x)*10,10);
					}
					else if(canyon[x][y]==2){
						g2d.setColor(Color.BLUE);
						g2d.fillOval(x*10,y*10,15,15);
					}
					else if(canyon[x][y]==3){
						g2d.setColor(Color.lightGray);
						g2d.fillOval(x*10,y*10,15,15);
					}
					else if(canyon[x][y]==4){
						g2d.setColor(Color.YELLOW);
						g2d.fillOval(x*10,y*10,15,15);
					}
					else if(canyon[x][y]==5){
						g2d.setColor(Color.GREEN);
						g2d.fillOval(x*10,y*10,15,15);
					}
					else if(canyon[x][y]==6){
						Color c=new Color(143,149,0);
						g2d.setColor(c);
						g2d.fillOval(x*10,y*10,15,15);
					}
				}
			}
				g2d.setColor(Color.WHITE);
				g2d.drawRect(20,20,230,190);
				g2d.setFont(new Font("Impact",Font.PLAIN,25));
				g2d.drawString("Points:  "+points,30,60);
				g2d.drawString("Level:  "+level,30,105);
				g2d.drawString("Steps until",30,155);
				g2d.drawString("next level:  "+toNext,30,185);
				g2d.setColor(Color.CYAN);
				g2d.fillOval(xpos*10,ypos*10,15,15);
			if(!gameEnd && !gameOn){
				if(!timerStart){
					g2d.setFont(new Font("Impact",Font.BOLD,75));
					g2d.setColor(Color.GRAY);
					g2d.fillRect(60,205,675,100);
					g2d.setColor(Color.WHITE);
					g2d.drawString("Press Enter to Start",65,280);
				}
				if(timerStart){
					g2d.setFont(new Font("Impact",Font.BOLD,150));
					g2d.setColor(Color.WHITE);
					g2d.drawString(""+timer,350,300);
				}
			}
			if(powerUp){
				g2d.setFont(new Font("Impact",Font.ITALIC,30));
				g2d.setColor(Color.MAGENTA);
				g2d.drawRect(5,5,775,490);
				switch(powNum){
					case 1: g2d.drawString("Points x2",630,50);
						break;
					case 2: g2d.drawString("Width + 5",630,50);
						break;
					case 3: g2d.drawString("Width - 1",630,50);
						break;
					case 4: g2d.drawString("Super slo-mo",575,50);
						break;
					case 5: g2d.drawString("Slo-mo",660,50);
				}
			}
		}
		if(gameEnd){
			try
			{
				t.sleep(1000);
			}catch(InterruptedException e)
			{
			}
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0,0,800,600);
			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font("Impact",Font.BOLD,150));
			if(level==45){
				g2d.drawString("YOU WON",25,175);
			}
			g2d.drawString("GAME OVER",25,175);
			g2d.setColor(Color.YELLOW);
			g2d.setFont(new Font("Impact",Font.PLAIN,60));
			g2d.drawString("FINAL RESULTS",220,320);
			g2d.drawString("Points:",50,430);
			g2d.drawString("Level:",520,430);
			g2d.setColor(Color.CYAN);
			g2d.drawString(""+points,230,430);
			g2d.drawString(""+level,670,430);
		}
	}
	public void run()
	{
		while(true)
		{
			repaint();
			if(timerStart && !gameEnd && !gameOn){
				repaint();
				for(int x=0;x<2;x++){
					try
					{
						t.sleep(1000);
					}catch(InterruptedException e)
					{
					}
					timer--;
					repaint();
				}
				try
				{
					t.sleep(1000);
				}catch(InterruptedException e)
				{
				}
				gameOn=true;
			}
			if(gameOn)
			{
				xpos+=changeX;
				counter++;
				int lftwall=0;
				int rtwall=79;
				while(canyon[lftwall][ypos]==0)
					lftwall++;
				while(canyon[rtwall][ypos]==0)
					rtwall--;
				if(xpos<=lftwall || xpos>=rtwall){
					gameEnd=true;
					gameOn=false;
				}
				switch(canyon[xpos][ypos]){
					case 1: gameEnd=true;
							gameOn=false;
						break;
					case 2: ptMod=2;
							powNum=1;
							powerUp=true;
						break;
					case 3: widthMod=5;
							width+=widthMod;
							powNum=2;
							powerUp=true;
						break;
					case 4: widthMod=-1;
							width+=widthMod;
							powNum=3;
							powerUp=true;
						break;
					case 5: spdMod=10;
							powNum=4;
							powerUp=true;
						break;
					case 6: spdMod=5;
							powNum=5;
							powerUp=true;
				}
				steps++;
				if(powerUp){
					if(powTime>0)
						powTime--;
					else{
						powTime=100;
						ptMod=1;
						width-=widthMod;
						widthMod=0;
						spdMod=0;
						powerUp=false;
					}
				}
				points+=level*ptMod;
				toNext--;
				moveWalls();
				newWall();
				if(toNext==0){
					level++;
					width--;
					toNext=50;
				}
				if(level==45){
					gameEnd=true;
					gameOn=false;
				}
				try
				{
					t.sleep(30+spdMod);
				}catch(InterruptedException e)
				{
				}
				repaint();
			}
		}
	}
	public void moveWalls(){
		for(int y=49;y>0;y--){
			for(int x=0;x<80;x++)
				canyon[x][y]=canyon[x][y-1];
		}
	}
	public void newWall(){
		int lftwall=0;
		while(canyon[lftwall][0]!=1)
			lftwall++;
		int rtwall=lftwall;
		boolean tower=false;
		int walls=0;
		for(int x=0;x<80;x++)
			if(canyon[x][0]==1)
				walls++;
		do{
			rtwall++;
			if(!tower && walls==3 && canyon[rtwall][0]==1){
				tower=true;
				canyon[rtwall][0]=0;
			}
		}while(canyon[rtwall][0]!=1);
		for(int x=0;x<80;x++){
			switch(canyon[x][0]){
				case 2:
				case 3:
				case 4:
				case 5:
				case 6: canyon[x][0]=0;
			}
		}
		int temp=(int)(Math.random()*3)-1;
		if(lftwall+temp<80-width & lftwall+temp>5){
			canyon[lftwall][0]=0;
			canyon[lftwall+temp][0]=1;
			canyon[rtwall][0]=0;
			canyon[lftwall+temp+width][0]=1;
		}
		if(counter%10==0)
			canyon[lftwall+temp+((int)(Math.random()*(width-6))+3)][0]=1;
		if(counter%50==0)
			canyon[lftwall+temp+((int)(Math.random()*(width-6))+3)][0]=(int)(Math.random()*5)+2;
	}
	public void keyPressed(KeyEvent ke)
	{
		if(ke.getKeyCode()==39)
			changeX=1;
		else if(ke.getKeyCode()==37)
			changeX=-1;
		else if(ke.getKeyCode()==10)
			timerStart=true;
		else if(ke.getKeyCode()==32){
			ypos=45;
			changeX=0;
			width=50;
			canyon=new int[80][50];
			level=1;
			steps=0;
			points=0;
			toNext=50;
			mid=0;
			counter=0;
			timerStart=false;
			timer=3;
			gameEnd=false;
			gameOn=false;
			//powerups
			powerUp=false;
			powNum=0;
			powTime=100;
			ptMod=1;
			widthMod=0;
			spdMod=0;

			wallSetup();
			xpos=mid;
		}
	}
	public void keyReleased(KeyEvent ke)
	{
		if(ke.getKeyCode()==39)
			changeX=0;
		else if(ke.getKeyCode()==37)
			changeX=0;
	}
	public void keyTyped(KeyEvent ke)
	{
	}
	public static void main(String args[])
	{
		PodRacerCode app=new PodRacerCode();
	}
}