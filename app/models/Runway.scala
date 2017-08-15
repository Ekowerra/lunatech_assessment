package models

case class Runway(id: Long,
                  airportRef: Long,
                  airportIdent: String,
                  lengthFt: Int,
                  widthFt: Int,
                  surface: String,
                  lighted: Int,
                  closed: Int,
                  leIdent: String,
                  leLatitudeDeg: Double,
                  leLongitudeDeg: Double,
                  leElevationFt: Int,
                  leHeadingDegT: Double,
                  leDisplacedThresholdFt: Int,
                  heIdent: String,
                  heLatitudeDeg: Double,
                  heLongitudeDeg: Double,
                  heElevation_ft: Int,
                  heHeadingDegT: String,
                  heDisplacedThresholdFt: Int) {

}


object Runway