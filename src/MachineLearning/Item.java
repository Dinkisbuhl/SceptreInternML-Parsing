package MachineLearning;

/*
 * This represents a single concept in the current model
 */
public class Item 
{
	String id; //class label
	String linkID; //concept id
	int rowNum; //processing id
	double[] vector;
	
	public String getId()
	{
		return id;
	}
	
	public double[] getVector()
	{
		return vector;
	}
	
	public int getRowNum()
	{
		return rowNum;
	}
	
	public String getLinkID()
	{
		return linkID;
	}
	
	public void setId(String i)
	{
		id = i;
	}
	
	public void setVector(double[] v)
	{
		vector = v;
	}
	
	public void setRowNum(int r)
	{
		rowNum = r;
	}
	
	public void setLinkID(String l)
	{
		linkID = l;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Num: " + rowNum + ", Id: " + linkID + ", Vector: ");
		for(int i = 0; i < vector.length; i++)
		{
			sb.append(vector[i]);
			sb.append(", ");
		}
		return sb.toString();	
	}
}
