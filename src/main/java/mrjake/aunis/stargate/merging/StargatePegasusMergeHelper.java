package mrjake.aunis.stargate.merging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import mrjake.aunis.Aunis;
import mrjake.aunis.AunisProps;
import mrjake.aunis.block.AunisBlocks;
import mrjake.aunis.block.stargate.StargateMilkyWayBaseBlock;
import mrjake.aunis.block.stargate.StargateMilkyWayMemberBlock;
import mrjake.aunis.config.AunisConfig;
import mrjake.aunis.config.StargateSizeEnum;
import mrjake.aunis.stargate.EnumMemberVariant;
import mrjake.aunis.tileentity.stargate.StargateMilkyWayBaseTile;
import mrjake.aunis.util.AunisAxisAlignedBB;
import mrjake.aunis.util.FacingToRotation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StargatePegasusMergeHelper extends StargateClassicSizedMergeHelper {
	public static final StargatePegasusMergeHelper INSTANCE = new StargatePegasusMergeHelper();
	
	@Override
	public StargatePegasusMemberBlock getMemberBlock() {
		return AunisBlocks.STARGATE_PEGASUS_MEMBER_BLOCK;
	}

	@Override
	public StargatePegasusMemberBlock getBaseBlock() {
		return AunisBlocks.STARGATE_PEGASUS_BASE_BLOCK;
	}
}
