package twitterSim;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import userJTree.UserTreeCellRenderer;
import userJTree.UserTreeModel;
import userJTree.UserTreeModelListener;
import userJTree.UserTreeNode;
import visitors.GroupCountVisitor;
import visitors.MessageCountVisitor;
import visitors.PositiveMessageCountVisitor;
import visitors.UserCountVistor;

public class ControlPanel
{
	private static ControlPanel		INSTANCE;
	private java.util.List<User>	users		= new ArrayList<>();
	private java.util.List<Group>	userGroups	= new ArrayList<>();

	private ControlPanelFrame		controlPanelFrame;

	private ControlPanel()
	{
		controlPanelFrame = new ControlPanelFrame();
	}

	public static synchronized ControlPanel getInstance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new ControlPanel();
		}
		return INSTANCE;
	}

	public void open()
	{
		controlPanelFrame.setVisible(true);
	}

	public java.util.List<User> getUsers()
	{
		return users;
	}

	public java.util.List<Group> getUserGroups()
	{
		return userGroups;
	}

	private class ControlPanelFrame extends JFrame
	{
		/**
		 * 
		 */
		private static final long	serialVersionUID	= -6341898097854481311L;

		private static final int	WIDTH				= 800;
		private static final int	HEIGHT				= 400;
		private static final int	OFFSET_X			= 100;
		private static final int	OFFSET_Y			= 100;
		private InnerPanel			innerPanel;
		private JTree				tree;
		private UserTreeModel		treeModel;
		private UserTreeNode		selection;

		public ControlPanelFrame()
		{
			// frame and window construction
			super("Control Panel");
			setSize(WIDTH, HEIGHT);
			setLocation(OFFSET_X, OFFSET_Y);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			// treeView construction
			UserTreeNode root = new UserTreeNode(new Group("Root"), true);
			treeModel = new UserTreeModel(root);
			Group gp1 = new Group("CS 356 Students");
			userGroups.add(gp1);
			root.add(new UserTreeNode(gp1, true));
			User u1 = new User("Dr. Yu Sun");
			users.add(u1);
			root.add(new UserTreeNode(u1, false));
			tree = new JTree(treeModel);
			tree.setCellRenderer(new UserTreeCellRenderer());
			treeModel.addTreeModelListener(new UserTreeModelListener());
			tree.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					int row = tree.getRowForLocation(e.getX(), e.getY());
					if (row == -1) // When user clicks on the "empty surface"
					    tree.clearSelection();
				}
			});
			tree.addTreeSelectionListener(new TreeSelectionListener()
			{
				@Override
				public void valueChanged(TreeSelectionEvent e)
				{
					if (tree.getLastSelectedPathComponent() != null)
					{
						selection = ((UserTreeNode) tree.getLastSelectedPathComponent());

					}
					else
					{
						selection = null;
					}
					innerPanel.update();
				}
			});

			JScrollPane treeView = new JScrollPane(tree);

			// inner Panel construction
			innerPanel = new InnerPanel();

			// Label Construction
			JLabel treeLabel = new JLabel("Structures");
			JLabel controlLabel = new JLabel("Control Panel");
			// assemble
			GridBagLayout gridBagLayout = new GridBagLayout();
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.fill = GridBagConstraints.BOTH;
			setLayout(gridBagLayout);

			constraints.weightx = 0.4;
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			add(treeLabel, constraints);
			constraints.gridwidth = GridBagConstraints.REMAINDER;
			constraints.weightx = 0.6;
			add(controlLabel, constraints);
			constraints.weighty = 1;
			constraints.weightx = 0.4;
			constraints.gridwidth = 1;
			constraints.gridheight = GridBagConstraints.REMAINDER;
			add(treeView, constraints);
			constraints.gridheight = 1;
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 0.6;
			add(innerPanel, constraints);
		}

		private class InnerPanel extends JPanel
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 9070336044908135221L;

			JTextField					userId;

			JTextField					groupId;

			private InnerPanel()
			{
				// Data Display
				userId = new JTextField("UserID: ");
				userId.setPreferredSize(new Dimension(WIDTH, 30));
				userId.setEditable(false);
				groupId = new JTextField("GroupID: ");
				groupId.setEditable(false);
				groupId.setPreferredSize(new Dimension(WIDTH, 30));

				// Button construction
				JButton addUserBtn = new JButton("Add User");
				addUserBtn.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if (selection != null)
						{
							new NewItemFrame(new User(), (selection.getData() instanceof Group) ? selection
							        : (UserTreeNode) selection.getParent());
						}
						else
						{
							new NewItemFrame(new User(), (UserTreeNode) treeModel.getRoot());
						}
					}
				});

				JButton modifyUserBtn = new JButton("Modify User");
				modifyUserBtn.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if (selection != null && selection.getData() instanceof User)
						{
							new NewItemFrame(new User(), selection.getData().getName(), selection);
						}
					}
				});

				JButton addUserGroupBtn = new JButton("Add Group");
				addUserGroupBtn.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if (selection != null)
						{
							new NewItemFrame(new Group(), (selection.getData() instanceof Group) ? selection
							        : (UserTreeNode) selection.getParent());
						}
						else
						{
							new NewItemFrame(new Group(), (UserTreeNode) treeModel.getRoot());
						}
					}
				});

				JButton modifyUserGroupBtn = new JButton("Modify Group");
				modifyUserGroupBtn.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if (selection != null)
						{
							UserTreeNode target = (selection.getData() instanceof Group) ? selection
							        : (UserTreeNode) selection.getParent();
							new NewItemFrame(new Group(), target.getData().getName(), target);
						}
					}
				});

				JButton openUserProfileBtn = new JButton("Open User Profile");
				openUserProfileBtn.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if (selection != null && selection.getData() instanceof User)
						{
							((User) selection.getData()).toggleProfile();
						}
					}
				});

				JButton userTotalBtn = new JButton("Show User Total");
				userTotalBtn.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						UserCountVistor ucv = new UserCountVistor();
						((UserTreeNode) treeModel.getRoot()).acceptVistor(ucv);
						
						JOptionPane.showMessageDialog(controlPanelFrame, "Count: " + ucv.getCount(), "User Count",
						        JOptionPane.INFORMATION_MESSAGE);
					}
				});

				JButton groupTotalBtn = new JButton("Show Group Total");
				groupTotalBtn.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						GroupCountVisitor gcv = new GroupCountVisitor();
						((UserTreeNode) treeModel.getRoot()).acceptVistor(gcv);

						JOptionPane.showMessageDialog(controlPanelFrame, "Count: " + gcv.getCount(), "Group Count",
						        JOptionPane.INFORMATION_MESSAGE);
					}
				});

				JButton msgTotalBtn = new JButton("Show Message Total");
				msgTotalBtn.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						MessageCountVisitor mcv = new MessageCountVisitor();
						((UserTreeNode) treeModel.getRoot()).acceptVistor(mcv);
						JOptionPane.showMessageDialog(controlPanelFrame, "Count: " + mcv.getCount(), "Message Count",
						        JOptionPane.INFORMATION_MESSAGE);
					}
				});

				JButton posPercBtn = new JButton("Show Positive Message Total");
				posPercBtn.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						PositiveMessageCountVisitor pmcv = new PositiveMessageCountVisitor();
						((UserTreeNode) treeModel.getRoot()).acceptVistor(pmcv);
						JOptionPane.showMessageDialog(controlPanelFrame, "Count: " + pmcv.getCount(), "Positive Message Count",
						        JOptionPane.INFORMATION_MESSAGE);
					}
				});

				// assemble
				GridBagLayout gridBagLayout = new GridBagLayout();
				GridBagConstraints constraints = new GridBagConstraints();
				constraints.fill = GridBagConstraints.BOTH;
				setLayout(gridBagLayout);

				constraints.weightx = 1;
				constraints.weighty = 0.25;
				constraints.gridwidth = GridBagConstraints.REMAINDER;
				add(userId, constraints);
				add(groupId, constraints);
				constraints.gridwidth = 1;
				add(addUserBtn, constraints);
				constraints.gridwidth = GridBagConstraints.REMAINDER;
				add(modifyUserBtn, constraints);
				constraints.gridwidth = 1;
				add(addUserGroupBtn, constraints);
				constraints.gridwidth = GridBagConstraints.REMAINDER;
				add(modifyUserGroupBtn, constraints);
				constraints.weighty = 1;
				constraints.gridheight = 1;
				add(openUserProfileBtn, constraints);
				constraints.weighty = 0.25;
				constraints.gridwidth = 1;
				add(userTotalBtn, constraints);
				constraints.gridwidth = GridBagConstraints.REMAINDER;
				add(groupTotalBtn, constraints);
				constraints.gridwidth = 1;
				constraints.gridheight = GridBagConstraints.REMAINDER;
				add(msgTotalBtn, constraints);
				constraints.gridwidth = GridBagConstraints.REMAINDER;
				add(posPercBtn, constraints);
			}

			private void update()
			{
				if (selection != null)
				{
					if (selection.getData() instanceof User)
					{
						userId.setText("User ID: " + selection.getData().getId());
						groupId.setText("GroupID: " + ((selection.getParent() != null)
						        ? ((UsersComposite) ((UserTreeNode) selection.getParent()).getData()).getId() : ""));
					}
					else
					{
						userId.setText("User ID: ");
						groupId.setText("GroupID: " + selection.getData().getId());
					}
				}
				else
				{
					userId.setText("User ID: ");
					groupId.setText("GroupID: ");
				}
			}
		}

		private class NewItemFrame extends JFrame
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 3874239818700278555L;
			private static final int	WIDTH				= 300;
			private static final int	HEIGHT				= 120;

			private JTextField			enteredName;
			private JButton				confirmBtn;

			private NewItemFrame(UsersComposite type, UserTreeNode target)
			{
				super("Add New " + type.getClass().getSimpleName());
				setSize(WIDTH, HEIGHT);
				setLocation(ControlPanelFrame.WIDTH / 2 - WIDTH / 2 + ControlPanelFrame.OFFSET_X,
				        ControlPanelFrame.HEIGHT / 2 - HEIGHT / 2 + ControlPanelFrame.OFFSET_Y);
				setDefaultCloseOperation(DISPOSE_ON_CLOSE);

				setLayout(new BorderLayout());

				JPanel namePanel = new JPanel();
				namePanel.add(new JLabel("Name"));
				enteredName = new JTextField();
				enteredName.setPreferredSize(new Dimension(150, 30));
				enteredName.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyPressed(KeyEvent e)
					{
						super.keyTyped(e);
						switch (e.getKeyCode())
						{
							case KeyEvent.VK_ENTER:
								confirmBtn.doClick();
								break;
							case KeyEvent.VK_ESCAPE:
								NewItemFrame.this.dispose();
						}
					}
				});
				namePanel.add(enteredName);
				add(namePanel, BorderLayout.NORTH);

				JPanel responsePanel = new JPanel();
				confirmBtn = new JButton("Confirm");
				confirmBtn.setActionCommand("add");
				confirmBtn.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if (e.getActionCommand().equals("add"))
						{
							type.setName(enteredName.getText());
							if (type instanceof User)
							{
								users.add((User) type);
							}
							else
							{
								userGroups.add((Group) type);
							}
							UserTreeNode newNode = new UserTreeNode(type, !(type instanceof User));

							tree.expandPath(treeModel.addNode(newNode, target));
						}
						else
						{
							type.setName(enteredName.getText());
							target.setData(type);
							treeModel.nodeChanged(target);
						}
						dispose();
					}
				});
				JButton cancelBtn = new JButton("Cancel");
				cancelBtn.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						dispose();
					}
				});
				responsePanel.add(confirmBtn);
				responsePanel.add(cancelBtn);
				add(responsePanel, BorderLayout.SOUTH);

				setVisible(true);
			}

			private NewItemFrame(UsersComposite type, String value, UserTreeNode target)
			{
				this(type, target);
				this.setTitle("Modify " + type.getClass().getSimpleName());
				confirmBtn.setActionCommand("edit");
				enteredName.setText(value);
			}
		}

	}
}
