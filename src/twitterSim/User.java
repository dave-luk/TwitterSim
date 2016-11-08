package twitterSim;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import visitors.Visitor;

public class User extends Observable implements UsersComposite, Observer
{
	private String				id;
	private String				groupId;
	private String				name;
	private List<User>			following	= new ArrayList<>();
	private List<User>			followers	= new ArrayList<>();
	private List<Tweet>			tweets		= new ArrayList<>();

	private UserProfileFrame	userProfile;

	public User(String name)
	{
		this();
		this.name = name;
		userProfile = new UserProfileFrame();
	}

	public User()
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
		notifyObservers();
	}

	public String getId()
	{
		return id.toString();
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId()
	{
		return groupId;
	}

	/**
	 * @param groupId
	 *            the groupId to set
	 */
	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}

	public List<User> getFollowing()
	{
		return following;
	}

	public void addFollowing(User u)
	{
		if (!following.contains(u) && !u.equals(this))
		{
			following.add(u);
			u.addFollower(this);
			u.addObserver(this);
		}
	}

	public List<User> getFollowers()
	{
		return followers;
	}

	public void addFollower(User u)
	{
		if (!followers.contains(u))
		{
			followers.add(u);
		}
	}

	public List<Tweet> getTweets()
	{
		return tweets;
	}

	public void addTweet(Tweet t)
	{
		tweets.add(t);
		notifyObservers();
	}

	public String toString()
	{
		return this.getName();
	}

	@Override
	public void update(Observable o, Object arg)
	{
		userProfile.update();
	}

	@Override
	public boolean equals(Object o)
	{
		return (o instanceof User && ((User) o).id.equals(this.id));
	}

	public void toggleProfile()
	{
		if (userProfile == null)
		{
			userProfile = new UserProfileFrame();
		}
		userProfile.setVisible(!userProfile.isVisible());
	}

	public class UserProfileFrame extends JFrame
	{

		private static final int		WIDTH					= 400;
		private static final int		HEIGHT					= 600;
		private static final int		OFFSET_X				= 200;
		private static final int		OFFSET_Y				= 200;

		private static final long		serialVersionUID		= 6330404217846170244L;

		private JList<User>				followingList			= new JList<>();
		private JList<Tweet>			newsFeed				= new JList<>();
		private DefaultListModel<User>	defaultUserListModel	= new DefaultListModel<>();
		private DefaultListModel<Tweet>	defaultTweetListModel	= new DefaultListModel<>();

		public UserProfileFrame()
		{
			super(name + "'s Profile");
			setSize(WIDTH, HEIGHT);
			setLocation(OFFSET_X, OFFSET_Y);
			setDefaultCloseOperation(HIDE_ON_CLOSE);

			// Construction
			JTextField followUserInput = new JTextField();
			JButton followUserBtn = new JButton("Follow User");
			followUserBtn.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					String input = followUserInput.getText().trim();
					for (User u : ControlPanel.getInstance().getUsers())
					{
						if (u.getId().equals(input))
						{
							addFollowing(u);
							updateFollowingList();
							followUserInput.setText("");
							updateNewsFeed();
						}
					}
				}
			});

			followUserInput.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyPressed(KeyEvent e)
				{
					super.keyTyped(e);
					switch (e.getKeyCode())
					{
						case KeyEvent.VK_ENTER:
							followUserBtn.doClick();
						case KeyEvent.VK_ESCAPE:
							followUserInput.setText("");
					}
				}
			});

			updateFollowingList();

			JLabel followingLabel = new JLabel("Following");

			JTextArea tweetInput = new JTextArea();
			tweetInput.setBorder(new LineBorder(new Color(0, 0, 0)));
			JButton tweetBtn = new JButton("Tweet");
			tweetBtn.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					String str = tweetInput.getText();
					if (str.length() != 0 && str.length() <= 140)
					{
						addTweet(new Tweet(tweetInput.getText()));
						tweetInput.setText("");
						updateNewsFeed();
					}
				}
			});

			JLabel newsFeedLabel = new JLabel("NewsFeed");

			updateNewsFeed();

			tweetInput.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyPressed(KeyEvent e)
				{
					super.keyTyped(e);
					switch (e.getKeyCode())
					{
						case KeyEvent.VK_ESCAPE:
							tweetInput.setText("");
					}
				}
			});

			// assemble
			GridBagLayout gridBagLayout = new GridBagLayout();
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.fill = GridBagConstraints.BOTH;
			setLayout(gridBagLayout);
			constraints.weightx = 1;
			// constraints.weighty = 1;
			constraints.gridheight = 1;

			add(followUserInput, constraints);
			constraints.gridwidth = GridBagConstraints.REMAINDER;
			constraints.weightx = 0;
			add(followUserBtn, constraints);
			add(followingLabel, constraints);
			constraints.weightx = 1;
			constraints.weighty = 1;
			add(new JScrollPane(followingList), constraints);
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			add(tweetInput, constraints);
			constraints.gridwidth = GridBagConstraints.REMAINDER;
			constraints.weightx = 0;
			add(tweetBtn, constraints);
			add(newsFeedLabel, constraints);
			constraints.weighty = 1;
			add(new JScrollPane(newsFeed), constraints);
		}

		private void updateFollowingList()
		{
			defaultUserListModel.clear();
			for (User u : following)
			{
				defaultUserListModel.addElement(u);
			}

			followingList.setModel(defaultUserListModel);
		}

		private void updateNewsFeed()
		{
			defaultTweetListModel.clear();
			List<Tweet> l = new ArrayList<>();
			for (User u : following)
			{
				l.addAll(u.getTweets());
			}
			l.addAll(tweets);
			Collections.sort(l);
			for (Tweet t : l)
			{
				defaultTweetListModel.addElement(t);
			}
			newsFeed.setModel(defaultTweetListModel);
		}

		public void update()
		{
			updateNewsFeed();
		}
	}

	@Override
	public void acceptVistor(Visitor v)
	{
		v.visitUser(this);
	}

}
