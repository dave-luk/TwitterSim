package visitors;

import twitterSim.Group;
import twitterSim.User;

public class UserCountVistor implements Visitor
{
	private int count = 0;
	@Override
	public void visitUser(User u)
	{
		count++;
	}

	@Override
	public void visitGroup(Group g)
	{
		//Do nothing
	}

	public int getCount()
	{
		return count;
	}	
}
