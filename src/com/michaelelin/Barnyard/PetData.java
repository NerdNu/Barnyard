package com.michaelelin.Barnyard;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name = "pets")
public class PetData {

    @Id
    private int id;
    @Version
    private Timestamp version;

    @NotNull
    private String owner;
    private String uuid;
    
    private int chunkX;
    private int chunkZ;
    private String world;
    private int type;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public Timestamp getVersion() {
        return this.version;
    }
    
    public void setVersion(Timestamp version) {
        this.version = version;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    public String getUuid() {
        return this.uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    public int getChunkX() {
        return this.chunkX;
    }
    
    public void setChunkX(int chunkX) {
        this.chunkX = chunkX;
    }
    
    public int getChunkZ() {
        return this.chunkZ;
    }
    
    public void setChunkZ(int chunkZ) {
        this.chunkZ = chunkZ;
    }
    
    public String getWorld() {
        return this.world;
    }
    
    public void setWorld(String world) {
        this.world = world;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void setType(int type) {
        this.type = type;
    }

}
