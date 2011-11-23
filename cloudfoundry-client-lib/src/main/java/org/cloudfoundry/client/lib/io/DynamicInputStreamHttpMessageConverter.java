
package org.cloudfoundry.client.lib.io;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.FileCopyUtils;

/**
 * Implementation of {@link HttpMessageConverter} that can write {@link DynamicInputStream}s. The {@code Content-Type}
 * of written resources is {@code application/octet-stream}.
 * 
 * @author Phillip Webb
 */
public class DynamicInputStreamHttpMessageConverter implements HttpMessageConverter<DynamicInputStream> {

    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return DynamicInputStream.class.isAssignableFrom(clazz);
    }

    public List<MediaType> getSupportedMediaTypes() {
        return Collections.singletonList(MediaType.ALL);
    }

    public DynamicInputStream read(Class<? extends DynamicInputStream> clazz, HttpInputMessage inputMessage) throws IOException,
        HttpMessageNotReadableException {
        throw new UnsupportedOperationException();
    }

    public void write(DynamicInputStream t, MediaType contentType, HttpOutputMessage outputMessage) throws IOException,
        HttpMessageNotWritableException {
        HttpHeaders headers = outputMessage.getHeaders();
        if (contentType == null || contentType.isWildcardType() || contentType.isWildcardSubtype()) {
            contentType = MediaType.APPLICATION_OCTET_STREAM;

        }
        if (contentType != null) {
            headers.setContentType(contentType);
        }
        FileCopyUtils.copy(t, outputMessage.getBody());
        outputMessage.getBody().flush();
    }

}
