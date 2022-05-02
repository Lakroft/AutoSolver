package com.lakroft.autosolver.solvers;

import com.lakroft.autosolver.RulesMaster;
import com.lakroft.autosolver.Solver;
import com.lakroft.autosolver.State;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class RunnableBasedSolver extends Solver {
    private static final int DEFAULT_THREADS_NUM = 5;
    private final int threadsNum;


    public RunnableBasedSolver(RulesMaster rulesMaster) {
        this(rulesMaster, DEFAULT_THREADS_NUM);
    }

    public RunnableBasedSolver(RulesMaster rulesMaster, int threadsNum) {
        super(rulesMaster);
        this.threadsNum = threadsNum;
    }

    @Override
    public List<State> solve() {
        BlockingQueue<LinkedList<State>> mainQueue = new LinkedBlockingQueue<>();
        Set<State> everSeenStates = ConcurrentHashMap.newKeySet();

        State startState = rulesMaster.getStartState();
        everSeenStates.add(startState);
        mainQueue.add(new LinkedList<>(Arrays.asList(startState)));

        InnerSolver solver = new InnerSolver(mainQueue, everSeenStates);
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < threadsNum; i++) {
            Thread thread = new Thread(solver);
            thread.start();
            threads.add(thread);
        }

        solver.waitForResult();
        threads.forEach(Thread::interrupt);

        return solver.getResult();
    }

    private class InnerSolver implements Runnable {
        private final BlockingQueue<LinkedList<State>> mainQueue;
        private final Set<State> everSeenStates;
        AtomicReference<List<State>> result = new AtomicReference<>();
        volatile boolean isTaskSolved = false;

        public InnerSolver(BlockingQueue<LinkedList<State>> mainQueue, Set<State> everSeenStates) {
            this.mainQueue = mainQueue;
            this.everSeenStates = everSeenStates;
        }

        @Override
        public void run() {
            try {
                while (!isTaskSolved) {
                    LinkedList<State> currentChain = mainQueue.take();
                    State lastState = currentChain.size() > 1 ? currentChain.getLast() : currentChain.getFirst();

                    List<State> nextStates = rulesMaster.availableMoves(lastState).stream().filter(p -> !everSeenStates.contains(p)).collect(Collectors.toList());
                    everSeenStates.addAll(nextStates);

                    for (State state : nextStates) {
                        if (rulesMaster.isSolved(state)) {
                            synchronized (this) {
                                if (!isTaskSolved) {
                                    isTaskSolved = true;
                                    currentChain.add(state);
                                    result.set(currentChain);
                                    notifyAll();
                                }
                            }
                        }

                        LinkedList<State> newChain = new LinkedList<>(currentChain);
                        newChain.add(state);
                        mainQueue.add(newChain);
                    }
                }
                System.out.println("finished due to solving");
            } catch (InterruptedException e) {
                System.out.println("finished due to interruption");
                Thread.currentThread().interrupt();
            }
        }

        public List<State> getResult() {
            synchronized (this) {
                if (isTaskSolved) {
                    return result.get();
                }
            }
            return null;
        }


        public InnerSolver waitForResult() {
            try {
                synchronized (this) {
                    while (!isTaskSolved) {
                        wait(500);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return this;
        }
    }
}
