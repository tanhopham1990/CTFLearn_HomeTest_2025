# QA Automation

Automation Framework for QA

## QA Automation Structure Framework

The framework builds for QA Team trigger automation for web and mobile. 

### Prepare the environment

- Java version: JDK 17
- Maven version: 3.9.9
- Node version: 22.14.0
- Appium version: 2.17.1

### IDE Plugins require

In case you use IntelliJ, you need to install some required plug-ins are:

- Lombok
- .ignored
- SonarLint
- Rainbow CSV
- JsonToPojo

### Download apk file & Set app path in configuration files

- Download APK file:
  https://drive.google.com/file/d/11Q5tKIAPlOAYOcz3EHZEqz-_slQrErk9/view?usp=sharing

- After downloaded, please move the file to a place what you want and set the absolute path for `app` in `android.properties` or `ios.properties`

## How to build Automation project

To build the automation framework, we need check-out the project to local

### How to build framework

- From the terminal

`cd {baseDir}`

- Run the maven build code

`mvn clean install`

### How to execute one or more test cases

- Execute the test

-- Web: `mvn test -Denvironment=qa -Dgroups=webCases`

-- Mobile: `mvn test -Dgroups=androidCases`

### How to get uiAutomation results

[Extent Report] After the uiAutomation is executed completed on terminal, the result will be generated and put on 'test-output' folder

- From the terminal

`cd {baseDir}/test-output`
