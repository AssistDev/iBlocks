package test;

import java.util.List;

import org.bukkit.block.Block;

public class Task implements Runnable {

	private Dropper dropper;
	private int taskId;

	private List<Block> blocks;
	int current = 0;

	public Task(Dropper dropper, List<Block> blocks) {
		this.dropper = dropper;
		this.blocks = blocks;
	}

	@Override
	public void run() {
		dropper.drop(blocks.get(current));
		current++;
		
		if (current == blocks.size())
			dropper.cancel(this);
	}

	public int getId() {
		return taskId;
	}

	public void setId(int id) {
		this.taskId = id;
	}
}
