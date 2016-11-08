package twitterSim;

import visitors.Visitor;

public interface UsersComposite
{
	public String getName();
	public void setName(String name);
	public String getId();
	public void acceptVistor(Visitor v);

}
