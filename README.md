# Spoon Examples

## Introduction

spoon-examples gives analysis and transformation processors showing the usage of the open-source library [Spoon](https://github.com/INRIA/spoon).

You can see these processors in:

- [`src/main/java/fr/inria/gforge/spoon/analysis`](https://github.com/SpoonLabs/spoon-examples/tree/master/src/main/java/fr/inria/gforge/spoon/analysis) for analysis.
- [`src/main/java/fr/inria/gforge/spoon/transformation`](https://github.com/SpoonLabs/spoon-examples/tree/master/src/main/java/fr/inria/gforge/spoon/transformation) for transformation.
  - [notnullcheck](https://github.com/SpoonLabs/spoon-examples/tree/master/src/main/java/fr/inria/gforge/spoon/transformation/notnullcheck) improves reliability
  - [bound](https://github.com/SpoonLabs/spoon-examples/tree/master/src/main/java/fr/inria/gforge/spoon/transformation/bound) is a transformation based on annotations
  - [mutation](https://github.com/SpoonLabs/spoon-examples/tree/master/src/main/java/fr/inria/gforge/spoon/transformation/mutation) is a simple mutation testing engine
  - [spoonerism](https://github.com/SpoonLabs/spoon-examples/tree/master/src/main/java/fr/inria/gforge/spoon/transformation/spoonerism) a basic example of transforming classes to extend a common parent class.  Accompanied by the [spoonerism.fodp](https://github.com/SpoonLabs/spoon-examples/blob/master/docs/spoonerism.fodp) presentation.


## Usage

You can run the tests of the project with a `mvn test` command on the root of the project to execute all examples (all test classes).

```console
git clone https://github.com/SpoonLabs/spoon-examples.git
cd spoon-examples
mvn test
```

