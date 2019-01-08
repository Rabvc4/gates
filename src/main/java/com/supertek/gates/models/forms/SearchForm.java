package com.supertek.gates.models.forms;

import com.supertek.gates.models.enumerations.SearchFieldType;

import javax.validation.constraints.Size;

public class SearchForm {

    private SearchFieldType[] fields = SearchFieldType.values();

    private SearchFieldType searchField = SearchFieldType.NAME;

    @Size(min = 1, message = "Please type something to search for")
    @Size(max = 50, message = "Please shorten your search")
    private String keyword;

    public SearchFieldType getSearchField() {
        return searchField;
    }

    public void setSearchField(SearchFieldType searchField) {
        this.searchField = searchField;
    }

    public SearchFieldType[] getFields() {
        return fields;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
