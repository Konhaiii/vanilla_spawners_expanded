package konhaiii.vanilla_spawners_expanded.jade;

import konhaiii.vanilla_spawners_expanded.blocks.CalibratedSpawnerBlock;
import net.minecraft.resources.Identifier;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;

public class JadeClientPlugin implements IWailaPlugin {

	public static final Identifier UID_ENTITY = Identifier.fromNamespaceAndPath("vanilla_spawners_expanded", "calibrated_spawner");

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerBlockComponent(JadeClientProvider.INSTANCE, CalibratedSpawnerBlock.class);
	}
}