# TRAFFIC LIGHT SIMULATION🚦
• It is a project that performs dynamic traffic light management based on vehicle density on the roads.The user can define traffic directions manually or start the simulation in random mode.

## Project Objectives 🎯
• Simulation of a four-way intersection where vehicles pass from the North, South, East and West directions without colliding.
• Determination of green light durations according to vehicle densities entered in real time or manually. Vehicle densities can be entered manually or generated randomly. Green light duration is calculated proportionally according to the density.
• Traffic simulation with graphical interface using JavaFX Real-time visual representation is made with timers and moving vehicles.

## Project Features 🧩
• **Welcome screen**:
  -Title texts, background image and "START" button 
  
• **İnput screen**:
  - MANUAL and RANDOM mode selection
  - Entering the number of vehicles in NORTH, SOUTH, EAST, WEST directions in MANUAL mode
  - "START" button
   
• **Simulation screen**:
  - Simulation with traffic flow
  - Table showing the number of cars passing and remaining according to direction
  - "PAUSE" , "START" , "RESET" buttons
  - Digital counter (lights remaining time)
  - Real-time light transition animation
  - Animated cars
   
• **Light control algorithm**:
  - Proportional calculation of green light duration according to vehicle density
  - Yellow light duration is fixed
   
• **Simulation behavior**:
  - Lights change automatically
  - Vehicles only pass on green lights
  - There will be no risk of collision, controlled passage of vehicles will be ensured
   
• **Interface design**:
  -FXML based responsive design prepared with Scene Builder
  
• **Software principles**:
  - MVC (Model-View-Controller) architecture
  - JavaFX event handling
  - Controller connection

## How to Run? 🚀
1. Open this project with a JavaFX supported IDE (IntelliJ IDEA is recommended).
2. Run the `Main.java` file.
3. Steps:
 - **Welcome screen** opens (`START` button)
 - When `START` button is pressed, **login screen** appears
 - User selects **manual** or **random** mode
 - If manual is selected, the number of vehicles is entered for NORTH/SOUT
 - When the simulation starts, the lights turn on according to the vehicle density and the traffic flow beginsH/EAST/WEST

 ## Traffic Light Timing Logic ⏱️

 - **Total cycle time:** 120 seconds
 - **Green light duration:** Proportional to vehicle density
 - **Yellow light duration:** Fixed (3 seconds)

   **Example Calculation:**

| Direction | Number of Vehicles | Rate   | Green Time |
|-----------|--------------------|--------|------------|
| North     | 40                 | 50%    | 60 sec     |
| South     | 20                 | 25%    | 30 sec     |
| East      | 10                 | 12.5%  ​​| 15 sec     |
| West      | 10                 | 12.5% ​​ | 15 sec     |


## Simulation Behavior 🎮
- When the light is **green** vehicles pass
- When the light is **red** vehicles wait
- After passing the intersection, vehicles exit the system
- **No collisions**, transitions are made in a controlled manner
- No new vehicles enter during the cycle

## Technologies Used 🖥️
  - Java 17+
  - JavaFX
  - Scene Builder (FXML)
  - MVC (Model-View-Controller) Design
  - Java Collection Framework (List, Map)

## Developers 🙋‍♂️
  - ÖZGE NUR KÖK
  - FADİMANA NİSA ÖZTÜRK
  - ZEHRA NUR ERCİYAZ 










