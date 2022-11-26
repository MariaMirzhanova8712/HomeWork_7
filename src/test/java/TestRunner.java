
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
        tags = "@deleteBookingWithWrongId_negative",
        features = "src/test/resources",
        glue = "steps"
)
public class TestRunner {
}
