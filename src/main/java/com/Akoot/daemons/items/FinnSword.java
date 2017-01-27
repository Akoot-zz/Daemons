package com.Akoot.daemons.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class FinnSword extends CustomWeapon
{
	public FinnSword()
	{
		this.material = Material.GOLD_SWORD;
		this.displayName = ChatColor.RED + "Scarlet";
		this.lore.add(ChatColor.AQUA + "\"The golden sword of battle\"");
		this.durability = 1;
	}

	@Override
	public void onLeftClick()
	{
		wielder.getWorld().playSound(wielder.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0F, 1.0F);
	}

	@Override
	public void onAttack(LivingEntity entity)
	{
		wielder.getWorld().playSound(wielder.getLocation(), Sound.BLOCK_NOTE_HARP, 1.5F, 0.5F + (float) (Math.random() * 0.5));
		entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 5, 1));
		entity.damage(Math.random() * 3.0);
	}
}
