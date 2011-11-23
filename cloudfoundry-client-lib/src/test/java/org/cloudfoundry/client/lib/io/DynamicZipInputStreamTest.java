
package org.cloudfoundry.client.lib.io;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.cloudfoundry.client.lib.io.DynamicZipInputStream.Entry;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;

/**
 * Tests for {@link DynamicZipInputStream}.
 * 
 * @author Phillip Webb
 */
public class DynamicZipInputStreamTest {

    private static final SecureRandom RANDOM = new SecureRandom();

    @Test
    public void shouldCreateValidZipContent() throws Exception {

        byte[] f1 = newRandomBytes(10000);
        byte[] f2 = newRandomBytes(10000);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(bos);
        zipOutputStream.putNextEntry(new ZipEntry("/a/b/c"));
        zipOutputStream.write(f1);
        zipOutputStream.closeEntry();
        zipOutputStream.putNextEntry(new ZipEntry("/d/e/f"));
        zipOutputStream.write(f2);
        zipOutputStream.closeEntry();
        zipOutputStream.flush();
        zipOutputStream.close();
        byte[] expected = bos.toByteArray();

        List<DynamicZipInputStream.Entry> entries = new ArrayList<DynamicZipInputStream.Entry>();
        entries.add(newEntry("/a/b/c", f1));
        entries.add(newEntry("/d/e/f", f2));
        DynamicZipInputStream inputStream = new DynamicZipInputStream(entries);
        bos.reset();
        FileCopyUtils.copy(inputStream, bos);
        byte[] actual = bos.toByteArray();

        System.out.println(String.format("%x", new BigInteger(expected)));
        System.out.println(String.format("%x", new BigInteger(actual)));

        assertThat(actual, is(equalTo(expected)));
    }

    private Entry newEntry(final String name, final byte[] content) {
        return new Entry() {

            public String getName() {
                return name;
            }

            public InputStream getInputStream() {
                return new ByteArrayInputStream(content);
            }
        };
    }

    private byte[] newRandomBytes(int len) {
        byte[] bytes = new byte[len];
        RANDOM.nextBytes(bytes);
        return bytes;
    }

}
