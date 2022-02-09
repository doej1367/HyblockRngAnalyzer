package me.hyblockrnganalyzer;

import me.hyblockrnganalyzer.command.CsvFileCreationCommand;
import me.hyblockrnganalyzer.command.OpenFolderCommand;
import me.hyblockrnganalyzer.command.TestCommand;
import me.hyblockrnganalyzer.eventhandler.DungeonChestEventHandler;
import me.hyblockrnganalyzer.eventhandler.GiftEventHandler;
import me.hyblockrnganalyzer.eventhandler.JerryBoxEventHandler;
import me.hyblockrnganalyzer.eventhandler.NucleusLootEventHandler;
import me.hyblockrnganalyzer.eventhandler.TreasureChestEventHandler;
import me.hyblockrnganalyzer.status.DungeonChestStatus;
import me.hyblockrnganalyzer.status.JerryBoxStatus;
import me.hyblockrnganalyzer.status.LobbyStatus;
import me.hyblockrnganalyzer.util.TxtDatabase;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Main.MODID, version = Main.VERSION)
public class Main {
	public static final String MODID = "hyblockrnganalyzer";
	public static final String VERSION = "1.6";

	private TxtDatabase txtDatabase;
	private LobbyStatus lobbyStatus;
	private DungeonChestStatus dungeonChestStatus;
	private JerryBoxStatus jerryBoxStatus;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		txtDatabase = new TxtDatabase(this);
		lobbyStatus = new LobbyStatus();
		dungeonChestStatus = new DungeonChestStatus();
		jerryBoxStatus = new JerryBoxStatus();
		txtDatabase.setFolder(event);
		System.out.println("[OK] preInit Hyblock RNG Analyzer");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		ClientCommandHandler.instance.registerCommand(new TestCommand());
		ClientCommandHandler.instance.registerCommand(new OpenFolderCommand(this));
		ClientCommandHandler.instance.registerCommand(new CsvFileCreationCommand(this));
		// converting Minecraft events into specific Hypixel SkyBlock events
		MinecraftForge.EVENT_BUS.register(new HypixelEventHandler(this));
		// handling Hypixel events
		MinecraftForge.EVENT_BUS.register(new TreasureChestEventHandler(this));
		MinecraftForge.EVENT_BUS.register(new DungeonChestEventHandler(this));
		MinecraftForge.EVENT_BUS.register(new JerryBoxEventHandler(this));
		MinecraftForge.EVENT_BUS.register(new NucleusLootEventHandler(this));
		MinecraftForge.EVENT_BUS.register(new GiftEventHandler(this));
		// TODO (placeholder for more event handlers)

		txtDatabase.createFiles();
		System.out.println("[OK] registered events");
		System.out.println("[OK] init Hyblock RNG Analyzer");
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		System.out.println("[OK] postInit Hyblock RNG Analyzer");
	}

	public TxtDatabase getTxtDatabase() {
		return txtDatabase;
	}

	public LobbyStatus getLobbyStatus() {
		return lobbyStatus;
	}

	public DungeonChestStatus getDungeonChestStatus() {
		return dungeonChestStatus;
	}

	public JerryBoxStatus getJerryBoxStatus() {
		return jerryBoxStatus;
	}
}
