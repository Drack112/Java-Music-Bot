package org.discordmusic.events;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;


public class ReadyEvent implements EventListener {
	@Override
	@SuppressWarnings("ConstantConditions")
	public void onEvent(GenericEvent event) {
		if (event instanceof net.dv8tion.jda.api.events.ReadyEvent) {

			System.out.println("\u001B[35m" +
				"██████╗      ██╗    ███████╗███╗   ██╗ █████╗ ██╗  ██╗███████╗\n" +
				"██╔══██╗     ██║    ██╔════╝████╗  ██║██╔══██╗██║ ██╔╝██╔════╝\n" +
				"██║  ██║     ██║    ███████╗██╔██╗ ██║███████║█████╔╝ █████╗  \n" +
				"██║  ██║██   ██║    ╚════██║██║╚██╗██║██╔══██║██╔═██╗ ██╔══╝  \n" +
				"██████╔╝╚█████╔╝    ███████║██║ ╚████║██║  ██║██║  ██╗███████╗\n" +
				"╚═════╝  ╚════╝     ╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝\n" +
				"                                                              ");

			System.out.println("\u001B[35mO DJ está de pé!");
			System.out.println("\u001B[35mAtualmente está em tocando músicas em : " + ((net.dv8tion.jda.api.events.ReadyEvent) event).getGuildAvailableCount() + " \u001B[35mservidores");
		}
	}
}
