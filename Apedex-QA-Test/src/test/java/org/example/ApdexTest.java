// David rapeckman gomes de Souza - RM 556607
package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApdexTest {

    static Apdex apdex;

    @BeforeAll
    static void setup() {
        apdex = new Apdex(300.0);
        assertEquals(556_607, Apdex.TOTAL_AMOSTRAS);
    }

    @Test
    void excellent() {
        int s = Apdex.TOTAL_AMOSTRAS, t = 0, f = 0;
        double score = Apdex.computeScoreByCounts(s, t, f);
        assertEquals(1.0, score, 1e-12);
        assertEquals(Apdex.Rating.EXCELLENT, Apdex.classify(score));
    }

    @Test
    void good() {
        int s = (int) Math.floor(Apdex.TOTAL_AMOSTRAS * 0.90);
        int t = 0;
        int f = Apdex.TOTAL_AMOSTRAS - s;
        double score = Apdex.computeScoreByCounts(s, t, f);
        assertTrue(score >= 0.85 && score < 0.94);
        assertEquals(Apdex.Rating.GOOD, Apdex.classify(score));
    }

    @Test
    void fair() {
        int s = (int) Math.floor(Apdex.TOTAL_AMOSTRAS * 0.75);
        int t = 0;
        int f = Apdex.TOTAL_AMOSTRAS - s;
        double score = Apdex.computeScoreByCounts(s, t, f);
        assertTrue(score >= 0.70 && score < 0.85);
        assertEquals(Apdex.Rating.FAIR, Apdex.classify(score));
    }

    @Test
    void poor() {
        int s = 0;
        int t = Apdex.TOTAL_AMOSTRAS;
        int f = 0;
        double score = Apdex.computeScoreByCounts(s, t, f);
        assertEquals(0.5, score, 1e-12);
        assertEquals(Apdex.Rating.POOR, Apdex.classify(score));
    }

    @Test
    void unacceptable() {
        int s = (int) Math.floor(Apdex.TOTAL_AMOSTRAS * 0.40);
        int t = 0;
        int f = Apdex.TOTAL_AMOSTRAS - s;
        double score = Apdex.computeScoreByCounts(s, t, f);
        assertTrue(score < 0.50);
        assertEquals(Apdex.Rating.UNACCEPTABLE, Apdex.classify(score));
    }

    @Test
    void somaDeveBaterRM() {
        assertThrows(IllegalArgumentException.class,
                () -> Apdex.computeScoreByCounts(1, 0, 0));
    }

    @Test
    void contagensNegativas() {
        assertThrows(IllegalArgumentException.class,
                () -> Apdex.computeScoreByCounts(-1, 0, Apdex.TOTAL_AMOSTRAS + 1));
    }

    @Test
    void utilitarioDeDuracoes() {
        // 100 e 200 => S; 1100 e 1200 => T; 1301 => F
        var counts = apdex.countsFromDurations(java.util.List.of(100.0, 200.0, 1100.0, 1200.0, 1301.0));
        assertEquals(2, counts.satisfied());
        assertEquals(2, counts.tolerating());
        assertEquals(1, counts.frustrated());
    }
}
