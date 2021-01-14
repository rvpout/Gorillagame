package mesh;

import java.io.Serializable;
import java.util.*;

import fi.utu.tech.distributed.gorilla.logic.Player;

public class MeshPackage implements Serializable{
    public final long sender;
    public final long receiver;
    public final long token;
    public final Serializable data;
    public final long SerialUID = 12345L;
    //public final Player player;

    public MeshPackage(long sender, long receiver, Serializable data) {
        this.sender=sender;
        this.receiver=receiver;
        Random rand = new Random();
        this.token=rand.nextLong();
        this.data=data;
    }
}