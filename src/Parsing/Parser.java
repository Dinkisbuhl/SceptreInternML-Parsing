package Parsing;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 * For now, parses files given in the sample_concepts format into
 * an array list of concepts, a custom class, from the list of files filenames.
 * Additionally, has many methods to print out formatted information on the
 * parsed concepts.
 */
public class Parser 
{
	private static ArrayList<Concept> concepts = new ArrayList<Concept>();
	private static ArrayList<Concept> sortedConcepts = new ArrayList<Concept>(); 
	private static ArrayList<Concept> alphaSortedConcepts = new ArrayList<Concept>();
	private static ArrayList<Concept> level1C = new ArrayList<Concept>();
	private static ArrayList<Concept> level2C = new ArrayList<Concept>();
	private static ArrayList<Concept> level3C = new ArrayList<Concept>();
	private static ArrayList<Concept> level4C = new ArrayList<Concept>();
	private static ArrayList<Concept> level5C = new ArrayList<Concept>();
	private static String[] fileNames = new String[32];
	private static int level1S = 0;
	private static int level2S = 0;
	private static int level3S = 0;
	private static int level4S = 0;
	private static int level5S = 0;
	private static int dupNum = 0;
	
	public Parser()
	{
		readFiles(32);
		sortByLevel();
		establishLevels();
		sortAlpha();
		assignChildren();
	}
	
	public static void main(String args[]) throws IOException
	{
		readFiles(32); //passes in the number of files to read
		sortByLevel();
		for(int i = 0; i < 100; i++)
		{
			System.out.println(sortedConcepts.get(i));
		}
		establishLevels();
		assignChildren();
	}
	
	/*
	 * Reads the files given in the format of sample_concepts and
	 * creates an ArrayList of concepts from them.
	 */
	public static void readFiles(int files)
	{
		for(int i = 0; i < files; i++)
		{
			if(i < 10)
			{
				fileNames[i] = "000" + i + "_part_00";
			}
			else
			{
				fileNames[i] = "00" + i + "_part_00";
			}
		}
		//reads each file
		for(int i = 0; i < files; i++)
		{
			System.out.println("Scanning: " + i);
			Scanner scn = null;
			File file = new File(fileNames[i]);
			try 
			{
				scn = new Scanner(file);
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}
			
			int limit = 10000;
			int count = 0;
			
			//separating by line
			while(scn.hasNextLine() && count < limit)
			{
				String next = scn.nextLine();
				//System.out.println(next);
				JSONObject jobj = new JSONObject(next);

				String id = jobj.getString("id");
				String name = jobj.getString("display_name");
				int level = jobj.getInt("level");
				String desc = null;
				Object o = jobj.get("description");
				if (o.getClass().equals(String.class))
				{
					desc = jobj.getString("description");
				}
				
				ArrayList<String> languages = new ArrayList<String>();
				ArrayList<String> translations = new ArrayList<String>();
				
				JSONObject interObj = jobj.getJSONObject("international");
				//System.out.println(interObj);
				Object tester = interObj.get("display_name");
				if (tester.getClass().equals(JSONObject.class))
				{
					JSONObject nameObj = interObj.getJSONObject("display_name");
					//System.out.println(nameObj);
					String names[] = nameObj.getNames(nameObj);
					for (int j = 0; j < names.length; j++)
					{
						languages.add(names[j]);
						translations.add(nameObj.getString(names[j]));
					}
				}
				else
				{
					System.out.println("but why");
				}
				
				ArrayList<Concept> ancestors = new ArrayList<Concept>();
				JSONArray jarr = new JSONArray(jobj.getJSONArray("ancestors"));
				for (int j = 0; j < jarr.length(); j++)
				{
					JSONObject tempJObj = jarr.getJSONObject(j);
					String tID = tempJObj.getString("id");
					String tName = tempJObj.getString("display_name");
					int tLevel = tempJObj.getInt("level");
					String tDesc = null;
					Concept aConc = new Concept(tID, tName, tDesc, tLevel);
					ancestors.add(aConc);
				}
				
				ArrayList<Concept> children = new ArrayList<Concept>();
				Concept newCon = new Concept(id, name, desc, level, languages, translations, ancestors, children);
				concepts.add(newCon);
				count++;
			}
			scn.close();
		}
	}
	
