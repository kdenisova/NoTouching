package com.notouching.model;

public class Virus extends GameEntity {
    private VirusType type;
    private int damage;

    public Virus(VirusType type, int y, int x) {
        super(EntityType.VIRUS, y, x);
        this.type = type;
        setDamage();
    }

    public VirusType getType() {
        return type;
    }

    public void setType(VirusType type) {
        this.type = type;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage() {
        switch (type) {
            case COVID19:
                this.damage = 30;
                break;
            case INFLUENZA:
                this.damage = 15;
                break;
            case ROTAVIRUS:
                this.damage = 10;
                break;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        return ((Virus)obj).getType() == this.getType();
    }
}