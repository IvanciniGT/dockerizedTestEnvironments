
# Test environments

In a company wer make use of different environments: Development, Test, Production...

The test environment is where we perform tests.
Time ago, all those environments were pre-created whenever a new project starts.
Nowadays we make use of a different strategy.
We don't want a precreated test-environment... Why?

Why were we using a separate environment for testing?
Why we didn't use the development environment for testing?
Because we have no control of that environment... Each developer configures it's own development environment...
And even in case we have a separate development environment... Evebody is touching (installing, coping,...) that environment.
We cannot guarantee that the environment is stable enough to execute those tests.

Actually samething happens with the test environment... in case we are using a precreated test-environment.
On day1, the environment is clean... but ... as soon as we start to install, copy... in that environment,
we cannot guarantee the stability of that environment anymore.

All this changed when we started to apply agile methodologies to out projects.

Before applying those agile methodologies, we use to make use of Traditional methodologies (Waterfal):
- Collect requirements
- Plan and design the whole system
- Develop the whole system
- Perform tests
- Deliver (install on production)

With traditional methodologies, we only executed tests once... at the very very end of the project.

With agile methodologies, everything changed!

# What's the main characteristic of an agile methodology?

Incremental delivery of the product.

- First delivery... 1 month after the project started   (Sprint 1) - Deliver 10% of the final functionality
    -> Test 10% of the requirements
- In a couple of additional weeks... Second delivery    (Sprint 2) - Deliver +5% of the final functionality
    -> Test 5% + previous 10% again... as we have changed the code...  
- In a couple of additional weeks... Third delivery     (Sprint 3)

Each time we deliver... we deliver to PRODUCTION...
That means... we need to perform exhaustive testing on the software... It goes to production.

- Working software is the primary MEASURE of progress. (1)

This sentence is definining a metric... for a dashboard.
How are we going to measure the prograss of our project? What can we use for that?
- The concept: "WORKING SOFTWARE" 
  What "WORKING SOFTWARE" is? Software that works...
  Who says that a software works? 
    CUSTOMER. Imagine I am producing cars... Who is goiung to say that the car is working propertly? is ok?
              The customer? NOT AT ALL... That is not its RESPONSABILITY. It's mine.
    TESTING is who guarantee that the software is working.

How are we going to measure the prograss of our project? What can we use for that?
THE NUMBER OF TESTS BEING PASSED IN A PERIOD OF TIME (week... a day...)

In each iteration (SPRING) we have to test... we don't have money, resources, time for that in the project.
So... what do we need to do here? AUTOMATION

---

# DEVOPS

Devops is a culture... it's a movement... it is a phylosophy... defending: AUTOMATION!

We want to aumate whatever we can (all we can) between DEV ---> OPS

                                Automate?       Tools   
    PLAN                            x
    CODE                            x
    BUILD                           √
                                                JAVA: maven, gradle, sbt
                                                C#:   msbuild, dotnet, nuget
                                                PY:   PIP, poetry
                                                JS:   yarn, npm, webpack

---> If we manage to automate to this point : AGILE DEVELOPMENT

    TEST            
        design the test             x
        execute the test            √
                                                JUNIT, UNITTEST, MSTEST, NUNIT
                                                Web FrontEnd:
                                                    Selenium, Karma, Cypress
                                                Mobile frontend:
                                                    Appium
                                                Performance... stress:
                                                    JMeter, Loadrunner
                                                Backend web services: (REST, SOAP)
                                                    Postman, SOAPUI, ReadyAPI, karate
    Those tests need to be run on a test environment...
    And time ago, as we said earlier, we use to precreate those test environments (on day 1)
    Nowadays, we prefer to create these environments ON DEMAND... and DISPOSE them as soon as the test is done.
    
    I want a CLEAN test environment each time.

---> If we manage to automate to this point : Continuous Integration

    If we can CONTINUOUSLY have the latest verion under software control deployed in an INTEGRATION environment,
    being tested by automated process... then we said that we are doing CONTINUOUS INTEGRATION.
    
    What is the product of a CONTINUOUS INTEGRATION PIPELINE? A TEST REPORT!
        That is what JENKINS PROVIDES... whe implementing a CI pipeline!
        Why do I want that automatically generated TEST REPORT? (1)

    Which name does that environment receive in most companies?
    I mean the environment where we execute tests?
    Beacuse this environment actually receives a bunch of manes: TEST, QA, PRE-PRODUCTION...
    The most traditional name for this environment is the INTEGRATION ENVIRONMENT.

    RELEASE                         √
        Is the fact of putting an stable version of my software, ready to deploy, on my client hands
        
---> If we manage to automate to this point : Continuous Delivery

    DEPLOY

---> If we manage to automate to this point : Continuous Deployment

    OPERATE
    MONITOR

---> Full devops implementation.

