package visitors;

import twitterSim.Group;
import twitterSim.User;

public class GroupCountVisitor implements Visitor
{
	private int count = 0;

	@Override
	public void visitUser(User u)
	{
		//Do nothing
	}

	@Override
	public void visitGroup(Group g)
	{
		count++;
	}

	public int getCount()
	{
		return count;
	}
}
