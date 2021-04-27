package mrjake.aunis.block.stargate;

import mrjake.aunis.tileentity.stargate.StargatePegasusBaseTile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public final class StargatePegasusBaseBlock extends StargateClassicBaseBlock {

  public static final String BLOCK_NAME = "stargate_pegasus_base_block";

  public StargatePegasusBaseBlock() {
    super(BLOCK_NAME);
    setResistance(2000.0f);
  }

  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return new StargatePegasusBaseTile();
  }
}
