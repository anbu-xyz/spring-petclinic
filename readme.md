# Spring PetClinic Sample Application 

This project is copied from the original [Spring PetClinic](https://github.com/spring-projects/spring-petclinic) and customized to the way I like to build my spring boot projects.

The purpose of the project is to use this as a template for my new startup projects.

## Layered architecture

```
          +----------------------------------------------------+
          | Endpoint - deals with http/REST, error handling    |
          |                         etc                        |
          +----------------------------------------------------+
                               |
                               v
          +----------------------------------------------------+
          | Controller - deals with non-business concerns      |
          | like model to DTO translation, security, session   |
          |                     management                     |
          +----------------------------------------------------+
                               |
                               v
          +----------------------------------------------------+
          | Model - contains business logic - deals with       |
          |             domain level objects                   |
          +----------------------------------------------------+
                               |
                               v
          +----------------------------------------------------+
          | Repository - Repositories that fire SQL against    |
          |         database - serves Entity Objects           |
          +----------------------------------------------------+
                               |
                               v
          +----------------------------------------------------+
          |                Database                            |
          +----------------------------------------------------+
```

## License

The Spring PetClinic sample application is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).

The changes in this project is also licensed under [Apache License](LICENSE).
