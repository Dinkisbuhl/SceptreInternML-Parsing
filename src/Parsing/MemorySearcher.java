package Parsing;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemorySearcher implements Searcher
{
	private static ArrayList<Concept> concepts = new ArrayList<Concept>();
	private static ArrayList<Concept> sortedConcepts = new ArrayList<Concept>();
	private static ArrayList<Concept> alphaSortedConcepts = new ArrayList<Concept>();
	private static int level1S = 0;
	private static int level2S = 0;
	private static int level3S = 0;
	private static int level4S = 0;
	private static int level5S = 0;
	private static int dupNum = 0;
	
	public MemorySearcher(ArrayList<Concept> c, ArrayList<Concept> s, ArrayList<Concept> a,
			int s1, int s2, int s3, int s4, int s5, int dm)
	{
		concepts = c;
		sortedConcepts = s;
		alphaSortedConcepts = a;
		level1S = s1;
		level2S = s2;
		level3S = s3;
		level4S = s4;
		level5S = s5;
		dupNum = dm;
	}
	
	@Override
	public ArrayList<Concept> searchConcepts(String inp)
	{
		Pattern p = Pattern.compile(Pattern.quote(inp), Pattern.CASE_INSENSITIVE);
		boolean foundMatch = false;
		ArrayList<Concept> output = new ArrayList<Concept>();
		for(int i = 0; i < alphaSortedConcepts.size(); i++)
		{
			Matcher m = p.matcher(alphaSortedConcepts.get(i).getName());
			if(m.find())
			{
				//System.out.println(alphaSortedConcepts.get(i).getName());
				output.add(alphaSortedConcepts.get(i));
				foundMatch = true;
			}
		}
		if(foundMatch == false)
		{
			//System.out.println("No Matches");
			//System.out.println("");
		}
		return output;
	}

	@Override
	public ArrayList<Concept> searchDesc(String inp)
	{
		Pattern p = Pattern.compile(Pattern.quote(inp), Pattern.CASE_INSENSITIVE);
		boolean foundMatch = false;
		ArrayList<Concept> output = new ArrayList<Concept>();
		for(int i = 0; i < alphaSortedConcepts.size(); i++)
		{
			if(alphaSortedConcepts.get(i).getDesc() != null)
			{
				Matcher m = p.matcher(alphaSortedConcepts.get(i).getDesc());
				if(m.find())
				{
					output.add(alphaSortedConcepts.get(i));
					//System.out.println("From " + alphaSortedConcepts.get(i).getName() + ":");
					//System.out.println(alphaSortedConcepts.get(i).getDesc());
					//System.out.println("");
					foundMatch = true;
				}
			}
		}
		if(foundMatch == false)
		{
			//System.out.println("No Matches");
			//System.out.println("");
		}
		return output;
	}

	@Override
	public ArrayList<Concept> getChildren(String name, int width)
	{
		int k = 0;
		Concept curr = alphaSortedConcepts.get(k);
		Concept found = null;
		boolean stop = false;
		ArrayList<Concept> output = new ArrayList<Concept>();
		//System.out.println(curr2.getName());
		for (k = 0; k < alphaSortedConcepts.size(); k++)
		{
			if (stop == false)
			{
				//System.out.println(curr2.getName());
				curr = alphaSortedConcepts.get(k);
				if (curr.getName().equals(name))
				{
					found = alphaSortedConcepts.get(k);
					stop = true;
					//System.out.println("found ancestor\n");
				}
			}
		}
		if (stop == false)
		{
			//System.out.println("name not found");
		}
		else
		{
			//System.out.println("Children of " + name + ":");
			ArrayList<Concept> children = found.getChildren();
			int counter = 0;
			for(int i = 0; i < children.size(); i++)
			{
				output.add(children.get(i));
				/*
				if(counter < width)
				{
					System.out.print(children.get(i).getName() + ", ");
					counter++;
				}
				else
				{
					System.out.println(children.get(i).getName() + ", ");
					counter = 0;
				}
				*/
			}
		}
		return output;
	}

	@Override
	public ArrayList<Concept> printConcepts(int level)
	{
		ArrayList<Concept> output = new ArrayList<Concept>();
		if(level == -1)
		{
			for(int i = 0; i < concepts.size(); i++)
			{
				//System.out.println(concepts.get(i));
				output.add(concepts.get(i));
			}
		}
		else
		{
			for(int i = 0; i < concepts.size(); i++)
			{
				if(concepts.get(i).getLevel() == level)
				{
					//System.out.println(concepts.get(i));
					output.add(concepts.get(i));
				}
			}
		}
		return output;
	}

	@Override
	public ArrayList<Concept> printConceptNames(int width)
	{
		/*
		int counter = 0;
		for(int i = 0; i < alphaSortedConcepts.size(); i++)
		{
			if (counter < width)
			{
				System.out.print(alphaSortedConcepts.get(i).getName() + ", ");
				counter++;
			}
			else
			{
				System.out.println(alphaSortedConcepts.get(i).getName() + ", ");
				counter = 0;
			}
		}
		*/
		return alphaSortedConcepts;
	}

	@Override
	public int printNumLvlConcepts(int level)
	{
		int count = 0;
		for(int i = 0; i < concepts.size(); i++)
		{
			if (concepts.get(i).getLevel() == level)
			{
				count++;
			}
		}
		return count;
		//System.out.println("Number of Level " + level + " Concepts: " + count);
	}

	@Override
	public int printTotalConcepts()
	{
		int count = 0;
		for(int i = 0; i < concepts.size(); i++)
		{
			count++;
		}
		//System.out.println("Number of Total Concepts: " + count);
		return count;
	}

	@Override
	public ArrayList<Concept> printTranslations()
	{
		/*
		for(int i = 0; i < concepts.size(); i++)
		{
			Concept curr = concepts.get(i);
			ArrayList<String> lang = curr.getLanguages();
			ArrayList<String> trans = curr.getTranslations();
			System.out.println(curr.getName() + " Translations: ");
			for(int j = 0; j < lang.size(); j++)
			{
				System.out.print(lang.get(j) + ": " + trans.get(j) + ", ");
			}
		}
		*/
		return alphaSortedConcepts;
	}

	@Override
	public Concept printTranslations(String name)
	{
		int i = 0;
		Concept curr = concepts.get(i);
		Concept found = null;
		boolean stop = false;
		//System.out.println(curr.getName());
		for (i = 0; i < concepts.size(); i++)
		{
			if (stop == false)
			{
				//System.out.println(curr.getName());
				curr = concepts.get(i);
				if (curr.getName().equals(name))
				{
					found = concepts.get(i);
					stop = true;
				}
			}
		}
		if (stop == false)
		{
			//System.out.println("name not found");
		}
		else
		{
			/*
			ArrayList<String> lang = found.getLanguages();
			ArrayList<String> trans = found.getTranslations();
		 	System.out.println(found.getName() + " Translations:");
			for(int j = 0; j < lang.size(); j++)
			{
				System.out.println(lang.get(j) + ": " + trans.get(j));
			}
			*/
		}
		return found;
	}

	@Override
	public ArrayList<Concept> printAncestorsList(String name)
	{
		int i = 0;
		Concept curr = concepts.get(i);
		Concept found = null;
		boolean stop = false;
		ArrayList<Concept> ancestors = new ArrayList<Concept>();
		for(i = 0; i < concepts.size(); i++ )
		{
			if (stop == false)
			{
				curr = concepts.get(i);
				if (curr.getName().equals(name))
				{
					found = concepts.get(i);
					stop = true;
				}
			}
		}
		if (stop == false)
		{
			//System.out.println("name not found");
		}
		else
		{		
			//System.out.println("Ancestors of " + name + ": ");
			ancestors = found.getAncestors();
		}
		return ancestors;
	}

	@Override
	public ArrayList<String> printAncestorsNames(String name)
	{
		int i = 0;
		Concept curr = concepts.get(i);
		Concept found = null;
		boolean stop = false;
		ArrayList<String> retVal = new ArrayList<String>();
		for(i = 0; i < concepts.size(); i++ )
		{
			if (stop == false)
			{
				curr = concepts.get(i);
				if (curr.getName().equals(name))
				{
					found = concepts.get(i);
					stop = true;
				}
			}
		}
		if (stop == false)
		{
			//System.out.println("name not found");
		}
		else
		{		
			//System.out.println("Ancestors of " + name + ": ");
			ArrayList<Concept> ancestors = found.getAncestors();
			for(int j = 0; j < ancestors.size(); j++)
			{
				//System.out.print(ancestors.get(j).getName() + ", ");
				retVal.add(ancestors.get(j).getName());
			}
		}
		return retVal;
	}
	
}
