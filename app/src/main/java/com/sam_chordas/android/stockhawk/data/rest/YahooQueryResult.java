package com.sam_chordas.android.stockhawk.data.rest;

import java.util.ArrayList;
import java.util.List;

public class YahooQueryResult {

    private Query query;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public static class Query {

        private int count;
        private String created;
        private String lang;
        private Results results;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public Results getResults() {
            return results;
        }

        public void setResults(Results results) {
            this.results = results;
        }

    }

    public static class Results {

        private List<Quote> quote = new ArrayList<>();

        public List<Quote> getQuote() {
            return quote;
        }

        public void setQuote(List<Quote> quote) {
            this.quote = quote;
        }
    }
}
