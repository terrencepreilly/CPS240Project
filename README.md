# CPS240Project
Zomble is a 2D zombie hack-and-slash created for the CPS240 class at Central Michigan University.  Zomble is multiplayer and supports enemy path-finding.

INSTALLING AND RUNNING
	1. Download and extract the files into a chosen directory.  For our 
	purposes, let's call this directory "ZombleGame".

	2. Compile the .class files using the makefile:
		$ make zomble
	Alternatively, you could run javac on the files:
		$ javac Zomble.java ZombieClient.java Server.java

	3. To play, you will first need to start the Server and Zombie Client
		$ java Server
		$ java ZombieClient

	4. Then, to play, you start the GUI.
		$ java Zomble
