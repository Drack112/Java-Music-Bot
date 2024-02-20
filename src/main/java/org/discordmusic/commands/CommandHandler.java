package org.discordmusic.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.discordmusic.commands.subcommands.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class CommandHandler extends ListenerAdapter {

	@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
	private final Set<DCommand> commands;

	public CommandHandler() {
		this.commands = new HashSet<DCommand>() {{
			add(new UptimeCommand());
			add(new JoinCommand());
			add(new LeaveCommand());
			add(new PauseCommand());
			add(new PlayCommand());
			add(new StopCommand());
			add(new RepeatCommand());
			add(new SkipCommand());
		}};
	}

	@Override
	public void onSlashCommand(@NotNull SlashCommandEvent event) {
		String message = event.getName();

		for (DCommand command : this.commands) {
			if (command.isMatching(message)) {
				command.execute(new CommandArguments(event));
				break;
			}
		}
	}
}
