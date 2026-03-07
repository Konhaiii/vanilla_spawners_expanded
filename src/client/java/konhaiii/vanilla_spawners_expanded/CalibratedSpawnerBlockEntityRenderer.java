package konhaiii.vanilla_spawners_expanded;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import konhaiii.vanilla_spawners_expanded.blocks.BaseCalibratedSpawner;
import konhaiii.vanilla_spawners_expanded.blocks.CalibratedSpawnerBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.SpawnerRenderState;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class CalibratedSpawnerBlockEntityRenderer implements BlockEntityRenderer<@NotNull CalibratedSpawnerBlockEntity, @NotNull SpawnerRenderState> {
	private final EntityRenderDispatcher entityRenderer;

	public CalibratedSpawnerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.entityRenderer = context.entityRenderer();
	}

	public SpawnerRenderState createRenderState() {
		return new SpawnerRenderState();
	}

	public void extractRenderState(
			CalibratedSpawnerBlockEntity calibratedSpawnerBlockEntity,
			SpawnerRenderState spawnerRenderState,
			float f,
			@NotNull Vec3 vec3,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay
	) {
		BlockEntityRenderer.super.extractRenderState(calibratedSpawnerBlockEntity, spawnerRenderState, f, vec3, crumblingOverlay);
		if (calibratedSpawnerBlockEntity.getLevel() != null) {
			BaseCalibratedSpawner baseCalibratedSpawner = calibratedSpawnerBlockEntity.getSpawner();
			Entity entity = baseCalibratedSpawner.getOrCreateDisplayEntity(calibratedSpawnerBlockEntity.getLevel(), calibratedSpawnerBlockEntity.getBlockPos());
			extractSpawnerData(spawnerRenderState, f, entity, this.entityRenderer, baseCalibratedSpawner.getOSpin(), baseCalibratedSpawner.getSpin());
		}
	}

	static void extractSpawnerData(
			SpawnerRenderState spawnerRenderState, float f, @Nullable Entity entity, EntityRenderDispatcher entityRenderDispatcher, double d, double e
	) {
		if (entity != null) {
			spawnerRenderState.displayEntity = entityRenderDispatcher.extractEntity(entity, f);
			spawnerRenderState.displayEntity.lightCoords = spawnerRenderState.lightCoords;
			spawnerRenderState.spin = (float) Mth.lerp(f, d, e) * 10.0F;
			spawnerRenderState.scale = 0.53125F;
			float g = Math.max(entity.getBbWidth(), entity.getBbHeight());
			if (g > 1.0) {
				spawnerRenderState.scale /= g;
			}
		}
	}

	public void submit(SpawnerRenderState spawnerRenderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState cameraRenderState) {
		if (spawnerRenderState.displayEntity != null) {
			submitEntityInSpawner(
					poseStack, submitNodeCollector, spawnerRenderState.displayEntity, this.entityRenderer, spawnerRenderState.spin, spawnerRenderState.scale, cameraRenderState
			);
		}
	}

	public static void submitEntityInSpawner(
			PoseStack poseStack,
			SubmitNodeCollector submitNodeCollector,
			EntityRenderState entityRenderState,
			EntityRenderDispatcher entityRenderDispatcher,
			float f,
			float g,
			CameraRenderState cameraRenderState
	) {
		poseStack.pushPose();
		poseStack.translate(0.5F, 0.4F, 0.5F);
		poseStack.mulPose(Axis.YP.rotationDegrees(f));
		poseStack.translate(0.0F, -0.2F, 0.0F);
		poseStack.mulPose(Axis.XP.rotationDegrees(-30.0F));
		poseStack.scale(g, g, g);
		entityRenderDispatcher.submit(entityRenderState, cameraRenderState, 0.0, 0.0, 0.0, poseStack, submitNodeCollector);
		poseStack.popPose();
	}
}