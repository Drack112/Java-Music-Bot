package org.discordmusic.commands;

public abstract class DCommand {
	protected final String name, aliases[];

	public DCommand(String name) {
		this(name, new String[0]);
	}

	public DCommand(String name, String... aliases) {
		this.name = name;
		this.aliases = aliases;
	}

	public abstract void execute(CommandArguments arguments);

	public final boolean isMatching(String name){
		if(this.name.equalsIgnoreCase(name)) return true;

		for (String alias : this.aliases) {
			if(this.name.equalsIgnoreCase(name)) return  true;
			if(name.equalsIgnoreCase(alias)){
				return true;
			}
		}

		return false;
	}
}
