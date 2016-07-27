# ftry
Functional wrapper for try-catch block in Java

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

boolean result = t.evaluator().eval();
```

## Release history

1.0.0