package me.admiroh.staffmode.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.admiroh.staffmode.Main;
import me.admiroh.staffmode.utils.ColorUtils;

public class StaffModeCommand implements CommandExecutor {

	@SuppressWarnings("unused")
	private Main plugin;

	public StaffModeCommand(Main plugin) {
		this.plugin = plugin;
	}

	public static HashSet<UUID> isStaff = new HashSet<>();
	public static HashMap<UUID, ItemStack[]> inventory = new HashMap<>();
	public static HashMap<UUID, ItemStack[]> armor = new HashMap<>();
	

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cYou must be a player to execute this command.");
			return true;
		}

		Player player = (Player) sender;
		if (!(player.hasPermission("staffmode.use"))) {
			player.sendMessage("§cYou do not have permission to use this.");
			return true;
		}
		
		if (args.length == 0) {
			if (isStaff.contains(player.getUniqueId())) {
				disableStaff(player);
				return true;
			}
			
			enableStaff(player);
			return true;
		}
		
		if (args.length == 1) {
			if (Bukkit.getPlayer(args[0]) == null) {
				player.sendMessage("§cA player with that name was not found");
				return true;
			}
			
			Player target = Bukkit.getPlayer(args[0]);
			
			if (isStaff.contains(target.getUniqueId())) {
				disableStaff(target);
				return true;
			}
			
			enableStaff(target);
			return true;
		}

		return true;
	}

	@SuppressWarnings("deprecation")
	public void enableStaff(Player player) {
		isStaff.add(player.getUniqueId());
		player.sendMessage("enabled staff mode");
		
		if(player.hasPermission("staff.mode.creative")) {
			player.setGameMode(GameMode.CREATIVE);
		} else {
			player.setGameMode(GameMode.SURVIVAL);
		}
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setSaturation(20);
		player.setFireTicks(0);
		player.setAllowFlight(true);

		inventory.put(player.getUniqueId(), player.getInventory().getContents());
		armor.put(player.getUniqueId(), player.getInventory().getArmorContents());
	
		player.getInventory().clear();
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
		
		player.getInventory().addItem(new ItemStack(Material.DIRT));
		
		ItemStack compass = nameItem(Material.COMPASS, ColorUtils.Color("&ePhase Compass"), (short) 0, 1, colorList(Arrays.asList("&cYou can jump with right click.", "&cYou can phase with left click.")));
		ItemStack teleport = nameItem(Material.WATCH, ColorUtils.Color("&eRandom Teleport"), (short) 0, 1, colorList(Arrays.asList("&cTeleport to a random player")));
		ItemStack carpet = nameItem(Material.CARPET, ColorUtils.Color("&eBetter View"), (short) 0, 1, colorList(Arrays.asList("")));
		ItemStack inspect = nameItem(Material.BOOK, ColorUtils.Color("&eInspection Book"), (short) 0, 1, colorList(Arrays.asList("&cRight click to view a player's inventory.")));
		ItemStack vanish = nameItem(Material.INK_SACK, ColorUtils.Color("&eVanish"), (short) 8, 1, colorList(Arrays.asList("&cToggle your vanish.")));
		
		player.getInventory().setItem(0, compass);
		player.getInventory().setItem(8, teleport);
		player.getInventory().setItem(2, carpet);
		player.getInventory().setItem(1, inspect);
		player.getInventory().setItem(7, vanish);
		// adds to inventory.
	}

	@SuppressWarnings("deprecation")
	public void disableStaff(Player player) {
		isStaff.remove(player.getUniqueId());
		player.sendMessage("disabled staff mode");
		
		player.setGameMode(GameMode.SURVIVAL);
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setSaturation(20);
		player.setFireTicks(0);
		player.setAllowFlight(false);
		
		player.getInventory().clear();
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
		
		player.getInventory().setContents(inventory.get(player.getUniqueId()));
		player.getInventory().setArmorContents(armor.get(player.getUniqueId()));
	

		// adds to inventory.
	}
	
	public List<String> colorList(List<String> list) {
        ArrayList<String> newList = new ArrayList<>();
        for (String string : list) {
            newList.add(ColorUtils.Color(string)); 
        }
        return newList;
    }

    private ItemStack nameItem(ItemStack item, String name, short durability, int amount, List<String> lores) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(colorList(lores));
        item.setItemMeta(meta);
        item.setAmount(amount);
        item.setDurability(durability);
        return item;
    }

    private ItemStack nameItem(Material item, String name, short durability, int amount, List<String> lores) {
        return nameItem(new ItemStack(item), name, durability, amount, lores);

    }

}
