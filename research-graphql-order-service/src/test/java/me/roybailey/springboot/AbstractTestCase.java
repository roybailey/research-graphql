package me.roybailey.springboot;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.JUnitBDDSoftAssertions;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;


@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
abstract public class AbstractTestCase {

    @Rule
    public TestName name = new TestName();

    @Rule
    public final JUnitBDDSoftAssertions softly = new JUnitBDDSoftAssertions();

    @Before
    public void banner() {
        log.info("------------------------------------------------------------");
        log.info(this.getClass().getSimpleName()+"."+name.getMethodName()+"()");
        log.info("------------------------------------------------------------");
    }
}

