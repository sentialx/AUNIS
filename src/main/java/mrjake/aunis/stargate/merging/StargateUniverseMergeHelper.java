package mrjake.aunis.stargate.merging;

import mrjake.aunis.block.AunisBlocks;
import mrjake.aunis.block.stargate.StargateUniverseBaseBlock;
import mrjake.aunis.block.stargate.StargateUniverseMemberBlock;

public class StargateUniverseMergeHelper extends StargateClassicDefaultMergeHelper {
	public static final StargateUniverseMergeHelper INSTANCE = new StargateUniverseMergeHelper();

	@Override
	public StargateUniverseMemberBlock getMemberBlock() {
		return AunisBlocks.STARGATE_UNIVERSE_MEMBER_BLOCK;
	}

	@Override
	public StargateUniverseBaseBlock getBaseBlock() {
		return AunisBlocks.STARGATE_UNIVERSE_BASE_BLOCK;
	}
}
