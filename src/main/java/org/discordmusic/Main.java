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


		jda.upsertCommand("join", "Faça o bot entrar no canal de voz onde você se encontra").queue();
		jda.upsertCommand("leave", "O bot vai deixar o canal de voz que está conectado.").queue();
		jda.upsertCommand("pause", "Pause a música atual.").queue();
		jda.upsertCommand(new CommandData("play", "Tocar uma música a partir de uma URL do YouTube").addOption(OptionType.STRING, "yt_link", "Youtube link.", true)).queue();
		jda.upsertCommand("repeat", "Habilitar ou desabilitar o modo de repetição.").queue();
		jda.upsertCommand("skip", "Pular a música atual que está tocando.").queue();
		jda.upsertCommand("stop", "Parar de tocar música").queue();
		jda.upsertCommand("uptime", "Mostra quantos dias consecutivos o bot está de pé").queue();


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