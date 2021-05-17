package mrjake.aunis.stargate;

import io.netty.buffer.ByteBuf;
import mrjake.aunis.Aunis;
import mrjake.aunis.renderer.stargate.StargatePegasusRendererState;
import mrjake.aunis.stargate.network.SymbolInterface;
import mrjake.aunis.stargate.network.SymbolTypeEnum;

/**
 * Client-side class helping with the ring's rotation.
 *
 * @author sentialx
 */
public class StargatePegasusSpinHelper implements ISpinHelper {
	public StargatePegasusSpinHelper() {
	}

	public static final float A_ANGLE_PER_TICK = 5;

	public SymbolTypeEnum symbolType;

	private boolean spinning;
	public SymbolInterface currentSymbol;
	public EnumSpinDirection direction = EnumSpinDirection.CLOCKWISE;

	private long spinStartTime;
	private SymbolInterface targetSymbol;
	private float startOffset;


	public boolean isSpinning() {
		return spinning;
	}

	public void setSpinning(boolean value) {
		spinning = value;
	}

	public SymbolInterface getCurrentSymbol() {
		return currentSymbol;
	}

	public void setCurrentSymbol(SymbolInterface symbol) {
		currentSymbol = symbol;
	}

	public SymbolInterface getTargetSymbol() {
		return targetSymbol;
	}

	public StargatePegasusSpinHelper(SymbolTypeEnum symbolType, SymbolInterface currentSymbol, EnumSpinDirection spinDirection, boolean isSpinning, SymbolInterface targetRingSymbol, long spinStartTime) {
		this.symbolType = symbolType;
		this.currentSymbol = currentSymbol;
		this.direction = spinDirection;
		this.spinning = isSpinning;
		this.targetSymbol = targetRingSymbol;
		this.spinStartTime = spinStartTime;
	}

	public void initRotation(long totalWorldTime, SymbolInterface targetSymbol, EnumSpinDirection direction, float startOffset) {
		this.targetSymbol = targetSymbol;
		this.direction = direction;
		this.spinStartTime = totalWorldTime;
		this.startOffset = startOffset;

		spinning = true;
	}

	private float calculate(float tick) {
		if (tick < 0) {
			Aunis.logger.warn("Negative argument");
			return 0;
		}

		return (tick) % StargatePegasusRendererState.GLYPHS_COUNT;
	}

	public float apply(double tick) {
		float slot = calculate((float) tick - spinStartTime);
		int glyphsCount = StargatePegasusRendererState.GLYPHS_COUNT;
		return ((direction.mul == -1 ? glyphsCount - slot : slot) + startOffset) % glyphsCount;
	}

	public void toBytes(ByteBuf buf) {
		buf.writeInt(symbolType.id);

		buf.writeBoolean(spinning);
		buf.writeInt(currentSymbol.getId());
		buf.writeInt(direction.id);

		buf.writeLong(spinStartTime);
		buf.writeInt(targetSymbol.getId());
	}

	public void fromBytes(ByteBuf buf) {
		symbolType = SymbolTypeEnum.valueOf(buf.readInt());

		spinning = buf.readBoolean();
		currentSymbol = symbolType.valueOfSymbol(buf.readInt());
		direction = EnumSpinDirection.valueOf(buf.readInt());

		spinStartTime = buf.readLong();
		targetSymbol = symbolType.valueOfSymbol(buf.readInt());

		if (spinning) {
			initRotation(spinStartTime, targetSymbol, direction, 0);
		}
	}
}
