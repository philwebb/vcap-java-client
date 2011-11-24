
package org.cloudfoundry.client.lib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.cloudfoundry.client.lib.archive.ApplicationArchive;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;

@JsonSerialize(using = CloudResources.Serializer.class)
@JsonDeserialize(using = CloudResources.Deserializer.class)
public class CloudResources {

    private static final String HEX_CHARS = "0123456789ABCDEF";

    private List<CloudResource> resources;

    public CloudResources(Collection<? extends CloudResource> resources) {
        this.resources = new ArrayList<CloudResource>(resources);
    }

    public CloudResources(Iterator<? extends CloudResource> resources) {
        this.resources = new ArrayList<CloudResource>();
        while (resources.hasNext()) {
            this.resources.add(resources.next());
        }
    }

    public CloudResources(ApplicationArchive archive) throws IOException {
        this.resources = new ArrayList<CloudResource>();
        for (ApplicationArchive.Entry entry : archive.getEntries()) {
            if (!entry.isDirectory()) {
                String name = entry.getName();
                long size = entry.getSize();
                String sha1 = bytesToHex(entry.getSha1Digest());
                CloudResource resource = new CloudResource(name, size, sha1);
                resources.add(resource);
            }
        }
    }

    public Set<String> getAllFilenames() {
        Set<String> filenames = new LinkedHashSet<String>();
        for (CloudResource resource : resources) {
            filenames.add(resource.getFilename());
        }
        return filenames;
    }

    public List<CloudResource> asList() {
        return Collections.unmodifiableList(resources);
    }

    private static String bytesToHex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * bytes.length);
        for (final byte b : bytes) {
            hex.append(HEX_CHARS.charAt((b & 0xF0) >> 4)).append(HEX_CHARS.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

    public static class Deserializer extends JsonDeserializer<CloudResources> {
        @SuppressWarnings("unchecked")
        @Override
        public CloudResources deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            TypeReference<List<CloudResource>> ref = new TypeReference<List<CloudResource>>() {
            };
            return new CloudResources((Collection<? extends CloudResource>) jp.readValueAs(ref));
        }
    }

    public static class Serializer extends JsonSerializer<CloudResources> {
        @Override
        public void serialize(CloudResources value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeObject(value.asList());
        }

    }

}
