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
