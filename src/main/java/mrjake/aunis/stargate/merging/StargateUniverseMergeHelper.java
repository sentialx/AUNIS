package mrjake.aunis.stargate.merging;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import mrjake.aunis.block.AunisBlocks;
import mrjake.aunis.block.stargate.StargateUniverseMemberBlock;
import mrjake.aunis.stargate.EnumMemberVariant;
import mrjake.aunis.tileentity.stargate.StargateMilkyWayBaseTile;
import mrjake.aunis.util.AunisAxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class StargateUniverseMergeHelper extends StargateClassicDefaultMergeHelper {
	public static final StargateUniverseMergeHelper INSTANCE = new StargateUniverseMergeHelper();

	@Override
	public StargateUniverseMemberBlock getMemberBlock() {
		return AunisBlocks.STARGATE_UNIVERSE_MEMBER_BLOCK;
	}

	@Override
	public StargateUniverseMemberBlock getBaseBlock() {
		return AunisBlocks.STARGATE_UNIVERSE_BASE_BLOCK;
	}
}
