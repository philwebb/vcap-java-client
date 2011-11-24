
package org.cloudfoundry.client.lib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import org.cloudfoundry.client.lib.archive.UploadableArchive;
import org.cloudfoundry.client.lib.archive.UploadableArchiveEntry;

public class CloudFoundryResources extends ArrayList<CloudFoundryResource> {

    public static CloudFoundryResources forArchive(UploadableArchive archive) throws IOException {
        CloudFoundryResources resources = new CloudFoundryResources();
        for (UploadableArchiveEntry entry : archive) {
            if (!entry.isDirectory()) {
                resources.add(new CloudFoundryResource(entry.getName(), entry.getSize(), bytesToHex(entry.getSha1Digest())));
            }
        }
        return resources;
    }

    public Set<String> getAllFilenames() {
        Set<String> filenames = new LinkedHashSet<String>();
        for (CloudFoundryResource resource : this) {
            filenames.add(resource.getFilename());
        }
        return filenames;
    }

    private static final String HEX_CHARS = "0123456789ABCDEF";

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

}
