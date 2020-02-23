package mrjake.aunis.tileentity.stargate;

import java.util.Arrays;
import java.util.List;

import mrjake.aunis.Aunis;
import mrjake.aunis.AunisProps;
import mrjake.aunis.gui.StargateOrlinGui;
import mrjake.aunis.packet.AunisPacketHandler;
import mrjake.aunis.packet.StateUpdatePacketToClient;
import mrjake.aunis.packet.stargate.StargateRenderingUpdatePacketToServer;
import mrjake.aunis.sound.AunisSoundHelper;
import mrjake.aunis.sound.EnumAunisSoundEvent;
import mrjake.aunis.stargate.EnumScheduledTask;
import mrjake.aunis.stargate.EnumStargateState;
import mrjake.aunis.stargate.StargateAbstractMergeHelper;
import mrjake.aunis.stargate.StargateNetwork;
import mrjake.aunis.stargate.StargateOrlinMergeHelper;
import mrjake.aunis.state.StargateAbstractRendererState;
import mrjake.aunis.state.StargateOrlinGuiState;
import mrjake.aunis.state.StargateOrlinRendererState;
import mrjake.aunis.state.StargateOrlinSparkState;
import mrjake.aunis.state.State;
import mrjake.aunis.state.StateTypeEnum;
import mrjake.aunis.tileentity.util.ScheduledTask;
import mrjake.aunis.util.AunisAxisAlignedBB;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StargateOrlinBaseTile extends StargateAbstractBaseTile {
	
	// ------------------------------------------------------------------------
	// Ticking
	
	@Override
	protected BlockPos getGateCenterPos() {
		return pos.offset(EnumFacing.UP, 1);
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		
		if (!world.isRemote) {
			StargateNetwork.get(world).addOrlinAddress(gateAddress);
		}
	}
	
	public long animStart;
	
	@Override
	public void update() {
		super.update();
		
		if (world.isRemote) {
			if (!world.getBlockState(pos).getValue(AunisProps.RENDER_BLOCK) && rendererStateClient != null)
				Aunis.proxy.orlinRendererSpawnParticles(world, getRendererStateClient());
		}
	}
	
	
	// ------------------------------------------------------------------------
	// Redstone
	
	private boolean isPowered;
	
	public void redstonePowerUpdate(boolean power) {
		if ((isPowered && !power) || (!isPowered && power)) {
			isPowered = power;
			
			if (isPowered) {
				if (stargateState == EnumStargateState.IDLE) {
					if (StargateRenderingUpdatePacketToServer.checkDialedAddress(world, this)) {
						startSparks();
						AunisSoundHelper.playSoundEvent(world, pos, EnumAunisSoundEvent.GATE_ORLIN_DIAL, 1.0f);
						
						addTask(new ScheduledTask(EnumScheduledTask.STARGATE_ORLIN_OPEN));
					}
					
					else {
						Aunis.info("wrong dialed address");
					}
				}
			}
			
			else {
				if (stargateState == EnumStargateState.ENGAGED_INITIATING)
					StargateRenderingUpdatePacketToServer.closeGatePacket(this, false);
			}
			
			markDirty();
			Aunis.info("Gate is powered: " + isPowered);
		}
	}
	
	
	// ------------------------------------------------------------------------
	// Merging
	
	@Override
	protected void unmergeGate() {}
	
	@Override
	protected void mergeGate() {}
	
	@Override
	protected StargateAbstractMergeHelper getMergeHelper() {
		return StargateOrlinMergeHelper.INSTANCE;
	}
		
	
	// ------------------------------------------------------------------------
	// Killing
	
	@Override
	protected AunisAxisAlignedBB getHorizonKillingBox(boolean server) {
		return new AunisAxisAlignedBB(-0.5, 1, -0.5, 0.5, 2, 1.5);
	}
	
	@Override
	protected int getHorizonSegmentCount(boolean server) {
		return 2;
	}
	
	@Override
	protected List<AunisAxisAlignedBB> getGateVaporizingBoxes(boolean server) {
		return Arrays.asList(new AunisAxisAlignedBB(-0.5, 1, -0.5, 0.5, 2, 0.5));
	}
		
	
	// ------------------------------------------------------------------------
	// Rendering
	
	@Override
	protected AunisAxisAlignedBB getHorizonTeleportBox(boolean server) {
		return new AunisAxisAlignedBB(-1.0, 0.6, -0.15, 1.0, 2.7, -0.05);
	}
	
	@Override
	protected StargateAbstractRendererState getRendererStateServer() {
		return new StargateOrlinRendererState(stargateState);
	}
	
	@Override
	protected StargateAbstractRendererState createRendererStateClient() {
		return new StargateOrlinRendererState();
	}
	
	@Override
	public StargateOrlinRendererState getRendererStateClient() {
		return (StargateOrlinRendererState) super.getRendererStateClient();
	}
	
	
	// ------------------------------------------------------------------------
	// States
	
	@Override
	public State getState(StateTypeEnum stateType) {
		switch (stateType) {
			case GUI_STATE:
				return new StargateOrlinGuiState(energyStorage.getEnergyStored(), energyStorage.getMaxEnergyStored());
		
			case SPARK_STATE:
				return null;
				// Shouldn't be done this way
				
			default:
				return super.getState(stateType);
		}
	}

	@Override
	public State createState(StateTypeEnum stateType) {
		switch (stateType) {
			case GUI_STATE:
				return new StargateOrlinGuiState();
		
			case SPARK_STATE:
				return new StargateOrlinSparkState();
				
			default:
				return super.createState(stateType);
		}
	}

	private StargateOrlinGui stargateGui;
	
	@Override
	@SideOnly(Side.CLIENT)
	public void setState(StateTypeEnum stateType, State state) {
		switch (stateType) {
			case GUI_STATE:
				if (stargateGui == null || !stargateGui.isOpen) {
					stargateGui = new StargateOrlinGui(pos, (StargateOrlinGuiState) state);
					Minecraft.getMinecraft().displayGuiScreen(stargateGui);
				}
				
				else {
					stargateGui.state = (StargateOrlinGuiState) state;
				}
				
				break;
		
			case SPARK_STATE:
				StargateOrlinSparkState sparkState = (StargateOrlinSparkState) state;
				getRendererStateClient().sparkFrom(sparkState.sparkIndex, sparkState.spartStart);
				
				break;
				
			default:
				super.setState(stateType, state);
				break;
		}
	}
	
	
	// ------------------------------------------------------------------------
	// Sparks
	
	
	private int sparkIndex;
	
	public void startSparks() {
		sparkIndex = 0;
		
		addTask(new ScheduledTask(EnumScheduledTask.STARGATE_ORLIN_SPARK, 5));
	}
	
	// ------------------------------------------------------------------------
	// Scheduled tasks
	
	@Override
	public void executeTask(EnumScheduledTask scheduledTask, NBTTagCompound customData) {
		switch (scheduledTask) {
			case STARGATE_ORLIN_OPEN:
				StargateRenderingUpdatePacketToServer.attemptLightUp(world, this);
				StargateRenderingUpdatePacketToServer.attemptOpen(world, this, null, false);
				
				break;
				
			case STARGATE_ORLIN_SPARK:
				Aunis.info("sparkIndex: " + sparkIndex);
				
				AunisPacketHandler.INSTANCE.sendToAllTracking(new StateUpdatePacketToClient(pos, StateTypeEnum.SPARK_STATE, new StargateOrlinSparkState(sparkIndex, world.getTotalWorldTime())), targetPoint);
				
				if (sparkIndex < 6 && sparkIndex != -1)
					addTask(new ScheduledTask(EnumScheduledTask.STARGATE_ORLIN_SPARK, 24));
				
				sparkIndex++;
				
				break;
				
			default:
				super.executeTask(scheduledTask, customData);
				break;
		}
	}
	

	// ------------------------------------------------------------------------
	// OpenComputers
	
	
	
	
	// ------------------------------------------------------------------------
	// NBT
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setBoolean("isPowered", isPowered);
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		isPowered = compound.getBoolean("isPowered");
		
		super.readFromNBT(compound);
	}
}
