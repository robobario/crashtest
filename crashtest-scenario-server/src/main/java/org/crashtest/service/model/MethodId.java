package org.crashtest.service.model;

import com.google.common.base.Objects;
import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
public class MethodId {
    private long id;

    private MethodId(long id) {
        this.id = id;
    }

    public static MethodId of(long id){
        return new MethodId(id);
    }

    public long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("id",id).toString();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof MethodId){
            final MethodId other = (MethodId) obj;
            return Objects.equal(id, other.id);
        } else{
            return false;
        }
    }
}
