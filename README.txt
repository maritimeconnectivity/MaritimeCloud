THIS REPOSITORY IS NO LONGER ACTIVELY BEING MAINTAINED

----------------------------------------------------------------------------------------------------------------------------------------

MaritimeCloud - A communication framework enabling efficient, secure, reliable and seamless electronic information exchange between maritime stakeholders across available communication systems.

Last updated 3. June 2015, Copyright 2012 Danish Maritime Authority


Links
-------------------------------------------------------------------------------
Homepage      : http://dev.maritimecloud.net
Javadoc       : http://dev.maritimecloud.net/apidocs/
Source        : https://github.com/MaritimeCloud/MaritimeCloud

Issues        : https://github.com/MaritimeCloud/MaritimeCloud/issues
Mailing Lists : http://groups.google.com/group/maritimecloud

Build CI      : https://travis-ci.org/MaritimeCloud/MaritimeCloud

License       : http://www.apache.org/licenses/LICENSE-2.0.html


Build Instructions
-------------------------------------------------------------------------------
Prerequisites: Java 1.8 + Maven 3.x.x
> git clone https://github.com/MaritimeCloud/MaritimeCloud.git
> cd MaritimeCloud
> mvn install

See the developer guide at http://dev.maritimecloud.net for getting started.


Source Code Organization
-------------------------------------------------------------------------------
The repository is organized into the following components.

mc-core/                Core classes for the Maritime Cloud

mc-mms/                 The Maritime Messaging Service
   distribution         Generates single jar for distribution
   mms-common           Common code used for both the client and server
   mms-client-api       The API exposed to users
   mms-client-impl      The implementation of the MMS service
   mms-server           The server part of the MMS service
   mms-tck              A TCK for testing MMS implementations
   
mc-msdl/                The Maritime Service Definition Language
   msdl-core            The parser of MSDL
   msdl-javagenerator   Generates Java stubs from MSDL
   msdl-maven-plugin    A maven plugin for using MSDL
   msdl-testproject     A testproject used for running some tests

[![Build Status](https://travis-ci.org/MaritimeCloud/MaritimeCloud.svg?branch=master)](https://travis-ci.org/MaritimeCloud/MaritimeCloud)
