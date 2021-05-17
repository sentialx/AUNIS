package mrjake.aunis.tileentity.stargate;

import mrjake.aunis.Aunis;
import mrjake.aunis.config.AunisConfig;
import mrjake.aunis.config.StargateSizeEnum;
import mrjake.aunis.packet.AunisPacketHandler;
import mrjake.aunis.packet.StateUpdatePacketToClient;
import mrjake.aunis.renderer.biomes.BiomeOverlayEnum;
import mrjake.aunis.renderer.stargate.ChevronEnum;
import mrjake.aunis.renderer.stargate.StargateAbstractRendererState;
import mrjake.aunis.renderer.stargate.StargatePegasusRendererState;
import mrjake.aunis.sound.SoundEventEnum;
import mrjake.aunis.sound.SoundPositionedEnum;
import mrjake.aunis.sound.StargateSoundEventEnum;
import mrjake.aunis.sound.StargateSoundPositionedEnum;
import mrjake.aunis.stargate.EnumScheduledTask;
import mrjake.aunis.stargate.EnumSpinDirection;
import mrjake.aunis.stargate.EnumStargateState;
import mrjake.aunis.stargate.merging.StargateAbstractMergeHelper;
import mrjake.aunis.stargate.merging.StargatePegasusMergeHelper;
import mrjake.aunis.stargate.network.*;
import mrjake.aunis.state.StargateRendererActionState;
import mrjake.aunis.state.StargateRendererActionState.EnumGateAction;
import mrjake.aunis.state.StargateSpinState;
import mrjake.aunis.state.State;
import mrjake.aunis.state.StateTypeEnum;
import mrjake.aunis.tileentity.util.ScheduledTask;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class StargatePegasusBaseTile extends StargateClassicBaseTile {
	// ------------------------------------------------------------------------
	// Stargate state

	@Override
	protected void addFailedTaskAndPlaySound() {
		addTask(new ScheduledTask(EnumScheduledTask.STARGATE_FAIL, stargateState.dialingComputer() ? 83 : 53));
		playSoundEvent(StargateSoundEventEnum.DIAL_FAILED);
	}

	// ------------------------------------------------------------------------
	// Stargate Network

	@Override
	public SymbolTypeEnum getSymbolType() {
		return SymbolTypeEnum.MILKYWAY;
	}

	// ------------------------------------------------------------------------
	// Merging

	@Override
	public StargateAbstractMergeHelper getMergeHelper() {
		return StargatePegasusMergeHelper.INSTANCE;
	}

	// ------------------------------------------------------------------------
	// NBT

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("stargateSize", stargateSize.id);

		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("patternVersion")) stargateSize = StargateSizeEnum.SMALL;
		else {
			if (compound.hasKey("stargateSize")) stargateSize = StargateSizeEnum.fromId(compound.getInteger("stargateSize"));
			else stargateSize = StargateSizeEnum.LARGE;
		}

		super.readFromNBT(compound);
	}

	// ------------------------------------------------------------------------
	// Sounds

	@Override
	protected SoundPositionedEnum getPositionedSound(StargateSoundPositionedEnum soundEnum) {
		switch (soundEnum) {
			case GATE_RING_ROLL:
				return SoundPositionedEnum.PEGASUS_RING_ROLL;
		}

		return null;
	}

	@Override
	protected SoundEventEnum getSoundEvent(StargateSoundEventEnum soundEnum) {
		switch (soundEnum) {
			case OPEN:
				return SoundEventEnum.GATE_PEGASUS_OPEN;
			case CLOSE:
				return SoundEventEnum.GATE_MILKYWAY_CLOSE;
			case DIAL_FAILED:
				return stargateState.dialingComputer() ? SoundEventEnum.GATE_MILKYWAY_DIAL_FAILED_COMPUTER : SoundEventEnum.GATE_MILKYWAY_DIAL_FAILED;
			case INCOMING:
				return SoundEventEnum.GATE_MILKYWAY_INCOMING;
			case CHEVRON_OPEN:
				return SoundEventEnum.GATE_PEGASUS_CHEVRON_OPEN;
			case CHEVRON_SHUT:
				return SoundEventEnum.GATE_PEGASUS_CHEVRON_SHUT;
		}

		return null;
	}


	// ------------------------------------------------------------------------
	// Ticking and loading

	@Override
	public BlockPos getGateCenterPos() {
		return pos.offset(EnumFacing.UP, 4);
	}

	@Override
	protected boolean onGateMergeRequested() {
		return StargatePegasusMergeHelper.INSTANCE.checkBlocks(world, pos, facing);
	}

	public static final EnumSet<BiomeOverlayEnum> SUPPORTED_OVERLAYS = EnumSet.of(BiomeOverlayEnum.NORMAL, BiomeOverlayEnum.FROST, BiomeOverlayEnum.MOSSY, BiomeOverlayEnum.AGED, BiomeOverlayEnum.SOOTY);

	@Override
	public EnumSet<BiomeOverlayEnum> getSupportedOverlays() {
		return SUPPORTED_OVERLAYS;
	}

	// ------------------------------------------------------------------------
	// Rendering

	private StargateSizeEnum stargateSize = AunisConfig.stargateSize;

	/**
	 * Returns stargate state either from config or from client's state.
	 * THIS IS NOT A GETTER OF stargateSize.
	 *
	 * @param server Is the code running on server
	 *
	 * @return Stargate's size
	 */
	@Override
	protected StargateSizeEnum getStargateSizeConfig(boolean server) {
		return server ? AunisConfig.stargateSize : getRendererStateClient().stargateSize;
	}

	@Override
	protected StargatePegasusRendererState.StargatePegasusRendererStateBuilder getRendererStateServer() {
		return new StargatePegasusRendererState.StargatePegasusRendererStateBuilder(super.getRendererStateServer()).setStargateSize(stargateSize);
	}

	@Override
	protected StargateAbstractRendererState createRendererStateClient() {
		return new StargatePegasusRendererState();
	}

	@Override
	public StargatePegasusRendererState getRendererStateClient() {
		return (StargatePegasusRendererState) super.getRendererStateClient();
	}

	@Override
	protected long getSpinStartOffset() {
		return StargatePegasusRendererState.slotFromChevron(getRendererStateClient().chevronTextureList.getCurrentChevron());
	}

	// -----------------------------------------------------------------
	// States

	@Override
	public State createState(StateTypeEnum stateType) {
		switch (stateType) {


			default:
				return super.createState(stateType);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setState(StateTypeEnum stateType, State state) {
		switch (stateType) {
			case RENDERER_UPDATE:
				StargateRendererActionState gateActionState = (StargateRendererActionState) state;

				switch (gateActionState.action) {
					case CHEVRON_OPEN:
						getRendererStateClient().openChevron(world.getTotalWorldTime());
						break;

					case CHEVRON_CLOSE:
						getRendererStateClient().closeChevron(world.getTotalWorldTime());
						break;

					case CHEVRON_ACTIVATE:
						getRendererStateClient().spinHelper.setSpinning(false);
						ChevronEnum chevron = gateActionState.modifyFinal ? ChevronEnum.getFinal() : getRendererStateClient().chevronTextureList.getNextChevron();
						getRendererStateClient().lockChevron(getRendererStateClient().spinHelper.getTargetSymbol().getId(), chevron);

						break;

					default:
						break;
				}

				break;

			case SPIN_STATE:
				if (getRendererStateClient().chevronTextureList.getNextChevron().rotationIndex == 1) {
					getRendererStateClient().slotToGlyphMap.clear();
				}

				break;

			default:
				break;
		}

		super.setState(stateType, state);
	}

	@Override
	public void addSymbolToAddressManual(SymbolInterface targetSymbol, @Nullable Object context) {
		stargateState = EnumStargateState.DIALING_COMPUTER;

		targetRingSymbol = targetSymbol;

		boolean moveOnly = targetRingSymbol == currentRingSymbol;

		if (moveOnly) {
			addTask(new ScheduledTask(EnumScheduledTask.STARGATE_SPIN_FINISHED, 0));
		} else {
			spinDirection = spinDirection.opposite();

			ChevronEnum targetChevron = targetSymbol.origin() ? ChevronEnum.getFinal() : ChevronEnum.valueOf(dialedAddress.size());
			ChevronEnum currentChevron = dialedAddress.size() == 0 ? ChevronEnum.C1 : ChevronEnum.valueOf(targetChevron.index - 1);

			if (targetSymbol.origin() && dialedAddress.size() == 6) currentChevron = ChevronEnum.C6;

			int indexDiff = StargatePegasusRendererState.slotFromChevron(currentChevron) - StargatePegasusRendererState.slotFromChevron(targetChevron);

			EnumSpinDirection counterDirection = indexDiff < 0 ? EnumSpinDirection.COUNTER_CLOCKWISE : EnumSpinDirection.CLOCKWISE;

			if (spinDirection == counterDirection) {
				indexDiff = StargatePegasusRendererState.GLYPHS_COUNT - Math.abs(indexDiff);
			}

			float distance = (float) Math.abs(indexDiff);
			if (distance <= 20) distance += StargatePegasusRendererState.GLYPHS_COUNT;

			int duration = (int) (distance);

			Aunis.logger.debug("addSymbolToAddressManual: " + "current:" + currentRingSymbol + ", " + "target:" + targetSymbol + ", " + "direction:" + spinDirection + ", " + "distance:" + distance + ", " + "duration:" + duration + ", " + "moveOnly:" + moveOnly);

			AunisPacketHandler.INSTANCE.sendToAllTracking(new StateUpdatePacketToClient(pos, StateTypeEnum.SPIN_STATE, new StargateSpinState(targetRingSymbol, spinDirection, false)), targetPoint);
			addTask(new ScheduledTask(EnumScheduledTask.STARGATE_SPIN_FINISHED, duration - 1));
			playPositionedSound(StargateSoundPositionedEnum.GATE_RING_ROLL, true);

			isSpinning = true;
			spinStartTime = world.getTotalWorldTime();

			ringSpinContext = context;
			if (context != null)
				sendSignal(context, "stargate_spin_start", new Object[]{dialedAddress.size(), stargateWillLock(targetRingSymbol), targetSymbol.getEnglishName()});
		}

		markDirty();
	}

	// -----------------------------------------------------------------
	// Scheduled tasks


	@Override
	public void executeTask(EnumScheduledTask scheduledTask, NBTTagCompound customData) {
		switch (scheduledTask) {
			case STARGATE_ACTIVATE_CHEVRON:
				stargateState = EnumStargateState.IDLE;
				markDirty();

				playSoundEvent(StargateSoundEventEnum.CHEVRON_OPEN);
				sendRenderingUpdate(EnumGateAction.CHEVRON_ACTIVATE, -1, isFinalActive);
				updateChevronLight(dialedAddress.size(), isFinalActive);
				//			AunisPacketHandler.INSTANCE.sendToAllTracking(new StateUpdatePacketToClient(pos, StateTypeEnum.RENDERER_UPDATE, new StargateRendererActionState(EnumGateAction.CHEVRON_ACTIVATE, -1, customData.getBoolean("final"))), targetPoint);
				break;

			case STARGATE_SPIN_FINISHED:
				addTask(new ScheduledTask(EnumScheduledTask.STARGATE_CHEVRON_OPEN, 0));

				break;

			case STARGATE_CHEVRON_OPEN:
				playSoundEvent(StargateSoundEventEnum.CHEVRON_OPEN);
				sendRenderingUpdate(EnumGateAction.CHEVRON_OPEN, 0, false);

				if (canAddSymbol(targetRingSymbol)) {
					addSymbolToAddress(targetRingSymbol);

					if (stargateWillLock(targetRingSymbol)) {
						if (checkAddressAndEnergy(dialedAddress).ok()) {
							addTask(new ScheduledTask(EnumScheduledTask.STARGATE_CHEVRON_OPEN_SECOND, 0));
						} else addTask(new ScheduledTask(EnumScheduledTask.STARGATE_CHEVRON_FAIL, 60));
					} else addTask(new ScheduledTask(EnumScheduledTask.STARGATE_CHEVRON_OPEN_SECOND, 0));
				} else addTask(new ScheduledTask(EnumScheduledTask.STARGATE_CHEVRON_FAIL, 60));

				break;

			case STARGATE_CHEVRON_OPEN_SECOND:
				playSoundEvent(StargateSoundEventEnum.CHEVRON_OPEN);
				addTask(new ScheduledTask(EnumScheduledTask.STARGATE_CHEVRON_LIGHT_UP, 0));

				break;

			case STARGATE_CHEVRON_LIGHT_UP:
				if (stargateWillLock(targetRingSymbol)) sendRenderingUpdate(EnumGateAction.CHEVRON_ACTIVATE, 0, true);
				else sendRenderingUpdate(EnumGateAction.CHEVRON_ACTIVATE, 0, false);

				updateChevronLight(dialedAddress.size(), isFinalActive);

				addTask(new ScheduledTask(EnumScheduledTask.STARGATE_CHEVRON_CLOSE, 1));

				break;

			case STARGATE_CHEVRON_CLOSE:
				if (stargateWillLock(targetRingSymbol)) {
					stargateState = EnumStargateState.IDLE;
					sendSignal(ringSpinContext, "stargate_spin_chevron_engaged", new Object[]{dialedAddress.size(), true, targetRingSymbol.getEnglishName()});
				} else addTask(new ScheduledTask(EnumScheduledTask.STARGATE_CHEVRON_DIM, 1));

				break;

			case STARGATE_CHEVRON_DIM:
				//sendRenderingUpdate(EnumGateAction.CHEVRON_DIM, 0, false);
				stargateState = EnumStargateState.IDLE;

				sendSignal(ringSpinContext, "stargate_spin_chevron_engaged", new Object[]{dialedAddress.size(), false, targetRingSymbol.getEnglishName()});

				break;

			case STARGATE_CHEVRON_FAIL:
				sendRenderingUpdate(EnumGateAction.CHEVRON_CLOSE, 0, false);
				dialingFailed(checkAddressAndEnergy(dialedAddress));

				break;

			default:
				break;
		}

		super.executeTask(scheduledTask, customData);
	}

	@Override
	public int getSupportedCapacitors() {
		return 3;
	}
}
