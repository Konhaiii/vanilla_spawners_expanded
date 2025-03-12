/*
 * Decompiled with CFR 0.2.2 (FabricMC 7c48b8c4).
 */
package konhaiii.vanilla_spawners_expanded;

import konhaiii.vanilla_spawners_expanded.block.calibrated_spawner.CalibratedSpawnerBlockEntity;
import konhaiii.vanilla_spawners_expanded.block.calibrated_spawner.CalibratedSpawnerLogic;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

@Environment(value=EnvType.CLIENT)
public class CalibratedSpawnerBlockEntityRenderer
		implements BlockEntityRenderer<CalibratedSpawnerBlockEntity> {
	private final EntityRenderDispatcher entityRenderDispatcher;

	public CalibratedSpawnerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		this.entityRenderDispatcher = ctx.getEntityRenderDispatcher();
	}

	@Override
	public void render(CalibratedSpawnerBlockEntity calibratedSpawnerBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
		World world = calibratedSpawnerBlockEntity.getWorld();
		if (world == null) {
			return;
		}
		CalibratedSpawnerLogic mobSpawnerLogic = calibratedSpawnerBlockEntity.getLogic();
		Entity entity = mobSpawnerLogic.getRenderedEntity(world, calibratedSpawnerBlockEntity.getPos());
		if (entity != null) {
			CalibratedSpawnerBlockEntityRenderer.render(f, matrixStack, vertexConsumerProvider, i, entity, this.entityRenderDispatcher, mobSpawnerLogic.getLastRotation(), mobSpawnerLogic.getRotation());
		}
	}

	public static void render(float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, EntityRenderDispatcher entityRenderDispatcher, double lastRotation, double rotation) {
		matrices.push();
		matrices.translate(0.5f, 0.0f, 0.5f);
		float f = 0.53125f;
		float g = Math.max(entity.getWidth(), entity.getHeight());
		if ((double)g > 1.0) {
			f /= g;
		}
		matrices.translate(0.0f, 0.4f, 0.0f);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)MathHelper.lerp(tickDelta, lastRotation, rotation) * 10.0f));
		matrices.translate(0.0f, -0.2f, 0.0f);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-30.0f));
		matrices.scale(f, f, f);
		entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0f, tickDelta, matrices, vertexConsumers, light);
		matrices.pop();
	}
}

