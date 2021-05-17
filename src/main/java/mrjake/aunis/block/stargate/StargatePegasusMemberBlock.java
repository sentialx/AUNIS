package mrjake.aunis.block.stargate;

import mrjake.aunis.AunisProps;
import mrjake.aunis.stargate.EnumMemberVariant;
import mrjake.aunis.stargate.merging.StargateAbstractMergeHelper;
import mrjake.aunis.stargate.merging.StargateMilkyWayMergeHelper;
import mrjake.aunis.stargate.merging.StargatePegasusMergeHelper;
import mrjake.aunis.tileentity.stargate.StargatePegasusMemberTile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public final class StargatePegasusMemberBlock extends StargateClassicMemberBlock {
	public static final String BLOCK_NAME = "stargate_pegasus_member_block";

	public StargatePegasusMemberBlock() {
		super(BLOCK_NAME);
		setResistance(2000.0f);
	}

	@Override
	protected StargateAbstractMergeHelper getMergeHelper() {
		return StargatePegasusMergeHelper.INSTANCE;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new StargatePegasusMemberTile();
	}
}
