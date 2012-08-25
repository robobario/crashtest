package org.crashtest.service;

import com.google.common.base.Objects;

public class ParameterDescription {

    private String name;

    private String value;

    private ParameterDescription(String name, String value){
        this.name = name;
        this.value = value;
    }

    public static ParameterDescription create(String name, String value){
        return new ParameterDescription(name,value);
    }

    public String toString(){
        return Objects.toStringHelper(this).add("name",name).add("value",value).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name,value);
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof ParameterDescription){
            final ParameterDescription other = (ParameterDescription) obj;
            return Objects.equal(name, other.name)
                    && Objects.equal(value,other.value);
        } else{
            return false;
        }
    }

}
