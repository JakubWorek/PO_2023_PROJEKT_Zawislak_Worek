# Evolution simulator
Java application that simulates the evolution of animals inspired by the Charles Darwin's theory of natural selection.
Created by:
- [Jakub Worek](https://github.com/JakubWorek)
- [Dawid Zawiślak](https://github.com/dawidzawislak)

# Menu
## Parameters
User can enter simulation parameters or import them from config files
<img src="https://github.com/JakubWorek/evolution_simulator/blob/main/images/menu_1.png">

## Logs
Application will save logs about simulation to csv file

<img src="https://github.com/JakubWorek/evolution_simulator/blob/main/images/csv_output.png">

Logs will look like this:
<img src="https://github.com/JakubWorek/evolution_simulator/blob/main/images/logs.png">

# Simulation
## Interface
<img src="https://github.com/JakubWorek/evolution_simulator/blob/main/images/game.png">
Application shows:
  - Plots ilustrating changes in animals and plants quantity
  - Grid for simulation map
  - Statistics about simulation

## Features
### Following animal
<img src="https://github.com/JakubWorek/evolution_simulator/blob/main/images/clicked_animal.png">
User can click on animal and application will highlight them and show its own statistics  

### Best animal
<img src="https://github.com/JakubWorek/evolution_simulator/blob/main/images/best_animal.png">
Application highlights animals with most popular genotype

### Best grass
<img src="https://github.com/JakubWorek/evolution_simulator/blob/main/images/best_grass.png">
Application highlights fields where plant will most likely spawn on next days

### Water
<img src="https://github.com/JakubWorek/evolution_simulator/blob/main/images/water_map.png">
Simulating flows of water fields where animals can't enter and plant won't spawn

# Tests
System tests are aimed at checking the correctness of the implemented functionalities.
Test cases cover a variety of scenarios including potential error conditions.
The testing strategy includes unit testing and integration testing.
