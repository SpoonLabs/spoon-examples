package ow2con.publicapi.subpack;

import ow2con.privateapi.PrivateType;
import ow2con.privateapi.subpack.AnotherType;

public class TypePublic {

    private PrivateType getPrivateType() {
        return new PrivateType();
    }

    public AnotherType getAnotherType() {
        return new AnotherType();
    }
}
