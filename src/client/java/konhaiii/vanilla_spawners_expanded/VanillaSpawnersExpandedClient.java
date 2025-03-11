package konhaiii.vanilla_spawners_expanded;

import konhaiii.vanilla_spawners_expanded.block.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class VanillaSpawnersExpandedClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CALIBRATED_SPAWNER, RenderLayer.getCutout());
		BlockEntityRendererFactories.register(ModBlocks.CALIBRATED_SPAWNER_BLOCK_ENTITY, CalibratedSpawnerBlockEntityRenderer::new);
    }
}