	/*
	 * Takes parsed concepts and sorts them by their level attribute into sortedConcepts
	 */
	public static void sortByLevel()
	{
		for(int i = 0; i < concepts.size(); i++)
		{
			Concept curr = concepts.get(i);
			int level = curr.getLevel();
			boolean stop = false;
			if(sortedConcepts.size() == 0)
			{
				sortedConcepts.add(curr);
			}
			for(int j = 0; j < sortedConcepts.size(); j++)
			{
				if(stop == false)
				{
					Concept temp = sortedConcepts.get(j);
					if(level <= temp.getLevel())
					{
						sortedConcepts.add(j, curr);;
						stop = true;
					}
				}
				else
				{
					break;
				}
			}
			if(stop == false)
			{
				System.out.println("Biggest");
				sortedConcepts.add(curr);
			}
			if(i == concepts.size()-1)
			{
				System.out.println("Sorting Complete");
			}
		}
	}
	
	/*
	 * Figures out the starting points of each level in the sortedConcepts, to be eventually 
	 * passed to other classes for quicker searches
	 */
	public static void establishLevels()
	{
		boolean firstC = true;
		boolean secondC = true;
		boolean thirdC = true;
		boolean fourthC = true;
		boolean fifthC = true;
		for(int i = 0; i < sortedConcepts.size(); i++)
		{
			if(firstC == true && sortedConcepts.get(i).getLevel() == 1)
			{
				level1S = i;
				firstC = false;
			}
			if(secondC == true && sortedConcepts.get(i).getLevel() == 2)
			{
				level2S = i;
				secondC = false;
			}
			if(thirdC == true && sortedConcepts.get(i).getLevel() == 3)
			{
				level3S = i;
				thirdC = false;
			}
			if(fourthC == true && sortedConcepts.get(i).getLevel() == 4)
			{
				level4S = i;
				fourthC = false;
			}
			if(fifthC == true && sortedConcepts.get(i).getLevel() == 5)
			{
				level5S = i;
				fifthC = false;
			}
		}
	}
	
	/*
	 * Now sorts within each level division alphabetically, so level 0 is still all at the front
	 * but now is sorted A-Z, then level 1 comes next, sorted within level 1s A-Z, etc.
	 * This is stored in alphaSortedConcepts
	 */
	public static void sortAlpha()
	{
		System.out.println(level1S);
		System.out.println(level2S);
		System.out.println(level3S);
		System.out.println(level4S);
		System.out.println(level5S);
		int oneS = level1S;
		int numDuplicate = 0;
		for(int i = 0; i < oneS; i++)
		{
			int dexMin = 0;
			for(int j = 0; j < level1S; j++)
			{
				if((sortedConcepts.get(dexMin).getName().compareTo(sortedConcepts.get(j).getName()) == 0) && dexMin != j)
				{
					numDuplicate++;
				}
				if(sortedConcepts.get(dexMin).getName().compareTo(sortedConcepts.get(j).getName()) > 0)
				{
					dexMin = j;
				}
			}
			
			alphaSortedConcepts.add(sortedConcepts.get(dexMin));
			sortedConcepts.remove(dexMin);
			level1S--;
			level2S--;
			level3S--;
			level4S--;
			level5S--;
		}
		int twoS = level2S;
		for(int i = level1S; i < twoS; i++)
		{		
			int dexMin = 0;
			for(int j = level1S; j < level2S; j++)
			{
				if((sortedConcepts.get(dexMin).getName().compareTo(sortedConcepts.get(j).getName()) == 0) && dexMin != j)
				{
					numDuplicate++;
				}
				if(sortedConcepts.get(dexMin).getName().compareTo(sortedConcepts.get(j).getName()) > 0)
				{
					dexMin = j;
				}
			}
			
			alphaSortedConcepts.add(sortedConcepts.get(dexMin));
			sortedConcepts.remove(dexMin);
			level2S--;
			level3S--;
			level4S--;
			level5S--;
		}
		int threeS = level3S;
		for(int i = level2S; i < threeS; i++)
		{
			int dexMin = 0;
			for(int j = level2S; j < level3S; j++)
			{
				if((sortedConcepts.get(dexMin).getName().compareTo(sortedConcepts.get(j).getName()) == 0) && dexMin != j)
				{
					numDuplicate++;
				}
				if(sortedConcepts.get(dexMin).getName().compareTo(sortedConcepts.get(j).getName()) > 0)
				{
					dexMin = j;
				}
			}
			
			alphaSortedConcepts.add(sortedConcepts.get(dexMin));
			sortedConcepts.remove(dexMin);
			level3S--;
			level4S--;
			level5S--;
		}
		int fourS = level4S;
		for(int i = level3S; i < fourS; i++)
		{
			int dexMin = 0;
			for(int j = level3S; j < level4S; j++)
			{
				if((sortedConcepts.get(dexMin).getName().compareTo(sortedConcepts.get(j).getName()) == 0) && dexMin != j)
				{
					numDuplicate++;
				}
				if(sortedConcepts.get(dexMin).getName().compareTo(sortedConcepts.get(j).getName()) > 0)
				{
					dexMin = j;
				}
			}
			
			alphaSortedConcepts.add(sortedConcepts.get(dexMin));
			sortedConcepts.remove(dexMin);
			level4S--;
			level5S--;
		}
		int fiveS = level5S;
		for(int i = level4S; i < fiveS; i++)
		{
			int dexMin = 0;
			for(int j = level4S; j < level5S; j++)
			{
				if((sortedConcepts.get(dexMin).getName().compareTo(sortedConcepts.get(j).getName()) == 0) && dexMin != j)
				{
					numDuplicate++;
				}
				if(sortedConcepts.get(dexMin).getName().compareTo(sortedConcepts.get(j).getName()) > 0)
				{
					dexMin = j;
				}
			}
			
			alphaSortedConcepts.add(sortedConcepts.get(dexMin));
			sortedConcepts.remove(dexMin);
			level5S--;
		}
		int staticSize = sortedConcepts.size();
		for(int i = level5S; i < staticSize; i++)
		{
			int dexMin = 0;
			for(int j = level5S; j < sortedConcepts.size(); j++)
			{
				if((sortedConcepts.get(dexMin).getName().compareTo(sortedConcepts.get(j).getName()) == 0) && dexMin != j)
				{
					numDuplicate++;
				}
				if(sortedConcepts.get(dexMin).getName().compareTo(sortedConcepts.get(j).getName()) > 0)
				{
					dexMin = j;
				}
			}
			
			alphaSortedConcepts.add(sortedConcepts.get(dexMin));
			sortedConcepts.remove(dexMin);
		}
		System.out.println("Duplicates found: " + numDuplicate);
		dupNum = numDuplicate;
	}
	
