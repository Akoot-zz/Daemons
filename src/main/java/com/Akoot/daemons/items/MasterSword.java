package com.Akoot.daemons.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MasterSword extends CustomWeapon
{
	public MasterSword()
	{
		this.material = Material.DIAMOND_SWORD;
		this.displayName = ChatColor.LIGHT_PURPLE + "Master Sword";
		this.lore.add(ChatColor.AQUA + "\"yes chef\"");
		this.durability = 16;
	}

	@Override
	public void onAttack(LivingEntity entity)
	{
		if(Math.random() <= 0.25)
		{
			entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 5, 1));
			entity.damage(5.0 + (Math.random() * 2.5));
			entity.getWorld().playSound(wielder.getLocation(), Sound.BLOCK_GLASS_BREAK, 2.0F, 1.0F);
			entity.getWorld().playSound(wielder.getLocation(), Sound.BLOCK_GLASS_BREAK, 2.0F, 0.5F);
			entity.playEffect(EntityEffect.DEATH);
		}
	}

	@Override
	public void onEntityDeath(EntityDeathEvent event)
	{
		List<ItemStack> drops = new ArrayList<ItemStack>();
		for(ItemStack drop: event.getDrops()) drops.add(drop);
		if(wielder.getType() == EntityType.PLAYER)
			((Player) wielder).giveExp(event.getDroppedExp());
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> deathExplosion(event.getEntity().getLocation()), 20L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> dropItems(event.getEntity().getLocation(), drops), 47L);
		event.getDrops().clear();
		event.setDroppedExp(0);
	}

	private void deathExplosion(Location loc)
	{
		wielder.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
		wielder.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 0);
	}

	private void dropItems(Location loc, List<ItemStack> drops)
	{
		for(ItemStack drop: drops) wielder.getWorld().dropItem(loc, drop);
	}
}

