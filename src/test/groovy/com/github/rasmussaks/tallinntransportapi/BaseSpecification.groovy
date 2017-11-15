package com.github.rasmussaks.tallinntransportapi

import org.apache.log4j.BasicConfigurator
import spock.lang.Specification

class BaseSpecification extends Specification {
    def setup() {
        BasicConfigurator.configure()
    }
}
