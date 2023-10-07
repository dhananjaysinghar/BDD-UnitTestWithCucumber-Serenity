package com.user;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        tags = "@Users",
        plugin = {"pretty", "html:target/reports/cucumber.html",
                "json:target/reports/cucumber.json"},
        features = "src/test/resources/",
        snippets = CucumberOptions.SnippetType.CAMELCASE)
public class TestRunner {


}
