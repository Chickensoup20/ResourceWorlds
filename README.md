# ResourceWorlds Plugin

![Plugin Logo](plugin-logo.png) <!-- Replace with your plugin's logo or image -->

## Description

The ResourceWorlds plugin is designed to create and manage resource worlds in your Minecraft server. Resource worlds are separate dimensions with custom settings that players can access for resource gathering, exploration, and more.

## Features

- Create custom resource worlds with unique names.
- Teleport players to resource worlds.
- List available worlds.
- Customizable permissions.
- Automatic generation of a default configuration file.

## Installation

1. Download the plugin JAR file from [GitHub Releases](https://github.com/your-plugin-repo/releases) or your preferred source.

2. Place the downloaded JAR file into the `plugins` directory of your Minecraft server.

3. Start or reload your server.

## Usage

### Commands

- `/createresourceworld <worldname>`: Create a custom resource world.
- `/rctp <world>`: Teleport to a resource world.
- `/listworlds`: List available resource worlds.

### Permissions

- `resourceworld.create`: Allows players to create resource worlds.
- `resourceworld.goto`: Allows players to teleport to resource worlds.
- `resourceworld.list`: Allows players to list available worlds.

## Configuration

The plugin comes with a configuration file (`config.yml`) that allows you to customize the behavior of the plugin. You can configure which worlds can be teleported to and their display names.

```yaml
# Sample Configuration
worlds:
  world1:
    displayname: "Resource World 1"
    teleport: true
  world2:
    displayname: "Resource World 2"
    teleport: false
