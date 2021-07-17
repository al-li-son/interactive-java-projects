import java.util.ArrayList;
public class LavaFlow
{
	private int startCol;
	private int startRow;
	private ArrayList<Lava> lava;
	private ArrayList<Lava> newLava;
	public LavaFlow(int startCol, int startRow)
	{
		this.startCol=startCol;
		this.startRow=startRow;
		lava=new ArrayList<Lava>();
		newLava=new ArrayList<Lava>();
	}
	public void move(ArrayList<Wall> walls)
	{
		ArrayList<Integer> temp=new ArrayList<Integer>();
		ArrayList<Lava> created=new ArrayList<Lava>();
		for(int x=0; x<newLava.size(); x++)
		{
			Lava l=newLava.get(x);
			temp=l.getAdjacentLocations(l.getCol(),l.getRow(),walls,lava);
			for(int y:temp)
			{
				switch(y)
				{
					case 1: created.add(new Lava(l.getCol(),l.getRow()-1,5));
						break;
					case 2: created.add(new Lava(l.getCol()+1,l.getRow(),5));
						break;
					case 3: created.add(new Lava(l.getCol(),l.getRow()+1,5));
						break;
					case 4: created.add(new Lava(l.getCol()-1,l.getRow(),5));
						break;
				}
			}
		}
		newLava=created;
		lava.addAll(newLava);
		if(lava.size()==0)
		{
			lava.add(new Lava(startCol,startRow,5));
			newLava.add(lava.get(0));
		}
	}
	public ArrayList<Lava> getLavaFlow()
	{
		return lava;
	}
	public int getStartCol()
	{
		return startCol;
	}
	public int getStartRow()
	{
		return startRow;
	}
}