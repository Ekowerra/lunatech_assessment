package models

case class Airport(id: Long,
                   ident: String,
                   airportType: String,
                   name: String,
                   latitudeDeg: Double,
                   longitudeDeg: Double,
                   elevationFt: Int,
                   continent: String,
                   isoCountry: String,
                   isoRegion: String,
                   municipality: String,
                   scheduledService: String,
                   gpsCode: String,
                   iataCode: String,
                   localCode: String,
                   homeLink: String,
                   wikipediaLink: String,
                   keywords: String) {

}

object Airport
