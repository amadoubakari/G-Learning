package com.flys.dao.service;

import org.androidannotations.rest.spring.annotations.Accept;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.MediaType;
import org.androidannotations.rest.spring.api.RestClientRootUrl;
import org.androidannotations.rest.spring.api.RestClientSupport;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Rest(converters = {MappingJackson2HttpMessageConverter.class})
public interface WebClient extends RestClientRootUrl, RestClientSupport {

    @Get(value = "{url}")
    @Accept(MediaType.APPLICATION_OCTET_STREAM)
    byte[] downloadUrl(@Path("url") String url);

    @Get(value = "{url}?type={type}")
    @Accept(MediaType.APPLICATION_OCTET_STREAM)
    byte[] downloadFacebookImage(@Path("url") String url, @Path("type") String type);

    @Get(value = "{baseUrl}?asid={facebookAppId}&height=640&width=640&ext={ext}&hash={params}")
    @Accept(MediaType.APPLICATION_OCTET_STREAM)
    byte[] downloadFacebookProfileImage(@Path("baseUrl") String baseUrl, @Path("ext") String ext, @Path("params") String params, @Path("facebookAppId") String facebookAppId);
}
