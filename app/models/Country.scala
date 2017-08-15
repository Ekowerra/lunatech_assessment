package models

case class Country(id: Long,
                   code: String,
                   name: String,
                   continent: String,
                   wikipediaLink: String,
                   keywords: String) {

}

object Country
