package main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	private BlockFace[] directions = new BlockFace[] { BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST };

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	List<ArrayList<Block>> placedBlocksArray = new ArrayList<>();

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		Block block = event.getBlock();
		ArrayList<Block> connectionList = findConnectionList(block);

		if (connectionList == null) {
			connectionList = new ArrayList<Block>();
			placedBlocksArray.add(connectionList);
		}

		connectionList.add(block);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		ArrayList<Block> connectionList = findConnectionList(block);

		if (connectionList == null) {
			return;
		}

		ArrayList<Block> newConnectionList = directionalSearch(connectionList);

		Dropper dropper = new Dropper(this);
		dropper.scheduledDrop(newConnectionList, 1);

		placedBlocksArray.remove(connectionList);
	}

	private ArrayList<Block> findConnectionList(Block origin) {
		for (ArrayList<Block> list : placedBlocksArray) {
			for (BlockFace direction : directions) {
				if (list.contains(origin.getRelative(direction))) {
					return list;
				}
			}
		}

		return null;
	}

	private ArrayList<Block> directionalSearch(ArrayList<Block> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}

		List<Block> blocks = collectBlocks(list.get(0), list, new ArrayList<Block>());
		return (ArrayList<Block>) blocks;
	}

	private List<Block> collectBlocks(Block origin, List<Block> list, List<Block> collected) {
		for (BlockFace direction : directions) {
			Block b = origin.getRelative(direction);

			if (b.getType() == origin.getType() && !collected.contains(b) && list.contains(b)) {
				collected.add(b);
				collectBlocks(b, list, collected);
			}
		}

		return collected;
	}
}
