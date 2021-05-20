package com.pawoolley.model;

import java.util.Collection;
import java.util.UUID;
import lombok.Data;

@Data
public class List {
    private String id;
    private String name;
    private String description;
    private Collection<ListItem> listItems;
}
