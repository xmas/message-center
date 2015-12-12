# message-center
To deploy to remote server from command prompt you need:
    create file settings.xml in local machine ${user.home}/.m2 folder wich contains nex text
    
       <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                                 http://maven.apache.org/xsd/settings-1.0.0.xsd">
               <servers>
                       <server>
                               <id>remote</id>
                               <username>specify username here</username>
                               <password>specify password here</password>
                       </server>
               </servers>
       </settings>
    
    create directory .pushmessages/properties in ${user.home} and put jdbc.properties and hibernate.properties there 
