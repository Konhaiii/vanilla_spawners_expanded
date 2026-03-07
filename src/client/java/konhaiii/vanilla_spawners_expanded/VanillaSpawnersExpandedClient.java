package konhaiii.vanilla_spawners_expanded;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class VanillaSpawnersExpandedClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.putBlock(ModBlocks.CALIBRATED_SPAWNER, ChunkSectionLayer.CUTOUT);
		BlockEntityRenderers.register(ModBlocks.CALIBRATED_SPAWNER_BLOCK_ENTITY, CalibratedSpawnerBlockEntityRenderer::new);
	}
}