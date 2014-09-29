package main;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.inventory.ItemStack;

public class Dropper {

	Main plugin;

	public Dropper(Main instance) {
		plugin = instance;
	}

	public void scheduledDrop(final List<Block> blocks, long interval) {
		reverse(blocks);
		
		Task task = new Task(this, blocks);
		task.setId(Bukkit.getServer().getScheduler().runTaskTimer(plugin, task, 0L, interval).getTaskId());
	}

	public void cancel(Task task) {
		Bukkit.getServer().getScheduler().cancelTask(task.getId());
	}

	public void normalDrop(List<Block> blocks) {
		reverse(blocks);
	
		for (Block b : blocks) {
			drop(b);
		}
	}

	@SuppressWarnings("deprecation")
	public void drop(final Block block) {
		FallingBlock fb = block.getWorld().spawnFallingBlock(block.getLocation(), block.getType(), block.getData());
		fb.setDropItem(false);

		block.breakNaturally(new ItemStack(Material.AIR));
	}
	
	private List<Block> reverse(List<Block> list) {
		Collections.reverse(list);
		return list;
	}
}
