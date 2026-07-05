package konhaiii.vanilla_spawners_expanded.client.jade;

import konhaiii.vanilla_spawners_expanded.blocks.CalibratedSpawnerBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;

public class JadeClientPlugin implements IWailaPlugin {

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerBlockComponent(JadeClientProvider.INSTANCE, CalibratedSpawnerBlock.class);
	}
}