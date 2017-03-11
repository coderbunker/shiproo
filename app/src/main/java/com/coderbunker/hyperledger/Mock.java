package com.coderbunker.hyperledger;


public class Mock {

    private final String json;

    public Mock() {
        json = "{\n" +
                "\tchains: [\n" +
                "\t  {\n" +
                "\t\t\"time\":\"1d 4h\",\n" +
                "\t\t\"cost\": 245$,\n" +
                "\t\t\"checkpoints\":[ \"Shanghai\", \"Hongkong port\", \"San Francisco airport\" ]\n" +
                "\t  },\n" +
                "\t  {\n" +
                "\t\t\"time\":\"2h\",\n" +
                "\t\t\"cost\": 345$,\n" +
                "\t\t\"checkpoints\":[ \"Shanghai\", \"Hongkong airport\", \"San Francisco airport\" ]\n" +
                "\t  },\n" +
                "\t  {\n" +
                "\t\t\"time\":\"1d\",\n" +
                "\t\t\"cost\": 645$,\n" +
                "\t\t\"checkpoints\":[ \"Shanghai\", \"San Francisco airport\" ]\n" +
                "\t  }\n" +
                "\t]\n" +
                "}";
    }

    public String getJson() {
        return json;
    }
}
