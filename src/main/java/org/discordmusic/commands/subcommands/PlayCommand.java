package org.discordmusic.commands.subcommands;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.discordmusic.commands.CommandArguments;
import org.discordmusic.commands.DCommand;
import org.discordmusic.commands.music.PlayerManager;
import org.discordmusic.util.Utils;

public class PlayCommand extends DCommand {

	public PlayCommand() {
		super("play", "p");
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

		final String msg = event.getOption("yt_link").getAsString();

		if (msg == null) {
			event.reply("Não consigo tocar algo sem um link ou nome de música!").queue();
			return;
		}

		String url = Utils.getVideoId(msg);

		if (!Utils.isURL(url)) {
			url = "ytsearch:" + msg.substring(name.length()).trim();
		}

		PlayerManager.getInstance()
			.loadAndPlay(channel, url, member, event);
	}
}