package org.discordmusic.commands.subcommands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.discordmusic.commands.CommandArguments;
import org.discordmusic.commands.DCommand;

import java.lang.management.ManagementFactory;

public class UptimeCommand extends DCommand {
	public UptimeCommand(){
		super("uptime");
	}

	@Override
	public void execute(CommandArguments arguments){
		final SlashCommandEvent event = arguments.getEvent();
		final MessageChannel channel = arguments.getChannel();
		final long
			duration = ManagementFactory.getRuntimeMXBean().getUptime(),
			days = duration / 86400000L % 30,
			hours = duration / 3600000L % 24,
			minutes = duration / 60000L % 60,
			seconds = duration / 1000L % 60;

		event.replyFormat("Estou trabalhando há exatamente %d dias, %d horas, %d minutos e %d segundos.", days, hours, minutes, seconds).queue();
	}
}
