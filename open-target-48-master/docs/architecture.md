### Architectural decisions

#### Programming languages

- Java 8
- Groovy

Java because it is one of the most widely used languages in the current team
which makes it easier to get the feedbacks and collaborate with others.

Groovy is chosen`because of its expressiveness and nice DSL.


#### Framework

- Spring Batch
- Spock

The project does not require any user interactions and is better suited for
offline processing. Spring batch is already already used by many organisations
and it seems a de facto for such use cases, also it is built on Java.