package Parsing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class allows console-based interaction with either a database of
 * pre-loaded concepts or a parser that reads files to load into memory
 * @author Kyle H.
 */
public class Interaction 
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
	
	public static void main(String args[]) throws IOException
	{
		Searcher searcher;
		System.out.println("Options: memScn, noScn");
		Scanner inScn = new Scanner(System.in);
		String tInp = inScn.nextLine();
		System.out.println("accepted: " + tInp);
		if(tInp.equals("noScn")) //tInp.equals("noScn")
		{
			searcher = new NographSearcher();
			System.out.println("Using NographSearcher");
		}
		else
		{
			Parser parser = new Parser();
			searcher = new MemorySearcher(parser.getReferenceConcepts(), parser.getSortedConcepts(),
					parser.getAlphaSorted(), parser.getL1(), parser.getL2(), parser.getL3(), parser.getL4(),
					parser.getL5(), parser.getDup());
			sortedConcepts = parser.getSortedConcepts();
			concepts = parser.getReferenceConcepts();
			alphaSortedConcepts = parser.getAlphaSorted();
			level1S = parser.getL1();
			level2S = parser.getL2();
			level3S = parser.getL3();
			level4S = parser.getL4();
			level5S = parser.getL5();
			dupNum = parser.getDup();
		}
		//selScn.close();
		
		System.out.println("\n");
		System.out.println("Number of Duplicates: " + dupNum);
		//for(int i = 0; i < alphaSortedConcepts.size(); i++)
		//{
		//	System.out.println(alphaSortedConcepts.get(i));
		//}
		System.out.println("\n");
		//printConcepts(-1);
		pPrintConcepts(0, searcher);
		//printConcepts(1);
		//printConceptNames(10);
		System.out.println("\n");
		pPrintNumLvlConcepts(0, searcher);
		pPrintNumLvlConcepts(1, searcher);
		pPrintNumLvlConcepts(2, searcher);
		pPrintNumLvlConcepts(3, searcher);
		pPrintNumLvlConcepts(4, searcher);
		pPrintNumLvlConcepts(5, searcher);
		pPrintNumLvlConcepts(6, searcher);
		pPrintTotalConcepts(searcher);
		System.out.println("\n");
		pPrintTranslations("Plasmacytosis", searcher);
		System.out.println("\n");
		pPrintAncestorsList("Plasmacytosis", searcher);
		pPrintAncestorsNames("Plasmacytosis", searcher);
		System.out.println("\n");
		//printTranslations("Submarine");
		//printAncestorsList("Submarine");
		//printAncestorsNames("Submarine");
		
		//pGetChildren("Plasmacytosis", 8, searcher);
		//System.out.println("\n");
		pGetChildren("Philosophy", 8, searcher);
		System.out.println("\n");
		
		/*
		calculateAncestorsAverage(searcher);
		System.out.println("\n");
		calculateAncestorsAverage(1, searcher);
		System.out.println("\n");
		calculateAncestorsAverage(2, searcher);
		System.out.println("\n");
		calculateAncestorsAverage(3, searcher);
		System.out.println("\n");
		calculateAncestorsAverage(4, searcher);
		System.out.println("\n");
		calculateAncestorsAverage(5, searcher);
		System.out.println("\n");
		
		calculateChildAverage(searcher);
		*/
		System.out.println("\n");
		//Console Output Section
		System.out.println("Acceptable Commands:");
		System.out.println("print_concepts, print_concept_names, print_num_level_concept, "
				+ "print_total_concepts, print_translations, print_ancestors_list, "
				+ "print_ancestors_names, print_children, search_concepts, search_desc, print_commands, close");
		System.out.println("");
		boolean running = true;
		while(running == true)
		{
			inScn = new Scanner(System.in);
			String inp = inScn.nextLine();
			System.out.println("accepted: " + inp);
			if(inp.equals("close"))
			{
				inScn.close();
				running = false;
			}
			else if(inp.equals("print_concepts"))
			{
				System.out.println("What Level Number? Note, -1 for all levels");
				int inp2 = inScn.nextInt();
				pPrintConcepts(inp2, searcher);
				System.out.println("");
			}
			else if(inp.equals("print_concept_names"))
			{
				pPrintConceptNames(10, searcher);
				System.out.println("");
			}
			else if(inp.equals("print_num_level_concept"))
			{
				System.out.println("What Level Number?");
				int inp2 = inScn.nextInt();
				pPrintNumLvlConcepts(inp2, searcher);
				System.out.println("");
			}
			else if(inp.equals("print_total_concepts"))
			{
				pPrintTotalConcepts(searcher);
				System.out.println("");
			}
			else if(inp.equals("print_translations"))
			{
				System.out.println("Enter Name: ");
				String inp2 = inScn.nextLine();
				pPrintTranslations(inp2, searcher);
				System.out.println("");
			}
			else if(inp.equals("print_ancestors_list"))
			{
				System.out.println("Enter Name: ");
				String inp2 = inScn.nextLine();
				pPrintAncestorsList(inp2, searcher);
				System.out.println("");
			}
			else if(inp.equals("print_ancestors_names"))
			{
				System.out.println("Enter Name: ");
				String inp2 = inScn.nextLine();
				pPrintAncestorsNames(inp2, searcher);
				System.out.println("");
			}
			else if(inp.equals("print_children"))
			{
				System.out.println("Enter Name: ");
				String inp2 = inScn.nextLine();
				pGetChildren(inp2, 8, searcher);
				System.out.println("");
			}
			else if(inp.equals("search_concepts"))
			{
				System.out.println("Enter Name: ");
				String inp2 = inScn.nextLine();
				pSearchConcepts(inp2, searcher);
				System.out.println("");
			}
			else if(inp.equals("search_desc"))
			{
				System.out.println("Search descriptions for?... :");
				String inp2 = inScn.nextLine();
				pSearchDesc(inp2, searcher);
				System.out.println("");
			}
			else if(inp.equals("print_commands"))
			{
				System.out.println("Acceptable Commands:");
				System.out.println("print_concepts, print_concept_names, print_num_level_concept, "
						+ "print_total_concepts, print_translations, print_ancestors_list, "
						+ "print_ancestors_names, print_children, search_concepts, search_desc, close");
				System.out.println("");
			}
			else
			{
				System.out.println("Invalid Command");
				System.out.println("Acceptable Commands:");
				System.out.println("print_concepts, print_concept_names, print_num_level_concept, "
						+ "print_total_concepts, print_translations, print_ancestors_list, "
						+ "print_ancestors_names, print_children, search_concepts, search_desc, print_commands, close");
				System.out.println("");
			}
		}
	}
	
	public static void pSearchConcepts(String inp, Searcher searcher)
	{
		ArrayList<Concept> arr = searcher.searchConcepts(inp);
		if(arr.size() == 0)
		{
			System.out.println("No Matches");
			System.out.println("");
		}
		else
		{
			for(int i = 0; i < arr.size(); i++)
			{
				System.out.println(arr.get(i).getName());
			}
		}
	}
	
	public static void pSearchDesc(String inp, Searcher searcher)
	{
		ArrayList<Concept> arr = searcher.searchDesc(inp);
		if(arr.size() == 0)
		{
			System.out.println("No Matches");
			System.out.println("");
		}
		else
		{
			for(int i = 0; i < arr.size(); i++)
			{
				System.out.println("From " + arr.get(i).getName() + ":");
				System.out.println(arr.get(i).getDesc());
				System.out.println("");
			}
		}
	}
	
	public static void pGetChildren(String name, int width, Searcher searcher)
	{
		ArrayList<Concept> arr = searcher.searchDesc(name);
		if(arr.size() == 0)
		{
			System.out.println("name not found");
		}
		else
		{
			System.out.println("Children of " + name + ":");
			int counter = 0;
			for(int i = 0; i < arr.size(); i++)
			{
				if(counter < width)
				{
					System.out.print(arr.get(i).getName() + ", ");
					counter++;
				}
				else
				{
					System.out.println(arr.get(i).getName() + ", ");
					counter = 0;
				}
			}
		}
	}
	
	public static void pPrintConcepts(int level, Searcher searcher)
	{
		ArrayList<Concept> arr = searcher.printConcepts(level);
		for(int i = 0; i < arr.size(); i++)
		{
			System.out.println(arr.get(i));
		}
	}
	
	public static void pPrintNumLvlConcepts(int level, Searcher searcher)
	{
		int count = searcher.printNumLvlConcepts(level);
		System.out.println("Number of Level " + level + " Concepts: " + count);
	}
	
	public static void pPrintConceptNames(int width, Searcher searcher)
	{
		ArrayList<Concept> arr = searcher.printConceptNames(width);
		int counter = 0;
		for(int i = 0; i < arr.size(); i++)
		{
			if (counter < width)
			{
				System.out.print(arr.get(i).getName() + ", ");
				counter++;
			}
			else
			{
				System.out.println(arr.get(i).getName() + ", ");
				counter = 0;
			}
		}
	}
	
	public static void pPrintTotalConcepts(Searcher searcher)
	{
		int count = searcher.printTotalConcepts();
		System.out.println("Number of Total Concepts: " + count);
	}
	
	public static void pPrintTranslations(Searcher searcher)
	{
		ArrayList<Concept> arr = searcher.printTranslations();
		for(int i = 0; i < arr.size(); i++)
		{
			Concept curr = arr.get(i);
			ArrayList<String> lang = curr.getLanguages();
			ArrayList<String> trans = curr.getTranslations();
			System.out.println(curr.getName() + " Translations: ");
			for(int j = 0; j < lang.size(); j++)
			{
				System.out.print(lang.get(j) + ": " + trans.get(j) + ", ");
			}
		}
	}
	
	public static void pPrintTranslations(String name, Searcher searcher)
	{
		Concept found = searcher.printTranslations(name);
		if (found == null)
		{
			System.out.println("name not found");
		}
		else
		{
			ArrayList<String> lang = found.getLanguages();
			ArrayList<String> trans = found.getTranslations();
		 	System.out.println(found.getName() + " Translations:");
			for(int j = 0; j < lang.size(); j++)
			{
				System.out.println(lang.get(j) + ": " + trans.get(j));
			}
		}
	}
	
	public static void pPrintAncestorsList(String name, Searcher searcher)
	{
		ArrayList<Concept> found = searcher.printAncestorsList(name);
		if(found == null)
		{
			System.out.println("name not found");
		}
		else
		{
			System.out.println("Ancestors of " + name + ":");
			for(int i = 0; i < found.size(); i++)
			{
				System.out.println(found.get(i));
			}
		}
	}
	
	public static void pPrintAncestorsNames(String name, Searcher searcher)
	{
		//Concept found = searcher.printAncestorsNames(name);
		ArrayList<String> ancestors = searcher.printAncestorsNames(name);
		if(ancestors.size() >= 0)
		{
			System.out.println("Ancestors of " + name + ": ");
			/*
			System.out.println(ancestors);
			System.out.println(ancestors.getClass());
			System.out.println(ancestors.get(0).getClass());
			
			if(ancestors.get(0).getClass().equals(String.class))
			{
				System.out.println("thanks");
			*/
			for(int j = 0; j < ancestors.size(); j++)
			{
				System.out.print(ancestors.get(j) + ", ");
			}
		}
		else
		{
			System.out.println("name not found");
		}
	}
	
	public static void calculateAncestorsAverage(Searcher searcher)
	{
		int count = searcher.printTotalConcepts();
		ArrayList<Concept> arr = searcher.printTranslations();
		int numerator = 0;
		for(int i = 0; i < count; i++)
		{
			Concept curr = arr.get(i);
			ArrayList<Concept> ancestors = curr.getAncestors();
			numerator += ancestors.size();
		}
		int avg = numerator / count;
		int mod = numerator % count;
		System.out.println("Ancestors Concept Average: ");
		System.out.println("Average: " + avg);
		System.out.println("Mod: " + mod);
		System.out.println("Numerator: " + numerator);
		System.out.println("Count: " + count);
	}
	
	public static void calculateAncestorsAverage(int level, Searcher searcher)
	{
		int count = searcher.printTotalConcepts();
		int count2 = 0;
		ArrayList<Concept> arr = searcher.printTranslations();
		int numerator = 0;
		for(int i = 0; i < count; i++)
		{
			Concept curr = arr.get(i);
			if(curr.getLevel() == level)
			{
				ArrayList<Concept> ancestors = curr.getAncestors();
				numerator += ancestors.size();
				count2++;
			}
		}
		int avg = numerator / count2;
		int mod = numerator % count2;
		System.out.println("Level " + level + ":");
		System.out.println("Average: " + avg);
		System.out.println("Mod: " + mod);
		System.out.println("Numerator: " + numerator);
		System.out.println("Count: " + count2);
	}
	
	public static void calculateChildAverage(Searcher searcher)
	{
		int count = searcher.printTotalConcepts();
		ArrayList<Concept> arr = searcher.printTranslations();
		int numerator = 0;
		for(int i = 0; i < count; i++)
		{
			Concept curr = arr.get(i);
			ArrayList<Concept> children = curr.getChildren();
			numerator += children.size();
		}
		int avg = numerator / count;
		int mod = numerator % count;
		System.out.println("Children Concepts Average: ");
		System.out.println("Average: " + avg);
		System.out.println("Mod: " + mod);
		System.out.println("Numerator: " + numerator);
		System.out.println("Count: " + count);
	}
}
