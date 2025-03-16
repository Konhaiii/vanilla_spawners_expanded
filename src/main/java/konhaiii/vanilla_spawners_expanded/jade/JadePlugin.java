package konhaiii.vanilla_spawners_expanded.jade;

import konhaiii.vanilla_spawners_expanded.block.calibrated_spawner.CalibratedSpawnerBlock;
import konhaiii.vanilla_spawners_expanded.block.calibrated_spawner.CalibratedSpawnerBlockEntity;
import net.minecraft.util.Identifier;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {

	public static final Identifier UID_ENTITY = Identifier.tryParse("debug:calibrated_spawner");
	private static IWailaClientRegistration client;

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.registerBlockDataProvider(BlockComponentProvider.INSTANCE, CalibratedSpawnerBlockEntity.class);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		JadePlugin.client = registration;
		registration.registerBlockComponent(BlockComponentProvider.INSTANCE, CalibratedSpawnerBlock.class);
	}
}