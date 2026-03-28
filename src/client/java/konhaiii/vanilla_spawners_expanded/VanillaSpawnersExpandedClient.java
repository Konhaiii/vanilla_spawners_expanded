package konhaiii.vanilla_spawners_expanded;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class VanillaSpawnersExpandedClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRenderers.register(ModBlocks.CALIBRATED_SPAWNER_BLOCK_ENTITY, CalibratedSpawnerBlockEntityRenderer::new);
	}
}