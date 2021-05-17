package mrjake.aunis.stargate.merging;

import mrjake.aunis.block.AunisBlocks;
import mrjake.aunis.block.stargate.StargatePegasusBaseBlock;
import mrjake.aunis.block.stargate.StargatePegasusMemberBlock;

public class StargatePegasusMergeHelper extends StargateClassicSizedMergeHelper {
	public static final StargatePegasusMergeHelper INSTANCE = new StargatePegasusMergeHelper();

	@Override
	public StargatePegasusMemberBlock getMemberBlock() {
		return AunisBlocks.STARGATE_PEGASUS_MEMBER_BLOCK;
	}

	@Override
	public StargatePegasusBaseBlock getBaseBlock() {
		return AunisBlocks.STARGATE_PEGASUS_BASE_BLOCK;
	}
}
