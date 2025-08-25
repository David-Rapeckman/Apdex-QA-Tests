// David rapeckman gomes de Souza - RM 556607
package org.example;

import java.util.List;

public final class Apdex {

    public static final int TOTAL_AMOSTRAS = 556_607;

    public enum Rating { EXCELLENT, GOOD, FAIR, POOR, UNACCEPTABLE }

    private final double thresholdMillis;

    public Apdex(double thresholdMillis) {
        if (thresholdMillis <= 0) throw new IllegalArgumentException("Threshold deve ser > 0");
        this.thresholdMillis = thresholdMillis;
    }

    public double getThresholdMillis() { return thresholdMillis; }

    /** APDEX = (S + T/2) / TOTAL_AMOSTRAS. Valida S+T+F == RM. */
    public static double computeScoreByCounts(int satisfied, int tolerating, int frustrated) {
        validateCounts(satisfied, tolerating, frustrated);
        return (satisfied + (tolerating / 2.0)) / TOTAL_AMOSTRAS;
    }

    /** Faixas padrão. */
    public static Rating classify(double score) {
        if (score < 0 || score > 1) throw new IllegalArgumentException("Score fora de [0,1]");
        if (score >= 0.94) return Rating.EXCELLENT;
        if (score >= 0.85) return Rating.GOOD;
        if (score >= 0.70) return Rating.FAIR;
        if (score >= 0.50) return Rating.POOR;
        return Rating.UNACCEPTABLE;
    }

    /** Conta S/T/F a partir de durações. T usa 4x threshold. */
    public Counts countsFromDurations(List<Double> durationsMillis) {
        if (durationsMillis == null) throw new IllegalArgumentException("Lista nula");
        int s = 0, t = 0, f = 0;
        for (Double d : durationsMillis) {
            if (d == null || d < 0) throw new IllegalArgumentException("Duração inválida");
            if (d <= thresholdMillis) s++;
            else if (d <= 4 * thresholdMillis) t++;
            else f++;
        }
        return new Counts(s, t, f);
    }

    public record Counts(int satisfied, int tolerating, int frustrated) {}

    private static void validateCounts(int s, int t, int f) {
        if (s < 0 || t < 0 || f < 0) throw new IllegalArgumentException("Contagens negativas");
        int total = s + t + f;
        if (total != TOTAL_AMOSTRAS)
            throw new IllegalArgumentException("S+T+F deve ser exatamente " + TOTAL_AMOSTRAS + " (RM)");
    }
}
