package com.pawoolley.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.pawoolley.model.List;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class InMemoryStore {

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();
    private static final TypeReference LIST_TYPE_REF = new TypeReference<List>() {
    };
    private final Map<String, List> lists = new LinkedHashMap<>();

    public Collection<List> getLists() {
        return lists.values().stream().map(this::clone).collect(Collectors.toUnmodifiableList());
    }

    public List getList(String id) {
        List list = lists.get(id);
        return list != null ? clone(list) : null;
    }

    public List upsertList(String id, List list) {
        list = clone(list);
        list.setId(id);
        lists.put(id, list);
        return list;
    }

    public List deleteList(String id) {
        return lists.remove(id);
    }

    private List clone(List list) {
        TokenBuffer tb = new TokenBuffer(MAPPER.getFactory().getCodec(), false);
        try {
            MAPPER.writeValue(tb, list);
            return (List) MAPPER.readValue(tb.asParser(), LIST_TYPE_REF);
        } catch (IOException e) {
            throw new RuntimeException("Failed to clone", e);
        }
    }
}
