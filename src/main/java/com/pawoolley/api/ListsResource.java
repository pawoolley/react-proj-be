package com.pawoolley.api;

import com.pawoolley.api.dto.List;
import com.pawoolley.api.dto.ListDTOConverter;
import com.pawoolley.service.ListsService;
import java.util.Collection;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("lists")
public class ListsResource {

    @Inject
    ListsService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<List> getLists() {
        return ListDTOConverter.toDTO(service.getLists());
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List getList(@PathParam("id") String id) {
        com.pawoolley.model.List list = service.getList(id);
        if (list == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return ListDTOConverter.toDTO(list);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createList(List list) {
        if (list == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        com.pawoolley.model.List ret = service.createList(ListDTOConverter.fromDTO(list));
        return ret.getId();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateList(@PathParam("id") String id, List list) {
        if (id == null || list == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        service.updateList(id, ListDTOConverter.fromDTO(list));
    }

    @DELETE
    @Path("{id}")
    public void deleteList(@PathParam("id") String id) {
        List deleted = ListDTOConverter.toDTO(service.deleteList(id));
        if (deleted == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }
}
