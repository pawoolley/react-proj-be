package com.pawoolley.api.dto;

import java.time.Instant;
import java.util.Collection;
import lombok.Data;

@Data
public class List {
    private String id;
    private String name;
    private String description;
    private int listItemsCount;
    private Collection<ListItem> listItems;
    private Instant createdAt;
    private Instant updatedAt;
}
