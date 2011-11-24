
package org.cloudfoundry.client.lib;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

/**
 * Tests for {@link CloudResources}.
 * 
 * @author Phillip Webb
 */
public class CloudResourcesTest {

    private static final String SHA = "677E1B9BCA206D6534054348511BF41129744839";

    private static final String JSON = "[{\"fn\":\"index.html\",\"size\":93,\"sha1\":\"" + SHA + "\"}]";

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldSerialize() throws Exception {
        CloudResource r = new CloudResource("index.html", 93L, SHA);
        CloudResources o = new CloudResources(Collections.singleton(r));
        String s = mapper.writeValueAsString(o);
        assertThat(s, is(equalTo(JSON)));
    }

    @Test
    public void shouldDeserialize() throws JsonParseException, JsonMappingException, IOException {
        CloudResources o = mapper.readValue(JSON, CloudResources.class);
        List<CloudResource> l = o.asList();
        CloudResource r = l.get(0);
        assertThat(l.size(), is(1));
        assertThat(r.getFilename(), is("index.html"));
        assertThat(r.getSha1(), is(SHA));
        assertThat(r.getSize(), is(93L));
    }
}
