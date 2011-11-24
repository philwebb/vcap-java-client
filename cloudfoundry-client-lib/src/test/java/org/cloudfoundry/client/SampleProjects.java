
package org.cloudfoundry.client;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

public class SampleProjects {

    private static final String TEST_APP_DIR = "src/test/resources/apps";

    public static File springTravel() throws IOException {
        File file = new File(TEST_APP_DIR + "/travelapp/swf-booking-mvc.war");
        assertTrue("Expected test app at " + file.getCanonicalPath(), file.exists());
        return file;
    }

}
