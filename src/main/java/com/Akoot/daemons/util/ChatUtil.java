package com.Akoot.daemons.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import com.Akoot.daemons.OfflineUser;

public class ChatUtil 
{
	public static String[] rainbowseq = {"a","3","9","5","d","c","6","e"};
	private static Random random = new Random();
	private static Calendar cal;

	public static String color(String s)
	{
		s = s.replace("&h", randomColor());
		if(s.contains("&x"))
		{
			String toColor = getRegex("&x[^&]*", s);
			s = s.replace(toColor, rainbow(toColor.substring(2)));
		}
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public static String censor(String msg)
	{
		String censored = "";
		char[] replacer = {'!','@','#','$','%','%','^','&','*', '='};
		int last = -1;
		int r = last;
		for(int i = 0; i < msg.length(); i++)
		{
			r = random.nextInt(replacer.length - 1);
			while(r == last)
				r = random.nextInt(replacer.length - 1);
			censored += replacer[r];
			last = r;
		}
		return censored;
	}
	
	public static String getCurrentDate(String format)
	{
		cal = new GregorianCalendar();   
		return String.format(format, cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR));
	}

	public static String getCurrentDate()
	{
		return getCurrentDate("%d-%d-%d");
	}

	public static String getCurrentTime()
	{
		cal = new GregorianCalendar();   
		return String.format("%d:%02d %s", cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), (cal.get(Calendar.AM_PM) == Calendar.PM ? "PM" : "AM"));
	}

	public static String getRegex(String regex, String data)
	{
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(data);
		if(matcher.find()) data = matcher.group(0);
		return data;
	}

	public static String randomColor()
	{
		return "&" + allColors().get(random.nextInt(allColors().size() - 1));
	}

	public static List<String> allColors()
	{
		List<String> colors = new ArrayList<String>();
		for(ChatColor c: ChatColor.values())
			colors.add("&" + Character.toString(c.getChar()));
		colors.add("&h");
		colors.add("&x");
		return colors;
	}

	public static String itemName(ItemStack item)
	{
		return item.getType().name().toLowerCase().replace("_", " ");
	}

	public static String toProperString(String[] args)
	{
		String m = args[0];
		if(args.length > 1)
		{
			m = "";
			for(int i = 0; i < args.length; i++)
			{
				if(i < args.length - 1) m += args[i] + ", ";
				else m += "and " + args[i];
			}
		}
		return m.trim();
	}

	public static String toProperStringOr(String[] args)
	{
		String m = args[0];
		if(args.length > 1)
		{
			m = "";
			for(int i = 0; i < args.length; i++)
			{
				if(i < args.length - 1) m += args[i] + ", ";
				else m += "or " + args[i];
			}
		}
		return m.trim();
	}

	public static String getTime(int minutes)
	{
		String message = "";
		List<String> args = new ArrayList<String>();
		if(minutes >= 60)
		{
			int hours = minutes / 60;
			if(minutes > 60) minutes %= 60;
			args.add(hours + toPlural(hours, " hour"));
		}
		args.add(minutes + toPlural(minutes, " minute"));
		message = toString(args);
		return message;
	}
	public static String rainbow(String msg)
	{
		String rainbow = "";
		int i = random.nextInt(rainbowseq.length - 1);
		for(char c: msg.toCharArray())
		{
			if(i >= rainbowseq.length) i = 0;
			String ch = String.valueOf(c);
			if(!ch.equals(" "))
				ch = "&" + rainbowseq[i] + ch;
			rainbow += ch;
			i++;
		}
		return rainbow;
	}

	public static String toListString(List<String> args)
	{
		String msg = "";
		for(String s: args)
			msg += s + "\n";
		msg = msg.trim();
		return msg;
	}

	public static String toString(Object[] args)
	{
		String msg = "";
		for(Object s: args)
			msg += s + " ";
		msg = msg.trim();
		return msg;
	}

	public static String toPlural(int amt, String msg)
	{
		return msg + (amt == 1 ? "" : "s");
	}

	public static String toString(List<String> args)
	{
		String msg = "";
		for(String s: args)
			msg += s + " ";
		msg = msg.trim();
		return msg;
	}

	public static String[] replace(String[] a, int index, String replace)
	{
		String[] arr = new String[a.length];
		for(int i = 0; i < a.length; i++)
		{
			if(i == index) arr[i] = replace;
			else arr[i] = a[i];
		}
		return arr;
	}

	public static String getArgFor(String arg, String[] args)
	{
		for(int i = 0; i < args.length; i++)
		{
			String result = args[i];
			if(i + 1 < args.length)
			{
				if(args[i].equalsIgnoreCase(arg))
				{
					result = args[i + 1];
					if(result.startsWith("\""))
					{
						int k = i + 2;
						for(int j = i + 2; j < args.length; j++)
						{
							if(args[j].endsWith("\""))
							{
								k = j;
								break;
							}
						}
						result = toString(substr(args, i + 1, k + 1));
					}
					return result.replaceAll("\"", "");
				}
			}
		}
		return "";
	}

	public static boolean hasArgFor(String arg, String[] args)
	{
		for(int i = 0; i < args.length; i++)
		{
			if(i + 1 < args.length)
				if(args[i].equalsIgnoreCase(arg)) return true;
		}
		return false;
	}

	public static boolean contains(String arg, String[] args)
	{
		for(int i = 0; i < args.length; i++) if(args[i].equalsIgnoreCase(arg)) return true;
		return false;
	}

	public static String[] removeFirst(String[] a)
	{
		return Arrays.copyOfRange(a, 1, a.length);
	}

	public static String[] substr(String[] a, int startIndex, int endIndex)
	{
		return Arrays.copyOfRange(a, startIndex, endIndex);
	}

	public static String[] substr(String[] a, int startIndex)
	{
		return Arrays.copyOfRange(a, startIndex, a.length);
	}

	public static List<UUID> getUUIDSFromStrings(List<String> strings)
	{
		List<UUID> uuids = new ArrayList<UUID>();
		for(String s: strings)
			uuids.add(UUID.fromString(s));
		return uuids;
	}

	public static List<OfflineUser> getOfflineUsers(List<UUID> uuids)
	{
		List<OfflineUser> users = new ArrayList<OfflineUser>();
		for(UUID uuid: uuids)
			users.add(new OfflineUser(uuid));
		return users;
	}

	public static List<UUID> getUUIDSFromUsers(List<OfflineUser> users)
	{
		List<UUID> uuids = new ArrayList<UUID>();
		for(OfflineUser user: users)
			uuids.add(user.getUUID());
		return uuids;
	}

	public static List<String> getUUIDSAsStrings(List<UUID> uuids)
	{
		List<String> strings = new ArrayList<String>();
		for(UUID uuid: uuids)
			strings.add(uuid.toString());
		return strings;
	}
}
