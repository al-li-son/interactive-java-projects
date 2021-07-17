//Sanjana Kasarla, Allison Li, Annie Malik
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Scanner;
public class ColorMatch extends JPanel implements KeyListener
{
	JFrame frame;
	String[][] Grid;
	String[][] p1Grid;
	String[][] p2Grid;
	int level=1;
	int x1;
	int x2;
	int y1;
	int y2;
	boolean won1=false;
	boolean won2=false;
	int p1Score=0;
	int p2Score=0;
	boolean end=false;
	boolean menu=true;
	public ColorMatch()
	{
		setGrid();
		frame=new JFrame();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(3000,1000);
		frame.setVisible(true);
		frame.addKeyListener(this);
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0,0,3000,1000);
		int size=0;

		if(!end){
			g.setFont(new Font("Impact",Font.PLAIN,100));
			g.setColor(Color.CYAN);
			g.drawString("PLAYER 1",180,300);
			g.drawString("PLAYER 2",1070,300);
			switch(level){
				case 1: size=100;
					break;
				case 2: size=75;
					break;
				case 3: size=60;
					break;
				case 4: size=50;
			}
			for(int y=0;y<Grid.length;y++){
				for(int x=0;x<Grid.length;x++){
					g.setColor(color(Grid[x][y]));
					g.fillRect(x*size+650,y*size+20,size-10,size-10);
				}
			}
			for(int y=0;y<Grid.length;y++){
				for(int x=0;x<Grid.length;x++){
					g.setColor(color(p1Grid[x][y]));
					g.fillRect(x*size+200,y*size+420,size-10,size-10);
				}
			}
			for(int y=0;y<Grid.length;y++){
				for(int x=0;x<Grid.length;x++){
					g.setColor(color(p2Grid[x][y]));
					g.fillRect(x*size+1100,y*size+420,size-10,size-10);
				}
			}
		}
		if(won1){
			g.setFont(new Font("Impact",Font.BOLD,200));
			g.setColor(Color.MAGENTA);
			if(!end){
				g.drawString("Player 1 Wins!",175,450);
				g.setFont(new Font("Berlin Sans FB",Font.PLAIN,50));
				g.setColor(Color.WHITE);
				g.drawString("Press any key to continue",530,530);
				level++;
				won1=false;
				setGrid();
			}
		}
		else if(won2){
			g.setFont(new Font("Impact",Font.BOLD,200));
			g.setColor(Color.MAGENTA);
			if(!end){
				g.drawString("Player 2 Wins!",175,450);
				g.setFont(new Font("Berlin Sans FB",Font.PLAIN,50));
				g.setColor(Color.WHITE);
				g.drawString("Press any key to continue",530,530);
				level++;
				won2=false;
				setGrid();
			}
		}
		if(end){
			g.setColor(Color.CYAN);
			g.setFont(new Font("Impact",Font.PLAIN,150));
			if(p1Score==p2Score)
				g.drawString("It's a tie!",530,450);
			else if(p1Score>p2Score){
				g.drawString("Player 1 wins the game!",80,200);
				g.setColor(Color.WHITE);
				g.drawString(""+p1Score+" to "+p2Score,600,400);
			}
			else if(p2Score>p1Score){
				g.drawString("Player 2 wins the game!",80,200);
				g.setColor(Color.WHITE);
				g.drawString(""+p2Score+" to "+p1Score,600,400);
			}
		}
		if(menu){
			g.setColor(Color.WHITE);
			g.fillRect(200,200,1200,450);
			g.setFont(new Font("Berlin Sans FB",Font.PLAIN,50));
			g.setColor(Color.BLACK);
			g.drawString("Instructions:",230,270);
			g.drawString("Match your board to the board on top.",230,340);
			g.drawString("Player 1 uses WASD and Player 2 uses the arrow keys",230,400);
			g.drawString("to move the empty space block.",230,460);
			g.drawString("Press \"i\" to open/close these instructions at any time.",230,520);
			g.drawString("Have fun! May the best matcher win.",230,580);
		}
	}
	public Color color(String a)
	{
		switch(a){
			case "r": return Color.red;
			case "o": return new Color(255,128,0);
			case "g": return Color.green;
			case "b": return Color.blue;
			case "w": return Color.white;
			case "y": return Color.yellow;
			default: return Color.black;
		}
	}
	public void setGrid()
	{
		String[] colors={"r","o","g","b","w","y"};
		switch(level){
			case 1: Grid=new String[3][3];
				break;
			case 2: Grid=new String[4][4];
				break;
			case 3: Grid=new String[5][5];
				break;
			case 4: Grid=new String[6][6];
		}
		for(int y=0;y<Grid.length;y++){
			for(int x=0;x<Grid.length;x++)
				Grid[x][y]=colors[random(6,0)];
		}
		x1=random(Grid.length,0);
		x2=x1;
		y1=random(Grid.length,0);
		y2=y1;
		Grid[x1][y2]=" ";
		switch(level){
			case 1: p1Grid=new String[3][3];
					p2Grid=new String[3][3];
				break;
			case 2: p1Grid=new String[4][4];
					p2Grid=new String[4][4];
				break;
			case 3: p1Grid=new String[5][5];
					p2Grid=new String[5][5];
				break;
			case 4: p1Grid=new String[6][6];
					p2Grid=new String[6][6];
		}
		for(int y=0;y<Grid.length;y++){
			for(int x=0;x<Grid.length;x++){
				p1Grid[x][y]=Grid[x][y];
				p2Grid[x][y]=Grid[x][y];
			}
		}
		for(int x=0;x<50;x++){
			int temp1=(int)(Math.random()*4)+1;
			switch(temp1){
				case 1: if(x1>0){
							String temp=p1Grid[x1][y1];
							p1Grid[x1][y1]=p1Grid[x1-1][y1];
							p1Grid[x1-1][y1]=temp;
							x1--;
						}
					break;
				case 2: if(x1<Grid.length-1){
							String temp=p1Grid[x1][y1];
							p1Grid[x1][y1]=p1Grid[x1+1][y1];
							p1Grid[x1+1][y1]=temp;
							x1++;
						}
					break;
				case 3: if(y1>0){
							String temp=p1Grid[x1][y1];
							p1Grid[x1][y1]=p1Grid[x1][y1-1];
							p1Grid[x1][y1-1]=temp;
							y1--;
						}
					break;
				case 4: if(y1<Grid.length-1){
							String temp=p1Grid[x1][y1];
							p1Grid[x1][y1]=p1Grid[x1][y1+1];
							p1Grid[x1][y1+1]=temp;
							y1++;
						}
			}
			int temp2=(int)(Math.random()*4)+1;
			switch(temp2){
				case 1: if(x2>0){
							String temp=p2Grid[x2][y2];
							p2Grid[x2][y2]=p2Grid[x2-1][y2];
							p2Grid[x2-1][y2]=temp;
							x2--;
						}
					break;
				case 2: if(x2<Grid.length-1){
							String temp=p2Grid[x2][y2];
							p2Grid[x2][y2]=p2Grid[x2+1][y2];
							p2Grid[x2+1][y2]=temp;
							x2++;
						}
					break;
				case 3: if(y2>0){
							String temp=p2Grid[x2][y2];
							p2Grid[x2][y2]=p2Grid[x2][y2-1];
							p2Grid[x2][y2-1]=temp;
							y2--;
						}
					break;
				case 4: if(y2<Grid.length-1){
							String temp=p2Grid[x2][y2];
							p2Grid[x2][y2]=p2Grid[x2][y2+1];
							p2Grid[x2][y2+1]=temp;
							y2++;
						}
			}
		}
	}
	public int random(int a, int b)
	{
		return (int)(Math.random()*a)+b;
	}
	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode()){
			case 65: if(x1>0){
						String temp=p1Grid[x1][y1];
						p1Grid[x1][y1]=p1Grid[x1-1][y1];
						p1Grid[x1-1][y1]=temp;
						x1--;
					}
				break;
			case 68: if(x1<Grid.length-1){
						String temp=p1Grid[x1][y1];
						p1Grid[x1][y1]=p1Grid[x1+1][y1];
						p1Grid[x1+1][y1]=temp;
						x1++;
					}
				break;
			case 87: if(y1>0){
						String temp=p1Grid[x1][y1];
						p1Grid[x1][y1]=p1Grid[x1][y1-1];
						p1Grid[x1][y1-1]=temp;
						y1--;
					}
				break;
			case 83: if(y1<Grid.length-1){
						String temp=p1Grid[x1][y1];
						p1Grid[x1][y1]=p1Grid[x1][y1+1];
						p1Grid[x1][y1+1]=temp;
						y1++;
					}
				break;
			case 73: menu=!menu;
		}
		switch(e.getKeyCode()){
			case 37: if(x2>0){
						String temp=p2Grid[x2][y2];
						p2Grid[x2][y2]=p2Grid[x2-1][y2];
						p2Grid[x2-1][y2]=temp;
						x2--;
					}
				break;
			case 39: if(x2<Grid.length-1){
						String temp=p2Grid[x2][y2];
						p2Grid[x2][y2]=p2Grid[x2+1][y2];
						p2Grid[x2+1][y2]=temp;
						x2++;
					}
				break;
			case 38: if(y2>0){
						String temp=p2Grid[x2][y2];
						p2Grid[x2][y2]=p2Grid[x2][y2-1];
						p2Grid[x2][y2-1]=temp;
						y2--;
					}
				break;
			case 40: if(y2<Grid.length-1){
						String temp=p2Grid[x2][y2];
						p2Grid[x2][y2]=p2Grid[x2][y2+1];
						p2Grid[x2][y2+1]=temp;
						y2++;
					}
		}
		won1=true;
		won2=true;
		for(int y=0;y<Grid.length;y++){
			for(int x=0;x<Grid.length;x++){
				if(p1Grid[x][y]!=Grid[x][y])
					won1=false;
				if(p2Grid[x][y]!=Grid[x][y])
					won2=false;
			}
		}
		if(won1){
			p1Score++;
		}
		else if(won2){
			p2Score++;
		}
		if(level==5)
			end=true;
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
		ColorMatch app=new ColorMatch();
	}
}