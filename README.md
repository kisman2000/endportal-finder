<div align="center">

# Ender portal finder

Calculating intersection of the 2 directions

May help with finding ender portals

# Building

To build `endportal finder` you need to have [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) installed

</div>

1. Download `endportal finder` source code by clicking `Code -> Download ZIP` on [the main page](https://github.com/kisman2000/endportal-finder)
2. Extract the source code somewhere and open cmd (on Windows) or Terminal
3. Execute `gradlew build` (if you're on Windows) or `chmod +x ./gradlew && ./gradlew build` (if you're on Linux) and wait until the building process finishes
4. After that you should have a file called `endportal-finder-VERSION.jar` inside `<endportal finder src>/build/libs` folder
5. Use it anywhere you need

<div align="center">

# Downloading

You can download stable prebuilt JARs from [the releases page](https://github.com/kisman2000/endportal-finder/releases)

# How to

To launch `endportal finder` you need to have Fabric `1.20.1+`

</div>

1. Download the latest Fabric for `1.20.1+`
2. Download the mod and put into mods folder
3. Start the game

<div align="center">

To use `endportal finder` you need to use `REGISTER_POS` keybind to register the 2 directions and calculate their intersection and `RESET_POS` keybind to reset them

`REGISTER_POS` is `G` by default

`RESET_POS` is `H` by default

</div>

1. Get on the place of the first direction 
2. Press `REGISTER_POS` to register this direction 
3. Repeat 1-3 points for the second direction 
4. Press `REGISTER_POS` to calculate their intersection
5. Press `RESET_POS` if you need to remove the tracer
