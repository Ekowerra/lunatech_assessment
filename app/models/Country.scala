package models

import javax.inject.Inject

import anorm.SqlParser._
import anorm._
import play.api.db.DBApi

import scala.concurrent.Future

case class Country(id: Long,
                   code: String,
                   name: String,
                   continent: String,
                   wikipediaLink: String,
                   keywords: Option[String]) {

}

@javax.inject.Singleton
class CountryRepository @Inject()(dbapi: DBApi)(implicit ec: DatabaseExecutionContext) {
  private val db = dbapi.database("default")

  private val simple = {
    get[Long]("country.id") ~
      get[String]("country.code") ~
      get[String]("country.name") ~
      get[String]("country.continent") ~
      get[String]("country.wikipedia_link") ~
      get[Option[String]]("country.keywords") map {
      case id ~ code ~ name ~ continent ~ wikipediaLink ~ keywords =>
        Country(id, code, name, continent, wikipediaLink, keywords)
    }
  }

  def findById(id: Long): Future[Option[Country]] = Future {
    db.withConnection { implicit connection =>
      SQL("select * from country where id = {id}").on('id -> id).as(simple.singleOpt)
    }
  }

  def findAll(): Future[List[Country]] = Future {
    db.withConnection { implicit connection =>
      SQL("select * from country").as(simple *)
    }
  }

  def findByNameOrCode(input: String): Future[Option[Country]] = Future {
    db.withConnection { implicit connection =>
      SQL("select * from country where code = {input} or name = {input}").on('input -> input).as(simple.singleOpt)
    }
  }

}
