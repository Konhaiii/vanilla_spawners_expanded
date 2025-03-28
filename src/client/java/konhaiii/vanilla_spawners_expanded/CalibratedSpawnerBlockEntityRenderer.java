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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class CalibratedSpawnerBlockEntityRenderer implements BlockEntityRenderer<CalibratedSpawnerBlockEntity> {
	private final EntityRenderDispatcher entityRenderDispatcher;

	public CalibratedSpawnerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		this.entityRenderDispatcher = ctx.getEntityRenderDispatcher();
	}

	public void render(CalibratedSpawnerBlockEntity calibratedSpawnerBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, Vec3d vec3d) {
		World world = calibratedSpawnerBlockEntity.getWorld();
		if (world != null) {
			CalibratedSpawnerLogic calibratedSpawnerLogic = calibratedSpawnerBlockEntity.getLogic();
			Entity entity = calibratedSpawnerLogic.getRenderedEntity(world, calibratedSpawnerBlockEntity.getPos());
			if (entity != null) {
				render(f, matrixStack, vertexConsumerProvider, i, entity, this.entityRenderDispatcher, calibratedSpawnerLogic.getLastRotation(), calibratedSpawnerLogic.getRotation());
			}

		}
	}

	public static void render(
			float tickDelta,
			MatrixStack matrices,
			VertexConsumerProvider vertexConsumers,
			int light,
			Entity entity,
			EntityRenderDispatcher entityRenderDispatcher,
			double lastRotation,
			double rotation
	) {
		matrices.push();
		matrices.translate(0.5F, 0.0F, 0.5F);
		float f = 0.53125F;
		float g = Math.max(entity.getWidth(), entity.getHeight());
		if ((double)g > 1.0) {
			f /= g;
		}

		matrices.translate(0.0F, 0.4F, 0.0F);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) MathHelper.lerp(tickDelta, lastRotation, rotation) * 10.0F));
		matrices.translate(0.0F, -0.2F, 0.0F);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-30.0F));
		matrices.scale(f, f, f);
		entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, tickDelta, matrices, vertexConsumers, light);
		matrices.pop();
	}
}
