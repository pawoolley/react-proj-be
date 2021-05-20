package com.pawoolley.api;

import com.pawoolley.model.List;
import com.pawoolley.model.ListItem;
import com.pawoolley.persistence.InMemoryStore;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import java.util.Arrays;
import java.util.UUID;
import javax.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@QuarkusTest
@TestHTTPEndpoint(ListsResource.class)
class ListsResourceTest {

    @Inject
    InMemoryStore store;

    @BeforeEach
    void beforeEach() {
        store.getLists().forEach(list -> store.deleteList(list.getId()));
    }

    @Test
    void testGetLists() {

        List list1 = createList(
            "list1",
            "list1 description",
            createListItem("list1Item1 description", true),
            createListItem("list1Item2 description", false));

        List list2 = createList(
            "list2",
            "list2 description",
            createListItem("list2Item1 description", true),
            createListItem("list2Item2 description", false),
            createListItem("list2Item3 description", false));

        store.upsertList(list1.getId(), list1);
        store.upsertList(list2.getId(), list2);

        given()
            .when()
            .get()
            .then()
//            .log().body()
            .statusCode(is(200))
            .body("$", hasSize(2))
            .body("[0].name", is("list1"))
            .body("[0].description", is("list1 description"))
            .body("[0].listItemsCount", is(2))
            .body("[0].listItems", is(nullValue()))
            .body("[1].name", is("list2"))
            .body("[1].description", is("list2 description"))
            .body("[1].listItemsCount", is(3))
            .body("[1].listItems", is(nullValue()));
    }

    @Test
    void testGetLists_empty() {

        given()
            .when()
            .get()
            .then()
//            .log().body()
            .statusCode(is(200))
            .body("$", hasSize(0));
    }

    @Test
    void testGetList() {

        List list1 = createList(
            "list1",
            "list1 description",
            createListItem("list1Item1 description", true),
            createListItem("list1Item2 description", false));

        List list2 = createList(
            "list2",
            "list2 description",
            createListItem("list2Item1 description", true),
            createListItem("list2Item2 description", false),
            createListItem("list2Item3 description", false));

        store.upsertList(list1.getId(), list1);
        store.upsertList(list2.getId(), list2);

        given()
            .pathParam("id", list1.getId())
            .when()
            .get("/{id}")
            .then()
//            .log().body()
            .statusCode(is(200))
            .body("name", is("list1"))
            .body("description", is("list1 description"))
            .body("listItemsCount", is(2))
            .body("listItems.$", hasSize(2))
            .body("listItems[0].description", is("list1Item1 description"))
            .body("listItems[0].ticked", is(true))
            .body("listItems[1].description", is("list1Item2 description"))
            .body("listItems[1].ticked", is(false));
    }

    @Test
    void testGetList_notFound() {
        given()
            .pathParam("id", "does-not-exist")
            .when()
            .get("/{id}")
            .then()
            .log().body()
            .statusCode(is(404));
    }

    @Test
    void testCreateList() {

        // Verify the store is empty
        assertThat(store.getLists().size(), is(0));

        // Create a new list without an id.
        List list1 = createList(
            "list1",
            "list1 description",
            createListItem("list1Item1 description", true),
            createListItem("list1Item2 description", false));
        list1.setId(null);

        given()
            .contentType(ContentType.JSON)
            .body(list1)
            .when()
            .post()
            .then()
            .log().body()
            .statusCode(is(200))
            .contentType(ContentType.TEXT)
            .body(is(notNullValue()));

        // Verify the store is populated
        assertThat(store.getLists().size(), is(1));
    }

    @Test
    void testCreateList_badRequest() {

        given()
            .contentType(ContentType.JSON)
            .when()
            .post()
            .then()
            .statusCode(is(400));
    }

    @Test
    void testUpdateList() {

        // Store a new list.
        List list1 = createList(
            "list1",
            "list1 description",
            createListItem("list1Item1 description", true),
            createListItem("list1Item2 description", false));
        store.upsertList(list1.getId(), list1);

        // Update the list
        list1.setDescription("new description");
        given()
            .pathParam("id", list1.getId())
            .contentType(ContentType.JSON)
            .body(list1)
            .when()
            .put("/{id}")
            .then()
            .statusCode(is(204));

        // Verify the store is updated
        assertThat(store.getList(list1.getId()).getDescription(), is("new description"));
    }

    @Test
    void testUpdateList_badRequest() {

        given()
            .pathParam("id", "someId")
            .contentType(ContentType.JSON)
            .when()
            .put("/{id}")
            .then()
            .statusCode(is(400));
    }

    @Test
    void testDeleteList() {

        // Store a new list.
        List list1 = createList(
            "list1",
            "list1 description",
            createListItem("list1Item1 description", true),
            createListItem("list1Item2 description", false));
        store.upsertList(list1.getId(), list1);

        // Delete it
        given()
            .pathParam("id", list1.getId())
            .when()
            .delete("/{id}")
            .then()
            .statusCode(is(204));

        // Verify the store is empty
        assertThat(store.getLists().size(), is(0));
    }

    @Test
    void testDeleteList_notFound() {

        given()
            .pathParam("id", "doesnt-exist")
            .when()
            .delete("/{id}")
            .then()
            .statusCode(is(404));
    }

    private List createList(String name, String description, ListItem... listItems) {
        List list = new List();
        list.setId(UUID.randomUUID().toString());
        list.setName(name);
        list.setDescription(description);
        list.setListItems(Arrays.asList(listItems));
        return list;
    }

    private ListItem createListItem(String description, boolean isTicked) {
        ListItem listItem = new ListItem();
        listItem.setDescription(description);
        listItem.setTicked(isTicked);
        return listItem;
    }
}