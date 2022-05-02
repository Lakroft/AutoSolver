package com.lakroft.autosolver;

import java.util.Arrays;
import java.util.Objects;

public class State {

    private final boolean[] torchLightStates;
    private Integer move;

    public State(boolean[] torchLightStates, Integer move) {
        this.torchLightStates = torchLightStates;
        this.move = move;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        for (boolean torchState : torchLightStates) {
            builder.append(torchState ? "|" : "O").append(" ");
        }
        if (Objects.nonNull(move)) {
            builder.append(" move=").append(move);
        } else {
            builder.append(" start position");
        }
        return builder.append("}").toString();
//        return "State{" +
//                "torchLightStates=" + Arrays.toString(torchLightStates) +
//                ", move=" + move +
//                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Arrays.equals(torchLightStates, state.torchLightStates);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(torchLightStates);
    }

    public boolean[] getTorchLightStates() {
        return torchLightStates;
    }
}
