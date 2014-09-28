package test;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	List<Block> placedBlocks = new ArrayList<>();

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		placedBlocks.add(event.getBlock());
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		ConnectedBlocks c = new ConnectedBlocks();
		c.run(placedBlocks);

		Block block = event.getBlock();
		ArrayList<Block> connectedBlocks = c.getConnectedBlocksFor(block);

		if (connectedBlocks == null || connectedBlocks.isEmpty())
			return;

		Dropper dropper = new Dropper(this);
		dropper.scheduledDrop(connectedBlocks, 1);
		
		placedBlocks.clear();
	}
}
