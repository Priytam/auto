package com.auto.framework.mock;

import com.auto.framework.AbstractTestComponent;
import com.auto.framework.TestComponentData;
import com.auto.framework.env.TestEnvironment;
import com.auto.framework.operation.Operation;
import com.auto.framework.operation.OperationFactory;
import com.auto.framework.reporter.TestReporter;
import com.auto.framework.rules.mock.MockConfig;
import com.auto.framework.rules.mock.RequestType;
import com.auto.framework.utils.FileUtil;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.matching.RequestPattern;

import java.io.File;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class MockServer extends AbstractTestComponent {

    private final MockConfig config;
    private WireMockServer wireMockServer;

    public MockServer(TestComponentData data, MockConfig config) {
        super(data);
        this.config = config;
    }

    @Override
    protected Operation getStartOperation() {
        return OperationFactory.consumableOp(() -> {
            wireMockServer = new WireMockServer(getPort());
            wireMockServer.start();
            WireMock.configureFor(getHost(), wireMockServer.port());
        }, "MockServerStart");
    }

    @Override
    protected Operation getStopOperation() {
        return OperationFactory.consumableOp(() -> wireMockServer.shutdownServer(), "MockServerStop");
    }

    @Override
    public boolean isRunning() {
        return Objects.nonNull(wireMockServer) && wireMockServer.isRunning();
    }

    @Override
    public void prepare() {
        if (Objects.nonNull(config)) {
            MappingBuilder mappingBuilder;
            if (RequestType.POST.name().equals(config.getType().name())) {
                String req = getContentFromFile(config.getRequestPath());
                mappingBuilder = WireMock.post(config.getUrl()).withRequestBody(equalToJson(req));
            } else {
                mappingBuilder = WireMock.get(config.getUrl());
            }
            WireMock.stubFor(mappingBuilder.willReturn(getResponseDefinitionBuilder()));
        } else {
            TestReporter.FATAL("Can't run Mock server without stub use annotation MockRequestResponse");
        }
    }

    @Override
    public void clean(boolean bForce) {
        wireMockServer.resetAll();
    }

    private ResponseDefinitionBuilder getResponseDefinitionBuilder() {
        String res = getContentFromFile(config.getResponsePath());
        ResponseDefinitionBuilder responseDefinitionBuilder = new ResponseDefinitionBuilder();
        responseDefinitionBuilder.withBody(res)
                .withFixedDelay(config.getDelay() * 1000);
        return responseDefinitionBuilder;
    }

    public int callCount() {
        RequestPattern pattern = new RequestPattern(
                WireMock.urlEqualTo(config.getUrl()),
                RequestMethod.fromString(config.getType().name()),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        return wireMockServer.countRequestsMatching(pattern).getCount();
    }


    private String getContentFromFile(String resourcePath) {
        String separator = resourcePath.startsWith("/") ? "" : "/";
        String filePath = TestEnvironment.getResourcePath() + separator + resourcePath;
        return FileUtil.getContents(new File(filePath));
    }
}
