package com.lakroft.autosolver;

import java.util.List;

public abstract class Solver {
    protected final RulesMaster rulesMaster;

    public Solver(RulesMaster rulesMaster) {
        this.rulesMaster = rulesMaster;
    }

    public abstract List<State> solve();
}
