
package org.cloudfoundry.client.lib;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
public class CloudResource {

    @JsonProperty("fn")
    private String filename;

    private long size;

    private String sha1;

    protected CloudResource() {
    }

    public CloudResource(String filename, long size, String sha1) {
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
