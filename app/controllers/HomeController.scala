package controllers

import javax.inject._

import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

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

  def getForm = Action { implicit request =>
    Ok(views.html.form())
  }

  def findAirports = Action { implicit request =>
    val singleForm = Form(
      single(
        "input" -> nonEmptyText
      )
    )
    singleForm.bindFromRequest.fold(
      _ => {
        Redirect(routes.HomeController.getForm())
      },
      input => {
        Redirect(routes.HomeController.getAirportsAndRunawaysByCountryCodeOrName(input))
      }
    )
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

  def getAirportsByCountryCode(code : String) = Action.async { implicit request =>
    airportRepository.findByCountryCode(code).map(list => Ok(list.toString))
  }

  def getAirportsByCountryCodeOrName(input : String) = Action.async { implicit request =>
    countryRepository.findByNameOrCode(input).map {
      case Some(country) => Redirect(routes.HomeController.getAirportsByCountryCode(country.code))
      case None => Redirect(routes.HomeController.getForm())
    }
  }

  def getAirportsAndRunawaysByCountryCodeOrName(input : String) = Action.async { implicit request =>
    countryRepository.findByNameOrCode(input).flatMap {
      case Some(country) => airportRepository.findByCountryCode(country.code).flatMap {
        airports => runwayRepository.findByAirportsRef(airports.map(_.id)).map {
          runways => {
            val output = airports.map(airport => (airport, runways.filter(runway => runway.airportRef == airport.id)))
            Ok(views.html.airports(output))
          }
        }
      }
      case None => Future.successful(Redirect(routes.HomeController.getForm()))
    }

  }

  def getHighestNumberOfAirports = Action.async {
    airportRepository.findAll().map(airports => {
      val completeList = airports.groupBy(airport => airport.isoCountry).mapValues(_.length).toList
      val listSorted = completeList.sortWith((x,y) => x._2 > y._2)
      Ok(views.html.showTopCountries(listSorted.take(10), listSorted.takeRight(10)))
    })
  }


}
