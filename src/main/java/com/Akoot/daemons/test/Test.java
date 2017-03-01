package com.Akoot.daemons.test;

import java.util.Random;

public class Test
{
	public static void main(String[] args)
	{
		long now;
		int size = 100;
		now = System.nanoTime();
		System.out.println((int)(Math.floor(Math.random() * (size - 1))));
		System.out.println("took " + (System.nanoTime() - now) + "ns");
		now = System.nanoTime();
		Random random = new Random();
		System.out.println(random.nextInt(size - 1));
		System.out.println("took " + (System.nanoTime() - now) + "ns");
	}
}
