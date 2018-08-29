[![][logo]][website] 

# About

The plugin `blueriq-plugin-webfileupload-container` used to be a part of the Blueriq Runtime but since the release of Blueriq 11, the support of this plugin is discontinued. 
Blueriq released the sources of this plugin with the intention of enabling customers to migrate to the AQ_File_Upload container at their own pace but without support from Blueriq. No rights reserved.

# Build from source

To compile and build war use:

```bash
mvn clean verify -DskipTests
```

To test the war, please add the Blueriq `license.aql` to `src\test\resources` and use:

```bash
mvn clean verify
```

# Run example

Deploy `Runtime.war` to Tomcat container. Create a configuration folder and add Blueriq `license.aql` or package Blueriq `license.aql` by adding it to `src\main\resources`.
Start Tomcat container with the following parameters:

```bash
-Dspring.config.additional-location=file://path_to_conf/ # URI of the configuration folder which contains the Blueriq license.
-Dspring.profiles.active=native,development-tools
```

# Studio container
![][webfileupload_definition]
---
![][webfileupload_params_1]
---
![][webfileupload_params_2]

[webfileupload_definition]: images/webfileupload_general.png
[webfileupload_params_1]: images/webfileupload_parameters_1.png
[webfileupload_params_2]: images/webfileupload_parameters_2.png

[logo]: https://www.blueriq.com/wp-content/uploads/2018/07/BLUERIQ-rgb-logo-kleur-gradient-PNG-300x111.png
[website]: http://www.blueriq.com