package MachineLearning;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class Cluster 
{
	List<Item> items = new ArrayList<Item>();
	String id;
	String group;
	int numVec;
	double[] center;
	
	public Cluster(String i, double[] c, int n)
	{
		id = i;
		center = c;
		numVec = n;
	}
	
	//loop over list of items and calculate average vector.
	public double[] computeCenter()
	{
		double[] retArr = new double[numVec];
		if(items.size() != 0)
		{
			for(int i = 0; i < items.size(); i++)
			{
				double[] vector = items.get(i).getVector();
				for(int j = 0; j < retArr.length; j++)
				{
					retArr[j] += vector[j];
				}
			}
			for(int i = 0; i < retArr.length; i++)
			{
				retArr[i] /= items.size();
			}
		}
		else
		{
			for(int i = 0; i < retArr.length; i++)
			{
				retArr[i] = -1.0;
			}
		}
		return retArr;
	}
	
	//distance between the arg and current center.
	public double distance(Item arg)
	{
		double[] vector = arg.getVector();
		double preSq = 0.0;
		for(int i = 0; i < vector.length; i++)
		{
			preSq += Math.pow(vector[i] - center[i], 2);
		}
		return Math.sqrt(preSq);
	}
	
	public double distance(double[] vector)
	{
		double preSq = 0.0;
		for(int i = 0; i < vector.length; i++)
		{
			preSq += Math.pow(vector[i] - center[i], 2);
		}
		return Math.sqrt(preSq);
	}
	
	public double[] getCenter()
	{
		return center;
	}
	
	public List<Item> getItems()
	{
		return items;
	}
	
	public void setCenter(double[] newC)
	{
		center = newC;
	}
	
	public String getGroup()
	{
		return group;
	}
	
	public void setGroup(String g)
	{
		group = g;
	}
	
	public void addToCluster(Item item)
	{
		items.add(item);
	}
	
	public void clearItems()
	{
		items = new ArrayList<Item>();
	}
}
