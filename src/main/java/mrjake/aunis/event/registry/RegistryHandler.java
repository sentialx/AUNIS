package mrjake.aunis.event.registry;

import mrjake.aunis.block.AunisBlocks;
import mrjake.aunis.item.AunisItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@EventBusSubscriber
public class RegistryHandler {
	
	@SuppressWarnings("deprecation")
	@SubscribeEvent
    public static void registerBlocks(Register<Block> event) {
    	final Block[] blocks = {
    			AunisBlocks.stargateBaseBlock,
    			AunisBlocks.ringBlock,
    			AunisBlocks.chevronBlock,
    			AunisBlocks.dhdBlock
    	};
    	
    	event.getRegistry().registerAll(blocks);
    	GameRegistry.registerTileEntity(AunisBlocks.stargateBaseBlock.getTileEntityClass(), AunisBlocks.stargateBaseBlock.getRegistryName().toString());
    	GameRegistry.registerTileEntity(AunisBlocks.dhdBlock.getTileEntityClass(), AunisBlocks.dhdBlock.getRegistryName().toString());
    	
    }
	
    @SubscribeEvent
    public static void registerItems(Register<Item> event) {
        final Item[] items = {
        		AunisItems.naquadahOreShard,
        		AunisItems.pureNaquadahOre,
        		AunisItems.refinedNaquadah
        };
        
        final Item[] ItemBlocks = {
        		AunisBlocks.stargateBaseBlock.getItemBlock(),
        		AunisBlocks.ringBlock.getItemBlock(),
        		AunisBlocks.chevronBlock.getItemBlock(),
        		AunisBlocks.dhdBlock.getItemBlock()
        };
        
        event.getRegistry().registerAll(items);
        event.getRegistry().registerAll(ItemBlocks);
    }
}