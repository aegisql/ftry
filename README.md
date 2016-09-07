# ftry
# Functional wrapper for try-catch block in Java

This is a fully functional analog of Java's
```java
try {
   doSomething(); // this code can throw E1 or E2 or unchecked exceptiuons
} catch(E1 e1) {
   processE1();
} catch(E2 e2) {
   processE2();
} finally {
  finish();
}
```

Same code can be overwritten using the ftry library:

```java
Try t = new Try(()->{
   doSomething(); // this code can throw E1 or E2 or unchecked exceptiuons
}).orCatch(E1.class, (E1 e)->{
   processE1();
}).orCatch(E2.class, (E2 e)->{
   processE2();
}).withFinal(()->{
  finish();
});

boolean result = t.evaluator(Exception.class).eval();
```

## Release history

1.0.1

bugfixes, simplified evaluator interface

1.0.0

First release

## maven dependency
```xml
<dependency>
    <groupId>com.aegisql</groupId>
    <artifactId>ftry</artifactId>
    <version>1.0.1</version>
</dependency>
```

Check for latest version at http://mvnrepository.com/artifact/com.aegisql/ftry

