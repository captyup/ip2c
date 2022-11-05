package io.captyup.service

import com.maxmind.geoip2.DatabaseReader
import com.maxmind.geoip2.record.Country
import io.quarkus.scheduler.Scheduled
import java.io.InputStream
import java.net.InetAddress
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.zip.GZIPInputStream
import javax.annotation.PostConstruct
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class Ip2cService {
    private lateinit var reader: DatabaseReader
    @PostConstruct
    fun initReader() {
        this.reader = this.refreshReader()
    }
    fun getCountryByIp(ipAddress: InetAddress): Country {
        return reader.country(ipAddress).country
    }
    private fun refreshReader():DatabaseReader {
        return DatabaseReader.Builder(this.getMmdbInputStream()).build()
    }
    private fun getMmdbInputStream(): InputStream {
        val yearMonthStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))
        return GZIPInputStream(
            URL("https://download.db-ip.com/free/dbip-country-lite-${yearMonthStr}.mmdb.gz")
                .openStream()
        )
    }
    @Scheduled(cron = "0 0 0 1 * ?")
    fun monthlyRefresh() {
        val thisMonthReader = this.refreshReader()
        this.reader = thisMonthReader
    }
}
