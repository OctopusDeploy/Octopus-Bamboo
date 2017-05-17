package com.octopus.services.impl;

import com.atlassian.bamboo.task.TaskContext;
import com.octopus.api.RestAPI;
import com.octopus.constants.OctoTestConstants;
import com.octopus.services.FeignService;
import com.octopus.services.FileService;
import com.octopus.services.MockObjectService;
import feign.Response;
import feign.okhttp.OkHttpClient;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

/**
 * Test of the fiegn service
 */
public class FeignServiceImplTest {

    private static final FeignService FEIGN_SERVICE = new FeignServiceImpl(new OkHttpClient());
    private static final MockObjectService MOCK_OBJECT_SERVICE = new MockObjectServiceImpl();
    private static final FileService FILE_SERVICE = new FileServiceImpl();

    @Test
    @Ignore("Needs an instance of Octopus Deploy running")
    public void doFileUpload() {
        checkState(StringUtils.isNotBlank(System.getProperty(OctoTestConstants.API_KEY_SYSTEM_PROP)),
                "You need to run tests with an " + OctoTestConstants.API_KEY_SYSTEM_PROP
                        + " value set to the api key of a local instance of Octopus Deploy.");

        final TaskContext taskContext = MOCK_OBJECT_SERVICE.getTaskContext(new File("."));
        final RestAPI restAPI = FEIGN_SERVICE.createClient(taskContext, true);
        final List<File> uploadFile = FILE_SERVICE.getMatchingFile(
                new File("."),
                "**/resources/test.0.0.1.zip");

        Assert.assertTrue(uploadFile.size() == 1);

        final Response response = restAPI.packagesRaw(
                true,
                uploadFile.get(0));

        Assert.assertEquals(201, response.status());
    }
}
