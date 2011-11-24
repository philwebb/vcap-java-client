
package org.cloudfoundry.client.lib.archive;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

import org.cloudfoundry.client.lib.CloudResources;
import org.cloudfoundry.client.lib.io.DynamicZipInputStream;
import org.cloudfoundry.client.lib.io.DynamicZipInputStream.Entry;

//FIXME PW rename
public class ArchivePayload {

    private ApplicationArchive archive;

    private ArrayList<Entry> entriesToUpload;

    private int totalUncompressedSize;

    public ArchivePayload(ApplicationArchive archive, CloudResources knownRemoteResources) throws IOException {
        this.archive = archive;
        this.totalUncompressedSize = 0;
        Set<String> matches = knownRemoteResources.getAllFilenames();
        this.entriesToUpload = new ArrayList<DynamicZipInputStream.Entry>();
        for (ApplicationArchive.Entry entry : archive.getEntries()) {
            if (entry.isDirectory() || !matches.contains(entry.getName())) {
                entriesToUpload.add(new DynamicZipInputStreamEntryAdapter(entry));
                totalUncompressedSize += entry.getSize();
            }
        }
    }

    public ApplicationArchive getArchive() {
        return archive;
    }
    
    public InputStream getInputStream() {
        return new DynamicZipInputStream(entriesToUpload);
    }

    private static class DynamicZipInputStreamEntryAdapter implements DynamicZipInputStream.Entry {

        private ApplicationArchive.Entry entry;

        public DynamicZipInputStreamEntryAdapter(ApplicationArchive.Entry entry) {
            this.entry = entry;
        }

        public String getName() {
            return entry.getName();
        }

        public InputStream getInputStream() throws IOException {
            if(entry.isDirectory()) {
                return null;
            }
            return entry.getInputStream();
        }

    }

    public int getTotalUncompressedSize() {
        return totalUncompressedSize;
    }

}
