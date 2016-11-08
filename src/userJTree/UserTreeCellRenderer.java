package userJTree;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import twitterSim.User;

public class UserTreeCellRenderer extends DefaultTreeCellRenderer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6308021902996986241L;
	
	private Icon userIcon;
	private Icon groupIcon;
	
	public UserTreeCellRenderer()
	{
		userIcon = new ImageIcon(getScaledImage(new ImageIcon("src/User.png").getImage() , 20, 20));
		
		groupIcon = new ImageIcon(getScaledImage(new ImageIcon("src/Group.png").getImage() , 20, 20));
	}
	
	private Image getScaledImage(Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
	        boolean leaf, int row, boolean hasFocus)
	{
		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		if(((UserTreeNode)value).getData() instanceof User)
		{
			setIcon(userIcon);
		}
		else
		{
			setIcon(groupIcon);
		}
		return this;
	}

}
