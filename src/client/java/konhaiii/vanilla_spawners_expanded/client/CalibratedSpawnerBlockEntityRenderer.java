package konhaiii.vanilla_spawners_expanded.client;

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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class CalibratedSpawnerBlockEntityRenderer implements BlockEntityRenderer<@NotNull CalibratedSpawnerBlockEntity, @NotNull SpawnerRenderState> {
	private final EntityRenderDispatcher entityRenderer;

	public CalibratedSpawnerBlockEntityRenderer(final BlockEntityRendererProvider.Context context) {
		this.entityRenderer = context.entityRenderer();
	}

	public @NonNull SpawnerRenderState createRenderState() {
		return new SpawnerRenderState();
	}

	public void extractRenderState(
			final @NonNull CalibratedSpawnerBlockEntity blockEntity,
			final @NonNull SpawnerRenderState state,
			final float partialTicks,
			final @NotNull Vec3 cameraPosition,
			final ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
	) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		if (blockEntity.getLevel() != null) {
			BaseCalibratedSpawner spawner = blockEntity.getSpawner();
			Entity displayEntity = spawner.getOrCreateDisplayEntity(blockEntity.getLevel(), blockEntity.getBlockPos());
			extractSpawnerData(state, partialTicks, displayEntity, this.entityRenderer, spawner.getOSpin(), spawner.getSpin());
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

	public void submit(final SpawnerRenderState state, final @NotNull PoseStack poseStack, final @NotNull SubmitNodeCollector submitNodeCollector, final net.minecraft.client.renderer.state.level.@NotNull CameraRenderState camera) {
		if (state.displayEntity != null) {
			submitEntityInSpawner(poseStack, submitNodeCollector, state.displayEntity, this.entityRenderer, state.spin, state.scale, camera);
		}
	}

	public static void submitEntityInSpawner(
			final PoseStack poseStack,
			final SubmitNodeCollector submitNodeCollector,
			final EntityRenderState displayEntity,
			final EntityRenderDispatcher entityRenderer,
			final float spin,
			final float scale,
			final net.minecraft.client.renderer.state.level.CameraRenderState camera
	) {
		poseStack.pushPose();
		poseStack.translate(0.5F, 0.4F, 0.5F);
		poseStack.mulPose(Axis.YP.rotationDegrees(spin));
		poseStack.translate(0.0F, -0.2F, 0.0F);
		poseStack.mulPose(Axis.XP.rotationDegrees(-30.0F));
		poseStack.scale(scale, scale, scale);
		entityRenderer.submit(displayEntity, camera, 0.0, 0.0, 0.0, poseStack, submitNodeCollector);
		poseStack.popPose();
	}
}