package com.pawoolley.api.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

public class ListDTOConverter {

    public static Collection<List> toDTO(Collection<com.pawoolley.model.List> lists) {
        if (CollectionUtils.isEmpty(lists)) {
            return Collections.emptyList();
        }
        return lists.stream().map(list -> toDTO(list, false)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static List toDTO(com.pawoolley.model.List list) {
        return toDTO(list, true);
    }

    public static com.pawoolley.model.List fromDTO(List listDTO) {
        if (listDTO == null) {
            return null;
        }
        com.pawoolley.model.List list = new com.pawoolley.model.List();
        list.setDescription(listDTO.getDescription());
        list.setId(listDTO.getId());
        list.setListItems(fromDTOs(listDTO.getListItems()));
        list.setName(listDTO.getName());
        return list;
    }

    private static List toDTO(com.pawoolley.model.List list, boolean isPopulated) {
        if (list == null) {
            return null;
        }
        List listDTO = new List();
        listDTO.setDescription(list.getDescription());
        listDTO.setId(list.getId());
        listDTO.setListItemsCount(CollectionUtils.isEmpty(list.getListItems()) ? 0 : list.getListItems().size());
        listDTO.setListItems(isPopulated ? toDTOs(list.getListItems()) : null);
        listDTO.setName(list.getName());
        return listDTO;
    }

    private static Collection<ListItem> toDTOs(Collection<com.pawoolley.model.ListItem> listItems) {
        if (CollectionUtils.isEmpty(listItems)) {
            return Collections.emptyList();
        }
        return listItems.stream().map(ListDTOConverter::toDTO).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static ListItem toDTO(com.pawoolley.model.ListItem listItem) {
        if (listItem == null) {
            return null;
        }
        ListItem listItemDTO = new ListItem();
        listItemDTO.setDescription(listItem.getDescription());
        listItemDTO.setTicked(listItem.isTicked());
        return listItemDTO;
    }

    private static Collection<com.pawoolley.model.ListItem> fromDTOs(final Collection<ListItem> listItemDTOs) {
        if (CollectionUtils.isEmpty(listItemDTOs)) {
            return Collections.emptyList();
        }
        return listItemDTOs.stream().map(ListDTOConverter::fromDTO).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static com.pawoolley.model.ListItem fromDTO(ListItem listItemDTO) {
        if (listItemDTO == null) {
            return null;
        }
        com.pawoolley.model.ListItem listItem = new com.pawoolley.model.ListItem();
        listItem.setDescription(listItemDTO.getDescription());
        listItem.setTicked(listItemDTO.isTicked());
        return listItem;
    }
}
