package com.lakroft.autosolver;

import java.util.List;

public abstract class RulesMaster {
    public abstract boolean isSolved(State state);

    public abstract State getStartState();

    public abstract List<State> availableMoves(State state);
}