	/*
	 * Now takes the alphaSortedConcepts and reverses the ancestors property to figure out the
	 * children of each concept
	 */
	public static void assignChildren()
	{
		for(int i = 0; i < alphaSortedConcepts.size(); i++)
		{
			System.out.println("assigning " + i);
			Concept curr = alphaSortedConcepts.get(i);
			ArrayList<Concept> ancestors = curr.getAncestors();
			for(int j = 0; j < ancestors.size(); j++)
			{
				Concept aCurr = ancestors.get(j);
				String name = aCurr.getName();
				int level = aCurr.getLevel();
				//System.out.println(name);
				int k = 0;
				if(level == 1)
				{
					k = level1S;
				}
				if(level == 2)
				{
					k = level1S;
				}
				if(level == 3)
				{
					k = level1S;
				}
				if(level == 4)
				{
					k = level1S;
				}
				if(level == 5)
				{
					k = level1S;
				}
				Concept curr2 = alphaSortedConcepts.get(k);
				Concept found = null;
				boolean stop = false;
				//System.out.println(curr2.getName());
				while(k < alphaSortedConcepts.size())
				{
					if (stop == false)
					{
						//System.out.println(curr2.getName());
						curr2 = alphaSortedConcepts.get(k);
						if (curr2.getName().equals(name))
						{
							found = alphaSortedConcepts.get(k);
							stop = true;
							//System.out.println("found ancestor\n");
						}
					}
					k++;
				}
				//k = sortedConcepts.size()/2;
				
				if (stop == false)
				{
					System.out.println("can't find ancestor\n");
				}
				else
				{
					found.addChild(curr);
				}
			}
		}
	}
	
	public static  ArrayList<Concept> getSortedConcepts()
	{
		return sortedConcepts;
	}
	
	public static ArrayList<Concept> getReferenceConcepts()
	{
		return concepts;
	}
	
	public static ArrayList<Concept> getAlphaSorted()
	{
		return alphaSortedConcepts;
	}
	
	public static int getL1()
	{
		return level1S;
	}
	
	public static int getL2()
	{
		return level2S;
	}
	
	public static int getL3()
	{
		return level3S;
	}
	
	public static int getL4()
	{
		return level4S;
	}
	
	public static int getL5()
	{
		return level5S;
	}
	
	public static int getDup()
	{
		return dupNum;
	}
}