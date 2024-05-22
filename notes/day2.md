Testing:
    Nowadays (with agile methodologies and DEVOPS practices) we do REALLY do TESTING!!!
    Not obly testibg: AUTOMATIC TESTING

In order to create our test environment (beacuse we don't want to have precreated environments for testing anymore)
we can make use of containers.

Container: ISOLATED ENVIRONMENT within a Linux OS (OS running a Linux kernel) where we can run process.
ISOLATED:
√ A container have its own network configuration (OWN IP in a virtual Docker network)
√ A container has its own ENV variables. We make use of them to configure the environments (containers)
  To customize the app that comes into the container image preinstalled
√ A container has its own FileSystem
- A container may have limitations in order to access HARDWARE resources

Docker is container manager.
It consist of 2 different parts:
- A daemon which runs in the background: dockerd
- A client (a command called docker) with allows us to send instructions to the docker daemon

Docker command (client) syntax:
$ docker OBJECT_TYPE VERB [args]
    
                        VERBS
    OBJECT_TYPES
        image           list    inspect     pull    rm push
        container       list    inspect     create  rm  start   stop    restart 
        network         list    ...
        volume          list    ...

Be careful.... as almost every single docker command has an alternative syntax (alias)...
    
                                        ALIAS
    docker image list           --->   docker images
    docker image pull IMAGE_ID  --->   docker pull IMAGE_ID
    docker image rm IMAGE_ID    --->   docker rmi IMAGE_ID
    docker container list       --->   docker ps                // THIS command only show running containers (STARTED)
    docker container list --all --->   docker ps --all
    docker container start CONTAINER_NAME ---> docker stary CONTAINER_NAME
    
    docker container create --name=MY-CONTAINER-NAME \
                            image(registry/repo:tag)
                                If we don't supply a registry, the container manager being used will search for that repo in its own confugured registries
                                If we don't supply a tag, tag "latest" would be used.
                                
                                
--- 
TODAY:

- We will create a JAVA project, using maven (It's gonna be a web app - traditional web app)
- We will create some tests for that
- We will make use of a container to compile, execute the automated tests and package the project
- Configure a Sonarqube (using a couple of containers)
- We will use a container to send the code (our project) to sonarqube
- We will create our custom docker image, containing a running version of my app... so that we can do More tests
  (using selenium)... for that we need a running instance of our app (I MEAN: an INTEGRATION/TEST environment)

BEFORE THAT, we have to talk a bit more about:
ISOLATED:
√ ACCESSING CONTAINER APP PORTS within that docker virtual network
√ A container has its own FileSystem... We need to talk about VOLUMES
√ Docker-compose... We are not gonna be creating containers with that DOCKER COMMAND... It's not worthy!!!

---
    
            ---------------------------------------------------------- My company network (192.168.0.0/16)
            |                                                   |
            192.168.10.23:8888-> 172.17.0.2:80              192.168.20.29
            |     NAT (port redirection).                       |
           IvanPc                                           AlexPc in its browser: writes: http://172.17.0.2:80
            |   |
            |   172.17.0.1
            |   |
            |   +------------------------ 172.17.0.2 - Nginx container (172.17.0.2:80)
            |   |
            |   | docker virtual network
            |
            127.0.0.1:8888-> 172.17.0.2:80 
            |
            loopback network (virtual network)
            
When working with docker usually we make use of port redirections... I mean... ALWAYS
The container IP can change over the time (each time the container is restarted)... I'm not gonna use the container IP.
I could... but its not worthy
    
    docker container create --name=MY-CONTAINER \
                            -p HOST_IP:HOST_PORT:CONTAINER_PORT
                            nginx:latest
    
    Example: docker container create --name=mynginx -p 172.31.0.67:8888:80 nginx:latest
    
    If we don't provide an IP, default vbalue would be the network mask: 0.0.0.0/0... which means:
    Redirect requests from ANY IP pof the host
    
    Example: docker container create --name=mynginx -p 8888:80 nginx:latest (using default 0.0.0.0 value)
    
    By doing this, I can access my container using: localhost:8888
                                            or:     my-ethernet-network IP:8888
    That solves 2 problems:
    - For me, It's gonna be easier to connect to my container port.. as I don't need to check the container IP
      I will just use LOCALHOST
    - Alex could connect to my container... buy using my company network IP
    
---

CONTAINER FILESYSTEMS

Does a container have persistance of its data? FOR SURE !!! It has!!!!!
Same as virtual machines...
If I remove a virtual machine... or ... If I remove a physical machine... I loose the information.
Samething happens with containers.

If I don't remove a container.. Information will be there for ever!!!

The point is that when working with containers... we remove them continuously!!
    CREATE CONTAINER
    REMOVE CONTAINER
    CREATE CONTAINER
    REMOVE CONTAINER
    
We don't do that when working with physical machines... nor virtual machines..
But we do it when working with containers.

Imagine I have a container with mysql 5.7.1 installed
And I want to upgrade myql to 5.7.2
If I was working with physical or virtual machines... I would just launch an upgrade!
But when working with containers... I what I'm gonna do is just to remove the container... 
and to create a new one... with an updated image (with the newer version of the image)
WHY? Because its easier!!!!

The point here is that if we remove the container we would lose the information.
And that is OK for some scenarios (for example TESTING)
But that's not ok for other scenarios (PRODUCTION)... WE CANNOT LOSE THE DATABASE INFORMATION

In those scenarios we can make use of VOLUMES.
A Volume is a mount point in the container FILESYSTEM... (Kind a symbolic link)...
pointing to somewhere outside the container filessytem

In the MYSQL container Filesystem, database files are created in: /var/lib/mysql
And what I can do is to create a VOLUME in the container so that:
    Folder  "/var/lib/mysql" of the container actually points to my HOST "/home/ubuntu/data" folder...
    That way.. if I remove the container... It's filesystem is gonna be destroyed... but contents of my host folders (VOLUMES)
    are not gonna be touched at all!
    
    And I will be able to recreate a NEW CONTAINER ... and assign to that container the same VOLUME...
    So that NEW container can access the same information as the previous container.


    EXAMPLE $ docker container create --name=mynginx -p 8888:80 -v /home/ubuntu/environment/data:/Ivan  nginx:latest

Actually we make use of volumes for 3 different things:
- Persists container information (files or folders) after container deletion
- To inject files or folders to my container (replacing the image ones)
    For example to provide custom configurations 


    EXAMPLE $ docker container create -v /home/ubuntu/environment/my-custom-nginx.conf:/etc/ngionx/nginx.conf nginx:latest
    
    My file is going to OVERRIDE the image one... I will customize my nginx cofiguration

- To share information (files or folders) between containers 

---

As you can see, when we start to configure more and more things in our containers:
- volumes
- ports
- environment vars..
the docker command gets more and more complex...

We prefer to work with docker-compose files. 

Docker-compose is a standard... defining a YAML SCHEME to declare containers...
We can supply a docker compose YAML file to the docker command, so that docker reads the containers
specification provided in that file and automatically creates the containers for us...

Actually this is gonna have a huge advantage... 
I can share (store) those files in a SCM (such as git) with my partners... and keep that file under version control.

THAT'S EXTREMELY IMPORTANT




---



# We will create a JAVA project, using maven (It's gonna be a web app - traditional web app)

Let's use a maven archetype (template) to create our project 

$ docker image pull 3.6.3-openjdk-17    We won't execute that... Once we try to create a new container with that image... 
                                        docker will automatically pull that for us.

$ docker container create --name=mymvn 3.6.3-openjdk-17
That would provide us an enviroment wth java and mven...
Inside that ebvironment is wehere we want to execute:
    mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes 
    -DarchetypeArtifactId=maven-archetype-webapp -DarchetypeVersion=1.4
But if I do that... where are the project files gonna be created? Within the container FILESYSTEM... that's what we want?
Not actually... I want to have the files in my /home/ubuntu/environment/training/project folder.
To solve that I could make use of a volume

$ docker container create --name=mymvn \
                        -v /home/ubuntu/environment/training/project:/project
                        -w /project                                             # any command should be excuted here by default: WORKING DIR (w)
                        3.6.3-openjdk-17
$ docker container start mymvn
$ docker exec mymvn mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-webapp -DarchetypeVersion=1.4
$ # docker container stop mymvn
$ docker container rm mymvn -f

We can execute all that with just 1 single command
$ docker run --rm \
            -v /home/ubuntu/environment/training/project:/project \
            -w /project \
            maven:3.6.3-openjdk-17 \
            mvn archetype:generate \
            -DarchetypeGroupId=org.apache.maven.archetypes \
            -DarchetypeArtifactId=maven-archetype-webapp \
            -DarchetypeVersion=1.4 \
            -B \
            -DgroupId=com.training \
            -DartifactId=myproject \
            -Dversion=1.0.0
        
    --rm Removes the container after the execution automatically
    -B Makes maven run in bath mode (non interactive mode)


- Let's create some code
- Let's compile that code

$ docker run --rm \
            -v /home/ubuntu/environment/training/project/myproject:/project \
            -w /project \
            maven:3.6.3-openjdk-17 \
            mvn compile

- Let's create some tests for that
- Let's make use of a container to execute the automated tests 

$ docker run --rm \
            -v /home/ubuntu/environment/training/project/myproject:/project \
            -w /project \
            maven:3.6.3-openjdk-17 \
            mvn test

- Let's package the project

$ docker run --rm \
            -v /home/ubuntu/environment/training/project/myproject:/project \
            -w /project \
            maven:3.6.3-openjdk-17 \
            mvn package

- Configure a Sonarqube (using a couple of containers)
- We will use a container to send the code (our project) to sonarqube

$ docker run --rm \
            -v /home/ubuntu/environment/training/project/myproject:/project \
            -w /project \
            maven:3.6.3-openjdk-17 \
            mvn sonar:sonar \
            -Dsonar.projectKey=myProject \
            -Dsonar.token=squ_47e0ec1345bf1b450d93113d115267a63b6e3d7e \
            -Dsonar.host.url=http://172.31.0.67:8080

- We will create our custom docker image, containing a running version of my app... so that we can do More tests
  (using selenium)... for that we need a running instance of our app (I MEAN: an INTEGRATION/TEST environment)



# Sonarqube uses elastic search in order to index proyect files... for searching

done
# And elasticsearch requires a couple of kernel configurations... that cannot be set from the containers.
done

---

At this point we did use containers for:
- Creating a JAVA PROJECT with maven
- Compiling the code
- Execute unit tests            <<< We can do Unit and Integration tests
- Package the project
- Install Postgre and Sonar
- Execute a sonnar scanner analysis in our project  <<< We are doing code quality tests

In our case, we have a web app... and we want to do some END2END tests: system tests.
For doing that we need a bunch of thing:
- We need to deploy our application (webapp) inside a java web application server (a tool such as Tomcat, weblogic, websphere)
  The truth is that nowadays, most of the java projects are being created with Spring/SpringBoot (which is a Java Framework for developing apps)
  And Spring automatically inclkudes a Tomcat Web app server by default... OUT OF THE BOX... in the software package.
  A Spring webapp is packaged as a jar file... and within that jar file an Apache tomcat is included.
We just need to execute that jar file. thats all... IF WE ARE WORKING WITH SPRING... but that is not our case...
We need to do some work here.

We will install a Tomcat Web app server... with out webapp deployed... thru a custom container IMAGE.
Actually we are going to create out custom container IMAGE...
and use that image to create a running container with our app already deployed on a tomcat web app server.

BUT, this is only the first part.

Once we have a deployed app, we will make use of SELENIUM to automate the end2end tests against the UI.
Selenium consist of several things:
- It requires a browser... or a pool of browsers.
- It requires a program call WEB DRIVER. Each web browser has it's own WEB DRIVER.
  A web driver is a program that can manage automatically a browser:
    - Open a new tab
    - Navigate to this page...
- Provides APIs for using those WEB DRIVERS (and ... as a consecuence, control a browser) for: JAVA, PY, C#...
- We have top create a script in any of those languages, 
  that making use of that API, can send instructions to a WEB-DRIVER to control a BROWSER

But... actually, Selenium also provides another component... SELENIUM GRID... 
A selenium GRID is a farm of BROWSERS and WEBDRIVERS to control those... that can be accesess via http

---

Creating a container image, implies:
- Generate a new Container from a BASE IMAGE
- Execute commandos or copy things inside that CONTAINER
- And finally, pack the container FileSystem into a TAR FILE (image)

all that is done automatically by the "$ docker build" command.
That command requires a Dockerfile





---
We do have:

- A container runnig tomcat woith our app deployed (test enviroment)
- A container running Chrome and its webdriver
- A container running Edge and its webdriver
- A container running Firefox and its webdriver
- A container running Selenium grid, to control those browsers

Let's create a selenium script to test the app
- And let's create a container to run that script.
- We will do that with python.

