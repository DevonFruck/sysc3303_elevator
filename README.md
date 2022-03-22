# SYSC 3303 Winter 2022 Project - Elevator System
---

Lab L4, Group 9
* Anish Tankala - 101079739
* Azizul Hasan - 101124159
* Daniel Wang - 101080842
* Devon Fruck - 101115219
* Laith Abdelrazeq - 101110866

### Included directories/files

* `L4G9_milestone_3` - Eclipse project directory
	* `src/elevator/ElevatorCar.java` - This class will represent the elevator which gets and receives the information and data from scheduler
	* `src/floorSubsystem/Floor.java` - This class will represents the floor subsystem that will be communicating events from the scheduler
	* `src/scheduler/Scheduler.java` - The scheduler is used to communicate all the event data between floors and the elevator
	* `src/elevator/ElevatorSubsystem.java` - The subsystem is responsible for initializing the three elevator cars thread
	* `src/scheduler/ElevatorSchedulerThread.java` - The program handles the communication between elevator threads and scheduler
	* `src/scheduler/FloorSchedulerThread.java` - The program handles the communication between floor threads and scheduler
	* `src/elevator/FloorSubsystem.java` - The subsystem is responsible for initializing the floor threads
	* `src/test/DataTest.java` - Unit test for reading the input file and communication of the data between the classes
	* `src/test/ElevatorButtonTest.java` - Unit test for all the methods in the ElevatorButton class
	* `src/test/ElevatorDoorTest.java` - Unit test for all the methods in the ElevatorDoor class
	* `src/test/ElevatorLampTest.java` - Unit test for all the methods in the ElevatorLamp class
	* `src/test/FloorButtonTest.java` - Unit test for all the methods in the FloorButton class
	* `src/test/FloorLampTest.java` - Unit test for all the methods in the FloorLamp class
	* `src/InputData.java` - The class is used by ElevatorCar and Floor
	* `src/TxtFileReader.java` - This class reads the input.txt file and adds the inputs to an arrayList
	* `src/ErrorHandler.java` - Error Handler takes care of the error events in the system using the class InputData
	* `src/Configurations.java` - the configuration for the values used in the system
	* `src/input.txt` - file that contains all the input events, as described by the project specification
	* `.classpath` - required Eclipse file, adds JUnit to buildpath
	* `.project` - required Eclipse file
	* `.settings/org.eclipse.jdt.core.prefs` - sets correct Java version used on lab machines (Java SE 10.0.2)
* `Diagrams`
	* `src/uml/UML_class.pdf` - the class diagram for all the work products for iteration 2 in the project specification
	* `src/uml/sequence_diagram.pdf` - the sequence diagram for all the work products for iteration 2 in the project specification
	* `src/uml/state_diagram.png` - state machine for all the states in our elevator system for iteration 2
* `README.md` - documentation about the deliverables and instructions about the setup and running the system 

### Setup instructions

* Extract the zip file into a new directory. Your file structure should look as follows (note that `src` would contain more directories/files, but are excluded here for brevity):

	ElevatorSystemSYSC3303
			L4G9_milestone_3
			.settings
			src
			.classpath
			.project
			README.md


*Launch Eclipse and use the workplace directory to launch the project
*Import the Project from the system

Note: Please make sure to add Junit 5 for the test cases to run.



### Breakdown of Responsibilities

* Iteration 3:
	* Elevator.java : Group
        * FloorSubsystem.java : Group
        * Scheduler.java : Group
        * InputInformation.java : Group
        * TxtFileReader.java : Group
		* TxtFileReader.java : Group
	* DataTest.java : Group
        * README.md : Group
	* Class Diagram : Group
	* Sequence Diagram: Group
	* State Diagram: Group 