import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Scanner;
public class MazeProgram extends JPanel implements KeyListener
{
	JFrame frame;
	int xpos=1;
	int ypos=0;
	int moves=0;
	int num=1;
	boolean solved=false;
	boolean key=false;
	boolean instruc=true;
	boolean red=false;
	boolean blue=false;
	String[][] maze=new String[75][30];
	int[] boxes=new int[2];
	String mazeDesign;
	public MazeProgram()
	{
		setBoard();
		frame=new JFrame();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(3050,1400);
		frame.setVisible(true);
		frame.addKeyListener(this);
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0,0,3000,1400);


		for(int y=0;y<maze[0].length;y++)
		{
			for(int x=0;x<maze.length;x++)
			{
				if(maze[x][y].charAt(0)=='#'){
					g.setColor(Color.GRAY);
					g.fillRect(x*40,y*40,40,40);
				}
				else if(maze[x][y].charAt(0)=='r'){
					g.setColor(Color.RED);
					g.drawRect(x*40+4,y*40+4,32,32);
				}
				else if(maze[x][y].charAt(0)=='b'){
					g.setColor(Color.CYAN);
					g.drawRect(x*40+4,y*40+4,32,32);
				}
				else if(maze[x][y].charAt(0)=='|'){
					g.setColor(Color.RED);
					g.drawLine(x*40+20,y*40+12,x*40+20,y*40+28);
				}
				else if(maze[x][y].charAt(0)=='-'){
					g.setColor(Color.RED);
					g.drawLine(x*40+12,y*40+20,x*40+28,y*40+20);
				}
				else if(maze[x][y].charAt(0)==':'){
					g.setColor(Color.CYAN);
					g.drawLine(x*40+20,y*40+12,x*40+20,y*40+28);
				}
				else if(maze[x][y].charAt(0)=='~'){
					g.setColor(Color.CYAN);
					g.drawLine(x*40+12,y*40+20,x*40+28,y*40+20);
				}
			}
		}
		g.setColor(Color.MAGENTA);
		g.fillOval(xpos*40,ypos*40,40,40);

