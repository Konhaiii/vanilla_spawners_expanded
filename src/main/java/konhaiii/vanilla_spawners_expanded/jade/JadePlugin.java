package konhaiii.vanilla_spawners_expanded.jade;

import konhaiii.vanilla_spawners_expanded.blocks.CalibratedSpawnerBlock;
import net.minecraft.resources.Identifier;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {

	public static final Identifier UID_ENTITY = Identifier.fromNamespaceAndPath("vanilla_spawners_expanded", "calibrated_spawner");

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.registerBlockDataProvider(BlockComponentProvider.INSTANCE, CalibratedSpawnerBlock.class);
	}
}