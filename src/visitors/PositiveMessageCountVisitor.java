package visitors;

import twitterSim.Group;
import twitterSim.Tweet;
import twitterSim.User;

public class PositiveMessageCountVisitor implements Visitor
{
	private int count = 0;
	@Override
	public void visitUser(User u)
	{
		for(Tweet t: u.getTweets())
		{
			if(isPositive(t))
			{
				count++;
			}
		}
	}

	private boolean isPositive(Tweet t)
	{
		return t.getMessage().contains(":)");
	}

	public int getCount()
	{
		return count;
	}

	@Override
	public void visitGroup(Group g)
	{
		//Do nothing
	}

}
