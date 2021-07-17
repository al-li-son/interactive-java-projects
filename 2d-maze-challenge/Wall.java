import java.awt.Rectangle;
public class Wall
{
	private int col;
	private int row;
	private int w;
	private int h;
	private int shift;
	public Wall(int col, int row, int w, int h, int shift)
	{
		this.col=col;
		this.row=row;
		this.w=w;
		this.h=h;
		this.shift=shift;
	}
	public int getCol()
	{
		return col;
	}
	public int getRow()
	{
		return row;
	}
	public int getWidth()
	{
		return w;
	}
	public int getHeight()
	{
		return h;
	}
	public int getShift()
	{
		return shift;
	}
	public Rectangle getRectangle()
	{
		return new Rectangle(col*w+shift, row*h+shift, w, h);
	}
}