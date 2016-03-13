package de.jens.Merchants;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import de.jens.Merchants.CraftMerchant.TradeRecipe;

public class HowToUse implements Listener {
	
	private CraftMerchant merchant;
	private TradeRecipe recipe;
	private TradeRecipe recipe2;
	
	// Spawn Merchant when Player joins...
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		
		merchant = new CraftMerchant("§6§lShop",
				e.getPlayer().getLocation(),
				MerchantMain.getMain());
		
		recipe = merchant.new TradeRecipe(new ItemStack(Material.DIAMOND),
				new ItemStack(Material.DIAMOND_AXE),
				10);
		
		recipe2 = merchant.new TradeRecipe(new ItemStack(Material.RED_ROSE),
				new ItemStack(Material.APPLE),
				new ItemStack(Material.ANVIL),
				99999);
		
		merchant.addRecipes(recipe, recipe2);
		
		merchant.craft();
	}
	
	// Change Merchant offer in Runtime...
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		
		if(e.getMessage().equalsIgnoreCase("change")){
			merchant.removeRecipes(recipe, recipe2); // Merchant will be invisible when there are 0 offers
		}
	}
}
