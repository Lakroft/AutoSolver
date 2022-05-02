package com.lakroft.autosolver.rulesmasters;

import com.lakroft.autosolver.RulesMaster;
import com.lakroft.autosolver.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PentagrammaRulesMaster extends RulesMaster {
    private static final boolean[] START_STATE = {false, false, false, false, false};
    private static final boolean[] SOLVED_STATE = {true, true, true, true, true};


    public boolean isSolved(State state) {
        return Arrays.equals(state.getTorchLightStates(), SOLVED_STATE);
    }

    public State getStartState() {
        return new State(START_STATE, null);
    }

    public List<State> availableMoves(State state) {
        List<State> resultList = new ArrayList<>();
        boolean[] torchesStates = state.getTorchLightStates();
        for (int i = 0; i < torchesStates.length; i++) {
            if (!torchesStates[i]) {
                resultList.add(new State(getNewState(torchesStates, i), i));
            }
        }
        return resultList;
    }

    private boolean[] getNewState(boolean[] oldState, int move) {
        boolean[] newState = Arrays.copyOf(oldState, oldState.length);
        newState[move] = true;
        int leftOpposite = (move+2)%newState.length;
        int rightOpposite = (move+3)%newState.length;
        newState[leftOpposite] = !newState[leftOpposite];
        newState[rightOpposite] = !newState[rightOpposite];
        return newState;
    }
}
