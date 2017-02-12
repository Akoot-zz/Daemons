package com.Akoot.daemons.test;

public class Test
{
	public static void main(String[] args)
	{
		int minVal = 10, maxVal = 20;
		for(int i = 0; i < 10; i++) System.out.println(minVal + (int)(Math.random() * ((maxVal - minVal) + 1)));
	}
}