		g.setColor(Color.RED);
		g.setFont(new Font("Impact",Font.PLAIN,75));
		g.drawString("Moves: "+moves,0,1300);
		if(!key){
			g.setColor(Color.GREEN);
			switch(num){
				case 1: g.fillOval(720*4,200*4,40,40);
						g.fillRect(728*4,201*4,32,20);
						g.fillRect(736*4,201*4,8,32);
					break;
				case 2: g.fillOval(350*4,130*4,40,40);
						g.fillRect(358*4,131*4,32,20);
						g.fillRect(366*4,131*4,8,32);
					break;
				case 3: g.setColor(Color.ORANGE);
					 	g.fillRect(331*4,101*4,80,32);
					 	g.setColor(Color.darkGray);
					 	g.drawRect(332*4,102*4,17*4,16);
					 	g.fillRect(338*4,104*4,20,16);
					break;
			}
		}
		if(solved){
			if(num<=2 && key && boxes[0]==2 && boxes[1]==2){
				num++;
				setBoard();
				xpos=1;
				ypos=0;
				solved=false;
				key=false;
			}
			else if(num==3 && key && boxes[0]==2 && boxes[1]==2){
				g.setColor(Color.YELLOW);
				g.setFont(new Font("Magneto",Font.BOLD,380));
				g.drawString("YOU DID IT!",30,700);
			}
			else
				solved=false;
		}
		if(instruc){
			g.setColor(Color.WHITE);
			g.fillRect(370,200,2200,800);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Courier",Font.BOLD,55));
			g.drawString("Welcome to The Labyrinth! Make sure you grab the key in each maze",395,370);
			g.drawString("to move onto the next. Also, you must connect the colored boxes",395,440);
			g.drawString("to each other, each pair with one another. A colored trail will",395,510);
			g.drawString("follow you when you have picked up one of the boxes and will end",395,580);
			g.drawString("when you hit the next box. THESE LINES CANNOT CROSS! Don't trap",395,650);
			g.drawString("yourself! Good luck! You can open and close these instructions",395,720);
			g.drawString("any time you wish by pressing \"i\" on your keyboard.",395,790);
		}
	}
	public void setBoard()
	{
		Scanner input1=new Scanner(System.in);
		File name=new File("maze"+num+".txt");
		boxes[0]=0;
		boxes[1]=0;
		for(int y=0;y<maze[0].length;y++)
		{
			for(int x=0;x<maze.length;x++)
			{
				maze[x][y]="";
			}
		}
		try
		{
			BufferedReader input=new BufferedReader(new FileReader(name));
			StringBuffer buffer=new StringBuffer();
			String text;
			int y=0;
			while((text=input.readLine())!=null)
			{
				for(int x=0;x<text.length();x++)
				{
					maze[x][y]=""+text.charAt(x);
				}
				y++;
			}
		}
		catch(IOException io)
		{
			System.err.println("File error");
		}
	}
	public void keyPressed(KeyEvent e)
	{
		boolean blocked=false;
		switch(e.getKeyCode()){
			case 73: instruc=!instruc;
				break;
			case 38: if(num!=1 && xpos==1 && ypos==0 && boxes[0]==2 && boxes[1]==2){
					 	xpos=74;
					 	ypos=28;
					 	num--;
					 	key=true;
					 	setBoard();
					 }
					 else if(!key && num==1 && xpos==72 && ypos==21){
						key=true;
						System.out.print((char)7);
						ypos--;
					 }
					 else if(ypos>0 && maze[xpos][ypos-1].charAt(0)!='#'){
						if(red){
							maze[xpos][ypos]="|";
							if(maze[xpos][ypos-1].charAt(0)==':' || maze[xpos][ypos-1].charAt(0)=='~')
								blocked=true;
						}
						else if(blue){
							maze[xpos][ypos]=":";
							if(maze[xpos][ypos-1].charAt(0)=='|' || maze[xpos][ypos-1].charAt(0)=='-')
								blocked=true;
						}
						if(!blocked){
							ypos--;
							moves++;
						}
						if(maze[xpos][ypos].charAt(0)=='r'){
							red=!red;
							boxes[0]++;
							maze[xpos][ypos]="|";
						}
						else if(maze[xpos][ypos].charAt(0)=='b'){
							blue=!blue;
							boxes[1]++;
							maze[xpos][ypos]=":";
						}
					 }
				break;
			case 40: if(ypos<maze[0].length && maze[xpos][ypos+1].charAt(0)!='#'){
						if(red){
					 		maze[xpos][ypos]="|";
					 		if(maze[xpos][ypos+1].charAt(0)==':' || maze[xpos][ypos+1].charAt(0)=='~')
								blocked=true;
						}
					 	else if(blue){
							maze[xpos][ypos]=":";
							if(maze[xpos][ypos-1].charAt(0)=='|' || maze[xpos][ypos-1].charAt(0)=='-')
								blocked=true;
						}
						if(!blocked){
							ypos++;
							moves++;
						}
						if(maze[xpos][ypos].charAt(0)=='r'){
							red=!red;
							boxes[0]++;
							maze[xpos][ypos]="|";
						}
						else if(maze[xpos][ypos].charAt(0)=='b'){
							blue=!blue;
							boxes[1]++;
							maze[xpos][ypos]=":";
						}
					}
				break;
			case 37: if(!key && num==2 && xpos==36 && ypos==13){
						key=true;
						System.out.print((char)7);
						xpos--;
					 }
					 else if(!key && num==3 && xpos==34 && ypos==10){
						 key=true;
						 System.out.print((char)7);
						 xpos--;
					 }
					 else if(xpos>0 && maze[xpos-1][ypos].charAt(0)!='#'){
						if(red){
					 		maze[xpos][ypos]="-";
					 		if(maze[xpos-1][ypos].charAt(0)==':' || maze[xpos-1][ypos].charAt(0)=='~')
								blocked=true;
						}
					 	else if(blue){
					 		maze[xpos][ypos]="~";
					 		if(maze[xpos-1][ypos].charAt(0)=='|' || maze[xpos-1][ypos].charAt(0)=='-')
								blocked=true;
						}
						if(!blocked){
							xpos--;
							moves++;
						}
						if(maze[xpos][ypos].charAt(0)=='r'){
							red=!red;
							boxes[0]++;
							maze[xpos][ypos]="-";
						}
						else if(maze[xpos][ypos].charAt(0)=='b'){
							blue=!blue;
							boxes[1]++;
							maze[xpos][ypos]="~";
						}
					}
				break;
			case 39: if(xpos==74 && ypos==28)
						solved=true;
					 else if(xpos<maze.length && maze[xpos+1][ypos].charAt(0)!='#'){
						if(red){
					 		maze[xpos][ypos]="-";
					 		if(maze[xpos+1][ypos].charAt(0)==':' || maze[xpos+1][ypos].charAt(0)=='~')
								blocked=true;
						}
					 	else if(blue){
					 		maze[xpos][ypos]="~";
					 		if(maze[xpos+1][ypos].charAt(0)=='|' || maze[xpos+1][ypos].charAt(0)=='-')
								blocked=true;
						}
						if(!blocked){
							xpos++;
							moves++;
						}
						if(maze[xpos][ypos].charAt(0)=='r'){
							red=!red;
							boxes[0]++;
							maze[xpos][ypos]="-";
						}
						else if(maze[xpos][ypos].charAt(0)=='b'){
							blue=!blue;
							boxes[1]++;
							maze[xpos][ypos]="~";
						}
					}
				break;
		}
		repaint();
	}
	public void keyReleased(KeyEvent e)
	{
	}
	public void keyTyped(KeyEvent e)
	{
	}
	public static void main(String args[])
	{
		MazeProgram app=new MazeProgram();
	}
}