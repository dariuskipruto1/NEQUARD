package com.nequard.anomaly;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnomalyDetectorTest {
    @Test void flagsThreeSigmaAnomaly() {
        var result = AnomalyDetector.zScore(130, 100, 10);
        assertTrue(result.anomaly());
        assertEquals(3.0, result.zScore(), 0.0001);
    }

    @Test void doesNotFlagNormalValue() {
        var result = AnomalyDetector.zScore(120, 100, 10);
        assertFalse(result.anomaly());
        assertEquals(2.0, result.zScore(), 0.0001);
    }

    @Test void handlesZeroDeviation() {
        var result = AnomalyDetector.zScore(100, 100, 0);
        assertFalse(result.anomaly());
        assertEquals(0.0, result.zScore());
    }
}
