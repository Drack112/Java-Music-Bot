package org.discordmusic.commands.subcommands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.discordmusic.commands.CommandArguments;
import org.discordmusic.commands.DCommand;
import org.discordmusic.commands.music.GuildMusicManager;
import org.discordmusic.commands.music.PlayerManager;

public class LeaveCommand extends DCommand {
	public LeaveCommand() {
		super("leave", "l");
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public void execute(CommandArguments arguments) {
		final TextChannel channel = arguments.getTextChannel();
		final Member self = channel.getGuild().getSelfMember();
		final GuildVoiceState state = self.getVoiceState();
		final SlashCommandEvent event = arguments.getEvent();

		if (state.inVoiceChannel()) {
			event.reply("Eu já estou na sala!").queue();
			return;
		}

		final Guild guild = arguments.getGuild();
		final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);

		musicManager.scheduler.clearQueue();
		musicManager.scheduler.getPlayer().stopTrack();

		final AudioManager audioManager = arguments.getGuild().getAudioManager();
		audioManager.closeAudioConnection();

		event.reply("O bot saiu do canal de voz").queue();
	}
}
