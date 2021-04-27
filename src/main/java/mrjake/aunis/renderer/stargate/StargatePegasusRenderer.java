package mrjake.aunis.renderer.stargate;

import mrjake.aunis.loader.ElementEnum;
import mrjake.aunis.loader.texture.TextureLoader;
import mrjake.aunis.util.math.MathFunction;
import mrjake.aunis.util.math.MathFunctionImpl;
import mrjake.aunis.util.math.MathRange;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class StargatePegasusRenderer extends StargateClassicRenderer<StargatePegasusRendererState> {

  private static final Vec3d RING_LOC = new Vec3d(0.0, -0.122333, -0.000597);
  private static final float GATE_DIAMETER = 10.1815f;

  @Override
  protected void applyTransformations(StargatePegasusRendererState rendererState) {
    GlStateManager.translate(0.50, GATE_DIAMETER / 2 + rendererState.stargateSize.renderTranslationY, 0.50);
    GlStateManager.scale(rendererState.stargateSize.renderScale, rendererState.stargateSize.renderScale, rendererState.stargateSize.renderScale);
  }

  @Override
  protected void renderGate(StargatePegasusRendererState rendererState, double partialTicks) {
    renderRing(rendererState, partialTicks);
    GlStateManager.rotate(rendererState.horizontalRotation, 0, 1, 0);
    renderChevrons(rendererState, partialTicks);

    ElementEnum.PEGASUS_GATE.bindTextureAndRender(rendererState.getBiomeOverlay());
  }

  // ----------------------------------------------------------------------------------------
  // Ring

  private void renderRing(StargatePegasusRendererState rendererState, double partialTicks) {
    GlStateManager.pushMatrix();
    float angularRotation = rendererState.spinHelper.getCurrentSymbol().getAngle();

    if (rendererState.spinHelper.getIsSpinning())
      angularRotation += rendererState.spinHelper.apply(getWorld().getTotalWorldTime() + partialTicks);

    if (rendererState.horizontalRotation == 90 || rendererState.horizontalRotation == 0) angularRotation *= -1;


    if (rendererState.horizontalRotation == 90 || rendererState.horizontalRotation == 270) {
      GlStateManager.translate(RING_LOC.y, RING_LOC.z, RING_LOC.x);
      GlStateManager.rotate(angularRotation, 1, 0, 0);
      GlStateManager.translate(-RING_LOC.y, -RING_LOC.z, -RING_LOC.x);
    } else {
      GlStateManager.translate(RING_LOC.x, RING_LOC.z, RING_LOC.y);
      GlStateManager.rotate(angularRotation, 0, 0, 1);
      GlStateManager.translate(-RING_LOC.x, -RING_LOC.z, -RING_LOC.y);
    }

    GlStateManager.rotate(rendererState.horizontalRotation, 0, 1, 0);

    ElementEnum.PEGASUS_RING.bindTextureAndRender(rendererState.getBiomeOverlay());

    GlStateManager.popMatrix();
  }


  // ----------------------------------------------------------------------------------------
  // Chevrons

  private float calculateTopChevronOffset(StargatePegasusRendererState rendererState, double partialTicks) {
    return 0;
  }

  @Override
  protected void renderChevron(StargatePegasusRendererState rendererState, double partialTicks, ChevronEnum chevron) {
    GlStateManager.pushMatrix();

    GlStateManager.rotate(chevron.rotation, 0, 0, 1);

    TextureLoader.getTexture(rendererState.chevronTextureList.get(rendererState.getBiomeOverlay(), chevron)).bindTexture();

    if (chevron.isFinal()) {
      float chevronOffset = calculateTopChevronOffset(rendererState, partialTicks);

      GlStateManager.pushMatrix();

      GlStateManager.translate(0, chevronOffset, 0);
      ElementEnum.PEGASUS_CHEVRON_LIGHT.render();

      GlStateManager.translate(0, -2 * chevronOffset, 0);
      ElementEnum.PEGASUS_CHEVRON_MOVING.render();

      GlStateManager.popMatrix();
    } else {
      ElementEnum.PEGASUS_CHEVRON_MOVING.render();
      ElementEnum.PEGASUS_CHEVRON_LIGHT.render();
    }

    ElementEnum.PEGASUS_CHEVRON_FRAME.bindTextureAndRender(rendererState.getBiomeOverlay());
    ElementEnum.PEGASUS_CHEVRON_BACK.render();


    GlStateManager.popMatrix();
  }
}
