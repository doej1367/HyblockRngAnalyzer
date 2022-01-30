# Contributing to HyblockRngAnalyzer [WIP]

## Required Software
- [Java Development Kit 8](https://www.oracle.com/java/technologies/downloads/#java8-windows) (latest version)
- [Forge 1.8.9 MDK (latest version)](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.8.9.html) MDK (latest version)
- [Eclipse](https://www.eclipse.org/downloads/) for Java

## Setup Forge Development Environment (steps for Windows)
1. Install `Minecraft` - if you haven't already
2. Install `Java Development Kit 8`
3. Install `Eclipse for Java` - wait with starting it
4. Set an environment variable under `Win` + `Break` > `Advances System Settings` > `Environment Variables` 
    - Add a `JAVA_HOME` variable in the top part of the window with the value of your `jdk 8` folder path
    - Add the `jdk 8 \ bin` folder path to the `Path` variable in the bottom part of the window
        - The `jdk 8` folder path should look something like this: `C:\Program Files\Java\jdk1.8.0_311`
5. Download and Extract the `Forge 1.8.9 MDK`
6. Copy the contents to a folder you like to work from
7. Open the command line (cmd) and navigate to that folder using `cd <your folder path>`
    - you can change drives using e.g. `C:`, `D:`, `E:`, etc.
9. Setup the Eclipse development environment by typing `gradlew setupDecompWorkspace eclipse`
10. Start `Eclipse`
11. Select `<your folder path>\Modding Workspace\eclipse` as eclipse workspace
12. Switch to Dark Mode if you like: `Window > Preferences > General > Appearance > Theme: Dark`

// TODO merge forge dev env setup with this git repo

## Getting Started
2. Log into github
3. Fork this repository
4. Clone the project to your local machine 
    - git command, Eclipse or some GUI (I for example used SourceTree)
5. Import the projekt into Eclipse
6. (Wait for the project to fully load)
7. Make your changes
8. Rightclick your Forge project in eclipse and go to `Show in > Terminal`
9. Build the projects jar file with `gradlew build`
10. Once you are you are happy with your result, you can commit and push everything to a new branch of your forked project
11. If you want to create a pull request you can do that on the github page of your forked project
    - github automatically offers you that option once you've made changes to your forked project

## An introduction to the structure of this Forge mod
The main Java class is logically `me.hyblockrnganalyzer.Main`. Here the initial functions executed on startup / initialisation of the mod are defined. The key part is what is in `public void init(FMLInitializationEvent event)`. Here commands - using `ClientCommandHandler.instance.registerCommand()` - and event handlers - using `MinecraftForge.EVENT_BUS.register()` - are registered.

The core event handler is the `me.hyblockrnganalyzer.HypixelEventHandler`. This one subscibes to standard Forge events like `ClientChatReceivedEvent`, `GuiScreenEvent.InitGuiEvent.Post`, `PlaySoundEvent`. If these events fulfill special criteria, they are used to trigger custom events listed in `me.hyblockrnganalyzer.event` using `MinecraftForge.EVENT_BUS.post()`.

Custom event handlers in `me.hyblockrnganalyzer.eventhandler` then subscribe to these custom events like the `NucleusLootEvent` or the `OpenCustomChestEvent`.

Finally the data contained in these events gets extracted, parsed and put into one of the files.

Commands are found in `me.hyblockrnganalyzer.command`. One of those would be the `CsvFileCreationCommand` which when triggered creates csv files fom the txt ones.

`me.hyblockrnganalyzer.util` contains all sorts of Java files that provide some kind of - more or less universal - functionality. Some also provide functionality used in more than one event like the `HypixelEntityExtractor` that extracts and summarizes the stacked armor stands Hypixel - among other servers - likes so much. Others like the `DungeonChestStatus` are more specific to one event.

