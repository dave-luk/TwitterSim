package visitors;

import twitterSim.Group;
import twitterSim.User;

public interface Visitor
{
	void visitUser(User u);
	void visitGroup(Group g);
}
