package MachineLearning;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;
import java.io.PrintWriter;


/*
 Notes for my notebook when I get home:
 
 -Classifies two groups as Iris-setosa another 1/10 runs, figure that out.
 -9/10 runs that work, accuracy is between 83 and 98 percent, usually in the 86-95 percent range.
 -Overall percentage averages over multiple runs to be 70-91 percent (usually in the 86-89 range)
 This issue gives me a question:
 What to do if one cluster center randomization swallows up two groups, and the other two
 end up fighting over a single group, how to prevent.
 
 Row id
 Write method to make a new csv file, replace class label with cluster id, take back to python to visualize.
 Get rid of hard coding in all classes.
 Different number of clusters experimentation.
 Once 3 clusters works try visuals for different number of clusters.
 Python selecting specific axes to analyze and leaving others out.
 
 average distances for cluster members from center compared to average distance between cluster centers.
 analyzing "good job"
 */
//CURRENTLY SET FOR:
//*CONCEPT PCS V2*
public class KMeans 
{
	static List<Item> items = new ArrayList<Item>();
	//List<Item> testItems = new ArrayList<Item>();
	static double[] mins;
	static double[] maxs;
	static double[] rNums;
	public static int numVec;
	enum clusterMode
	{
		RANDOM, CLOSE, CENTERED, SPREAD, ITEM
	}
	
	public static void main(String args[]) throws IOException
	{
		boolean multiRun = false;
		int runs = 10;
		int testCases = 0;
		double totalCorrect = 0.0;
		double totalTested = testCases * runs;
		int[] skipVecs = {};
		int[] skipLines = {};
		List<String> categories = generateItemsList(25, 25, skipVecs, skipLines, false, false);
		List<Item> testItems = validationSort(testCases);
		//neighbors segment
		/*
		List<Item> neighbors = findKNearestNeighbor(items, 3);
		for(int i = 0; i < neighbors.size(); i++)
		{
			System.out.println(neighbors.get(i).toString());
		}
		*/
		
		//machine learning segment
		
		if(multiRun == true)
		{
			for(int l = 0; l < runs; l++)
			{
				List<String> categories2 = generateItemsList(25, 25, skipVecs, skipLines, false, false);
				List<Item> testItems2 = validationSort(testCases);
				List<Cluster> clusters = cluster(items, 1000, 100, true, clusterMode.SPREAD);
				for(int i = 0; i < clusters.size(); i++)
				{
					double[] tempClC = clusters.get(i).getCenter();
					System.out.println("\nCluster " + (i + 1) + ": ");
					for(int j = 0; j < tempClC.length; j++)
					{
						System.out.print(tempClC[j] + ", ");
					}
				}
				totalCorrect += checkValidation(clusters, categories2, testItems2);
				analyzeEffectiveness(clusters);
				System.out.println(l + "th run completed");
			}
			double finalAcc = totalCorrect / totalTested;
			System.out.println("\nFinal Accuracy: " + finalAcc);
		}
		else
		{
			List<Cluster> clusters = cluster(items, 1000, 100, true, clusterMode.SPREAD);
			for(int i = 0; i < clusters.size(); i++)
			{
				double[] tempClC = clusters.get(i).getCenter();
				System.out.println("\nCluster " + (i + 1) + ": ");
				for(int j = 0; j < tempClC.length; j++)
				{
					System.out.print(tempClC[j] + ", ");
				}
			}
			checkValidation(clusters, categories, testItems);
			analyzeEffectiveness(clusters);			
		}
		
	}
	
