package com.example.akifcakar.healthtracker;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass
public class PersonInfo extends RealmObject {

    private int upper;
    private int lower;
    private int sugar;
    private int pulse;
    private int weight;

    public int getUpper() {
        return upper;
    }

    public void setUpper(int upper) {
        this.upper = upper;
    }

    public int getLower() {
        return lower;
    }

    public void setLower(int lower) {
        this.lower = lower;
    }

    public int getSugar() {
        return sugar;
    }

    public void setSugar(int sugar) {
        this.sugar = sugar;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "PersonInfo{" +
                "upper=" + upper +
                ", lower=" + lower +
                ", sugar=" + sugar +
                ", pulse=" + pulse +
                ", weight=" + weight +
                '}';
    }
}
