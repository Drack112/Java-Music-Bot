package org.discordmusic.commands.subcommands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.discordmusic.commands.CommandArguments;
import org.discordmusic.commands.DCommand;
import org.discordmusic.commands.music.GuildMusicManager;
import org.discordmusic.commands.music.PlayerManager;

public class StopCommand extends DCommand {
	public StopCommand() {
		super("stop", "s");
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public void execute(CommandArguments arguments) {
		final TextChannel channel = arguments.getTextChannel();
		final Member self = channel.getGuild().getSelfMember();
		final GuildVoiceState voiceState = self.getVoiceState();
		final SlashCommandEvent event = arguments.getEvent();

		if (!voiceState.inVoiceChannel()) {
			event.reply("Não consigo tocar música sem estar em uma sala!").queue();
			return;
		}

		final Member member = arguments.getMember();
		final GuildVoiceState memberVoiceState = member.getVoiceState();

		if (!memberVoiceState.inVoiceChannel()) {
			event.reply("Você tem que estar em uma sala para tocar música!").queue();
			return;
		}

		if (!memberVoiceState.getChannel().equals(voiceState.getChannel())) {
			event.reply("Você não pode tocar música sem estar na mesma sala que o bot!").queue();
			return;
		}

		final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(arguments.getGuild());
		final AudioPlayer audioPlayer = musicManager.audioPlayer;

		if (audioPlayer.getPlayingTrack() == null) {
			event.reply("Eu não estou tocando nada!").queue();
			return;
		}

		musicManager.scheduler.clearQueue();
		musicManager.scheduler.getPlayer().stopTrack();

		event.reply("Parando de tocarr música").queue();
	}
}