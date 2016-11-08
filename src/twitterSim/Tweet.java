package twitterSim;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class Tweet implements Comparable<Tweet>
{
	private String message;
	private Date postTime;
	
	public Tweet(String msg)
	{
		message = msg;
		postTime = Date.from(Instant.now());
	}
	
	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}
	/**
	 * @return the postTime
	 */
	public Date getPostTime()
	{
		return postTime;
	}
	
	@Override
	public String toString()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		return dateFormat.format(postTime) + " > " + message;
	}

	@Override
	public int compareTo(Tweet o)
	{
		//make older things place after
		return o.getPostTime().compareTo(this.postTime);
	}
}
