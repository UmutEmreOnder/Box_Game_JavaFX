# About the Game

The term project of the 1st grade is a mind game that we made in teams of 2, using the ```JavaFX framework```. The aim of the game is to try to finish each level with the highest score you can get.

<hr/>

# How To Play

To play the game, you must use ```JDK 8``` and have installed the ```JavaFX JAR``` file. When you ```run the GameBox class```, the game will have started.

There are 4 different box rounds in the game.
### 1. Empty Box:
An unclickable box with no features. Other clickable boxes change to this when clicked
### 2. Wall Box: 
Unlike EmptyBox, boxes that cannot be created by the user (ie other boxes cannot transform), which are only pre-added to the level for obstacle purposes.
### 3. WoodBox: 
Boxes that are broken with a single click will turn into EmptyBox when clicked
### 4. MirrorBox:
Boxes that are broken when clicked twice will turn into WoodBox when clicked for the first time and take its properties.

<hr/>

When you click on a box in the game, you click on that box and the boxes next to it. The point system in the game is determined by how many boxes you affect when you click.
1. If your click affects only 1 box, you get -3 points.
2. If your click affects only 2 boxes, you get -1 point.
3. If your click affects only 3 boxes, you get 1 point.
4. If your click affects only 4 boxes, you get 2 points.
5. If your click affects all 5 boxes at once, you get 4 points.

Basically, the goal is to avoid clicking as much as possible that will affect 2 or 1 boxes.

<hr />

# Features

There are 3 main features:

### 1. Restart Label: 
The restart label allows you to start the same level again if you think you messed it up.

### 2. Save Game / Load Game: 
They do what they called. Save game saves the level you left. Load game starts the level from scratch. It doesn't save your progress for that level.

### 3. Highscore: 
It saves the highest score of each level. It doesn't reset when you restart the game or etc.

<hr />

# Sample Gameplay 

![Sample Gameplay](https://github.com/UmutEmreOnder/Box_Game_JavaFX/blob/master/Sample%20Gameplay%20Gif/Gameplay.gif)
