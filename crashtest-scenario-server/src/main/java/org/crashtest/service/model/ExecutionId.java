package org.crashtest.service.model;

import com.google.common.base.Objects;
import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
public class ExecutionId {
    private long id;

    private ExecutionId(long id) {
        this.id = id;
    }

    public static ExecutionId of(long id){
        return new ExecutionId(id);
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
        if(obj instanceof ExecutionId){
            final ExecutionId other = (ExecutionId) obj;
            return Objects.equal(id, other.id);
        } else{
            return false;
        }
    }
}
