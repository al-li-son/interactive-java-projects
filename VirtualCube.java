import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Scanner;
public class VirtualCube extends JPanel implements KeyListener
{
	JFrame frame;
	int scale=5;
	int[][][] Cube = new int[6][3][3];
	String[] scramble = new String[20];
	String scram = "";
	int[][] face = new int[3][3];
	int[] side = new int[3];
	boolean menu = true;
	public VirtualCube()
	{
		solve();
		frame=new JFrame();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(155*scale,140*scale);
		frame.setVisible(true);
		frame.addKeyListener(this);
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0,0,190*scale,132*scale);
		for(int z=0;z<6;z++)
			for(int x=0;x<3;x++)
				for(int y=0;y<3;y++){
					int xmod=2*scale;
					int ymod=22*scale;
					switch(z){
						case 0: xmod+=34*scale; //U
							break;
						case 1: xmod+=34*scale; //D
								ymod+=68*scale;
							break;
						case 2: xmod+=34*scale; //F
								ymod+=34*scale;
							break;
						case 3: xmod+=102*scale; //B
								ymod+=34*scale;
							break;
						case 4: xmod+=68*scale; //R
								ymod+=34*scale;
							break;
						case 5: ymod+=34*scale; //L
					}
					g.setColor(color(Cube[z][x][y]));
					g.fillRect(x*11*scale+xmod,y*11*scale+ymod,10*scale,10*scale);
				}
		g.setFont(new Font("Impact",Font.PLAIN,7*scale));
		g.setColor(Color.MAGENTA);
		g.drawString(scram,2*scale,12*scale);

