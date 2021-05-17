package mrjake.aunis.tileentity.stargate;

import javax.annotation.Nullable;

import mrjake.aunis.block.AunisBlocks;
import mrjake.aunis.stargate.EnumStargateState;
import mrjake.aunis.stargate.network.DHDSymbolInterface;
import mrjake.aunis.stargate.network.StargatePos;
import mrjake.aunis.stargate.network.SymbolInterface;
import mrjake.aunis.stargate.network.SymbolMilkyWayEnum;
import mrjake.aunis.tileentity.DHDTile;
import mrjake.aunis.tileentity.DHDTile.DHDUpgradeEnum;
import mrjake.aunis.util.ILinkable;
import mrjake.aunis.util.LinkingHelper;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This class wraps common behavior for the fully-functional Stargates that are able to link to a DHD
 * i.e. Pegasus and Milky Way.
 *
 * @author MrJake222, sentialx
 */
public abstract class StargateClassicDHDLinkableBaseTile extends StargateClassicBaseTile implements ILinkable {
	// ------------------------------------------------------------------------
	// Stargate state

	@Override
	protected void disconnectGate() {
		super.disconnectGate();

		if (isLinkedAndDHDOperational())
			getLinkedDHD(world).clearSymbols();
	}

	@Override
	protected void failGate() {
		super.failGate();

		if (isLinkedAndDHDOperational())
			getLinkedDHD(world).clearSymbols();
	}

	// ------------------------------------------------------------------------
	// Stargate connection

	@Override
	public void openGate(StargatePos targetGatePos, boolean isInitiating) {
		super.openGate(targetGatePos, isInitiating);

		if (isLinkedAndDHDOperational()) {
			getLinkedDHD(world).activateSymbol(SymbolMilkyWayEnum.BRB);
		}
	}

	// ------------------------------------------------------------------------
	// Stargate Network

	@Override
	protected int getMaxChevrons() {
		return isLinkedAndDHDOperational() && stargateState != EnumStargateState.DIALING_COMPUTER && !getLinkedDHD(world).hasUpgrade(DHDUpgradeEnum.CHEVRON_UPGRADE) ? 7 : 9;
	}

	@Override
	public void addSymbolToAddress(SymbolInterface symbol) {
		super.addSymbolToAddress(symbol);

		if (isLinkedAndDHDOperational()) {
			getLinkedDHD(world).activateSymbol((DHDSymbolInterface) symbol);
		}
	}

	@Override
	public void addSymbolToAddressManual(SymbolInterface targetSymbol, Object context) {
		stargateState = EnumStargateState.DIALING_COMPUTER;

		super.addSymbolToAddressManual(targetSymbol, context);
	}

	public void incomingWormhole(int dialedAddressSize) {
		super.incomingWormhole(dialedAddressSize);

		if (isLinkedAndDHDOperational()) {
			getLinkedDHD(world).clearSymbols();
		}
	}


	// ------------------------------------------------------------------------
	// Merging

	@Override
	public void onGateBroken() {
		super.onGateBroken();

		if (isLinked()) {
			getLinkedDHD(world).clearSymbols();
			getLinkedDHD(world).setLinkedGate(null, -1);
			setLinkedDHD(null, -1);
		}
	}

	@Override
	protected void onGateMerged() {
		super.onGateMerged();
		this.updateLinkStatus();
	}

	// ------------------------------------------------------------------------
	// Linking

	private BlockPos linkedDHD = null;

	private int linkId = -1;

	@Nullable
	public DHDTile getLinkedDHD(World world) {
		if (linkedDHD == null)
			return null;

		return (DHDTile) world.getTileEntity(linkedDHD);
	}

	public boolean isLinked() {
		return linkedDHD != null && world.getTileEntity(linkedDHD) instanceof DHDTile;
	}

	public boolean isLinkedAndDHDOperational() {
		if (!isLinked())
			return false;

		DHDTile dhdTile = getLinkedDHD(world);
		if (!dhdTile.hasControlCrystal())
			return false;

		return true;
	}

	public void setLinkedDHD(BlockPos dhdPos, int linkId) {
		this.linkedDHD = dhdPos;
		this.linkId = linkId;

		markDirty();
	}

	@Override
	public boolean canLinkTo() {
		return isMerged() && !isLinked();
	}

	@Override
	public int getLinkId() {
		return linkId;
	}

	public void updateLinkStatus() {
		BlockPos closestDhd = LinkingHelper.findClosestUnlinked(world, pos, LinkingHelper.getDhdRange(), AunisBlocks.DHD_BLOCK, this.getLinkId());

		if (closestDhd != null) {
			int linkId = LinkingHelper.getLinkId();
			DHDTile dhdTile = (DHDTile) world.getTileEntity(closestDhd);

			dhdTile.setLinkedGate(pos, linkId);
			setLinkedDHD(closestDhd, linkId);
			markDirty();
		}
	}

	// ------------------------------------------------------------------------
	// NBT

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		if (isLinked()) {
			compound.setLong("linkedDHD", linkedDHD.toLong());
			compound.setInteger("linkId", linkId);
		}

		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("linkedDHD"))
			this.linkedDHD = BlockPos.fromLong(compound.getLong("linkedDHD"));
		if (compound.hasKey("linkId"))
			this.linkId = compound.getInteger("linkId");

		super.readFromNBT(compound);
	}

	@Override
	public boolean prepare(ICommandSender sender, ICommand command) {
		setLinkedDHD(null, -1);

		return super.prepare(sender, command);
	}

	// ------------------------------------------------------------------------
	// Ticking and loading

	private BlockPos lastPos = BlockPos.ORIGIN;

	@Override
	public void update() {
		super.update();

		if (!world.isRemote) {
			if (!lastPos.equals(pos)) {
				lastPos = pos;

				updateLinkStatus();
				markDirty();
			}
		}
	}
}
