package Parsing;

import java.util.ArrayList;

public interface Searcher 
{
	public ArrayList<Concept> searchConcepts(String inp);
	
	public ArrayList<Concept> searchDesc(String inp);
	
	public ArrayList<Concept> getChildren(String name, int width);
	
	public ArrayList<Concept> printConcepts(int level);
	
	public ArrayList<Concept> printConceptNames(int width);
	
	public int printNumLvlConcepts(int level);
	
	public int printTotalConcepts();
	
	public ArrayList<Concept> printTranslations();
	
	public Concept printTranslations(String name);
	
	public ArrayList<Concept> printAncestorsList(String name);
	
	public ArrayList<String> printAncestorsNames(String name);
}
