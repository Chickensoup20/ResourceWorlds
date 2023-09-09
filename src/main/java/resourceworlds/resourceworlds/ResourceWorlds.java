package resourceworlds.resourceworlds;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResourceWorlds extends JavaPlugin {

    private FileConfiguration config;
    private File configFile;

    @Override
    public void onEnable() {
        // Initialize the default configuration settings if the config file doesn't exist
        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            createDefaultConfig();
        }

        // Load the configuration file
        config = YamlConfiguration.loadConfiguration(configFile);

        // Register the tab completer for the /rctp command
        getCommand("rctp").setTabCompleter(new ResourceWorldsTabCompleter());
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("createresourceworld")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("resourceworld.create")) {
                    if (args.length >= 1) {
                        String worldName = args[0];
                        createResourceWorld(player, worldName);
                    } else {
                        player.sendMessage(ChatColor.RED + "Usage: /createresourceworld <worldname>");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You don't have permission to create a resource world.");
                }
            } else {
                sender.sendMessage("Only players can use this command.");
            }
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("rctp")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("resourceworld.goto")) {
                    if (args.length == 0) {
                        player.sendMessage(ChatColor.RED + "Usage: /rctp <world>");
                    } else {
                        String worldName = args[0];
                        teleportToResourceWorld(player, worldName);
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You don't have permission to go to a resource world.");
                }
            } else {
                sender.sendMessage("Only players can use this command.");
            }
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("listworlds")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("resourceworld.list")) {
                    List<String> worlds = getAvailableWorlds();
                    player.sendMessage("Available worlds: " + String.join(", ", worlds));
                } else {
                    player.sendMessage(ChatColor.RED + "You don't have permission to list worlds.");
                }
            } else {
                sender.sendMessage("Only players can use this command.");
            }
            return true;
        }

        return false;
    }

    private void createResourceWorld(Player player, String worldName) {
        WorldCreator worldCreator = new WorldCreator(worldName);
        World world = Bukkit.createWorld(worldCreator);

        if (world != null) {
            player.sendMessage("Resource world '" + worldName + "' created successfully!");
            saveResourceWorldSettings(worldName, worldName); // Save settings to config
        } else {
            player.sendMessage(ChatColor.RED + "Failed to create the resource world.");
        }
    }

    private void teleportToResourceWorld(Player player, String worldName) {
        World world = Bukkit.getWorld(worldName);

        if (world != null) {
            if (canTeleportTo(worldName)) {
                Location spawnLocation = world.getSpawnLocation();
                player.teleport(spawnLocation);
                player.sendMessage("Teleported to the resource world '" + worldName + "'!");
            } else {
                player.sendMessage(ChatColor.RED + "You don't have permission to go to this world.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "The specified world does not exist.");
        }
    }

    private boolean canTeleportTo(String worldName) {
        return config.getBoolean("worlds." + worldName + ".teleport", true); // True by default if not found
    }

    private List<String> getAvailableWorlds() {
        List<String> worldNames = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            String name = world.getName();
            worldNames.add(name);
        }
        return worldNames;
    }

    private void saveResourceWorldSettings(String worldName, String displayName) {
        config.set("worlds." + worldName + ".displayname", displayName);

        // Save the updated config
        try {
            config.save(configFile);
        } catch (IOException e) {
            getLogger().warning("Failed to save the configuration file: " + e.getMessage());
        }
    }

    private void createDefaultConfig() {
        config = new YamlConfiguration();
        config.options().copyDefaults(true);

        // Set default values for all worlds, enabling all except Nether and End
        for (World world : Bukkit.getWorlds()) {
            String worldName = world.getName();
            boolean isEnabled = !(worldName.equals("world_nether") || worldName.equals("world_the_end"));
            config.set("worlds." + worldName + ".teleport", isEnabled);
            config.set("worlds." + worldName + ".displayname", worldName);
        }

        // Save the default configuration to the file
        try {
            config.save(configFile);
            getLogger().info("Default configuration has been created.");
        } catch (IOException e) {
            getLogger().warning("Failed to save the default configuration file: " + e.getMessage());
        }
    }
}

class ResourceWorldsTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (cmd.getName().equalsIgnoreCase("rctp")) {
            if (args.length == 1) {
                List<String> worldNames = new ArrayList<>();
                for (World world : Bukkit.getWorlds()) {
                    String name = world.getName();
                    worldNames.add(name);
                }
                return worldNames;
            }
        }
        return null;
    }
}
