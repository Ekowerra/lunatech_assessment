import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.{Status}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.test.CSRFTokenHelper._

/**
 * Functional tests start a Play application internally, available
 * as `app`.
 */
class FunctionalSpec extends PlaySpec with GuiceOneAppPerSuite {

  "Routes" should {

    "send 404 on a bad request" in  {
      route(app, FakeRequest(GET, "/boum")).map(status(_)) mustBe Some(NOT_FOUND)
    }

    "send 200 on a good request" in  {
      route(app, FakeRequest(GET, "/")).map(status(_)) mustBe Some(OK)
      route(app, FakeRequest(GET, "/reports")).map(status(_)) mustBe Some(OK)
      route(app, FakeRequest(GET, "/countries/airports")).map(status(_)) mustBe Some(OK)
      route(app, FakeRequest(GET, "/countries/runways")).map(status(_)) mustBe Some(OK)
      route(app, FakeRequest(GET, "/form")).map(status(_)) mustBe Some(OK)
    }
    "send 303 on a good request" in {
      route(app, FakeRequest(POST, "/airports").withFormUrlEncodedBody("input" -> "RU").withCSRFToken).map(status(_)) mustBe Some(SEE_OTHER)
    }
    "send 400 on a bad request" in {
      route(app, FakeRequest(POST, "/airports").withFormUrlEncodedBody("input" -> "").withCSRFToken).map(status(_)) mustBe Some(BAD_REQUEST)
    }

  }

  "HomeController" should {

    "render the index page" in {
      val home = route(app, FakeRequest(GET, "/")).get

      status(home) mustBe Status.OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Index")
    }

  }
}
