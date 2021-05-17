package mrjake.aunis.stargate.merging;

import java.util.ArrayList;
import java.util.List;

import mrjake.aunis.Aunis;
import mrjake.aunis.AunisProps;
import mrjake.aunis.block.AunisBlocks;
import mrjake.aunis.block.stargate.StargateMilkyWayBaseBlock;
import mrjake.aunis.block.stargate.StargateMilkyWayMemberBlock;
import mrjake.aunis.config.StargateSizeEnum;
import mrjake.aunis.stargate.EnumMemberVariant;
import mrjake.aunis.util.FacingToRotation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StargateMilkyWayMergeHelper extends StargateClassicSizedMergeHelper {
	public static final StargateMilkyWayMergeHelper INSTANCE = new StargateMilkyWayMergeHelper();

	@Override
	public StargateMilkyWayMemberBlock getMemberBlock() {
		return AunisBlocks.STARGATE_MILKY_WAY_MEMBER_BLOCK;
	}

	@Override
	public StargateMilkyWayBaseBlock getBaseBlock() {
		return AunisBlocks.STARGATE_MILKY_WAY_BASE_BLOCK;
	}

	/**
	 * Converts merged Stargate from old pattern (1.5)
	 * to new pattern (1.6).
	 *
	 * @param world               {@link World} instance.
	 * @param basePos             Position of {@link StargateMilkyWayBaseBlock} the tiles should be linked to.
	 * @param baseFacing          Facing of {@link StargateMilkyWayBaseBlock}.
	 * @param currentStargateSize Current Stargate size as read from NBT.
	 * @param targetStargateSize  Target Stargate size as defined in config.
	 */
	public void convertToPattern(World world, BlockPos basePos, EnumFacing baseFacing, StargateSizeEnum currentStargateSize, StargateSizeEnum targetStargateSize) {
		Aunis.logger.debug(basePos + ": Converting Stargate from " + currentStargateSize + " to " + targetStargateSize);
		List<BlockPos> oldPatternBlocks = new ArrayList<BlockPos>();

		switch (currentStargateSize) {
			case SMALL:
			case MEDIUM:
				oldPatternBlocks.addAll(RING_BLOCKS_SMALL);
				oldPatternBlocks.addAll(CHEVRON_BLOCKS_SMALL);
				break;

			case LARGE:
				oldPatternBlocks.addAll(RING_BLOCKS_LARGE);
				oldPatternBlocks.addAll(CHEVRON_BLOCKS_LARGE);
				break;
		}

		for (BlockPos pos : oldPatternBlocks)
			world.setBlockToAir(pos.rotate(FacingToRotation.get(baseFacing)).add(basePos));

		IBlockState memberState = AunisBlocks.STARGATE_MILKY_WAY_MEMBER_BLOCK.getDefaultState()
			.withProperty(AunisProps.FACING_HORIZONTAL, baseFacing)
			.withProperty(AunisProps.RENDER_BLOCK, false);

		for (BlockPos pos : getRingBlocks())
			world.setBlockState(pos.rotate(FacingToRotation.get(baseFacing)).add(basePos), memberState.withProperty(AunisProps.MEMBER_VARIANT, EnumMemberVariant.RING));

		for (BlockPos pos : getChevronBlocks())
			world.setBlockState(pos.rotate(FacingToRotation.get(baseFacing)).add(basePos), memberState.withProperty(AunisProps.MEMBER_VARIANT, EnumMemberVariant.CHEVRON));
	}
}
