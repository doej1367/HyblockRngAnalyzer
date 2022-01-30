# Contributing to HyblockRngAnalyzer

## Required Software
- [Java Development Kit 8](https://www.oracle.com/java/technologies/downloads/#java8-windows) (latest version)
- [Forge 1.8.9 MDK (latest version)](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.8.9.html) MDK (latest version)
- [Eclipse](https://www.eclipse.org/downloads/) for Java

## Getting Started (Steps for Windows)
### Part 1 - Software Installations
1. Install `Minecraft` - if you haven't already
2. Install `Java Development Kit 8`
3. Install `Eclipse for Java` - wait with starting it
### Part 2 - Operating System Configuration
4. Set an environment variable under `Win` + `Break` > `Advances System Settings` > `Environment Variables` 
    - Add a `JAVA_HOME` variable in the top part of the window with the value of your `jdk 8` folder path
    - Add the `jdk 8 \ bin` folder path to the `Path` variable in the bottom part of the window
        - The `jdk 8` folder path should look something like this: `C:\Program Files\Java\jdk1.8.0_311`
### Part 3 - Fork and clone this github project
5. Log into github
6. Fork this repository
7. Clone the forked project to your local machine 
    - git command, Eclipse or some GUI (I for example used SourceTree)
### Part 4 - Forge MDK: How to setup the eclipse workspace
8. Download and Extract the `Forge 1.8.9 MDK`
9. Copy the `eclipse` folder to the folder you cloned this project into
10. Open the command line (cmd) 
11. Navigate to that folder you cloned this project into using `cd <your folder path>`
    - Tip: you can change drives using e.g. `C:`, `D:`, `E:`, etc.
12. Setup the Eclipse development environment by typing `gradlew setupDecompWorkspace eclipse`
13. Start `Eclipse`
14. Select `<your folder path>\Modding Workspace\eclipse` as eclipse workspace
15. Switch to Dark Mode if you like: `Window > Preferences > General > Appearance > Theme: Dark`
### Part 5 - Build the mod to a .jar file
16. Rightclick your Forge project in eclipse and go to `Show in > Terminal`
17. Build the projects jar file with `gradlew build`
### Part 6 - Git branch
18. Create a new branch and switch to it
    - git command, Eclipse or some GUI (e.g. SourceTree)
### Part 7 - The coding
19. Now you are all set. You can read a little bit about the structure of this Forge mod down below, start changing things, test your changes by building the mod and putting the jar in the mod folder (sadly the multiplayer testing environment is broken due to the new Mirosoft login changes), commit and push to your new branch and in the next step when you are all set, create a pull request.
### Part 8 - Contribute to this project
20. Once you are you are happy with your result, you can commit and push everything to a new branch of your forked project
21. If you want to create a pull request you can do that on the github page of your forked project
    - github automatically offers you that option once you've made changes to your forked project

## An introduction to the structure of this Forge mod
The main Java class is logically `me.hyblockrnganalyzer.Main`. Here the initial functions executed on startup / initialisation of the mod are defined. The key part is what is in `public void init(FMLInitializationEvent event)`. Here commands - using `ClientCommandHandler.instance.registerCommand()` - and event handlers - using `MinecraftForge.EVENT_BUS.register()` - are registered.

The core event handler is the `me.hyblockrnganalyzer.HypixelEventHandler`. This one subscibes to standard Forge events like `ClientChatReceivedEvent`, `GuiScreenEvent.InitGuiEvent.Post`, `PlaySoundEvent`. If these events fulfill special criteria, they are used to trigger custom events listed in `me.hyblockrnganalyzer.event` using `MinecraftForge.EVENT_BUS.post()`.

Custom event handlers in `me.hyblockrnganalyzer.eventhandler` then subscribe to these custom events like the `NucleusLootEvent` or the `OpenCustomChestEvent`.

Finally the data contained in these events gets extracted, parsed and put into one of the files.

Commands are found in `me.hyblockrnganalyzer.command`. One of those would be the `CsvFileCreationCommand` which when triggered creates csv files fom the txt ones.

`me.hyblockrnganalyzer.util` contains all sorts of Java files that provide some kind of - more or less universal - functionality. Some also provide functionality used in more than one event like the `HypixelEntityExtractor` that extracts and summarizes the stacked armor stands Hypixel - among other servers - likes so much. Others like the `DungeonChestStatus` are more specific to one event.

## Sources:
- for the step by step setup guide
    - [Hypixel Forum Post](https://hypixel.net/threads/guide-how-to-start-create-coding-minecraft-forge-mods.551741/#post-5352380)
    - [German Youtube Tutorial](https://www.youtube.com/watch?v=6YS-ExDnrjg) 
