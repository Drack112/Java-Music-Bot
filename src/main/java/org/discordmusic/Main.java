package org.discordmusic;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.discordmusic.commands.CommandHandler;
import org.discordmusic.events.ReadyEvent;

import javax.security.auth.login.LoginException;
import java.util.EventListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main implements EventListener {
	public static void main(String[] args) throws LoginException {

		Dotenv dotenv = Dotenv.load();

		JDA jda = JDABuilder.createDefault(dotenv.get("TOKEN"))
			.enableCache(CacheFlag.VOICE_STATE)
			.disableCache(CacheFlag.MEMBER_OVERRIDES)
			.enableIntents(GatewayIntent.GUILD_VOICE_STATES)
			.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER))
			.setBulkDeleteSplittingEnabled(false)
			.setCompression(Compression.NONE)
			.setActivity(Activity.listening("Music"))
			.addEventListeners(new ReadyEvent())
			.addEventListeners(new CommandHandler())
			.build();


		jda.upsertCommand("join", "Make bot join to your current channel.").queue();
		jda.upsertCommand("leave", "Make bot leave your current channel.").queue();
		jda.upsertCommand("pause", "Pause the current song.").queue();
		jda.upsertCommand(new CommandData("play", "Insert a link to play.").addOption(OptionType.STRING, "yt_link", "Youtube link to play.", true)).queue();
		jda.upsertCommand("repeat", "Enable or disable repeat mode.").queue();
		jda.upsertCommand("skip", "Skip the current song.").queue();
		jda.upsertCommand("stop", "Stop the current song.").queue();
		jda.upsertCommand("uptime", "Show current update of BOT").queue();


		OnlineStatus[] statusbot = {OnlineStatus.ONLINE, OnlineStatus.DO_NOT_DISTURB, OnlineStatus.IDLE};

		ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor(runnable -> {
			Thread thread = new Thread(runnable);
			thread.setDaemon(true);
			return thread;
		});

		int[] currentIndex = {0};

		pool.scheduleWithFixedDelay(() -> {
			currentIndex[0] = (currentIndex[0] + 1) % statusbot.length;
			jda.getPresence().setStatus((statusbot[currentIndex[0]]));
		}, 0, 5, TimeUnit.SECONDS);
	}

}