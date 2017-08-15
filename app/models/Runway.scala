package models

import javax.inject.Inject

import anorm.SqlParser._
import anorm._
import play.api.db.DBApi

import scala.concurrent.Future

case class Runway(id: Long,
                  airportRef: Long,
                  airportIdent: String,
                  lengthFt: Option[Int],
                  widthFt: Option[Int],
                  surface: Option[String],
                  lighted: Int,
                  closed: Int,
                  leIdent: Option[String],
                  leLatitudeDeg: Option[Double],
                  leLongitudeDeg: Option[Double],
                  leElevationFt: Option[Int],
                  leHeadingDegT: Option[Double],
                  leDisplacedThresholdFt: Option[Int],
                  heIdent: Option[String],
                  heLatitudeDeg: Option[Double],
                  heLongitudeDeg: Option[Double],
                  heElevationFt: Option[Int],
                  heHeadingDegT: Option[String],
                  heDisplacedThresholdFt: Option[Int]) {

}


@javax.inject.Singleton
class RunwayRepository @Inject()(dbapi: DBApi)(implicit ec: DatabaseExecutionContext) {
  private val db = dbapi.database("default")

  private val simple = {
    get[Long]("runway.id") ~
      get[Long]("runway.airport_ref") ~
      get[String]("runway.airport_ident") ~
      get[Option[Int]]("runway.length_ft") ~
      get[Option[Int]]("runway.width_ft") ~
      get[Option[String]]("runway.surface") ~
      get[Int]("runway.lighted") ~
      get[Int]("runway.closed") ~
      get[Option[String]]("runway.le_ident") ~
      get[Option[Double]]("runway.le_latitude_deg") ~
      get[Option[Double]]("runway.le_longitude_deg") ~
      get[Option[Int]]("runway.le_elevation_ft") ~
      get[Option[Double]]("runway.le_heading_degT") ~
      get[Option[Int]]("runway.le_displaced_threshold_ft") ~
      get[Option[String]]("runway.he_ident") ~
      get[Option[Double]]("runway.he_latitude_deg") ~
      get[Option[Double]]("runway.he_longitude_deg") ~
      get[Option[Int]]("runway.he_elevation_ft") ~
      get[Option[String]]("runway.he_heading_degT") ~
      get[Option[Int]]("runway.he_displaced_threshold_ft") map {
      case id ~
        airportRef ~
        airportIdent ~
        lengthFt ~
        widthFt ~
        surface ~
        lighted ~
        closed ~
        leIdent ~
        leLatitudeDeg ~
        leLongitudeDeg ~
        leElevationFt ~
        leHeadingDegT ~
        leDisplacedThresholdFt ~
        heIdent ~
        heLatitudeDeg ~
        heLongitudeDeg ~
        heElevationFt ~
        heHeadingDegT ~
        heDisplacedThresholdFt =>
        Runway(id,
          airportRef,
          airportIdent,
          lengthFt,
          widthFt,
          surface,
          lighted,
          closed,
          leIdent,
          leLatitudeDeg,
          leLongitudeDeg,
          leElevationFt,
          leHeadingDegT,
          leDisplacedThresholdFt,
          heIdent,
          heLatitudeDeg,
          heLongitudeDeg,
          heElevationFt,
          heHeadingDegT,
          heDisplacedThresholdFt)
    }
  }

  def findById(id: Long): Future[Option[Runway]] = Future {
    db.withConnection { implicit connection =>
      SQL("select * from runway where id = {id}").on('id -> id).as(simple.singleOpt)
    }
  }

  def findAll(): Future[List[Runway]] = Future {
    db.withConnection { implicit connection =>
      SQL("select * from runway").as(simple *)
    }
  }

}