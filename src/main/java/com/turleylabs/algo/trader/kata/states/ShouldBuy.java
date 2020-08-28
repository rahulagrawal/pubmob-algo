package com.turleylabs.algo.trader.kata.states;

import com.turleylabs.algo.trader.kata.Averages;
import com.turleylabs.algo.trader.kata.RefactorMeAlgorithm;
import com.turleylabs.algo.trader.kata.framework.Slice;

import java.util.ArrayList;
import java.util.OptionalDouble;
import java.util.stream.Stream;

public class ShouldBuy {
    private ArrayList<Double> day = new ArrayList<Double>(0);
    // new functionality test stub.
    // Goal:  initiate a buy when the VIX is greater than the threshold currently in the algorithm,
    // as long as it (the VIX) closes two days in a row below half of it's most recent high

    boolean shouldBuy(Slice data, String symbol, Averages averages, double lastVixClose) {
        boolean result =
                averages.arePricesRisingNearTerm(data, symbol)
                && !(averages.isPriceCloseToPeak(data, symbol))
                && vixRule(lastVixClose, RefactorMeAlgorithm.ENTRY_THRESHOLD)
                && averages.isPriceNearShortTermAverage(data, symbol);

        day.add(0, lastVixClose);
        return result;
    }

    public boolean vixRule(double lastVixClose, double entryThreshold) {
        return lastVixClose < entryThreshold || twoDaysInARowBelowHalfOfTheHigh();
    }

    private boolean twoDaysInARowBelowHalfOfTheHigh() {
        if ( day.size() < 2) {
            return false;
        }
        Stream<Double> stream = day.stream();
        OptionalDouble optionalDouble = stream.mapToDouble(n -> n.doubleValue()).max();
        Double halfHigh = optionalDouble.getAsDouble() / 2;
        Double oneDayAgo = day.get(0);
        Double twoDaysAgo = day.get(1);
        return  oneDayAgo < halfHigh && twoDaysAgo < halfHigh;
    }

}
