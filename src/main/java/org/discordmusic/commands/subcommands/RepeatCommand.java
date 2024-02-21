package org.discordmusic.commands.subcommands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.discordmusic.commands.CommandArguments;
import org.discordmusic.commands.DCommand;
import org.discordmusic.commands.music.GuildMusicManager;
import org.discordmusic.commands.music.PlayerManager;

import java.awt.*;

public class RepeatCommand extends DCommand {
	public RepeatCommand() {
		super("repeat", "r");
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public void execute(CommandArguments arguments) {
		final TextChannel channel = arguments.getTextChannel();
		final Member self = channel.getGuild().getSelfMember();
		final GuildVoiceState voiceState = self.getVoiceState();
		final SlashCommandEvent event = arguments.getEvent();

		if (!voiceState.inVoiceChannel()) {
			channel.sendMessageEmbeds(
				new EmbedBuilder()
					.setColor(Color.RED)
					.setFooter(event.getJDA().getSelfUser().getName() + " \uD83D\uDC0D")
					.setDescription("Não consigo tocar música sem estar em uma sala!"
					).build()).queue();
			return;
		}

		final Member member = arguments.getMember();
		final GuildVoiceState memberVoiceState = member.getVoiceState();

		if (!memberVoiceState.inVoiceChannel()) {
			channel.sendMessageEmbeds(
				new EmbedBuilder()
					.setColor(Color.RED)
					.setFooter(event.getJDA().getSelfUser().getName() + " \uD83D\uDC0D")
					.setDescription("Você não está conectado em nenhuma sala! Use o comando `/join` para eu conseguir entrar na sala!"
					).build()).queue();
			return;
		}

		if (!memberVoiceState.getChannel().equals(voiceState.getChannel())) {
			channel.sendMessageEmbeds(
				new EmbedBuilder()
					.setColor(Color.RED)
					.setFooter(event.getJDA().getSelfUser().getName() + " \uD83D\uDC0D")
					.setDescription("Você não pode parar a música sem estar na mesma sala que eu estou!"
					).build()).queue();
			return;
		}

		final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(arguments.getGuild());
		final AudioPlayer audioPlayer = musicManager.audioPlayer;

		if (audioPlayer.getPlayingTrack() == null) {
			channel.sendMessageEmbeds(
				new EmbedBuilder()
					.setColor(Color.RED)
					.setFooter(event.getJDA().getSelfUser().getName() + " \uD83D\uDC0D")
					.setDescription("Eu não estou tocando nada!"
					).build()).queue();
			return;
		}

		boolean repeting = musicManager.scheduler.toggleRepeating();

		channel.sendMessageEmbeds(
			new EmbedBuilder()
				.setColor(Color.GREEN)
				.setFooter(event.getJDA().getSelfUser().getName() + " \uD83D\uDC0D")
				.setDescription(repeting ? "Repetindo a música atual" : "Parando de fazer loop das músicas"
				).build()).queue();
		return;


	}
}
