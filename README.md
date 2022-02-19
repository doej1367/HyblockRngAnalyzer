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
    - `Soul Menu` necromancy souls
- Automatically uploads txt files onto the discord server once over 80kB of data have been collected
- `/open` command to open the folder containing all the database files
- `/csv` command to create more readable csv files from the database txt files

## Discord
- A [Discord server](https://discord.gg/mA3aBuPwcd) for discussions, ideas, mod-update notifications and to collect all the txt files

## How to intall and use this mod
- Make sure [Forge 1.8.9](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.8.9.html) is installed
- Download the latest `.jar` file from the [releases](https://github.com/doej1367/HyblockRngAnalyzer/releases) and put it into your mod folder
- The folder path of the database files should look something like the following:
    - `C:\Users\User\AppData\Roaming\.minecraft\config\hyblockrnganalyzer\`

## How to contribute to this mod
- [Contributing Manual](CONTRIBUTING.md) with a step by step setup guide for the development environment 
and a brief introduction on how this mod is structured and its inner workings. In case you need some inspiration on what to contribute, here is a [list of ideas](https://github.com/doej1367/HyblockRngAnalyzer/discussions/1#discussion-3846005).
