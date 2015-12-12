# message-center
 #To deploy to remote server from command prompt you need:
    # create file settings.xml in local machine ${user.home}/.m2 folder wich contains nex text
    
       <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                                 http://maven.apache.org/xsd/settings-1.0.0.xsd">
               <servers>
                       <server>
                               <id>remote</id>
                               <username>specify user name</username>
                               <password>specify user password</password>
                       </server>
                       <server>
                               <id>local</id>
                               <username>specify user name</username>
                               <password>specify user password</password>
                       </server>
               </servers>
       
               <profiles>
                       <profile>
                               <id>local</id>
                               <properties>
                                       <server.id>local</server.id>
                                       <server.url>http://localhost:8080/manager/text</server.url>
                               </properties>
                       </profile>
                       <profile>
                               <id>remote</id>
                               <properties>
                                       <server.id>remote</server.id>
                                       <server.url>http://45.79.109.120:8080/manager/text</server.url>
                               </properties>
                       </profile>
       
               </profiles>
       
               <activeProfiles>
                       <activeProfile>local</activeProfile>
               </activeProfiles>
       
       </settings>
    
 # create directory .pushmessages/properties in ${user.home} and put jdbc.properties and hibernate.properties there 
