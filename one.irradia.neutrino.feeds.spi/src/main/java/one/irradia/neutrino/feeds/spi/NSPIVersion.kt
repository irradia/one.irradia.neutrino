package one.irradia.neutrino.feeds.spi

import java.util.regex.Pattern

data class NSPIVersion(
  val major: Int,
  val minor: Int,
  val patch: Int) {

  override fun toString(): String =
    "$major.$minor.$patch"

  companion object {

    private val pattern =
      Pattern.compile("([0-9]+)\\.([0-9]+)\\.([0-9]+)(.*)")

    private val DEFAULT =
      NSPIVersion(
        major = 0,
        minor = 0,
        patch = 0)

    fun parse(text: String): NSPIVersion =
      try {
        val matcher = pattern.matcher(text)
        if (matcher.matches()) {
          NSPIVersion(
            major = Integer.parseInt(matcher.group(1)),
            minor = Integer.parseInt(matcher.group(2)),
            patch = Integer.parseInt(matcher.group(3)))
        } else {
          DEFAULT
        }
      } catch (e: Exception) {
        DEFAULT
      }
  }
}