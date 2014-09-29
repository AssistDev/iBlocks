package main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class ConnectedBlocks {

	private List<ArrayList<Block>> connectedBlockArrays = new ArrayList<>();

	public ConnectedBlocks() {
		connectedBlockArrays = new ArrayList<>();
	}

	public void run(List<Block> placedBlocks) {
		connectedBlockArrays.clear();
		iterate(placedBlocks);
	}

	public ArrayList<Block> getConnectedBlocksFor(Block block) {
		if (connectedBlockArrays == null)
			return null;

		for (ArrayList<Block> connectedBlocks : connectedBlockArrays) {
			if (connectedBlocks.contains(block))
				return connectedBlocks;
		}

		return null;
	}

	private void iterate(List<Block> placedBlocks) {
		if (placedBlocks == null || placedBlocks.isEmpty())
			return;

		List<Block> relatives = collectBlocks(placedBlocks.get(0), placedBlocks, new ArrayList<Block>());
		
		if (relatives == null || relatives.isEmpty())
			return;

		connectedBlockArrays.add((ArrayList<Block>) relatives);
	}

	BlockFace[] directions = new BlockFace[] { BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST };

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