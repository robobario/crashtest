package org.crashtest.service.model;

import com.google.common.base.Objects;
import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
public class RemoteMethodId {


    private long id;

    public RemoteMethodId(long id) {
        this.id = id;
    }

    public static RemoteMethodId of(long id){
        return new RemoteMethodId(id);
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
        if(obj instanceof RemoteMethodId){
            final RemoteMethodId other = (RemoteMethodId) obj;
            return Objects.equal(id, other.id);
        } else{
            return false;
        }
    }
}
