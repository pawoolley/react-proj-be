package com.pawoolley.model;

import java.time.Instant;
import java.util.Collection;
import lombok.Data;

@Data
public class List {
    private String id;
    private String name;
    private String description;
    private Collection<ListItem> listItems;
    private Instant createdAt;
    private Instant updatedAt;
}
