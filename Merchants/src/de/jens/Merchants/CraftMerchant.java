package de.jens.Merchants;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_9_R1.EntityVillager;
import net.minecraft.server.v1_9_R1.MerchantRecipe;
import net.minecraft.server.v1_9_R1.MerchantRecipeList;
import net.minecraft.server.v1_9_R1.WorldServer;

public class CraftMerchant {
	
	private Plugin plugin;
	
	private String title;
	private Location loc;
	private WorldServer world;
	private EntityVillager villager;
	private BukkitRunnable runnable;
	
	private boolean isAlive = false;
	
	private final List<TradeRecipe> recipes = new ArrayList<>();
	
	private final MerchantRecipeList merchantList = new MerchantRecipeList();
	
	public CraftMerchant(String title, Location loca, Plugin plugin) {
		this.plugin = plugin;
		
		this.title = title;
		this.loc = loca;
		this.world = ((CraftWorld)loc.getWorld()).getHandle();
		this.villager = new EntityVillager(this.world);
		
		this.runnable = new BukkitRunnable() {
			
			@Override
			public void run() {
				
				if(isAlive){
					loc = new Location(villager.getWorld().getWorld(),
							villager.locX,
							villager.locY,
							villager.locZ);
					
					if(recipes.size() <= 0){
						villager.setInvisible(true);
					} else {
						villager.setInvisible(false);
					}
				}
			}
		};
		
		this.runnable.runTaskTimer(this.plugin,
				0L,
				1L);
	}
	
	public void addRecipes(TradeRecipe... recipes){
		for(TradeRecipe recipe : recipes){
			
			this.addRecipe(recipe);
			
		}
	}
	
	public void addRecipe(TradeRecipe tRecipe){
		if(!recipes.contains(tRecipe)){
			
			MerchantRecipe recipe;
			
			if(tRecipe.getRequirementTwoAsNMS() != null){
				recipe = new MerchantRecipe(tRecipe.getRequirementOneAsNMS(),
						tRecipe.getRequirementTwoAsNMS(),
						tRecipe.getResultAsNMS());
			} else {
			    recipe = new MerchantRecipe(tRecipe.getRequirementOneAsNMS(),
					    tRecipe.getResultAsNMS());
			}
		
		    recipe.a(tRecipe.getMaxUses());
			    
			if(!isAlive){
				
				merchantList.add(recipe);
				
			} else {				
				merchantList.add(recipe);
				
				this.updateMerchantList();
			}
			
			recipes.add(tRecipe);
		}
	}
	
	public void removeRecipes(TradeRecipe... recipes){
		for(TradeRecipe recipe : recipes){
			
			this.removeRecipe(recipe);
			
		}
	}
	
	public void removeRecipe(TradeRecipe tRecipe){
		if(recipes.contains(tRecipe)){
			
			recipes.remove(tRecipe);
			
			merchantList.clear();
			
			for(TradeRecipe tRecipes : recipes){
				
				MerchantRecipe recipe;
				
				if(tRecipes.getRequirementTwoAsNMS() != null){
					recipe = new MerchantRecipe(tRecipes.getRequirementOneAsNMS(),
							tRecipes.getRequirementTwoAsNMS(),
							tRecipes.getResultAsNMS());
				} else {
					recipe = new MerchantRecipe(tRecipes.getRequirementOneAsNMS(),
							tRecipes.getResultAsNMS());
				}
				
				merchantList.add(recipe);
				
				this.updateMerchantList();
			}
		}
	}
	
	public void craft(){
		villager.setCustomName(title);
		villager.setCustomNameVisible(true);
		villager.setPosition(loc.getX(), loc.getY(), loc.getZ());
		
		try {
			IReflection.set(villager, "trades", merchantList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		world.addEntity(villager);
		
		isAlive = true;
	}
	
	public void destroy(){
		villager.die();
		
		isAlive = false;
	}
	
	public void teleport(Location loc){
		this.loc = loc;
		
		villager.teleportTo(loc, true);
	}
	
	public Location getLocation() {
		return loc;
	}
	
	private void updateMerchantList(){
		
		try {
			IReflection.set(villager, "trades", merchantList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public class TradeRecipe {
		
		private net.minecraft.server.v1_9_R1.ItemStack requirement1;
		private net.minecraft.server.v1_9_R1.ItemStack requirement2;
		private net.minecraft.server.v1_9_R1.ItemStack result;
		private int maxUses;
		
		public TradeRecipe(ItemStack req1, ItemStack req2, ItemStack res, int maxUses) {
			this.requirement1 = CraftItemStack.asNMSCopy(req1);
			this.requirement2 = CraftItemStack.asNMSCopy(req2);
			this.result = CraftItemStack.asNMSCopy(res);
			this.maxUses = maxUses;
		}
		
		public TradeRecipe(ItemStack req, ItemStack res, int maxUses) {
			this(req, null, res, maxUses);
		}
		
		public net.minecraft.server.v1_9_R1.ItemStack getRequirementOneAsNMS() {
			return requirement1;
		}
		
		public net.minecraft.server.v1_9_R1.ItemStack getRequirementTwoAsNMS() {
			return requirement2;
		}
		
		public net.minecraft.server.v1_9_R1.ItemStack getResultAsNMS() {
			return result;
		}
		
		public ItemStack getRequirementOneAsBukkitStack(){
			return CraftItemStack.asBukkitCopy(requirement1);
		}
		
		public ItemStack getRequirementTwoAsBukkitStack(){
			return CraftItemStack.asBukkitCopy(requirement2);
		}
		
		public ItemStack getResultAsBukkitStack(){
			return CraftItemStack.asBukkitCopy(result);
		}
		
		public int getMaxUses() {
			return maxUses;
		}
	}
}
