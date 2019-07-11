package mrjake.aunis.renderer.state;

import io.netty.buffer.ByteBuf;
import mrjake.aunis.stargate.EnumSpinDirection;

public class SpinState extends RendererState {
	
	/**
	 * Stores world's time when rotation started.
	 */
	public long tickStart;
	
	/**
	 * Stores starting ring pos to correctly shift the next rotation
	 * 
	 * Set by requestStart()
	 */
	public double startingRotation;

	/**
	 * Defines when ring is spinning ie. no stop animation was performed.
	 */
	public boolean isSpinning;
	
	/**
	 * Set by requestStop(). Set when ring needs to be stopped.
	 */
	public boolean stopRequested;
	
	/**
	 * Time when requestStop() was called
	 */
	public long tickStopRequested;
	
	/**
	 * Spin direction
	 */
	public EnumSpinDirection direction;
	

	public SpinState() {
		this(0, 0, false, false, 0, EnumSpinDirection.COUNTER_CLOCKWISE);
	}
	
	public SpinState(
			double startingRotation,
			long tickStart,
			boolean isSpinning,
			boolean stopRequested,
			long tickStopRequested,
			EnumSpinDirection direction) {

		this.startingRotation = startingRotation;
		
		this.tickStart = tickStart;
		this.isSpinning = isSpinning;
		this.stopRequested = stopRequested;
		this.tickStopRequested = tickStopRequested;
		this.direction = direction;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(startingRotation);
		
		buf.writeLong(tickStart);
		buf.writeBoolean(isSpinning);
		buf.writeBoolean(stopRequested);
		buf.writeLong(tickStopRequested);
		buf.writeInt(direction.id);
	}

	@Override
	public RendererState fromBytes(ByteBuf buf) {
		startingRotation = buf.readDouble();
		
		tickStart = buf.readLong();
		isSpinning = buf.readBoolean();
		stopRequested = buf.readBoolean();
		tickStopRequested = buf.readLong();
		direction = EnumSpinDirection.valueOf(buf.readInt());
		
		return this;
	}

	@Override
	protected String getKeyName() {
		return "spinState";
	}

}
