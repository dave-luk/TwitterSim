package userJTree;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.tree.TreeNode;

import twitterSim.UsersComposite;
import visitors.Visitor;

public class UserTreeNode implements TreeNode
{
	private UserTreeNode		parent;
	private List<UserTreeNode>	children = new ArrayList<>();
	private boolean				allowChildren;
	private UsersComposite				data;

	public UserTreeNode(UsersComposite data, boolean allowChildren)
	{
		this.data = data;
		this.allowChildren = allowChildren;
	}
	
	@Override
	public TreeNode getChildAt(int childIndex)
	{
		return children.get(childIndex);
	}

	@Override
	public int getChildCount()
	{
		return children.size();
	}

	@Override
	public TreeNode getParent()
	{
		return parent;
	}

	@Override
	public int getIndex(TreeNode node)
	{
		return children.indexOf(node);
	}

	@Override
	public boolean getAllowsChildren()
	{
		return allowChildren;
	}

	@Override
	public boolean isLeaf()
	{
		return !allowChildren;
	}

	@Override
	public Enumeration<UserTreeNode> children()
	{
		return new IteratorEnumeration<>(children.iterator());
	}
	
	public void add(UserTreeNode child)
	{
		if(allowChildren){
			child.parent = this;
			children.add(child);
			
		}
	}

	/**
	 * @return the data
	 */
	public UsersComposite getData()
	{
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(UsersComposite data)
	{
		this.data = data;
	}

	private class IteratorEnumeration<E> implements Enumeration<E>
	{
	    private final Iterator<E> iterator;

	    public IteratorEnumeration(Iterator<E> iterator)
	    {
	        this.iterator = iterator;
	    }

	    public E nextElement() {
	        return iterator.next();
	    }

	    public boolean hasMoreElements() {
	        return iterator.hasNext();
	    }

	}
	
	public void acceptVistor(Visitor v) {
		for(UserTreeNode c : children)
		{
			c.acceptVistor(v);
		}
		data.acceptVistor(v);
	}
	
	@Override
	public String toString()
	{
		return data.toString();
	}
}
