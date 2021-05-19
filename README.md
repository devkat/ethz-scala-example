# Example code for the talk "Scala: Functional Programming on the JVM"

## Setup

1. Install [sbt](https://www.scala-sbt.org)

## Build and run

1. Run sbt

    ```bash
    cd ethz-scala-example
    sbt
    ```

1. Build the client application

   ```bash
   sbt:todo> fastLinkJS
   ```

1. Start the server

    ```bash
    sbt:todo> reStart
    ```
    
1. Access the example application at http://localhost:8080/

## Hot-reload (development mode)

Start two separate terminals and use the `~` prefix for continuous compilation:

Terminal 1 (client):

```bash
sbt:todo> ~fastLinkJS
```

Terminal 2 (server):

```bash
sbt:todo> ~reStart
```

