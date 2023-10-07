package com.user.steps;

import io.cucumber.java.BeforeAll;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

@RequiredArgsConstructor
@ContextConfiguration(locations = "classpath:cucumber.xml")
public class BaseStepDefinition {

    @BeforeAll
    public static void setUp() {

        Network componentTestNetwork = Network.newNetwork();
        // MONGO DB
        GenericContainer mongoContainer = getMongoContainer(componentTestNetwork);
        startContainer(mongoContainer, "27017:27017");
        org.testcontainers.Testcontainers.exposeHostPorts(mongoContainer.getFirstMappedPort());
        setEnvPropertiesForService(mongoContainer);
    }

    public static GenericContainer getMongoContainer(Network componentTestNetwork) {
        return new GenericContainer<>(DockerImageName.parse("mongo:latest"))
                .withExposedPorts(27017)
                .withNetworkAliases("mongoDB")
                .withNetwork(componentTestNetwork)
                .withCreateContainerCmdModifier(cmd -> cmd.withName("mongoDbContainer"))
                .withEnv("MONGO_INITDB_ROOT_USERNAME", "admin")
                .withEnv("MONGO_INITDB_ROOT_PASSWORD", "password")
                .withEnv("MONGO_INITDB_DATABASE", "test")
                .withCommand("--auth")
                .withStartupTimeout(Duration.ofMinutes(10));

    }

    private static void setEnvPropertiesForService(GenericContainer mongoDBContainer) {
        System.setProperty("MONGO_DB_DATABASE_NAME", "test");
        System.setProperty("MONGO_DB_URI", "mongodb://admin:password@localhost:"
                + mongoDBContainer.getFirstMappedPort() + "/?authSource=admin");
    }

    private static void startContainer(GenericContainer container, String port) {
        List<String> portBindings = new ArrayList<>();
        portBindings.add(port);
        container.setPortBindings(portBindings);
        container.start();
        container.waitingFor(Wait.forHealthcheck());
    }
}
