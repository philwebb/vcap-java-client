
package org.cloudfoundry.client.lib;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.springframework.stereotype.Service;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
public class CloudFoundryResource {

    @JsonProperty("fn")
    private String filename;

    private long size;

    private String sha1;

    protected CloudFoundryResource() {
    }

    public CloudFoundryResource(String filename, long size, String sha1) {
        super();
        this.filename = filename;
        this.size = size;
        this.sha1 = sha1;
    }

    public String getFilename() {
        return filename;
    }

    public long getSize() {
        return size;
    }

    public String getSha1() {
        return sha1;
    }
}
