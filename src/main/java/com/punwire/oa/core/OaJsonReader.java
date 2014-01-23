package com.punwire.oa.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import sun.misc.IOUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by kanwal on 1/18/14.
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class OaJsonReader implements MessageBodyReader<ObjectNode> {
    protected static ObjectMapper mapper = new ObjectMapper();
    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return type == ObjectNode.class;
    }

    @Override
    public ObjectNode readFrom(Class<ObjectNode> objectNodeClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> map, InputStream inputStream) throws IOException, WebApplicationException {
        ObjectNode form = (ObjectNode)mapper.readTree(inputStream);
        return form;
    }
}
