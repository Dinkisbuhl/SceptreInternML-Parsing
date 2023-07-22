package Parsing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nograph.GraphManager;
import org.nograph.NoGraph;
import org.nograph.NoGraphException;
import org.nograph.Node;
import org.nograph.Relationship;

public class NographTest 
{
	private static ArrayList<Concept> concepts = new ArrayList<Concept>();
	private static ArrayList<Concept> sortedConcepts = new ArrayList<Concept>();
	private static ArrayList<Concept> alphaSortedConcepts = new ArrayList<Concept>();
	private static ArrayList<Concept> nodeToConcept = new ArrayList<Concept>();
	private static int level1S = 0;
	private static int level2S = 0;
	private static int level3S = 0;
	private static int level4S = 0;
	private static int level5S = 0;
	private static int dupNum = 0;
	public static void main(String args[])
	{
		
		Parser parser = new Parser();
		Searcher searcher = new MemorySearcher(parser.getReferenceConcepts(), parser.getSortedConcepts(),
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
		
		//test1();
		//ingest1();
		toNode2();	
		//printNodeConcepts();
		//toConcept();
		//deleteAll();
		//printNodeConcepts();
		System.exit(0);
	}
	/*
	private static void ingest1() 
	{
		GraphManager newManager = NoGraph.getInstance().getGraphManager();
		try 
		{
			//System.out.println("checkpoint1");
			//newManager.saveNode(toNode(alphaSortedConcepts.get(0)));
			for(int i = 0; i < 5; i++)
			{
				System.out.println(concepts.get(i));
				newManager.saveNode(toNode(concepts.get(i)));
			}
			//newManager.deleteNode(newManager.findNodes("name", "Aerospace engineering").get(0));
		} 
		catch (NoGraphException e) 
		{
			e.printStackTrace();
		}
	}

	public static void test1()
	{
		try
		{
			GraphManager newManager = NoGraph.getInstance().getGraphManager();
			Node n1 = NoGraph.getInstance().newNode("testNode");
			n1.addProperty("name", "someNode");
			//newManager.saveNode(n1);
			System.out.println(newManager.getNodeTypes());
			System.out.println(newManager.findNodes("name", "someNode"));
			//newManager.deleteNode(newManager.findNodes("name", "someNode").get(0));
		}
		catch (Exception e)
		{
			
		}
	}
	
	public static Node toNode(Concept concept)
	{
		//System.out.println("checkpoint2");
		Node n = NoGraph.getInstance().newNode("concept");
		n.addProperty("id", concept.getID());
		n.addProperty("name", concept.getName());
		if (concept.getDesc() != null)
		{
			n.addProperty("description", concept.getDesc());
		}
		else
		{
			n.addProperty("description", null);
		}
		n.addProperty("level", concept.getLevel());
		//System.out.println(concept);
		return n;
	}
	*/
	
	public static void toNode2()
	{
		GraphManager newManager = NoGraph.getInstance().getGraphManager();
		List<Node> nodes = new ArrayList<Node>(alphaSortedConcepts.size());
		System.out.println("Starting Adding Nodes...");
		for(int i = 0; i < alphaSortedConcepts.size(); i++) //alphaSortedConcepts.size()
		{
			//System.out.println("checkpoint2");
			Concept concept = alphaSortedConcepts.get(i);
			System.out.println(concept);
			Node n = NoGraph.getInstance().newNode("concept");
			n.setProperty("conceptid", concept.getID());
			n.setProperty("name", concept.getName());
			
			if (concept.getDesc() != null)
			{
				n.setProperty("description", concept.getDesc());
			}
			else
			{
				n.setProperty("description", "null");
			
			}
			n.setProperty("level", concept.getLevel());
			n.setProperty("languages", concept.getLanguages());
			n.setProperty("translations", concept.getTranslations());
			//n.addProperty("ancestors",concept.getAncestors());
			//n.addProperty("children", concept.getChildren());
			nodes.add(n);
			System.out.println(n);
		}
		System.out.println("Now Ingesting Nodes");
		try 
		{
			newManager.ingestNodes(nodes);
		} 
		catch (NoGraphException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//create relationships
		System.out.println("Finished Nodes... Starting Relationships");
		try
		{
			long length = newManager.countNodes("concept");
			//List<Node> foundNodes = newManager.findNodes("type", "concept");
			List<Node> foundNodes = newManager.findNodes("concept", null, null, 100000);
			List<Relationship> createdRelationships = new ArrayList<Relationship>();
			Map<String, Node> byConceptId = byConceptID(foundNodes);
			//System.out.println(byConceptId);
			for(int i = 0; i < alphaSortedConcepts.size(); i++)
			{
				Concept tempC = alphaSortedConcepts.get(i);
				Node childNode = byConceptId.get(tempC.getID());
				if(childNode == null)
				{
					continue;
				}
				ArrayList<Concept> ancestors = tempC.getAncestors();
				for(int k = 0; k < ancestors.size(); k++)
				{
					Relationship rel = NoGraph.getInstance().newRelationship("childOf");
					rel.setNode1(childNode);
					Concept tempAnc = ancestors.get(k);
					Node tempAncNode = byConceptId.get(tempAnc.getID());
					if(tempAncNode == null)
					{
						continue;
					}
					rel.setNode2(tempAncNode);
					createdRelationships.add(rel);
					//newManager.saveRelationship(rel);
				}
			}
			System.out.println("Now Ingesting Relationships");
			newManager.ingestRelationships(createdRelationships);
			System.out.println(createdRelationships.size());
			/*
			for(int i = 0; i < length; i++)
			{
				Node n = foundNodes.get(i);
				//System.out.println(n.getProperty("ancestors"));
				int level = (int) n.getProperty("level");
				int l = 0;
				if(level == 1)
					l = level1S;
				if(level == 2)
					l = level2S;
				if(level == 3)
					l = level3S;
				if(level == 4)
					l = level4S;
				if(level == 5)
					l = level5S;
				Concept found = null;
				for(int j = l; j < alphaSortedConcepts.size(); j++)
				{
					if(alphaSortedConcepts.get(j).getName().equals(n.getProperty("name")))
					{
						found = alphaSortedConcepts.get(j);
						break;
					}
				}
				if(found != null)
				{
					ArrayList<Concept> ancestors = found.getAncestors();
					//System.out.println(n.getProperty("name"));
					//System.out.println(found);
					for(int k = 0; k < ancestors.size(); k++)
					{
						Concept tempC = ancestors.get(k);
						List<Node> tempNodes = newManager.findNodes("name", tempC.getName());
						if(tempNodes.size() > 0)
						{
							if(tempNodes.get(0).getProperty("name").equals(tempC.getName()))
							{
								//System.out.println(tempNodes);
								Relationship rel = NoGraph.getInstance().newRelationship("childOf");
								rel.setNode1(tempNodes.get(0));
								rel.setNode2(n);
								createdRelationships.add(rel);
							}
							else
							{
								System.out.println("Name Not Found");
							}
						}
					}
				}
				else
				{
					System.out.println("Concept " + n.getProperty("name") + " Not Found");
				}
				System.out.println("Finished " + i);
			}
			System.out.println("Now Ingesting Relationships");
			newManager.ingestRelationships(createdRelationships);
			*/
		}
		catch (NoGraphException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	public static void toConcept()
	{
		GraphManager newManager = NoGraph.getInstance().getGraphManager();
		try 
		{
			long length = newManager.countNodes("concept");
			for(int i = 0; i < length; i++)
			{
				Node n = newManager.findNodes("type", "concept").get(i);
				String id = (String) n.getProperty("id");
				String name = (String) n.getProperty("name");
				String desc = (String) n.getProperty("description");
				int level = (int) n.getProperty("level");
				Concept temp = new Concept(id, name, desc, level);
				nodeToConcept.add(temp);
			}
			for(int j = 0; j < nodeToConcept.size(); j++)
			{
				System.out.println(nodeToConcept.get(j));
			}
		} 
		catch (NoGraphException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	public static void printNodeConcepts()
	{
		GraphManager newManager = NoGraph.getInstance().getGraphManager();
		//System.out.println("checkpoint3");
		try 
		{
			//System.out.println(newManager.findNodes("name", "Plasmacytosis"));
			//System.out.println(newManager.findNodes("name", "Aerospace engineering"));
			System.out.println(newManager.countNodes("concept"));
			//System.out.println(newManager.findRelationships("type", "childOf", true));
			System.out.println(newManager.getRelationshipCountsByType());
			System.out.println(newManager.countRelationships("childOf"));
			//System.out.println("checkpoint4");
		} 
		catch (NoGraphException e) 
		{
			e.printStackTrace();
			System.out.println("problem");
		}
	}
	
	public static void deleteAll()
	{
		GraphManager newManager = NoGraph.getInstance().getGraphManager();
		try 
		{
			//List<String> someList = newManager.getNodeTypes();
			//long length = newManager.countNodes("concept");
			System.out.println("Deleting");
			
			newManager.deleteNodes(newManager.findNodes("type", "concept"));
			System.out.println(newManager.findNodes("type", "concept"));
			newManager.deleteRelationships(newManager.findRelationships("type", "childOf", false));
		} 
		catch (NoGraphException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static Map<String, Node> byConceptID(List<Node> listNodes)
	{
		Map<String, Node> newMap = new HashMap<String, Node>();
		for(int i = 0; i < listNodes.size(); i++)
		{
			Node tempNode = listNodes.get(i);
			newMap.put(tempNode.getString("conceptid"), tempNode);
		}
		return newMap;
	}
}
