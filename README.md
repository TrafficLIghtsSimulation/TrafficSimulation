# Traffic Light Control System Based on Vehicle Density
This project is a traffic control system designed to adjust green light durations at a four - way intersection based on the vehicle density on each road. The primary purpose is to minimize 
traffic congestion by distributing  green light times proportionally.
Users can either manuallydefine vehicle counts for each direction or start the simulation in random mode, allowing for both controlled testing and realistic dynamic behavior.


## Project Objectives 
• Simulation of a four-way intersection where vehicles pass from the North, South, East and West directions without colliding.
• Determination of green light durations according to vehicle densities entered in real time or manually. Vehicle densities can be entered manually or generated randomly. Green light duration is calculated proportionally according to the density.
• Traffic simulation with graphical interface using JavaFX Real-time visual representation is made with timers and moving vehicles.


# Project Development Process
During the project development process, firstly the requirements analysis was performed and the information and details about the project were obtained, after the requirements analysis process, scenarios and use cases were extracted, each use case was detailed and the use case diagram and use case descriptions(unique name, participating actors, brief description, entry conditions, exit conditions and step-by-step description) were created. After this stage, the classes required within the scope of the project were discussed and class diagrams were prepared. The relationships and the types of these relationships between classes were determined using CRC cards. In parallel with this analysis, the UML diagram was prepared. After the project file organization structure and the data structures to be used, design patterns, visual screens and structures were discussed, the coding phase of the project was carried out. After the completion of coding stage, the UML diagram was updated and user manuals and reports were created.


![Traffic Light Control System Use Case Diagram](/resources/com/traffic/view/main_assets/TrafficLightControlSystemUseCaseDiagram.png)

![Traffic Light Control System UML Diagram](/resources//com/traffic/view/main_assets/TrafficLightControlSystemUMLDiagram.png)

## Project Features 
• **Welcome screen**:
  -Title texts, background image and "START" button
  ![Traffic Light Control System WelcomeView Screen (/com/traffic/view/main_assets/welcomescreen.png)

  
• **Input screen**:
  - MANUAL and RANDOM mode selection
  - Entering the number of vehicles in NORTH, SOUTH, EAST, WEST directions in MANUAL mode
  - "START" button
  ![Traffic Light Control System InputView Screen](/resources//com/traffic/view/main_assets/inputscreen.png)
  ![Traffic Light Control System InputView Manuel Screen](/resources//com/traffic/view/main_assets/inputscreenmanuel.png)

In order for the "Start" button to be activated, the user must select one of the "Manual Traffic Density Input" or "Random Traffic Density Input" options on the InputView screen. If the user selects the "Manual Traffic Density Input" checkbox, a vehicle count between 0-100 must be entered for each direction (North, South, East, West). The "Start" button will not be activated unless the vehicle count is entered for all directions. If the "Random Traffic Density Input" checkbox is selected, the "Start" button will be activated directly.
When "Manual Traffic Density Input" is selected, 
  - When the user enters a value outside the range of 0-100, the user is given the warning **"Please enter values between 0 and 100 for each direction."**. 
  - When numbers are expressed in words rather than numbers or when symbol characters are entered, the user is given the warning **"Invalid input! Please enter numbers only."**.
    
   
• **Simulation screen**:
  - Simulation with traffic flow
  - Table showing the number of cars passing and remaining according to direction
  - "PAUSE" , "START" , "RESET" buttons
  - Digital counter (lights remaining time)
  - Real-time light transition animation
  - Animated cars
  ![Traffic Light Control System SimulationView Screen](/resources//com/traffic/view/main_assets/simulationscreen.png)

If the "Manual Traffic Density Input" checkbox is selected by the user on the InputView screen, but the number of vehicles is 0 for all directions, the "Start" button becomes active and the user is directed to the SimulationView screen. However, when the user clicks the "Start" button on the SimulationView screen, the user is given the warning **"All directions have zero traffic. Simulation will not start."** because there is no vehicle to be generated.


• **Light control algorithm**:
  - Proportional calculation of green light duration according to vehicle density
  - Yellow light duration is fixed (3 seconds)
   
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

## How to Run? 
1. Open this project with a JavaFX supported IDE (IntelliJ IDEA is recommended).
2. Run the `Main.java` file.
3. Steps:
 - **Welcome screen** opens (`START` button)
 - When `START` button is pressed, **login screen** appears
 - User selects **manual** or **random** mode
 - If manual is selected, the number of vehicles is entered for NORTH/SOUT
 - When the simulation starts, the lights turn on according to the vehicle density and the traffic flow beginsH/EAST/WEST

 ## Traffic Light Timing Logic 

 - **Total cycle time:** 120 seconds
 - **Green light duration:** Proportional to vehicle density
 - **Yellow light duration:** Fixed (3 seconds)

   **Example Calculation:**

| Direction | Number of Vehicles | Rate   | Green Time |
|-----------|--------------------|--------|------------|
| North     | 40                 | 50%    | 60 sec     |
| South     | 20                 | 25%    | 30 sec     |
| East      | 10                 | 12.5%  ​​| 15 sec     |
| West      | 0                  | 12.5% ​​ | 15 sec     |

saat yönü

## Simulation Behavior 
- When the light is **green** vehicles pass
- When the light is **red** vehicles wait
- After passing the intersection, vehicles exit the system
- **No collisions**, transitions are made in a controlled manner
- No new vehicles enter during the cycle


## Alert Messages 
- Please enter values between 0 and 100 for each direction.
- Invalid input! Please enter numbers only.
- All directions have zero traffic. Simulation will not start.
- Simulation finished. All vehicles have passed.


## Technologies Used 
  - Java
  - JavaFX
  - Scene Builder (FXML)
  - MVC (Model-View-Controller) Design
  - Java Collection Framework (List, Map)


## Developers 🙋‍♂️
  - ÖZGE NUR KÖK
  - FADİMANA NİSA ÖZTÜRK
  - ZEHRA NUR ERCİYAZ 
