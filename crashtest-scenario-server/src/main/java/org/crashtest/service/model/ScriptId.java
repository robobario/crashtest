package org.crashtest.service.model;

import com.google.common.base.Objects;

public class ScriptId {
    private long id;

    private ScriptId(long id) {
        this.id = id;
    }

    public static ScriptId of(long id){
        return new ScriptId(id);
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
        if(obj instanceof ScriptId){
            final ScriptId other = (ScriptId) obj;
            return Objects.equal(id, other.id);
        } else{
            return false;
        }
    }
}