TO AUTOMATE TESTING is just to create programs that test my main program.
In order to make those we will use: 
                                                JUNIT, UNITTEST, MSTEST, NUNIT
                                                Web FrontEnd:
                                                    Selenium, Karma, Cypress
                                                Mobile frontend:
                                                    Appium
                                                Performance... stress:
                                                    JMeter, Loadrunner
                                                Backend web services: (REST, SOAP)
                                                    Postman, SOAPUI, ReadyAPI, karate

But ... who is going to execute those programs?
Actually... we will need to have an INTEGRATION environment (test environment)
for installing my software and execute those programs (automated tests)
So... who is going to (not only) execute those programs... but orchestrate them? JENKINS

On Jenkins, we create scripts (pipelines)... that will execute my automations (environment creations, test executions)


As we have said, we prefer to have a clean environment each time we need to execute the tests.
That is the best guarantee that we can have... That starting from scratch, in a clean environment, everything works properly.

In order to create those test environments we will use CONTAINERs.
We can create those container with docker... or any other container management tool: Podman, Containerd, CRIO.

Probably docker is the most common (used) option... and that is what we will use in this training.

---

What are we going to be doing:
- Small Review about containers                                                 TODAY
- I will share a project with you all (JAVA-> Webapp).                          TOMORROW
    -  Compile and package it (MAVEN) 
    -  Tests with JUNIT
    -  SELENIUM for the UI (python)
- We will install Jenkins                                                       WEDNESDAY
    -  We will create a pipeline to orchestrate: Compilation + Test execution (junit + selenium + sonarqube)
    ---> TEST REPORT (Implement a full continuous integration pipeline)

JAVA, MAVEN, JUNIT, SELENIUM, BROWSERS (edge + chrome + firefox), PYTHON + WEBDRIVER, SONARQUBE, JENKINS

---

# Containers

## What is a container?

An ISOLATED environment within a Linux OS (I mean a OS running a Linux kernel), where we can execute processes.
ISOLATED: 
    √ A container has its own network configuration (and therefore, its own IP address) in a docker virtual network
    √ A container has its own filesystem
    √ A container has its own environment variables
    - A container may have limitations accessing the hardware
    
A container is just another alternative to deploy and execute software on top of an OS.

## Traditional installation procedure

        App1  +  App2  +  App3              Problems:
    ------------------------------          - Not portable
        Operating System                    - If those apps require a different OS... that would be a problem
    ------------------------------          - One app can potencially access another app info.. (RAM, HDD): VIRUS
        Computer                            - App1 has a bug... and turns CPU to 100%... At that moment:
                                                App1 goes OFFLINE
                                                App2 goes OFFLINE too
                                                App3 goes OFFLINE too

## Virtual Machines

        App1   |  App2  +  App3              Problems:
    ------------------------------          - Makes configuration way more complex
        OS1    |    OS2                     - The performances is gonna be decreased
    ------------------------------          - I loss resources... 
        VM1    |    VM2                         Each OS requieres its own amount of RAM, HDD, CPU
    ------------------------------ 
        Hypervisor: 
        hyperV, vmware, citrix, 
        kvm, virtualbox
    ------------------------------ 
        Operating System           
    ------------------------------ 
        Computer                   

## Containers

Are another way of achive an isolated environment for running apps (process).
We cannot install an OS within a container... NOT AT ALL!!!!!

        App1   |  App2  +  App3             
    ------------------------------          
        C1     |    C2                      
    ------------------------------ 
        Container manager:
        Docker, Podman, CRIO, Containerd
        (NO KUBERNETES, NO OPENSHIFT)
    ------------------------------ 
        Operating System           
    ------------------------------ 
        Computer                   
        
This solves almost all the problems we have with traditional installations (but the os requirement one)
without any of the problems os VM.

And that is why we love containers... and why nowadays every single entreprise software is delivered via container images.

A continer is created from a container images.

### Container IMAGE

It's actually a compress file (tar file) containing:
- A tipical POSIX folder structure (not mandatory... but you will always see that)
    /bin
    /home
    /opt
    /var
    /etc
- With a software ALREADY installed on it.
- In addition we could have extra software installed on those folders.

It also contains some additional metadata:
- Command that needs to be executed once a container created from this image is started
- Some enviroment variables values
- ...

We find container images in container image repository registries. The most famous one is: Docker hub
- quay.io (the one from redhat)
- microsoft container registry
- oracle container registry

A container image is identified by:
- Registry
- Repository
- Tag
A container image id is an URL... in the form:
    http://registry/repo:tag

Usually, we don't provide the registry (http://registry/)
    By default, container manager have preconfigure some registries... And I don't use to provide that information...
    We just provide the repository:tag
    
IMPORTANT NOTE:
    - In case we only provide a repository... (we don't provide a tag)
    The "latest" tag is downloaded
    And be carefull... I din't say the "latest" image.... I said the "latest" tag
    "latest" is not a magical word in docker or containers...
    A company can provide a imagen with tag "latest" or not.
    
    Usually, tag "latest" is used by some (most) companies to identify the latest stable version (production ready) of their software.
    
---
Nginx: Reverse proxy (+ with web server capabilities)
Apache httpd: Web server (+ with reverse proxy capabilities)

