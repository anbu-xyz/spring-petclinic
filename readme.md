# Spring PetClinic Sample Application 

This project is copied from the original [Spring PetClinic](https://github.com/spring-projects/spring-petclinic) and customized to the way I like to build my spring boot projects.

The purpose of the project is to use this as a template for my new startup projects.

## Layered architecture

+----------------------------------------------------------------------------------------------------+
|  Controller - Manages endpoints redirects to view after updating the model - serves domain objects |
+----------------------------------------------------------------------------------------------------+
|  DAO - Data access layer containing the business logic - serves DTO                                |
+----------------------------------------------------------------------------------------------------+
|  Repository - Repositories that fire SQL against database - serves Entity Objects                  |
+----------------------------------------------------------------------------------------------------+
|  Database                                                                                          |
+----------------------------------------------------------------------------------------------------+

## License

The Spring PetClinic sample application is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).

The changes in this project is also licensed under [Apache License](LICENSE).
