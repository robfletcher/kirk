---
---

# Additional Modules

In addition to the core functionality provided by the `strikt-core` module, Strikt has the following optional modules:

## Protobuf

Extensions for testing code that uses Protobuf / gRPC.
See the {{ anchor('API docs', 'strikt.protobuf') }}.

Add the following to your dependencies:

```groovy
testCompile "io.strikt:strikt-protobuf:{{ site.version }}"
``` 