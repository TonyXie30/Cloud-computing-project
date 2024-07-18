package com.example.operator.pojo;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("example.com")
@Version("v1")
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
