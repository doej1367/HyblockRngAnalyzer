# HyblockRngAnalyzer
This is ment to be a data collector mod on all things Hypixel Skyblock  
with the goal to figure out as many drop chances as possible for the wiki.

## Features
- Extract various data and append it to `.txt` files in the config folder.
    - `Treasure and Loot Chest` contents
    - `Crystal Nucleus Loot Bundle` drops
    - `Jerry Box` contents
    - `Dungeon Chest` contents
    - `Gift` rewards
- `/csv` command to create more readable csv files from the database txt files

## How to intall and use this mod
- Make sure [Forge 1.8.9](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.8.9.html) is installed
- Download the latest `.jar` file from the [releases](https://github.com/doej1367/HyblockRngAnalyzer/releases) and put it into your mod folder
- The folder path of the database files should look something like the following:
    - `C:\Users\User\AppData\Roaming\.minecraft\config\hyblockrnganalyzer\`

## How to contribute to this mod
- [Contributing Manual](CONTRIBUTING.md) with step by step setup guide for the development environment  
and a brief introduction on how this mod is structured and its inner workings

### What to contribute
Want to get into mod development by adding a feature to this mod, but don't know where to start?  
Here are a few things you could try to implement:
- A system that automatically submits the results to a server
- Simple chat triggered trackers for
    - `Dark Monolith` (drops)
    - `Trick Or Treat Chest` (drops)
    - `Cake Souls` (cake type, soul drop yes/no)
    - `Slayer` (boss type, drops)
- Complicated chest menu related trackers for
    - `Farming Event` (crop type, minimum crops needed for medals)
    - `Experiments` (type, possible drops)
- Complicated armor stand related trackers for
    - `Dragon` (summoning eyes placed, damage position, drops)
    - `Endstone Protector` (zealot kills, damage, drops)
    - `Magma Boss` (damage position, drops)
    - `Bal` (drops)
    - `Rat` (spawn location, server time)
    - `Broodmother` (spawn location, server time)
- Complicated dropped item related trackers for
    - `Gulliver's Chicken` (drops)
    - `Glowing Block` (drops)
    - `Bat Pinata` (drops)
- Complicated tracker for
    - `Fishing` (location, drops)
