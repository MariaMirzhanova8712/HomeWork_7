import io.restassured.internal.RequestSpecificationImpl;
import io.restassured.internal.TestSpecificationImpl;
import io.restassured.specification.RequestSender;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class RestAssured {
   public static RequestSpecification given() {

       return given();
    }

    public static RequestSender given(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
       return new TestSpecificationImpl(requestSpecification,responseSpecification);
    }
}
