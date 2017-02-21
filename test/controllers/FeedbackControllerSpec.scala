//package controllers
//
//package controllers.feedback
//
//import play.api.http.Status
//import play.api.i18n.MessagesApi
//import play.api.test.FakeRequest
//import play.api.test.Helpers._
//import testHelpers.PAYERegSpec
//
//class FeedbackControllerSpec extends ControllerSpec {
//
//  val fakeRequest = FakeRequest("GET", "/")
//
//  class Setup {
//    val controller = new FeedbackCtrl {
//      implicit val messagesApi: MessagesApi = fakeApplication.injector.instanceOf[MessagesApi]
//    }
//  }
//
//  "GET /start" should {
//    "return 200" in new Setup {
//      val result = controller.show(fakeRequest)
//      status(result) shouldBe Status.OK
//      contentType(result) shouldBe Some("text/html")
//      charset(result) shouldBe Some("utf-8")
//    }
//  }
//
//}
