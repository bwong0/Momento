# Code Style Guide
In general, follow Google's Java Style Guide:
https://google.github.io/styleguide/javaguide.html#s2.3.3-non-ascii-characters

## Formatting Guideline
- Indent/Tab with 4 spaces
- Note the spacing and bracket {} placement in the following example:
```java
return () -> {
    while (condition()) {
        method();
    }
};

return new MyClass() {
    @Override public void method() {
        if (condition()) {
            try {
                something();
            } catch (ProblemException e) {
                recover();
            }
        } else if (otherCondition()) {
            somethingElse();
        } else {
            lastThing();
        }
        {
            int x = foo();
            frob(x);
        }
    }
};
```

## Comments Guideline
- Each class and method must have a Javadoc comment block explaining:
    + Purpose
    + Usage
- Example Javadoc:
```java
/**
 * Multiple lines of Javadoc text are written here,
 * wrapped normally...
 */
public int method(String p1) { ... }
```

## Other Coding Style

1. Classes
   1.1

2. Methods
   2.1

3. Variable Names
   3.1




