package Parsing;
import java.util.ArrayList;

/*
 * A somewhat abstract idea, a topic that fits into a hierarchy of
 * other concepts where first order concepts are parents to lower level,
 * more detailed concepts. Those concepts link back to their higher level
 * concepts by having a list of the concepts they link back to, called ancestors.
 */
public class Concept 
{
	private String id;
	private String name;
	private String description;
	private int level;
	private ArrayList<Concept> ancestors = new ArrayList<Concept>();
	private ArrayList<String> languages = new ArrayList<String>();
	private ArrayList<String> translations = new ArrayList<String>();
	private ArrayList<Concept> children = new ArrayList<Concept>();
	
	public Concept(String i, String n, String d, int l)
	{
		id = i;
		name = n;
		description = d;
		level = l;
	}
	
	public Concept(String i, String n, String d, int l, ArrayList<String> la, ArrayList<String> t)
	{
		id = i;
		name = n;
		description = d;
		level = l;
		languages = la;
		translations = t;
	}
	
	public Concept(String i, String n, String d, int l, ArrayList<Concept> a)
	{
		id = i;
		name = n;
		description = d;
		level = l;
		ancestors = a;
	}
	
	public Concept(String i, String n, String d, int l, ArrayList<String> la, ArrayList<String> t, 
			ArrayList<Concept> a, ArrayList<Concept> c)
	{
		id = i;
		name = n;
		description = d;
		level = l;
		languages = la;
		translations = t;
		ancestors = a;
		children = c;
	}
	public String getID()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDesc()
	{
		return description;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public ArrayList<Concept> getAncestors()
	{
		return ancestors;
	}
	
	public ArrayList<String> getLanguages()
	{
		return languages;
	}
	
	public ArrayList<String> getTranslations()
	{
		return translations;
	}
	
	public ArrayList<Concept> getChildren()
	{
		return children;
	}
	
	public void addChild(Concept conc)
	{
		children.add(conc);
	}
	
	public String toString()
	{
		return "ID: " + getID() + ", Name: " + getName() + ", Level: " + 
				getLevel() + ", Description: " + getDesc();
	}
}