package mrjake.aunis.stargate.merging;

import java.util.Arrays;
import java.util.List;

import mrjake.aunis.config.AunisConfig;
import mrjake.aunis.util.AunisAxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public abstract class StargateClassicSizedMergeHelper extends StargateClassicMergeHelper {
	/**
	 * Bounding box used for Base Tile search.
	 * Searches 3 blocks to the left/right and 7 blocks down.
	 */
	protected static final AunisAxisAlignedBB BASE_SEARCH_BOX_SMALL = new AunisAxisAlignedBB(-3, -7, 0, 3, 0, 0);
	protected static final AunisAxisAlignedBB BASE_SEARCH_BOX_LARGE = new AunisAxisAlignedBB(-5, -9, 0, 5, 0, 0);

	protected static final List<BlockPos> RING_BLOCKS_SMALL = Arrays.asList(
		new BlockPos(1, 7, 0),
		new BlockPos(3, 5, 0),
		new BlockPos(3, 3, 0),
		new BlockPos(2, 1, 0),
		new BlockPos(-2, 1, 0),
		new BlockPos(-3, 3, 0),
		new BlockPos(-3, 5, 0),
		new BlockPos(-1, 7, 0));

	protected static final List<BlockPos> CHEVRON_BLOCKS_SMALL = Arrays.asList(
		new BlockPos(2, 6, 0),
		new BlockPos(3, 4, 0),
		new BlockPos(3, 2, 0),
		new BlockPos(-3, 2, 0),
		new BlockPos(-3, 4, 0),
		new BlockPos(-2, 6, 0),
		new BlockPos(1, 0, 0),
		new BlockPos(-1, 0, 0),
		new BlockPos(0, 7, 0));

	protected static final List<BlockPos> RING_BLOCKS_LARGE = Arrays.asList(
		new BlockPos(-1, 0, 0),
		new BlockPos(-3, 1, 0),
		new BlockPos(-4, 3, 0),
		new BlockPos(-5, 4, 0),
		new BlockPos(-4, 6, 0),
		new BlockPos(-4, 7, 0),
		new BlockPos(-2, 9, 0),
		new BlockPos(-1, 9, 0),
		new BlockPos(1, 9, 0),
		new BlockPos(2, 9, 0),
		new BlockPos(4, 7, 0),
		new BlockPos(4, 6, 0),
		new BlockPos(5, 4, 0),
		new BlockPos(4, 3, 0),
		new BlockPos(3, 1, 0),
		new BlockPos(1, 0, 0));

	protected static final List<BlockPos> CHEVRON_BLOCKS_LARGE = Arrays.asList(
		new BlockPos(3, 8, 0),
		new BlockPos(5, 5, 0),
		new BlockPos(4, 2, 0),
		new BlockPos(-4, 2, 0),
		new BlockPos(-5, 5, 0),
		new BlockPos(-3, 8, 0),
		new BlockPos(2, 0, 0),
		new BlockPos(-2, 0, 0),
		new BlockPos(0, 9, 0));

	@Override
	public List<BlockPos> getRingBlocks() {
		switch (AunisConfig.stargateSize) {
			case SMALL:
			case MEDIUM:
				return RING_BLOCKS_SMALL;

			case LARGE:
				return RING_BLOCKS_LARGE;

			default:
				return null;
		}
	}

	@Override
	public List<BlockPos> getChevronBlocks() {
		switch (AunisConfig.stargateSize) {
			case SMALL:
			case MEDIUM:
				return CHEVRON_BLOCKS_SMALL;

			case LARGE:
				return CHEVRON_BLOCKS_LARGE;

			default:
				return null;
		}
	}

	@Override
	public AunisAxisAlignedBB getBaseSearchBox() {
		switch (AunisConfig.stargateSize) {
			case SMALL:
			case MEDIUM:
				return BASE_SEARCH_BOX_SMALL;

			case LARGE:
				return BASE_SEARCH_BOX_LARGE;

			default:
				return null;
		}
	}
}
