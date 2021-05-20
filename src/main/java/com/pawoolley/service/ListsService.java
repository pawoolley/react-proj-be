package com.pawoolley.service;

import com.pawoolley.model.List;
import com.pawoolley.persistence.InMemoryStore;
import java.util.Collection;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ListsService {

    @Inject
    InMemoryStore store;

    public Collection<List> getLists() {
        return store.getLists();
    }

    public List getList(String id) {
        return store.getList(id);
    }

    public List createList(List list) {
        String id = UUID.randomUUID().toString();
        return store.upsertList(id, list);
    }

    public void updateList(String id, List list) {
        store.upsertList(id, list);
    }

    public List deleteList(String id) {
        return store.deleteList(id);
    }
}
