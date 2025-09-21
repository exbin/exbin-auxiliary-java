ExBin Project - Auxiliary Libraries
===================================

Libraries and utility tools for ExBin Project written in Java.

Homepage: https://exbin.org  

Structure
---------

Project is constructed from multiple repositories.

  * modules - Sources split in separate modules
  * src - Sources related to building distribution packages
  * resources - Related resource files, like sample files, images, etc.
  * doc - Documentation + related presentations
  * gradle - Gradle wrapper

Compiling
---------

Java Development Kit (JDK) version 8 or later is required to build this project.

For project compiling Gradle 8.1 build system is used: https://gradle.org

You can either download and install gradle or use gradlew or gradlew.bat scripts to download separate copy of gradle to perform the project build.

Build commands: "gradle build" and "gradle distZip"

License
-------

Default license is Apache License, Version 2.0 - see LICENSE.txt. Specific libraries can have different license.
