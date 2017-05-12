# OpenData-Profinit

## About project
This project is dedicated to provide data from public contracts to everyone who want to process data
and public data himself.


## Before you run this app, you need to make following steps:

1. You need to have postgres installed
2. change values in application.properties to your values:
 * server=server.port
 * database name
 * database user
 * user password

Also you need to setup limits for throttling and blacklisting IP's in application.
* LIMITED_PATHS - Paths of application where throttling and blacklisting will be availble.
* WINDOW_SIZE_IN_MINUTES - Time in minutes in which IP's will be stored in blacklist.
* MAX_REQUEST_PER_IP_IN_WINDOW - Max requests before IP will be moved to blacklist.
* MAX_REQUEST_PER_IP_Before_Throttling_IN_WINDOW - Max req before ip will be throttled.
* addSecondsTimeTillNextReq - Time of throttling, till next req can be sended.

## How to run app:
You have to have maven installed. Next you need only to run maven and he automatically load all dependencies
from pom.xml file. After maven load all dependencies run application by starting main project class. 
SpringBoot already have tomcat inside so application can run without any server configuration.
