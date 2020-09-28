# SoS Runtime-Monitoring 
Runtime monitoring and runtime verification in a large-complex system, as in SoS, are very challenging.

First of all, SoS is large-complex, so the amount of information obtained through monitoring is very large. Because the system is huge, information must be collected through multiple monitoring rather than through one monitoring. Also, like CS's interaction, SoS is very complex, so it includes from simple monitoring information to very complex monitoring information.

Of the huge amount of data collected, sorting information is also very important. There is useful information among the huge information collected, there is information that should be used for verification, and there are information that is not useful. Therefore, the collected information should be well classified according to its purpose.

If a malfunction occurs in a system such as Safety-critical SoS, it can cause massive human or asset damage and should be safe from failure. It is not possible to prevent or minimize these damages when verifying SoS statistically. It is also necessary to validate continuously changing and evolving SoS in real time. In other words, if a system is added or removed, failure to proceed with verification in real time can result in damage. 

SoS is a black-box or grey-box system, not a white-box system. For the system run-time verification, the system can be monitored and the verification results can be obtained using the information and verification attributes obtained through monitoring. In order to carry out proper run-time verification, monitoring must be carried out properly first. To do so, it is necessary to be able to determine which part of the system to monitor. In the case of the white box system, it is very easy to decide which part of the system to monitor, because we know all the information in the system. However, in black-box or grey-box systems, it is very difficult to determine the monitoring location. In addition, a technology is required that allows monitoring codes to be inserted without modifying the source code after determining the monitoring location.

#To Do
- Using AspectJ for runtime monitor on JAVA project.
- Weaving the aspect file to .jar file by re-compiling.
   - Not modifying source code for runtime monitor.
   - That is, not modifying the java file.
   - Weaving aspects to class file using ajc compilation method, not javac compilation method, and creating jar file which includes the information of aspect.
