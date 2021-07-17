public class EndWall extends Wall
{
	private int col;
	private int row;
	private int w;
	private int h;
	private int shift;
	private boolean lock;

	public EndWall(int col, int row, int w, int h, int shift)
	{
		super(col,row,w,h,shift);
		lock=true;
	}
	public void lock()
	{
		lock=true;
	}
	public void unlock()
	{
		lock=false;
	}
	public boolean unlocked()
	{
		return !lock;
	}
}