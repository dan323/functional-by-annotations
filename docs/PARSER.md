# Parser Combinators

## Overview

The Parser module provides a functional approach to parsing using **parser combinators**. It implements the monadic and applicative interfaces, allowing you to compose simple parsers into complex parsing logic.

## Core Concepts

A parser is fundamentally a function that takes an input string and returns either:
- **Success**: A parsed value along with the remaining input
- **Failure**: A list of parsing errors

## Basic Parsers

### Character Parser
Parse a specific character:
```java
Parser<Character> plus = Parser.charParser('+');
Parser<Character> digit = Parser.predicateParser(Character::isDigit);
```

### String Parser
Parse an exact string:
```java
Parser<String> hello = Parser.stringParser("hello");
```

### Integer Parser
Parse an integer:
```java
Parser<Integer> number = Parser.intParser();
```

## Parser Combinators

### Optional Parsing
Try to parse a value, returning `Maybe<A>`. This also provides lookahead/peeking without consuming input:
```java
Parser<Maybe<Integer>> maybeInt = Parser.optional(Parser.intParser());

// For peeking without consuming input:
Parser<Maybe<Character>> nextIsDigit = Parser.optional(
    Parser.predicateParser(Character::isDigit)
);
var result = nextIsDigit.apply("123abc");
// Returns: Either.right(Pair(Maybe.of('1'), "123abc"))  // input not consumed
```

### Many/Some
Parse zero or more / one or more occurrences:
```java
// Zero or more digits
Parser<FiniteList<Character>> digits = 
    ParserApplicative.many(Parser.predicateParser(Character::isDigit));

// One or more digits  
Parser<FiniteList<Character>> oneOrMoreDigits = 
    ParserApplicative.some(Parser.predicateParser(Character::isDigit));
```

### Separated By (sepBy)
Parse values separated by a separator:
```java
// Parse comma-separated integers
Parser<FiniteList<Integer>> numbers = Parser.sepBy(
    Parser.intParser(),
    Parser.stringParser(",")
);

var result = numbers.apply("1,2,3");
// Returns: Either.right(Pair(FiniteList.of(1, 2, 3), ""))

var empty = numbers.apply("abc");
// Returns: Either.right(Pair(FiniteList.nil(), "abc"))  // zero values is ok
```

### Disjunction/Alternative
Try multiple parsers in sequence:
```java
Parser<Character> plusOrMinus = ParserApplicative.disjunction(
    Parser.charParser('+'),
    Parser.charParser('-')
);
```

## Advanced Usage

### Parsing Signed Numbers
```java
// Parse optional sign followed by digits
Parser<FiniteList<Character>> digits = 
    ParserApplicative.some(Parser.predicateParser(Character::isDigit));

Parser<Optional<Character>> sign = Parser.optional(
    ParserApplicative.disjunction(
        Parser.charParser('+'),
        Parser.charParser('-')
    )
);

Parser<String> signedNumber = ParserApplicative.liftA2(
    (opt, ds) -> (opt.isPresent() ? opt.get() : "") + Parser.fromChars(ds),
    sign,
    digits
);
```

## Error Handling

Parsers return errors as `FiniteList<Parser.ParserError>`:

```java
var result = Parser.intParser().apply("abc");
// Returns: Either.left(FiniteList.of(ParserError.unexpectedChar('a')))
```

Common errors:
- `ParserError.unexpectedChar(c)` - Unexpected character encountered
- `ParserError.unexpectedEnd()` - Unexpected end of input
- `ParserError.empty()` - Empty parser used

## Best Practices

1. **Leverage composition**: Build complex parsers from simple ones
2. **Use optional() for lookahead**: To inspect without consuming input, use `optional()`
3. **Error handling**: Always handle `Either.left()` cases in your application code
4. **Type safety**: The type system ensures parsers compose correctly

## Architecture

- **Parser Interface**: Extends `StateWithError<A, String, FiniteList<Parser.ParserError>>`
- **ParserApplicative**: Provides functor and applicative operations
- **ParserError**: Structured error representation

