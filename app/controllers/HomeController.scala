package controllers

import javax.inject._

import models.{AirportRepository, CountryRepository, RunwayRepository}
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, ws : WSClient)
                              (airportRepository: AirportRepository,
                               countryRepository: CountryRepository,
                              runwayRepository: RunwayRepository) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def test = Action.async {
    val request = ws.url("https://api-adresse.data.gouv.fr/search/?q=8 bd du port")
    val response = request.get()
    response.map(x => Ok(x.body))
  }

  def getAirports = Action.async { implicit request =>
    airportRepository.findAll().map(list => Ok(list.toString))
  }

  def getCountries = Action.async { implicit request =>
    countryRepository.findAll().map(list => Ok(list.toString))
  }

  def getRunways = Action.async { implicit request =>
    runwayRepository.findAll().map(list => Ok(list.toString))
  }

}
