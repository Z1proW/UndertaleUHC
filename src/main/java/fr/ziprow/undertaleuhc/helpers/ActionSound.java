package fr.ziprow.undertaleuhc.helpers;

import org.bukkit.Location;
import org.bukkit.Sound;

public enum ActionSound
{

	OPEN(Sound.CHEST_OPEN),
	MODIFY(Sound.ANVIL_USE),
	SELECT(Sound.LEVEL_UP),
	CLICK(Sound.CLICK),
	POP(Sound.CHICKEN_EGG_POP),
	BREAK(Sound.ANVIL_LAND),
	ERROR(Sound.BAT_DEATH),
	PING(Sound.NOTE_PLING);

	private final Sound sound;

	ActionSound(Sound sound)
	{
		this.sound = sound;
	}

	public Sound getSound()
	{
		return sound;
	}

	public void playSound(Location loc)
	{
		playSound(sound, loc);
	}

	public static void playSound(Sound sound, Location loc)
	{
		loc.getWorld().playSound(loc, sound, 1f, 1f);
	}

}
