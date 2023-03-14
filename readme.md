# A New Bank

A New Bank is an API based banking app that runs in the command line.




## Sprint Rotation

Contributions are always welcome!

| Week| Scrum Master| Product Owner | Scrum Team |  
| ------------- | ------------- | --------    | ------|
| 1 | Ben | Luke   | Bradley, Ali, Sam, Ray |
| 2 | Bradley | Sam | Ali, Luke, Ray, Ben |
| 3 | Ali | Ray | Luke, Ben, Sam, Bradley|
| 4 | Luke | Ben | Bradley, Ali, Sam, Ray |
| 5 | Sam | Bradley | Ali, Luke, Ray, Ben |
| 6 | Ray | Ali | Luke, Ben, Sam, Bradley|

## Getting started with git

To clone the main branch of the project run the following code:

``` git clone https://github.com/LOdweebles/SE2-Coursework.git ```

Each week, the ScrumMaster should create a new branch of the main project for the weeks sprint. To do this run the following command:

``` git branch <sprintname>
git checkout <sprintname>
```

This will create a new branch. To merge this back into the main branch, run the following:

``` 
git add .
git commit –m "Some commit message"
git checkout main
Switched to branch 'main'
git merge <sprintname>
```

Push the code back up to the main github repository:

``` git push https://github.com/LOdweebles/SE2-Coursework.git main ```


## How to compile and run
Navigate to your favourite Terminal/ command line utility to the NewBank folder from the clone request. Run the following code:

```
javac newbank/server/NewBankServer.java
java newbank/server/NewBankServer
```

This will compile and run the server in that Terminal window.

Then open another Terminal window and run the following:

```
javac newbank/client/NewBankClient.java
java newbank/client/NewBankClient
```

This will execute the client that will connect to the local host of the running server application.

To stop all processes, use CTRL+C in the Terminal window running the server process.

## Roadmap: Tasks for this weeks sprint

- Documentation and commenting of code

- Running of test code for adding a new customer

## Latest updates

Added .gitignore to the project to remove class files


