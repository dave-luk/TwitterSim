package userJTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import twitterSim.UsersComposite;

public class UserTreeModel implements TreeModel
{
	private UserTreeNode			root;
	private List<TreeModelListener>	listeners	= new ArrayList<>();

	public UserTreeModel(UserTreeNode root)
	{
		this.root = root;
	}
	
	public TreePath addNode(UserTreeNode node, UserTreeNode target)
	{
		Stack<UserTreeNode> stk = new Stack<>();
		UserTreeNode cursor = target;
		while(cursor != null)
		{
			stk.push((UserTreeNode)cursor);
			cursor = (UserTreeNode) cursor.getParent();
		}
		Object[] paths = new Object[stk.size()];
		int i=0;
		while(stk.size() >0)
		{
			paths[i] = stk.pop();
			i++;
		}
		TreePath path = new TreePath(paths);
		target.add(node);
		fireTreeModified(path);
		return path;
	}
	
	public void nodeChanged(UserTreeNode node)
	{
		Stack<UserTreeNode> stk = new Stack<>();
		UserTreeNode cursor = node;
		while(cursor != null)
		{
			stk.push((UserTreeNode)cursor);
			cursor = (UserTreeNode) cursor.getParent();
		}
		Object[] paths = new Object[stk.size()];
		int i=0;
		while(stk.size() >0)
		{
			paths[i] = stk.pop();
			i++;
		}
		TreePath path = new TreePath(paths);
		fireTreeModified(path);
	}

	@Override
	public Object getRoot()
	{
		return root;
	}

	@Override
	public Object getChild(Object parent, int index)
	{
		return ((UserTreeNode) parent).getChildAt(index);
	}

	@Override
	public int getChildCount(Object parent)
	{
		return ((UserTreeNode) parent).getChildCount();
	}

	@Override
	public boolean isLeaf(Object node)
	{
		return ((UserTreeNode) node).isLeaf();
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue)
	{
		UserTreeNode node = (UserTreeNode) path.getLastPathComponent();
		node.setData((UsersComposite) newValue);
	}

	@Override
	public int getIndexOfChild(Object parent, Object child)
	{
		return ((UserTreeNode) parent).getIndex((TreeNode) child);
	}

	@Override
	public void addTreeModelListener(TreeModelListener l)
	{
		listeners.add(l);
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l)
	{
		listeners.remove(l);
	}

	protected void fireTreeModified(TreePath p)
	{
		TreeModelEvent event = new TreeModelEvent(this, p);
		for(TreeModelListener tml : listeners)
		{
			tml.treeStructureChanged(event);;
		}
	}
}
