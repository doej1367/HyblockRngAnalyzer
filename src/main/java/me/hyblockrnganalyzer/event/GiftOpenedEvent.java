package me.hyblockrnganalyzer.event;

import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class GiftOpenedEvent extends Event {

	private PlaySoundEvent soundEvent;

	public GiftOpenedEvent(PlaySoundEvent event) {
		this.soundEvent = event;
	}

	public PlaySoundEvent getSoundEvent() {
		return soundEvent;
	}
}
