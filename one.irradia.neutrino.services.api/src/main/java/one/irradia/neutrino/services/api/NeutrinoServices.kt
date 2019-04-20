package one.irradia.neutrino.services.api

import one.irradia.servicedirectory.api.ServiceDirectoryType
import one.irradia.servicedirectory.vanilla.ServiceDirectory

/**
 * Access to application services.
 */

object NeutrinoServices {

  /**
   * The application service directory.
   */

  val services: ServiceDirectoryType =
    ServiceDirectory.create()

}
