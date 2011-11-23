
package org.cloudfoundry.client.lib;

import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;

public class CloudFoundryClientMockTest {

    private final String testAppDir = "src/test/resources/apps";

    private ClientHttpRequest httpClientRequest;

    private HttpHeaders header = new HttpHeaders();

    private ByteArrayOutputStream body = new ByteArrayOutputStream();

    @Before
    public void setup() throws IOException {
        httpClientRequest = mock(ClientHttpRequest.class);
        ClientHttpResponse response = mock(ClientHttpResponse.class);
        given(httpClientRequest.execute()).willReturn(response);
        given(httpClientRequest.getHeaders()).willReturn(header );
        given(httpClientRequest.getBody()).willReturn(body );
        given(response.getStatusCode()).willReturn(HttpStatus.OK);
    }

    private ClientHttpRequestFactory requestFactory = new ClientHttpRequestFactory() {

        public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
            return httpClientRequest;
        }
    };

    @Test
    public void should() throws Exception {
        File file = new File(testAppDir + "/travelapp/swf-booking-mvc.war");
        assertTrue("Expected test app at " + file.getCanonicalPath(), file.exists());

        CloudFoundryClient client = new CloudFoundryClient("http://api.cloudfoundry.com");
        client.getRestTemplate().setRequestFactory(requestFactory);
        client.uploadApplication("swf-booking-mvc.war", file);

    }

}
