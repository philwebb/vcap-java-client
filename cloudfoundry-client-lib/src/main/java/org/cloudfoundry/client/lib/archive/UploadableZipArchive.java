
package org.cloudfoundry.client.lib.archive;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.cloudfoundry.client.lib.util.FilteredIterator;
import org.springframework.util.FileCopyUtils;

public class UploadableZipArchive implements UploadableArchive {

    private ZipFile zipFile;

    private List<UploadableArchiveEntry> entries;

    public UploadableZipArchive(ZipFile zipFile) {
        this.zipFile = zipFile;
        List<UploadableArchiveEntry> entires = new ArrayList<UploadableArchiveEntry>();
        Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
        while (zipEntries.hasMoreElements()) {
            entires.add(new EntryAdapter(zipEntries.nextElement()));
        }
        this.entries = entires;
    }

    public Iterator<UploadableArchiveEntry> iterator() {
        return entries.iterator();

        //
        // final Iterator<ZipEntry> entries = new FilterDirectories(
        // CollectionUtils.toIterator(zipFile.entries()));
        //
        // return new Iterator<UploadableArchiveEntry>() {
        //
        // public boolean hasNext() {
        // return entries.hasNext();
        // }
        //
        // public UploadableArchiveEntry next() {
        // return new EntryAdapter(entries.next());
        // }
        //
        // public void remove() {
        // throw new UnsupportedOperationException();
        // }
        // };
    }

    private static class FilterDirectories extends FilteredIterator<ZipEntry> {

        public FilterDirectories(Iterator<? extends ZipEntry> sourceIterator) {
            super(sourceIterator);
        }

        @Override
        protected boolean isElementFiltered(ZipEntry element) {
            return false;
            // return element.isDirectory();
        }

    }

    private class EntryAdapter extends AbstractUploadableArchiveEntry {

        private ZipEntry entry;

        private byte[] bytes;

        public EntryAdapter(ZipEntry entry) {
            this.entry = entry;
        }

        public boolean isDirectory() {
            return entry.isDirectory();
        }

        public String getName() {
            return entry.getName();
        }

        public long getSize() {
            return entry.getSize();
        }

        public InputStream getInputStream() throws IOException {
            if (isDirectory()) {
                return null;
            }
            if (bytes == null) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                FileCopyUtils.copy(zipFile.getInputStream(entry), out);
                this.bytes = out.toByteArray();
            }
            return new ByteArrayInputStream(bytes);
        }

    }

}
