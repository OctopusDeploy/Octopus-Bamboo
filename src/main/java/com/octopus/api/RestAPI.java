package com.octopus.api;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;

import java.io.File;

/**
 * A small subset of the Octopus Delpoy REST API that this plugin will
 * use to interact with an Octopus Deploy server.
 */
public interface RestAPI {
    /**
     * https://octopus.com/docs/packaging-applications/package-repositories/pushing-packages-to-the-built-in-repository
     *
     * @param replace Replace an existing package
     * @param file    The file to be uploaded
     */
    @RequestLine("POST /packages/raw?replace={replace}")
    @Headers("Content-Type: multipart/form-data")
    Response packagesRaw(@Param("replace") Boolean replace,
                         @Param("file") File file);
}
