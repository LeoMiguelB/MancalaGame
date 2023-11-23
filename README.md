# Project Title

Text Based Mancala Game

## Description

Built using Java, and tested with Gradle. This game is played with two players, and is based of the classic game Mancala.

## Getting Started

### Dependencies

- Gradle 5
- Windows 11
- OOP

### Executing program

* How to build and run the program
* Step-by-step bullets
- first build the program
```
gradle build
```
Expected Output: 
```BUILD SUCCESSFUL in 913ms
5 actionable tasks: 5 up-to-date
```

- now run the echo command
```
gradle echo
```
Expected Output: 
```
> Task :echo
To run the program from jar:
java -jar build/libs/TextUI.jar
To run the program from class files:
java -cp build/classes/java/main ui.TextUI

BUILD SUCCESSFUL in 535ms
1 actionable task: 1 executed
```

- now use the last command listed
```
java -cp build/classes/java/main ui.TextUI
```

## Limitations

- bugs can occur if input for the pits is anything other than a number  

## Author Information

Name: Leo Bantolino

Email: lbantoli@uoguelph.ca

## Development History

Keep a log of what things you accomplish when.  You can use git's tagging feature to tag the versions or you can reference commits.

* 0.5
    * Test cases all implemented
    * See [commit change](112bcec680b4e5e2b2de48a8d0a9f5e842d58c5f)
* 0.4
    * Test case done for captureStones()
    * See [commit change](df73c7d0e3ca0e586b72e126cd6ec349e8e4b284)
* 0.3
    * Exception handling implemented
    * See [commit change](d0bc1d229d94eacb44aa06af1c90f0bf9a537c72)
* 0.2
    * Various bug fixes and optimizations
    * Passed all given test cases
    * See [commit change](0e55f743aec8a586f6659e5a05bbd9cc0e3a96b9)
* 0.1
    * Initial Release 
    * Skeleton is all setup
    * See [commit change](1e398d2d69b672aa8c3a7369509f6bd71ea49299)

## Acknowledgments

Inspiration, code snippets, etc.
* [awesome-readme](https://github.com/matiassingers/awesome-readme)
* [simple-readme] (https://gist.githubusercontent.com/DomPizzie/7a5ff55ffa9081f2de27c315f5018afc/raw/d59043abbb123089ad6602aba571121b71d91d7f/README-Template.md)