package twitterSim;

import java.util.UUID;

import visitors.Visitor;

public class Group implements UsersComposite
{
	private String id;
	private String name;

	public Group(String name)
	{
		this();
		this.name = name;
	}
	
	public Group()
	{
		this.id = UUID.randomUUID().toString();
	}

	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}


	public String getId()
	{
		return id;
	}
	
	public String toString()
	{
		return this.getName();
	}

	@Override
	public void acceptVistor(Visitor v)
	{
		v.visitGroup(this);
	}
}
