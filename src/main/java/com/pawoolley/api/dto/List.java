/*
 * Copyright 2022 CloudBees, Inc.
 * All rights reserved.
 */

package com.pawoolley.api.dto;

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
