package org.cloudfoundry.client.lib.archive;


public interface UploadableArchive extends Iterable<UploadableArchiveEntry> {

    String getFilename();

    
}
