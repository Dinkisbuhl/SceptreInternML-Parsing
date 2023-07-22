package Parsing;

import java.util.ArrayList;
import java.util.List;

import org.nograph.GraphManager;
import org.nograph.NoGraph;
import org.nograph.NoGraphException;
import org.nograph.Node;
import org.nograph.Relationship;

/*
 * This is the Searcher for the database (NoGraph)
 */
public class NographSearcher implements Searcher 
{
	GraphManager manager = null;
	public NographSearcher()
	{
		manager = NoGraph.getInstance().getGraphManager();
	}
	
	@Override
	public ArrayList<Concept> searchConcepts(String inp) 
	{
		List<Node> retVal = null;
		ArrayList<Concept> converted = new ArrayList<Concept>();
		try 
		{
			retVal = manager.findNodes("name", inp);
			converted = toConcept(retVal);
		} 
		catch (NoGraphException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return converted;
	}

	@Override
	public ArrayList<Concept> searchDesc(String inp) 
	{
		List<Node> retVal = null;
		ArrayList<Concept> converted = new ArrayList<Concept>();
		try 
		{
			retVal = manager.findNodes("description", inp);
			converted = toConcept(retVal);
		} 
		catch (NoGraphException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return converted;
	}

	@Override
	public ArrayList<Concept> getChildren(String name, int width) 
	{
		List<Node> retVal = new ArrayList<Node>();
		List<Node> tempVal = null;
		ArrayList<Concept> retValC = new ArrayList<Concept>();
		List<Node> retValN = new ArrayList<Node>();
		try 
		{
			tempVal = manager.findNodes("name", name);
			Node tempNode = null;
			if(tempVal.size() > 0)
			{
				tempNode = tempVal.get(0);
			}
			if(tempNode != null)
			{
				String tId = tempNode.getID();
				List<Relationship> rels = (ArrayList<Relationship>) manager.findRelationships("type", "childof", false);
				for(int i = 0; i < rels.size(); i++)
				{
					if(rels.get(i).getNode2ID().equals(tId))
					{
						retVal.add(rels.get(i).getNode1());
					}
				}
			}
			for(int i = 0; i < retVal.size(); i++)
			{
				//retValID.add(retVal.get(i).getID());
				String id = retVal.get(i).getID();
				List<Node> tempVal2 = manager.findNodes("id", id);
				Node tempNode2 = null;
				if(tempVal2.size() > 0)
				{
					tempNode2 = tempVal2.get(0);
				}
				retValN.add(tempNode2);
			}
			retValC = toConcept(retValN);
			
		} 
		catch (NoGraphException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retValC;
	}

	@Override
	public ArrayList<Concept> printConcepts(int level) 
	{
		List<Node> retVal = null;
		ArrayList<Concept> converted = new ArrayList<Concept>();
		try 
		{
			retVal = manager.findNodes("level", level);
			converted = toConcept(retVal);
		} 
		catch (NoGraphException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("In Searcher " + converted.size());
		return converted;
	}

	@Override
	public ArrayList<Concept> printConceptNames(int width) 
	{
		return allToConcept();
	}

	@Override
	public int printNumLvlConcepts(int level) 
	{
		List<Node> retVal = null;
		int count = 0;
		try 
		{
			retVal = manager.findNodes("level", level);
			count = retVal.size();
		} 
		catch (NoGraphException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int printTotalConcepts() 
	{
		int count = 0;
		try 
		{
			count = (int) manager.countNodes("concept");
		} 
		catch (NoGraphException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public ArrayList<Concept> printTranslations() 
	{
		return allToConcept();
	}

	@Override
	public Concept printTranslations(String name) 
	{
		List<Node> retVal = null;
		ArrayList<Concept> converted = new ArrayList<Concept>();
		Concept found = null;
		try 
		{
			retVal = manager.findNodes("name", name);
			converted = toConcept(retVal);
			if(converted.size() > 0)
			{
				found = converted.get(0);
			}
		} 
		catch (NoGraphException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return found;
	}

	@Override
	public ArrayList<Concept> printAncestorsList(String name) 
	{
		List<Node> retVal = new ArrayList<Node>();
		List<Node> tempVal = null;
		ArrayList<Concept> retValC = new ArrayList<Concept>();
		List<Node> retValN = new ArrayList<Node>();
		try 
		{
			tempVal = manager.findNodes("name", name);
			Node tempNode = null;
			if(tempVal.size() > 0)
			{
				tempNode = tempVal.get(0);
			}
			if(tempNode != null)
			{
				String tId = tempNode.getID();
				List<Relationship> rels = (ArrayList<Relationship>) manager.findRelationships("type", "childof", false);
				for(int i = 0; i < rels.size(); i++)
				{
					if(rels.get(i).getNode1ID().equals(tId))
					{
						retVal.add(rels.get(i).getNode2());
					}
				}
			}
			for(int i = 0; i < retVal.size(); i++)
			{
				//retValID.add(retVal.get(i).getID());
				String id = retVal.get(i).getID();
				List<Node> tempVal2 = manager.findNodes("id", id);
				Node tempNode2 = null;
				if(tempVal2.size() > 0)
				{
					tempNode2 = tempVal2.get(0);
				}
				retValN.add(tempNode2);
			}
			retValC = toConcept(retValN);
			
		} 
		catch (NoGraphException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retValC;
	}

	@Override
	public ArrayList<String> printAncestorsNames(String name) 
	{
		List<Node> retVal = new ArrayList<Node>();
		List<Node> tempVal = null;
		ArrayList<String> retValS = new ArrayList<String>();
		try 
		{
			tempVal = manager.findNodes("name", name);
			Node tempNode = null;
			if(tempVal.size() > 0)
			{
				tempNode = tempVal.get(0);
			}
			if(tempNode != null)
			{
				String tId = tempNode.getID();
				List<Relationship> rels = (ArrayList<Relationship>) manager.findRelationships("type", "childof", false);
				for(int i = 0; i < rels.size(); i++)
				{
					if(rels.get(i).getNode1ID().equals(tId))
					{
						retVal.add(rels.get(i).getNode2());
					}
				}
			}
			for(int i = 0; i < retVal.size(); i++)
			{
				//retValID.add(retVal.get(i).getID());
				String id = retVal.get(i).getID();
				List<Node> tempVal2 = manager.findNodes("id", id);
				Node tempNode2 = null;
				if(tempVal2.size() > 0)
				{
					tempNode2 = tempVal2.get(0);
				}
				retValS.add(tempNode2.getString("name"));
			}
			
		} 
		catch (NoGraphException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retValS;
	}
	
	public void findRelationships()
	{
		try 
		{
			List<Relationship> relations = manager.findRelationships("type", "childOf", false);
			for(int i = 0; i < relations.size(); i++)
			{
				System.out.println(relations.get(i));
			}
		} 
		catch (NoGraphException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Node> findAncestorsByRelation(String name)
	{
		List<Node> ancestorNodes = new ArrayList<Node>();
		try 
		{
			List<Relationship> relations = manager.findRelationships("type", "childOf", false);
			for(int i = 0; i < relations.size(); i++)
			{
				if (relations.get(i).getNode2().getProperty("name").equals(name))
				{
					ancestorNodes.add(relations.get(i).getNode1());
				}
			}
		} 
		catch (NoGraphException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ancestorNodes;
	}
	
	public List<Node> findAncestorsByRelationID(String id)
	{
		List<Node> ancestorNodes = new ArrayList<Node>();
		try 
		{
			List<Relationship> relations = manager.findRelationships("type", "childOf", false);
			for(int i = 0; i < relations.size(); i++)
			{
				if (relations.get(i).getNode2().getProperty("id").equals(id))
				{
					ancestorNodes.add(relations.get(i).getNode1());
				}
			}
		} 
		catch (NoGraphException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ancestorNodes;
	}
	
	public List<Node> searchByNodeId(String Id)
	{
		List<Node> retVal = null;
		try 
		{
			retVal = manager.findNodes("id", Id);
		} 
		catch (NoGraphException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;
	}
	
	public ArrayList<Concept> toConcept(List<Node> nodeArr)
	{
		ArrayList<Concept> nodeToConcept = new ArrayList<Concept>();
		long length = nodeArr.size();
		for(int i = 0; i < length; i++)
		{
			Node n = nodeArr.get(i); //manager.findNodes("type", "concept").get(i);
			String id = (String) n.getProperty("conceptid");
			String name = (String) n.getProperty("name");
			String desc = (String) n.getProperty("description");
			int level = (int) n.getProperty("level");
			
			ArrayList<String> translations = new ArrayList<String>();
			ArrayList<String> languages = new ArrayList<String>();
			if(n.getProperty("translations") != null)
			{
				if(n.getProperty("translations").getClass().equals(translations.getClass()))
				{
					translations = (ArrayList<String>) n.getProperty("translations");
				}
			}
			else
			{
				translations.add("no translations");
			}
			
			if(n.getProperty("languages") != null)
			{
				if(n.getProperty("languages").getClass().equals(languages.getClass()))
				{
					languages = (ArrayList<String>) n.getProperty("languages");
				}
			}
			else
			{
				languages.add("no languages");
			}
			
			//ArrayList<Concept> ancestors = null;
			//ArrayList<Concept> children = null;
			
			//System.out.println(translations);
			Concept temp = new Concept(id, name, desc, level, languages, translations);
			//Concept temp = new Concept(id, name, desc, level);
			nodeToConcept.add(temp);
		}
		return nodeToConcept;
	}

	public ArrayList<Concept> allToConcept()
	{
		ArrayList<Concept> nodeToConcept = new ArrayList<Concept>();
		try 
		{
			long length = manager.countNodes("concept");
			List<Node> foundNodes = manager.findNodes("concept", null, null, 100000);
			for(int i = 0; i < length; i++)
			{
				Node n = foundNodes.get(i);
				String id = (String) n.getProperty("conceptid");
				String name = (String) n.getProperty("name");
				String desc = (String) n.getProperty("description");
				int level = (int) n.getProperty("level");
				
				//ArrayList<String> translations = (ArrayList<String>) n.getProperty("translations");
				//ArrayList<String> languages = (ArrayList<String>) n.getProperty("languages");
				//ArrayList<Concept> ancestors = (ArrayList<Concept>) n.getProperty("ancestors");
				//ArrayList<Concept> children = (ArrayList<Concept>) n.getProperty("children");
				
				ArrayList<String> translations = new ArrayList<String>();
				ArrayList<String> languages = new ArrayList<String>();
				if(n.getProperty("translations") != null)
				{
					if(n.getProperty("translations").getClass().equals(translations.getClass()))
					{
						translations = (ArrayList<String>) n.getProperty("translations");
					}
				}
				if(n.getProperty("languages") != null)
				{
					if(n.getProperty("languages").getClass().equals(languages.getClass()))
					{
						languages = (ArrayList<String>) n.getProperty("languages");
					}
				}
				//ArrayList<Concept> ancestors = null;
				//ArrayList<Concept> children = null;
				
				Concept temp = new Concept(id, name, desc, level, languages, translations);
				//Concept temp = new Concept(id, name, desc, level);
				nodeToConcept.add(temp);
			}
		} 
		catch (NoGraphException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nodeToConcept;
	}
}
