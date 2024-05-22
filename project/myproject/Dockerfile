# First thing we want to do is to compile, test and package the app... fro the source code
# We can do that with maven... as we have seen.

##### CREATE A CONTAINER (a new one) from this image... Let's call that container PACKAGER
FROM maven:3.6.3-openjdk-17 AS packager

##### COPY my folder contents inside that container in /app
COPY . /app/
RUN rm -rf /app/target # Remove previous compilations/packages... We want to make sure a new one is created right now

##### Set that folder as the working folder... 
WORKDIR /app

##### Execute the project packaging
RUN mvn package
###### As a consecuence of that commandm we will have in side that container the app packaged in : /app/target/webapp.war

## Once packaged: we will have a .war file... inside the target folder of my project

## Then we need a Tomcat web app server
##### Let's create a SECOND CONTAINER based on the BITNAMI TOMCAT IMAGE
FROM bitnami/tomcat:latest
###### At this point we have 2 containers
#### Let's copy the /app/target/webapp.war FROM THE packager container to the /app folder of this container
#### In the documentation of this image they said, that's all we need to deploy the app wiothin this tomcat 
COPY --from=packager /app/target/webapp.war /app/

#### Let's set a PASSWORD for TOMCAT (It's mandatory)
ENV TOMCAT_PASSWORD=password

##### AND WE ARE DONE!
##### When executing "docker build" with this Docker file, docker will create these 2 containers... 
###### and will generate a container image based on the latest one
###### This is an example of what we call a MULTISTAGE Docker file

# EXECUTE WITH docker build . -t myproject:rc
## Thaty is something we could have done... but we are not going to do it like this.