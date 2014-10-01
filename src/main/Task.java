package main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Task implements Runnable {

	private Dropper dropper;
	private int taskId;

	private List<Block> blocks;
	private int current = 0;

	private List<Block> rootBlocks;

	public Task(Dropper dropper, List<Block> blocks) {
		this.dropper = dropper;
		this.blocks = blocks;
		this.rootBlocks = new ArrayList<>();
	}

	@Override
	public void run() {	
		if (current < blocks.size()) {
			Block block = blocks.get(current);
			Block lowest = lowest(block);
			
			if (lowest != null) {				
				if (!rootBlocks.contains(block))
					rootBlocks.add(block);
			} else {
				if (rootBlocks.contains(block))
					rootBlocks.remove(block);
			}

			dropper.drop(block);
		}

		for (Block rootBlock : rootBlocks) {
			Block lowest = lowest(rootBlock);

			if (lowest == null) {
				rootBlocks.remove(rootBlock);
				continue;
			}

			dropper.drop(lowest);
		}

		current++;

		if (current >= blocks.size() && rootBlocks.isEmpty()) {
			dropper.cancel(this);
			return;
		}
	}

	@SuppressWarnings("deprecation")
	private Block lowest(Block root) {
		Block below = root.getRelative(BlockFace.DOWN);

		if ((below == null || below.getType() == Material.AIR) || (below.getType() != root.getType() && below.getData() != root.getData())) {
			return root;
		}

		return lowest(below);
	}

	public int getId() {
		return taskId;
	}

	public void setId(int id) {
		this.taskId = id;
	}
}
