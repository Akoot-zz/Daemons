package com.Akoot.daemons.items;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;

public class CustomWeapon extends CustomItem
{
	protected List<UUID> owners = new ArrayList<UUID>();
	
	public void onEntityDeath(EntityDeathEvent event) {}
	
	public void onAttack(LivingEntity entity) {}
}
