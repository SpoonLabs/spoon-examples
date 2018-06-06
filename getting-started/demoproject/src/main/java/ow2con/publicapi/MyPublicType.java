package ow2con.publicapi;

import ow2con.privateapi.PrivateType;
import ow2con.publicapi.subpack.TypePublic;

public class MyPublicType {
    private PrivateType privateType;

    public MyPublicType() {
        this.privateType = new PrivateType();
    }

    public int getANumber() {
        return privateType.getANumber();
    }

    public PrivateType getPrivateType() {
        return privateType;
    }

    public TypePublic getTypePublic() {
        return new TypePublic();
    }
}
