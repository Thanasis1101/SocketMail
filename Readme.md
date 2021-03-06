﻿

# Email simulating app using sockets
This repo includes server and client apps that have command line interface and are written in java.



# Execute
The executable jar files are in the folder [Jars](https://github.com/Thanasis1101/SocketMail/tree/master/Jars) (Client.jar, Server.jar).

How to run SocketMail:
* **Download the folder [Jars](https://github.com/Thanasis1101/SocketMail/tree/master/Jars)**
* **Start Server**: Run the [ExecuteServer.bat](https://github.com/Thanasis1101/SocketMail/blob/master/Jars/ExecuteServer.bat) if you have Windows, or execute the command:  
`java -jar Server.jar`    
This will make the server start running on port 5005 which is its default port.
* **Start Client**: Run the [ExecuteClient.bat](https://github.com/Thanasis1101/SocketMail/blob/master/Jars/ExecuteClientbat) if you have Windows, or execute the command:  
`java -jar Client.jar 127.0.0.1 5005`    
This will start the client app and connect it to the localhost on port 5005.  
The first parameter sets the host and the second parameter sets the port. So, if you want to have the server app running on another computer, you will have to start the Client.jar with a first parameter being the ip of the other computer.

# Report
For more information about the implementation of the code and the communication between client and server, you can read the [Report.pdf](https://github.com/Thanasis1101/SocketMail/blob/master/Report.pdf) which is available only in Greek.

 ![SocketMail](https://raw.githubusercontent.com/Thanasis1101/SocketMail/master/mail-server.jpg)
