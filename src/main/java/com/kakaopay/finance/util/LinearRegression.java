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

    /*
    단순 선형 회귀
    x : 입력 변수, 예측 변수, 독립 변수
    y : 응답 변수, 결과 변수
     */
    public LinearRegression(List<Integer> x, List<Integer> y){
        if(x.size() != y.size()){
            throw new IllegalArgumentException("Array lengths are not equal");
        }
        int n = x.size();

        //xBar, yBar 계산
        double sumX = x.stream().mapToInt(Integer::intValue).sum();
        double sumY = y.stream().mapToInt(Integer::intValue).sum();
        double xBar = sumX / n;
        double yBar = sumY / n;

        double x2Bar = x.stream().mapToDouble(value -> (value - xBar) * (value - xBar)).sum();
        double y2Bar = y.stream().mapToDouble(value -> (value - xBar) * (value - xBar)).sum();
        double xyBar = IntStream.range(0, n).mapToDouble(i -> (x.get(i)-xBar)*(y.get(i)-yBar)).sum();

        //회귀 계수
        slope = xyBar / x2Bar;
        intercept = yBar - slope * xBar;

        //결과 분석
        List<Double> fits = x.stream().mapToDouble(value -> slope * value + intercept).boxed().collect(Collectors.toList());
        //Sum of squares error
        double sse = IntStream.range(0, n).mapToDouble(i->fits.get(i) - y.get(i)).sum();
        //Sum of squares regression
        double ssr = fits.stream().mapToDouble(value -> (value-yBar)*(value-yBar)).sum();

        // 결정 계수: 총 변화량 중 모델이 잡아낼 수 있는 변화량의 비율
        r2 = ssr/y2Bar;
        // 관측 오차, 평균 제곱 오차(mean squared error)
        double sVar = sse / n-2;
        // 회귀 계수의 오차
        sVar1 = sVar / x2Bar;
        sVar0 = sVar / n + xBar*xBar* sVar1;
    }

    public double interceptStdErr(){
        return Math.sqrt(sVar0);
    }

    public double predictStdErr(){
        return Math.sqrt(sVar1);
    }

    public Double predict(int x){
        return slope*x + intercept;
    }
}
