package models

import javax.inject.Inject

import anorm.SqlParser._
import anorm._
import play.api.db.DBApi

import scala.concurrent.Future

case class Airport(id: Long,
                   ident: String,
                   airportType: String,
                   name: String,
                   latitudeDeg: Double,
                   longitudeDeg: Double,
                   elevationFt: Option[Int],
                   continent: String,
                   isoCountry: String,
                   isoRegion: String,
                   municipality: Option[String],
                   scheduledService: String,
                   gpsCode: Option[String],
                   iataCode: Option[String],
                   localCode: Option[String],
                   homeLink: Option[String],
                   wikipediaLink: Option[String],
                   keywords: Option[String]) {


}

@javax.inject.Singleton
class AirportRepository @Inject()(dbapi: DBApi)(implicit ec: DatabaseExecutionContext) {
  private val db = dbapi.database("default")

  private val simple = {
    get[Long]("airport.id") ~
      get[String]("airport.ident") ~
      get[String]("airport.type") ~
      get[String]("airport.name") ~
      get[Double]("airport.latitude_deg") ~
      get[Double]("airport.longitude_deg") ~
      get[Option[Int]]("airport.elevation_ft") ~
      get[String]("airport.continent") ~
      get[String]("airport.iso_country") ~
      get[String]("airport.iso_region") ~
      get[Option[String]]("airport.municipality") ~
      get[String]("airport.scheduled_service") ~
      get[Option[String]]("airport.gps_code") ~
      get[Option[String]]("airport.iata_code") ~
      get[Option[String]]("airport.local_code") ~
      get[Option[String]]("airport.home_link") ~
      get[Option[String]]("airport.wikipedia_link") ~
      get[Option[String]]("airport.keywords") map {
      case id ~ ident ~ airportType ~ name ~ latitudeDeg ~ longitudeDeg ~ elevationFt ~ continent ~ isoCountry ~ isoRegion ~
        municipality ~ scheduledService ~ gpsCode ~ iataCode ~ localCode ~ homeLink ~ wikipediaLink ~ keywords =>
        Airport(id, ident, airportType, name, latitudeDeg, longitudeDeg, elevationFt, continent, isoCountry, isoRegion,
          municipality, scheduledService, gpsCode, iataCode, localCode, homeLink, wikipediaLink, keywords)
    }
  }

  def findById(id: Long): Future[Option[Airport]] = Future {
    db.withConnection { implicit connection =>
      SQL("select * from airport where id = {id}").on('id -> id).as(simple.singleOpt)
    }
  }

  def findAll() : Future[List[Airport]] = Future {
    db.withConnection { implicit connection =>
      SQL("select * from airport").as(simple *)
    }
  }

  def findByCountryName(name : String) : Future[List[Airport]] = Future {
    db.withConnection { implicit connection =>
      SQL("select * from airport where name = {name}").on('name -> name).as(simple *)
    }
  }

  def findByCountryCode(code : String) : Future[List[Airport]] = Future {
    db.withConnection { implicit connection =>
      SQL("select * from airport where iso_country = {code}").on('code -> code).as(simple *)
    }
  }
}
