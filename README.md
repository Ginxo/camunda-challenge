# Camunda Challenge

## Instructions

### Jar packaging

Prerequisites:

* Java 11+
* Maven 3.x+

Just execute
`mvn clean package spring-boot:repackage`

### Jar Execution

Prerequisites:

* Java 11+

Just execute
`java -jar target/camunda-challenge.jar`

Once executed, go to http://localhost:8080/swagger-ui/index.html
There you will find a swagger client for the rest API, execute the /query/bpmn by filling the required attributes (query
params) and play

## Why questions...

### Why documentation

I know it's not required but just wanted to provide very simple notes. I have the feeling no one will read this, but I'm
happy writer :)

### Why over-engineering for the simple use case

I'm happy doing things in what I consider "right way" and sometimes the most simple things can start being complicated
and then you start wondering whether you need to start structuring the stuff and so on... so my experience tells me,
always structure your stuff, no matter the complexity

### Why am I using spring for this?

it is faster than implementing standalone application, not due to spring itself but the set of things that come with it,
like REST + Swagger you don't have to waste time on implementing arguments, messaging, exceptions...

### Why Swagger?

it provides useful entry point for executing the stuff, you don't to worry about user interface for this specific case
where you just want to check whether things are working or not

### Why unit tests

I know they are not required, but I hope you can forgive me for being lazy on manually testing stuff. Again, sorry for
this.
They don't intend to cover the source code but to provide a way to easily test specific scenarios and not have to
manually trigger them for every implementation or change you make

### Why github actions and releases

I try to automatize everything that can be automatized, so that's the reason why

