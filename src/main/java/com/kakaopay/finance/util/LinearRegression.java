package com.kakaopay.finance.util;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class LinearRegression {
    private final double intercept;
    private final double slope;
    private final double r2;
    private final double sVar0;
    private final double sVar1;

    public LinearRegression(List<Integer> x, List<Integer> y){
        if(x.size() != y.size()){
            throw new IllegalArgumentException("Array lengths are not equal");
        }
        int n = x.size();

        //first pass
        double sumX = x.stream().mapToInt(Integer::intValue).sum();
        double sumY = y.stream().mapToInt(Integer::intValue).sum();
        double xBar = sumX / n;
        double yBar = sumY / n;

        //second pass: compute summary statistics
        double x2Bar = x.stream().mapToDouble(value -> (value - xBar) * (value - xBar)).sum();
        double y2Bar = y.stream().mapToDouble(value -> (value - xBar) * (value - xBar)).sum();
        double xyBar = IntStream.range(0, n).mapToDouble(i -> (x.get(i)-xBar)*(y.get(i)-yBar)).sum();

        slope = xyBar / x2Bar;
        intercept = yBar - slope * xBar;

        //more statistical analysis
        List<Double> fits = x.stream().mapToDouble(value -> slope * value + intercept).boxed().collect(Collectors.toList());
        double rss = IntStream.range(0, n).mapToDouble(i->fits.get(i) - y.get(i)).sum();
        double ssr = fits.stream().mapToDouble(value -> (value-yBar)*(value-yBar)).sum();

        int degreeOfFreedom = n-2;
        r2 = ssr/y2Bar;
        double sVar = rss / degreeOfFreedom;
        sVar1 = sVar / x2Bar;
        sVar0 = sVar / n + xBar*xBar* sVar1;
    }

    public double interceptStdErr(){
        return Math.sqrt(sVar0);
    }

    public double predictStdErr(){
        return Math.sqrt(sVar1);
    }

    public double predict(int x){
        return slope*x + intercept;
    }
}
