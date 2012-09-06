package org.crashtest.http.client.response;


import com.google.common.base.Objects;

public class AvailableParameter {
        public String getName() {
            return name;
        }

    public AvailableParameter() {
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("name",name).toString();
    }
}
