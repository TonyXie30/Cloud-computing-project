package com.example.pojo;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;

public class PgsqlReader extends CustomResource<PgsqlReaderSpec, PgsqlReaderStatus> implements Namespaced {

}

class PgsqlReaderStatus {
    private String result;

    // Getters and setters

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
