package com.rnkrsoft.bopomofo4j;

import com.rnkrsoft.embedded.boot.EmbeddedApplicationLoader;
import com.rnkrsoft.embedded.boot.annotation.EmbeddedBootApplication;

/**
 * Created by woate on 2019/9/20.
 */
@EmbeddedBootApplication
public class Bopomofo_dot_jsTest {

    public static void main(String[] args) {
        EmbeddedApplicationLoader.runWith(Bopomofo_dot_jsTest.class, args);
    }
}
