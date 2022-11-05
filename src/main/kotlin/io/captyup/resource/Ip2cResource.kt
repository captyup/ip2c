package io.captyup.resource

import io.captyup.service.Ip2cService
import io.vertx.core.http.HttpServerRequest
import java.net.InetAddress
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Context

@Path("/ip2c")
class Ip2cResource {
    @Inject
    lateinit var ip2cService: Ip2cService
    @GET
    fun getCountry(@Context request: HttpServerRequest): String {
        val ipAddress = InetAddress.getByName(request.remoteAddress().hostAddress())
        return ip2cService.getCountryByIp(ipAddress).name
    }
}