	public static List<String> generateItemsList(int usingVec, int dataVec, int[] skipVecs, int[] skipLines, 
			boolean skip, boolean sl)
	{
		Scanner scn = null;
		File file = new File("concept_pcs_v2m.csv");
		try 
		{
			scn = new Scanner(file);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		int count = 0;
		int limit = 100000;
		numVec = usingVec;
		mins = new double[numVec];
		maxs = new double[numVec];
		boolean firstNum = true;
		List<String> categories = new ArrayList<String>();
		int lineCount = 0;
		while(scn.hasNextLine() && count < limit)
		{
			//System.out.print(lineCount);
			//System.out.println(count);
			boolean willSkipLine = false;
			if(sl == true)
			{
				for(int i = 0; i < skipLines.length; i++)
				{
					if(skipLines[i] == lineCount)
					{
						willSkipLine = true;
					}
				}
			}
			if(willSkipLine)
			{
				scn.nextLine();
				lineCount++;
			}
			else
			{
				String next = scn.nextLine();
				lineCount++;
				Scanner scn2 = new Scanner(next);
				scn2.useDelimiter(",");
				while(scn2.hasNext())
				{
					Item item = new Item();
					double[] tempDL = new double[usingVec];
					int skippedVecNum = 0;
					//for codon only
					/*
					String tempId = scn2.next();
					boolean duplicate = false;
					for(int i = 0; i < categories.size(); i++)
					{
						if((categories.get(i).equals(tempId)))
						{
							duplicate = true;
						}
					}
					if(duplicate == false)
					{
						categories.add(tempId);
					}
					item.setId(tempId);
					//System.out.println(tempId);
					scn2.next();
					scn2.next();
					scn2.next();
					scn2.next();
					*/
					//end segment
					
					//for concept only
					
					String tempId = scn2.next();
					item.setId("Concept");
					item.setLinkID(tempId);
					boolean duplicate = false;
					for(int i = 0; i < categories.size(); i++)
					{
						if((categories.get(i).equals("Concept")))
						{
							duplicate = true;
						}
					}
					if(duplicate == false)
					{
						categories.add("Concept");
					}
					
					//end segment
					for(int i = 0; i < dataVec; i++)
					{
						double tempD = 0.0;
						if(scn2.hasNextDouble())
						{
							tempD = scn2.nextDouble();
						}
						//System.out.print(tempD + ", ");
						boolean willSkip = false;
						if(skip == true)
						{
							for(int j = 0; j < skipVecs.length; j++)
							{
								if(skipVecs[j] == i)
								{
									willSkip = true;
								}
							}
						}
						if(willSkip == true)
						{
							skippedVecNum++;
						}
						else
						{
							tempDL[i-skippedVecNum] = tempD;
							if(firstNum == true)
							{
								for(int j = 0; j < numVec; j++)
								{
									mins[j] = tempDL[i-skippedVecNum];
									maxs[j] = tempDL[i-skippedVecNum];
									firstNum = false;
								}
							}
							if(tempDL[i-skippedVecNum] > maxs[i-skippedVecNum])
							{
								maxs[i-skippedVecNum] = tempDL[i-skippedVecNum];
							}
							if(tempDL[i-skippedVecNum] < mins[i-skippedVecNum])
							{
								mins[i-skippedVecNum] = tempDL[i-skippedVecNum];
							}
						}
					}
					item.setVector(tempDL);
					//This is for iris and bean
					/*
					String tempId = scn2.next();
					boolean duplicate = false;
					for(int i = 0; i < categories.size(); i++)
					{
						if((categories.get(i).equals(tempId)))
						{
							duplicate = true;
						}
					}
					if(duplicate == false)
					{
						categories.add(tempId);
					}
					item.setId(tempId);
					*/
					//end segment
					item.setRowNum(count);
					try 
					{
						items.add(item);
					} 
					catch (Exception e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//System.out.println(item.toString());
				}
				scn2.close();
				count++;
			}
		}
		scn.close();
		System.out.println(items.get(0));
		System.out.println(items.get(149));
		for(int i = 0; i < numVec; i++)
		{
			System.out.println("Vector " + i + " Min: " + mins[i]);
			System.out.println("Vector " + i + " Max: " + maxs[i]);
		}
		System.out.println("Category size: " + categories.size());
		return categories;
	}
	
	//takes out a testCount number of randomly selected data from the 
	//data set for testing later
	public static List<Item> validationSort(int testCount)
	{
		List<Item> testItems = new ArrayList<Item>();
		for(int i = 0; i < testCount; i++)
		{
			Random rand = new Random();
			int ind = rand.nextInt(items.size()); //codon is + 1 otherwise isn't
			if(ind == 0)
			{
				ind = 1;
			}
			testItems.add(items.get(ind));
			items.remove(ind);
		}
		System.out.println(testItems.size());
		System.out.println(items.size());
		return testItems;
	}
	
	public static double checkValidation(List<Cluster> clusters, List<String> cat, List<Item> testItems)
	{
		for(int i = 0; i < clusters.size(); i++)
		{
			int[] counts = new int[cat.size()];
			Cluster tempCl = clusters.get(i);
			List<Item> tClL = tempCl.getItems();
			for(int j = 0; j < tClL.size(); j++)
			{
				String tId = tClL.get(j).getId();
				for(int k = 0; k < cat.size(); k++)
				{
					if(tId.equals(cat.get(k)))
					{
						counts[k]++;
					}
				}
			}
			int indGr = 0;
			for(int j = 0; j < counts.length; j++)
			{
				if(counts[j] > counts[indGr])
				{
					indGr = j;
				}
			}			
			//System.out.println("\ni: " + i + ", indGr: " + indGr);
			//System.out.println("clusters size: " + clusters.size() + ", categories size: " + cat.size());
			clusters.get(i).setGroup(cat.get(indGr));
		}
		
		System.out.println("\n");
		for(int i = 0; i < clusters.size(); i++)
		{
			System.out.println("Cluster " + (i +1) + " Group: " + clusters.get(i).getGroup());
		}
		
		//Now that each group corresponds to a category, test against testItems
		double correct = 0;
		for(int i = 0; i < testItems.size(); i++)
		{
			Item item = testItems.get(i);
			String id = item.getId();
			int tMin  = 0;
			double minDist = clusters.get(0).distance(item);
			for(int j = 0; j < clusters.size(); j++)
			{
				double tDist = clusters.get(j).distance(item);
				if(tDist < minDist)
				{
					tMin = j;
					minDist = tDist;
				}
			}
			if(clusters.get(tMin).getGroup().equals(id))
			{
				correct++;
			}	
		}
		System.out.println("Correct: " + correct);
		double per = correct / testItems.size();
		System.out.println("Percentage: " + per);
		return correct;
	}
	
	public static void analyzeEffectiveness(List<Cluster> clusters)
	{
		double averageCenterDist = 0.0;
		double averageItemToClusterDist = 0.0;
		for(int i = 0; i < clusters.size(); i++)
		{
			double tempClIDist = 0.0;
			double tempClCDist = 0.0;
			int countedClusters = 0;
			Cluster tempCl = clusters.get(i);
			for(int j = 0; j < clusters.size(); j++)
			{
				Cluster curCl = clusters.get(j);
				if((i != j) && (curCl.getItems().size() != 0))
				{
					double[] otherVec = curCl.getCenter();
					tempClCDist += tempCl.distance(otherVec);
					countedClusters++;
				}
			}
			tempClCDist /= (countedClusters);
			averageCenterDist += tempClCDist;
			List<Item> tempI = tempCl.getItems();
			for(int j = 0; j < tempI.size(); j++)
			{
				tempClIDist += tempCl.distance(tempI.get(j));
			}
			if(tempI.size() != 0)
			{
				tempClIDist /= tempI.size();
			}
			averageItemToClusterDist += tempClIDist;
		}
		averageItemToClusterDist /= clusters.size();
		averageCenterDist /= clusters.size();
		double ratio = averageItemToClusterDist / averageCenterDist;
		System.out.println("Average Items to Cluster Center Distance: " + averageItemToClusterDist);
		System.out.println("Average Cluster Center to Cluster Center Distance: " + averageCenterDist);
		System.out.println("Ratio: " + ratio);
	}
	
	public static List<Item> findKNearestNeighbor(List<Item> items, int k)
	{
		//System.out.println("Got here");
		Random rand = new Random();
		List<Item> IsOTD = new ArrayList<Item>();
		for(int i = 0; i < 1; i++)
		{
			Item IOTD = items.get(rand.nextInt(items.size())); //rand.nextInt(items.size())
			IsOTD.add(IOTD);
			System.out.println(IOTD.getLinkID());
			System.out.println(IOTD.toString());
		}
		List<double[]> selfVecs = new ArrayList<double[]>();
		for(int i = 0; i < IsOTD.size(); i++)
		{
			selfVecs.add(IsOTD.get(i).getVector());
		}
		List<Item> retItems = new ArrayList<Item>();
		double[] clNums = new double[k];
		for(int i = 0; i < k; i++)
		{
			clNums[i] = 100000;
		}
		for(int l = 0; l < k; l++)
		{
			boolean first = true;
			for(int i = 0; i < items.size(); i++)
			{		
				Item IOTR = items.get(i);
				double[] challenger = IOTR.getVector();
				double preSq = 0.0;
				for(int j = 0; j < numVec; j++)
				{
					preSq += Math.pow(selfVecs.get(0)[j] - challenger[j], 2);
				}
				boolean dup = false;
				for(int j = 0; j < l; j++)
				{
					if(Math.sqrt(preSq) == clNums[j])
					{
						dup = true;
					}
				}
				if(!dup && (Math.sqrt(preSq) < clNums[l]) && IOTR.equals(IsOTD.get(0)) != true)
				{
					clNums[l] =  Math.sqrt(preSq);
					if(first)
					{
						retItems.add(IOTR);
						first = false;
					}
					else
					{
						retItems.set(l, IOTR);
					}
				}
			}
		}
		/*
		for(int i = 0; i < retItems.size(); i++)
		{
			System.out.println(retItems.get(i).toString());
			//System.out.println(clNums[i]);
		}
		*/
		System.out.println("Done");
		return retItems;
	}
	
	/*
	 * The big one
	 */
	public static List<Cluster> cluster(List<Item> items, int numCluster, int iterations, boolean genFile, clusterMode cl)
	{
		//Step 1: Generate initial cluster centers.
		Random rand = new Random();
		List<Cluster> clusters = new ArrayList<Cluster>();
		double[] center = new double[numVec];
		rNums = new double[numVec];
		for(int i = 0; i < numVec; i++)
		{
			rNums[i] = maxs[i] - mins[i];
		}
		
		if(cl == clusterMode.RANDOM)
		{
			for(int i = 1; i <= numCluster; i++)
			{
				center = new double[numVec];
				for(int j = 0; j < numVec; j++)
				{
					center[j] = (rand.nextDouble() * rNums[j]) + mins[j];
					//System.out.println(center[j]);
				}
				Cluster tempC = new Cluster("Cluster " + i, center, numVec);
				clusters.add(tempC);
			}
		}
		else if(cl == clusterMode.CLOSE)
		{
			center = new double[numVec];
			for(int j = 0; j < numVec; j++)
			{
				double tempRN = rNums[j];
				center[j] = (rand.nextDouble() * (tempRN/4)) + ((tempRN / 2) - (tempRN / 8) + mins[j]);
				//System.out.println(center[j]);
			}
			Cluster pivotC = new Cluster("Cluster " + 1, center, numVec);
			double[] pivotV = pivotC.getCenter();
			clusters.add(pivotC);
			//PICK UP HERE
			for(int i = 2; i <= numCluster; i++)
			{
				center = new double[numVec];
				for(int j = 0; j < numVec; j++)
				{
					double tempRN = rNums[j];
					center[j] = pivotV[j] + ((rand.nextDouble() * (tempRN/2)) - (tempRN/4));
					//System.out.println(center[j]);
				}
				Cluster tempC = new Cluster("Cluster " + i, center, numVec);
				clusters.add(tempC);
			}
		}
		else if(cl == clusterMode.CENTERED)
		{
			for(int i = 1; i <= numCluster; i++)
			{
				center = new double[numVec];
				for(int j = 0; j < numVec; j++)
				{
					double tempRN = rNums[j];
					center[j] = (rand.nextDouble() * (tempRN/4)) + ((tempRN / 2) - (tempRN / 8) + mins[j]);
				}
				Cluster tempC = new Cluster("Cluster " + i, center, numVec);
				clusters.add(tempC);
			}
		}
		else if(cl == clusterMode.SPREAD)
		{
			for(int i = 1; i <= numCluster; i++)
			{
				center = new double[numVec];
				for(int j = 0; j < numVec; j++)
				{
					double tempRN = rNums[j];
					center[j] = (rand.nextDouble() * (tempRN/numCluster)) + (mins[j] + ((i-1) * tempRN/numCluster));
					//System.out.println(center[j]);
				}
				Cluster tempC = new Cluster("Cluster " + i, center, numVec);
				clusters.add(tempC);
			}
		}
		else if(cl == clusterMode.ITEM)
		{
			for(int i = 1; i <= numCluster; i++)
			{
				int choice = (rand.nextInt(items.size()));
				//System.out.println(choice);
				center = items.get(choice).getVector();
				Cluster tempC = new Cluster("Cluster " + i, center, numVec);
				clusters.add(tempC);
			}
		}
		
		//Step 2: Iterate
		Cluster bigCL = clusters.get(0);
		for(int i = 0; i < iterations; i++)
		{
			for(int j = 0; j < clusters.size(); j++)
			{
				double[] clTemp = clusters.get(j).getCenter();
				System.out.print("\nCluster " + (j + 1) + ": ");
				for(int k = 0; k < clTemp.length; k++)
				{
					System.out.print(clTemp[k] + ", ");
				}
			}
			System.out.println("");
			/*
			if(i == iterations - 1)
			{
				iterate(clusters, items, false);
			}
			else
			{
				iterate(clusters, items, true);
			}
			*/
			bigCL = iterate(clusters, items, bigCL, true);
		}	
		if(genFile)
		{
			generateFile(clusters, true, true);
		}
		return clusters;
	}
	
	public static Cluster iterate(List<Cluster> clusters, List<Item> items2, Cluster bigCL, Boolean clear)
	{
		//for each item calculate distance to the cluster and assign to a cluster.
		/*
		System.out.println("Length 1: " + clusters.get(0).getItems().size());
		System.out.println("Length 2: " + clusters.get(1).getItems().size());
		System.out.println("Length 3: " + clusters.get(2).getItems().size());
		System.out.println(items2.size());
		*/
		if(clear == true)
		{
			for(int i = 0; i < clusters.size(); i++)
			{
				clusters.get(i).clearItems();
			}
		}
		for(int i = 0; i < items2.size(); i++)
		{
			Item item = items2.get(i);
			int closestCluster = 0;
			double closestDist = clusters.get(0).distance(item);
			for(int j = 0; j < clusters.size(); j++)
			{
				double dist = clusters.get(j).distance(item);
				if(dist < closestDist)
				{
					closestCluster = j;
					closestDist = dist;
				}
			}
			clusters.get(closestCluster).addToCluster(item);
			/*
			System.out.println("Length 1: " + clusters.get(0).getItems().size());
			System.out.println("Length 2: " + clusters.get(1).getItems().size());
			System.out.println("Length 3: " + clusters.get(2).getItems().size());
			*/
		}
		/*
		System.out.println("Length 1: " + clusters.get(0).getItems().size());
		System.out.println("Length 2: " + clusters.get(1).getItems().size());
		System.out.println("Length 3: " + clusters.get(2).getItems().size());
		*/
		for(int j = 0; j < clusters.size(); j++)
		{
			clusters.get(j).setCenter(clusters.get(j).computeCenter());
			//double[] tCntr = clusters.get(j).getCenter();
			boolean redo = true;
			//old version
			/*
			for(int k = 0; k < tCntr.length; k++)
			{
				if(tCntr[k] != -1.0)
				{
					redo = false;
				}
			}		
			*/
			if(clusters.get(j).getItems().size() != 0)
			{
				redo = false;
			}
			if(redo == true)
			{
				double[] center = new double[numVec];
				Random rand = new Random();
				//this was randomly selecting new centers for empty clusters
				/*
				for(int k = 0; k < numVec; k++)
				{
					center[k] = (rand.nextDouble() * rNums[k]) + mins[k];
					//System.out.println(center[j]);
				}
				*/
				//random item
				int choice = (rand.nextInt(items.size()));
				center = items.get(choice).getVector();
				
				//random item from biggest cluster
				//int choice = rand.nextInt(bigCL.getItems().size());
				//center = bigCL.getItems().get(choice).getVector();
				clusters.get(j).setCenter(center);
			}
		}
		Cluster biggestCL = clusters.get(0);
		for(int i = 0; i < clusters.size(); i++)
		{
			Cluster curCL = clusters.get(i);
			if(biggestCL.getItems().size() < curCL.getItems().size())
			{
				biggestCL = curCL;
			}
		}
		System.out.println(biggestCL.getItems().size());
		return biggestCL;
	}
	
	public static void generateFile(List<Cluster> clusters, boolean multipleFiles, boolean supervisedExamine)
	{
		try 
		{
			if(multipleFiles == false)
			{
				PrintWriter pw = new PrintWriter(new File("output5TwoRemoved.csv"));
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < clusters.size(); i++)
				{
					Cluster tempCl = clusters.get(i);
					List<Item> tempI = tempCl.getItems();
					for(int j = 0; j < tempI.size(); j++)
					{
						Item currI = tempI.get(j);
						double[] itemVec = currI.getVector();
						for(int k = 0; k < itemVec.length; k++)
						{
							sb.append(itemVec[k]);
							sb.append(",");
						}
						sb.append("Cluster " + (i+1));
						sb.append("\n");
					}
				}
				pw.write(sb.toString());
				pw.close();
				System.out.println("\nfinished writing file\n");
			}
			else
			{
				if(supervisedExamine)
				{
					for(int i = 0; i < clusters.size(); i++)
					{
						if(clusters.get(i).getItems().size() != 0)
						{
							PrintWriter pw = new PrintWriter(new File("outputMulti" + i + ".csv"));
							StringBuilder sb = new StringBuilder();
							Cluster tempCl = clusters.get(i);
							List<Item> tempI = tempCl.getItems();
							for(int j = 0; j < tempI.size(); j++)
							{
								Item currI = tempI.get(j);
								double[] itemVec = currI.getVector();
								for(int k = 0; k < itemVec.length; k++)
								{
									sb.append(itemVec[k]);
									sb.append(",");
								}
								//sb.append(currI.getId()); //for non-cluster
								sb.append(currI.getLinkID());
								sb.append("\n");
							}
							pw.write(sb.toString());
							pw.close();
							System.out.println("\nfinished writing file " + i + "\n");
						}
					}
				}
				else
				{
					for(int i = 0; i < clusters.size(); i++)
					{
						if(clusters.get(i).getItems().size() != 0)
						{
							PrintWriter pw = new PrintWriter(new File("outputMulti" + i + ".csv"));
							StringBuilder sb = new StringBuilder();
							Cluster tempCl = clusters.get(i);
							List<Item> tempI = tempCl.getItems();
							for(int j = 0; j < tempI.size(); j++)
							{
								Item currI = tempI.get(j);
								double[] itemVec = currI.getVector();
								for(int k = 0; k < itemVec.length; k++)
								{
									if(k == itemVec.length - 1)
									{
										sb.append(itemVec[k]);
									}
									else
									{
										sb.append(itemVec[k]);
										sb.append(",");
									}
								}
								sb.append("\n");
							}
							pw.write(sb.toString());
							pw.close();
							System.out.println("\nfinished writing file " + i + "\n");
						}
					}
				}
				
			}
		} 
		catch (FileNotFoundException e) 
		{			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
