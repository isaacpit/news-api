package com.isaacpit.news.api

import groovy.util.logging.Slf4j
import spock.lang.Specification

@Slf4j
class NewsApiApplicationSpec extends Specification {

    def "Application loads"() {
        log.info("loaded...")
        expect:
        true
    }
}
