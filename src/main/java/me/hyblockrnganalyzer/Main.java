package me.hyblockrnganalyzer;

import me.hyblockrnganalyzer.command.CsvFileCreationCommand;
import me.hyblockrnganalyzer.command.TestCommand;
import me.hyblockrnganalyzer.eventhandler.DungeonChestEventHandler;
import me.hyblockrnganalyzer.eventhandler.GiftEventHandler;
import me.hyblockrnganalyzer.eventhandler.JerryBoxEventHandler;
import me.hyblockrnganalyzer.eventhandler.NucleusLootEventHandler;
import me.hyblockrnganalyzer.eventhandler.TreasureChestEventHandler;
import me.hyblockrnganalyzer.util.DungeonChestStatus;
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

	private TxtDatabase txtDatabase = new TxtDatabase();
	private DungeonChestStatus dungeonChestStatus = new DungeonChestStatus();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		txtDatabase.createFiles(event);
		System.out.println("[OK] preInit Hyblock RNG Analyzer");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		ClientCommandHandler.instance.registerCommand(new TestCommand());
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

	public DungeonChestStatus getDungeonChestStatus() {
		return dungeonChestStatus;
	}
}
