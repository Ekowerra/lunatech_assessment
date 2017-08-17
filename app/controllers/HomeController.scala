package controllers

import javax.inject._

import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class HomeController @Inject()(cc: ControllerComponents)
                              (airportRepository: AirportRepository,
                               countryRepository: CountryRepository,
                               runwayRepository: RunwayRepository) extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.index("Index"))
  }

  def getForm = Action { implicit request =>
    Ok(views.html.form())
  }

  def getReport = Action {
    Ok(views.html.report())
  }

  def findAirports = Action { implicit request =>
    val singleForm = Form(
      single(
        "input" -> nonEmptyText
      )
    )
    singleForm.bindFromRequest.fold(
      _ => {
        BadRequest(views.html.form())
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

  def getAirportsByCountryCode(code: String) = Action.async { implicit request =>
    airportRepository.findByCountryCode(code).map(list => Ok(list.toString))
  }

  def getAirportsByCountryCodeOrName(input: String) = Action.async { implicit request =>
    countryRepository.findByNameOrCode(input).map {
      case Some(country) => Redirect(routes.HomeController.getAirportsByCountryCode(country.code))
      case None => Redirect(routes.HomeController.getForm())
    }
  }

  def getAirportsAndRunawaysByCountryCodeOrName(input: String) = Action.async { implicit request =>
    countryRepository.findByNameOrCode(input).flatMap {
      case Some(country) => airportRepository.findByCountryCode(country.code).flatMap {
        airports =>
          runwayRepository.findByAirportsRef(airports.map(_.id)).map {
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
      val completeList = airports.groupBy(_.isoCountry).mapValues(_.length).toList
      val listSorted = completeList.sortWith((x, y) => x._2 > y._2)
      Ok(views.html.showTopCountries(listSorted.take(10), listSorted.reverse.takeWhile(_._2 <= 1)))
    })
  }


  def sortRunways = Action.async {
    val futRunways = runwayRepository.findAll().map(_.groupBy(_.airportRef))
    val futAirports = airportRepository.findAll().map(_.groupBy(_.isoCountry).mapValues(_.map(_.id)))
    futAirports.zipWith(futRunways)((airports, runways) => airports.toList.map {
      case (key, list) => (key, list.foldLeft(List[String]())((acc, elem) => acc ++ runways.getOrElse(elem, List()).foldLeft(List[String]()) {
        (acc2, elem2) =>
          elem2.surface match {
            case Some(s) if !(acc ++ acc2).contains(s) => acc2 :+ s
            case _ => acc2
          }
      }))
    }).map(list => Ok(views.html.showRunways(list)))
  }


}