		if(menu){
			g.setColor(Color.darkGray);
			g.fillRect(5*scale,6*scale,140*scale,100*scale);
			g.setColor(Color.CYAN);
			g.setFont(new Font("Impact",Font.BOLD,10*scale));
			g.drawString("CONTROLS",8*scale,18*scale);
			g.setFont(new Font("Impact",Font.PLAIN,8*scale));
			g.setColor(Color.WHITE);
			g.drawString("1) Go to CSTimer.net",10*scale,30*scale);
			g.drawString("2) Click the CSTimer logo on the top left",10*scale,40*scale);
			g.drawString("3) Click on \"Keyboard Shortcut\"",10*scale,50*scale);
			g.drawString("4) Use the Virtual Cube Key Map",10*scale,60*scale);
			g.drawString("Hit SPACE to scramble the cube",10*scale,75*scale);
			g.drawString("Hit BACKSPACE to solve the cube",10*scale,85*scale);
			g.drawString("Hit  \\  to close or open this menu",10*scale,95*scale);
		}
	}
	public void scramble()
	{
		solve();
		scram="";
		String[][] moves={{"U","U'","U2"},{"D","D'","D2"},{"F","F'","F2"},{"B","B'","B2"},{"R","R'","R2"},{"L","L'","L2"}};
		int prevSide=100;
		for(int x=0;x<20;x++){
			int temp=random(6,0);
			while(temp==prevSide)
				temp=random(6,0);
			scramble[x]=moves[temp][random(3,0)];
			prevSide=temp;
		}
		for(int x=0;x<20;x++)
			scram+=scramble[x]+" ";
		for(int x=0;x<20;x++)
			switch(scramble[x]){
				case "U": U();
					break;
				case "U'": Up();
					break;
				case "U2": U(); U();
					break;
				case "D": D();
					break;
				case "D'": Dp();
					break;
				case "D2": D(); D();
					break;
				case "F": F();
					break;
				case "F'": Fp();
					break;
				case "F2": F(); F();
					break;
				case "B": B();
					break;
				case "B'": Bp();
					break;
				case "B2": B(); B();
					break;
				case "R": R();
					break;
				case "R'": Rp();
					break;
				case "R2": R(); R();
					break;
				case "L": L();
					break;
				case "L'": Lp();
					break;
				case "L2": L(); L();
			}
	}
	public void solve()
	{
		for(int z=0;z<6;z++)
			for(int x=0;x<3;x++)
				for(int y=0;y<3;y++)
					Cube[z][x][y]=z;
		repaint();
	}
	public Color color(int a)
	{
		switch(a){
			case 0: return Color.white;
			case 1: return Color.yellow;
			case 2: return Color.green;
			case 3: return Color.blue;
			case 4: return Color.red;
			case 5: return new Color(255,128,0);
			default: return Color.black;
		}
	}
	public int random(int a, int b)
	{
		return (int)(Math.random()*a)+b;
	}
	public void keyPressed(KeyEvent e)
	{
		//Cube Movements
		switch(e.getKeyCode()){
			case 92: menu=!menu;
				break;
			case 8: solve();
				break;
			case 32: scramble();
				break;
			case 89: case 84: X();
				break;
			case 78: case 66: Xp();
				break;
			case 59: Y();
				break;
			case 65: Yp();
				break;
			case 80: X(); Y(); Xp(); //Z
				break;
			case 81: X(); Yp(); Xp(); //Z'
				break;
			case 74: U();
				break;
			case 70: Up();
				break;
			case 83: D();
				break;
			case 76: Dp();
				break;
			case 72: F();
				break;
			case 71: Fp();
				break;
			case 87: B();
				break;
			case 79: Bp();
				break;
			case 73: R();
				break;
			case 75: Rp();
				break;
			case 68: L();
				break;
			case 69: Lp();
				break;
			case 44: D(); Y(); //u
				break;
			case 67: Dp(); Yp(); //u'
				break;
			case 90: U(); Yp(); //d
				break;
			case 191: Up(); Y(); //d'
				break;
			case 85: L(); X(); //r
				break;
			case 77: Lp(); Xp(); //r'
				break;
			case 86: R(); Xp(); //l
				break;
			case 82: Rp(); X(); //l'
				break;
			case 46: Rp(); L(); X(); //M'
				break;
			case 88: R(); Lp(); Xp(); //M
				break;
		}
		repaint();
	}
	public void cwrotate(int a)
	{
		Cube[a][0][0]=face[0][2];
		Cube[a][1][0]=face[0][1];
		Cube[a][2][0]=face[0][0];
		Cube[a][2][1]=face[1][0];
		Cube[a][2][2]=face[2][0];
		Cube[a][1][2]=face[2][1];
		Cube[a][0][2]=face[2][2];
		Cube[a][0][1]=face[1][2];
	}
	public void ccwrotate(int a)
	{
		Cube[a][0][0]=face[2][0];
		Cube[a][1][0]=face[2][1];
		Cube[a][2][0]=face[2][2];
		Cube[a][2][1]=face[1][2];
		Cube[a][2][2]=face[0][2];
		Cube[a][1][2]=face[0][1];
		Cube[a][0][2]=face[0][0];
		Cube[a][0][1]=face[1][0];
	}
	public void X()
	{
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[4][x][y];
		cwrotate(4);
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[5][x][y];
		ccwrotate(5);
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[0][x][y];
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++){
				int temp=Cube[3][2-x][2-y];
				Cube[3][2-x][2-y]=face[x][y];
				face[x][y]=temp;
			}
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++){
				int temp=Cube[1][x][y];
				Cube[1][x][y]=face[x][y];
				face[x][y]=temp;
			}
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++){
				int temp=Cube[2][x][y];
				Cube[2][x][y]=face[x][y];
				face[x][y]=temp;
			}
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++){
				int temp=Cube[0][x][y];
				Cube[0][x][y]=face[x][y];
				face[x][y]=temp;
			}
	}
	public void Xp()
	{
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[5][x][y];
		cwrotate(5);
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[4][x][y];
		ccwrotate(4);
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[0][x][y];
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++){
				int temp=Cube[2][x][y];
				Cube[2][x][y]=face[x][y];
				face[x][y]=temp;
			}
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++){
				int temp=Cube[1][x][y];
				Cube[1][x][y]=face[x][y];
				face[x][y]=temp;
			}
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++){
				int temp=Cube[3][2-x][2-y];
				Cube[3][2-x][2-y]=face[x][y];
				face[x][y]=temp;
			}
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++){
				int temp=Cube[0][x][y];
				Cube[0][x][y]=face[x][y];
				face[x][y]=temp;
			}
	}
	public void Y()
	{
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[0][x][y];
		cwrotate(0);
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[1][x][y];
		ccwrotate(1);
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[2][x][y];
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++){
				int temp=Cube[5][x][y];
				Cube[5][x][y]=face[x][y];
				face[x][y]=temp;
			}
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++){
				int temp=Cube[3][x][y];
				Cube[3][x][y]=face[x][y];
				face[x][y]=temp;
			}
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++){
				int temp=Cube[4][x][y];
				Cube[4][x][y]=face[x][y];
				face[x][y]=temp;
			}
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++){
				int temp=Cube[2][x][y];
				Cube[2][x][y]=face[x][y];
				face[x][y]=temp;
			}
	}
	public void Yp()
	{
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[1][x][y];
		cwrotate(1);
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[0][x][y];
		ccwrotate(0);
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[2][x][y];
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++){
				int temp=Cube[4][x][y];
				Cube[4][x][y]=face[x][y];
				face[x][y]=temp;
			}
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++){
				int temp=Cube[3][x][y];
				Cube[3][x][y]=face[x][y];
				face[x][y]=temp;
			}
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++){
				int temp=Cube[5][x][y];
				Cube[5][x][y]=face[x][y];
				face[x][y]=temp;
			}
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++){
				int temp=Cube[2][x][y];
				Cube[2][x][y]=face[x][y];
				face[x][y]=temp;
			}
	}
	public void U()
	{
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[0][x][y];
		cwrotate(0);
		for(int x=0;x<3;x++)
			side[x]=Cube[2][x][0];
		for(int i=0;i<4;i++){
			int s=0;
			switch(i){
				case 0: s=5;
					break;
				case 1: s=3;
					break;
				case 2: s=4;
					break;
				case 3: s=2;
			}
			for(int x=0;x<3;x++){
				int temp=Cube[s][x][0];
				Cube[s][x][0]=side[x];
				side[x]=temp;
			}
		}
	}
	public void Up()
	{
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[0][x][y];
		ccwrotate(0);
		for(int x=0;x<3;x++)
			side[x]=Cube[2][x][0];
		for(int i=0;i<4;i++){
			int s=0;
			switch(i){
				case 0: s=4;
					break;
				case 1: s=3;
					break;
				case 2: s=5;
					break;
				case 3: s=2;
			}
			for(int x=0;x<3;x++){
				int temp=Cube[s][x][0];
				Cube[s][x][0]=side[x];
				side[x]=temp;
			}
		}
	}
	public void D()
	{
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[1][x][y];
		cwrotate(1);
		for(int x=0;x<3;x++)
			side[x]=Cube[2][x][2];
		for(int i=0;i<4;i++){
			int s=0;
			switch(i){
				case 0: s=4;
					break;
				case 1: s=3;
					break;
				case 2: s=5;
					break;
				case 3: s=2;
			}
			for(int x=0;x<3;x++){
				int temp=Cube[s][x][2];
				Cube[s][x][2]=side[x];
				side[x]=temp;
			}
		}
	}
	public void Dp()
	{
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[1][x][y];
		ccwrotate(1);
		for(int x=0;x<3;x++)
			side[x]=Cube[2][x][2];
		for(int i=0;i<4;i++){
			int s=0;
			switch(i){
				case 0: s=5;
					break;
				case 1: s=3;
					break;
				case 2: s=4;
					break;
				case 3: s=2;
			}
			for(int x=0;x<3;x++){
				int temp=Cube[s][x][2];
				Cube[s][x][2]=side[x];
				side[x]=temp;
			}
		}
	}
	public void F()
	{
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[2][x][y];
		cwrotate(2);
		for(int x=0;x<3;x++)
			side[x]=Cube[0][x][2];
		for(int x=0;x<3;x++){
			int temp=Cube[4][0][x];
			Cube[4][0][x]=side[x];
			side[x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[1][2-x][0];
			Cube[1][2-x][0]=side[x];
			side[x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[5][2][x];
			Cube[5][2][x]=side[2-x];
			side[2-x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[0][x][2];
			Cube[0][x][2]=side[x];
			side[x]=temp;
		}
	}
	public void Fp()
	{
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[2][x][y];
		ccwrotate(2);
		for(int x=0;x<3;x++)
			side[x]=Cube[0][x][2];
		for(int x=0;x<3;x++){
			int temp=Cube[5][2][x];
			Cube[5][2][x]=side[2-x];
			side[2-x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[1][2-x][0];
			Cube[1][2-x][0]=side[x];
			side[x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[4][0][x];
			Cube[4][0][x]=side[x];
			side[x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[0][x][2];
			Cube[0][x][2]=side[x];
			side[x]=temp;
		}
	}
	public void B()
	{
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[3][x][y];
		cwrotate(3);
		for(int x=0;x<3;x++)
			side[x]=Cube[0][x][0];
		for(int x=0;x<3;x++){
			int temp=Cube[5][0][2-x];
			Cube[5][0][2-x]=side[x];
			side[x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[1][x][2];
			Cube[1][x][2]=side[2-x];
			side[2-x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[4][2][x];
			Cube[4][2][x]=side[x];
			side[x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[0][x][0];
			Cube[0][x][0]=side[x];
			side[x]=temp;
		}
	}
	public void Bp()
	{
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[3][x][y];
		ccwrotate(3);
		for(int x=0;x<3;x++)
			side[x]=Cube[0][x][0];
		for(int x=0;x<3;x++){
			int temp=Cube[4][2][x];
			Cube[4][2][x]=side[x];
			side[x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[1][2-x][2];
			Cube[1][2-x][2]=side[x];
			side[x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[5][0][x];
			Cube[5][0][x]=side[2-x];
			side[2-x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[0][x][0];
			Cube[0][x][0]=side[x];
			side[x]=temp;
		}
	}
	public void R()
	{
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[4][x][y];
		cwrotate(4);
		for(int x=0;x<3;x++)
			side[x]=Cube[0][2][x];
		for(int x=0;x<3;x++){
			int temp=Cube[3][0][2-x];
			Cube[3][0][2-x]=side[x];
			side[x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[1][2][x];
			Cube[1][2][x]=side[x];
			side[x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[2][2][x];
			Cube[2][2][x]=side[x];
			side[x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[0][2][x];
			Cube[0][2][x]=side[x];
			side[x]=temp;
		}
	}
	public void Rp()
	{
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[4][x][y];
		ccwrotate(4);
		for(int x=0;x<3;x++)
			side[x]=Cube[0][2][x];
		for(int x=0;x<3;x++){
			int temp=Cube[2][2][x];
			Cube[2][2][x]=side[x];
			side[x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[1][2][x];
			Cube[1][2][x]=side[x];
			side[x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[3][0][2-x];
			Cube[3][0][2-x]=side[x];
			side[x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[0][2][x];
			Cube[0][2][x]=side[x];
			side[x]=temp;
		}
	}
	public void L()
	{
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[5][x][y];
		cwrotate(5);
		for(int x=0;x<3;x++)
			side[x]=Cube[0][0][x];
		for(int x=0;x<3;x++){
			int temp=Cube[2][0][x];
			Cube[2][0][x]=side[x];
			side[x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[1][0][x];
			Cube[1][0][x]=side[x];
			side[x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[3][2][2-x];
			Cube[3][2][2-x]=side[x];
			side[x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[0][0][x];
			Cube[0][0][x]=side[x];
			side[x]=temp;
		}
	}
	public void Lp()
	{
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				face[x][y]=Cube[5][x][y];
		ccwrotate(5);
		for(int x=0;x<3;x++)
			side[x]=Cube[0][0][x];
		for(int x=0;x<3;x++){
			int temp=Cube[3][2][2-x];
			Cube[3][2][2-x]=side[x];
			side[x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[1][0][x];
			Cube[1][0][x]=side[x];
			side[x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[2][0][x];
			Cube[2][0][x]=side[x];
			side[x]=temp;
		}
		for(int x=0;x<3;x++){
			int temp=Cube[0][0][x];
			Cube[0][0][x]=side[x];
			side[x]=temp;
		}
	}
	public void keyReleased(KeyEvent e)
	{
	}
	public void keyTyped(KeyEvent e)
	{
	}
	public static void main(String args[])
	{
		VirtualCube app=new VirtualCube();
	}
}