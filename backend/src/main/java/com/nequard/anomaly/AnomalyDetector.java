package com.nequard.anomaly;
public final class AnomalyDetector{private AnomalyDetector(){}public static Result zScore(double value,double mean,double stddev){if(stddev<=0)return new Result(false,0);double z=(value-mean)/stddev;return new Result(Math.abs(z)>=3,z);}public record Result(boolean anomaly,double zScore){}}
