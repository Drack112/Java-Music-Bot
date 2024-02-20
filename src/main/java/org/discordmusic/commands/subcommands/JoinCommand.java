package org.discordmusic.commands.subcommands;


import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.discordmusic.commands.CommandArguments;
import org.discordmusic.commands.DCommand;


public class JoinCommand extends DCommand {

	public JoinCommand() {
		super("join", "J");
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public void execute(CommandArguments arguments) {
		final TextChannel channel = arguments.getTextChannel();
		final Member self = channel.getGuild().getSelfMember();
		final GuildVoiceState state = self.getVoiceState();
		final SlashCommandEvent event = arguments.getEvent();


		if (state.inVoiceChannel()) {
			event.reply("Eu já estou no canal de voz!").queue();
			return;
		}

		final Member member = arguments.getMember();
		final GuildVoiceState memberVoiceState = member.getVoiceState();

		if (!memberVoiceState.inVoiceChannel()) {
			event.reply("Você não está conectado em nenhuma sala! Use o comando `/join` para eu conseguir entrar na sala!").queue();
			return;
		}


		final AudioManager audioManager = arguments.getGuild().getAudioManager();
		final VoiceChannel memberChannel = memberVoiceState.getChannel();

		audioManager.openAudioConnection(memberChannel);
		event.replyFormat("Conectando ao canal de voz %s", memberChannel.getName()).queue();
	}
}
