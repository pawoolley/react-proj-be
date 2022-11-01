/*
 * Copyright 2022 CloudBees, Inc.
 * All rights reserved.
 */

package com.pawoolley;

import com.pawoolley.model.List;
import com.pawoolley.model.ListItem;
import com.pawoolley.service.ListsService;
import io.quarkus.runtime.StartupEvent;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class PrePopulator {

    @Inject
    ListsService listsService;

    void onStart(@Observes StartupEvent startupEvent) {
        log.info("Prepopulating");
        listsService.createList(createList(1));
        listsService.createList(createList(2));
    }

    private List createList(int i) {
        final List list = new List();
        list.setCreatedAt(Instant.now());
        list.setDescription("Prepopulated list " + i);
        list.setName("List " + i);
        final Collection<ListItem> listItems = Arrays.asList(createListItem(i, 1, true), createListItem(i, 2, false));
        list.setListItems(listItems);
        return list;
    }

    private ListItem createListItem(final int listId, final int listItemId, final boolean ticked) {
        final ListItem listItem = new ListItem();
        listItem.setDescription("List item " + listId + ":" + listItemId);
        listItem.setTicked(ticked);
        return listItem;
    }
}
