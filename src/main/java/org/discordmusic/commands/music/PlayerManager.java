package org.discordmusic.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
	private static PlayerManager INSTANCE;
	private final Map<Long, GuildMusicManager> musicManagers;
	private final AudioPlayerManager audioPlayerManager;

	public PlayerManager() {
		this.musicManagers = new HashMap<>();
		this.audioPlayerManager = new DefaultAudioPlayerManager();

		AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
		AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
	}

	public GuildMusicManager getMusicManager(Guild guild) {
		return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
			final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);

			guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

			return guildMusicManager;
		});
	}

	public void loadAndPlay(TextChannel channel, String trackURL) {
		final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

		musicManager.audioPlayer.setPaused(false);

		this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack audioTrack) {
				musicManager.scheduler.queue(audioTrack);

				channel.sendMessage("Musíca adicionada na fila: `")
					.append(audioTrack.getInfo().title)
					.append("`por`")
					.append(audioTrack.getInfo().author)
					.append("`")
					.queue();

			}

			@Override
			public void playlistLoaded(AudioPlaylist audioPlaylist) {
				final AudioTrack audioTrack = audioPlaylist.getTracks().get(0);

				if (audioTrack == null) {
					channel.sendMessage("Sinto muito, mas não consegui encontrar nada, tente novamente.").queue();
					return;
				}

				channel.sendMessage("Musíca adicionada na fila: `")
					.append(audioTrack.getInfo().author)
					.append("`por`")
					.append(audioTrack.getInfo().title)
					.append("`")
					.queue();

				musicManager.scheduler.queue(audioTrack);
			}

			@Override
			public void noMatches() {
				channel.sendMessage("Sinto muito, mas não consegui encontrar nada, tente novamente.").queue();
			}

			@Override
			public void loadFailed(FriendlyException e) {
				e.printStackTrace();
			}
		});
	}

	public static PlayerManager getInstance() {
		return INSTANCE == null ? INSTANCE = new PlayerManager() : INSTANCE;
	}
}
