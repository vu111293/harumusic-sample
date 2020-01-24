/**/// DO NOT MODIFY THIS FILE MANUALLY
/**/// This class was automatically generated by "GeneratePatternClasses.java",
/**/// modify the "unique_patterns.json" and re-generate instead.

package org.schabi.newpipe.extractor.timeago.patterns;

import org.schabi.newpipe.extractor.timeago.PatternsHolder;

public class pt_PT extends PatternsHolder {
    private static final String WORD_SEPARATOR = " ";
    private static final String[]
            SECONDS  /**/ = {"segundo", "segundos"},
            MINUTES  /**/ = {"minuto", "minutos"},
            HOURS    /**/ = {"hora", "horas"},
            DAYS     /**/ = {"dia", "dias"},
            WEEKS    /**/ = {"semana", "semanas"},
            MONTHS   /**/ = {"meses", "mês"},
            YEARS    /**/ = {"ano", "anos"};

    private static final pt_PT INSTANCE = new pt_PT();

    public static pt_PT getInstance() {
        return INSTANCE;
    }

    private pt_PT() {
        super(WORD_SEPARATOR, SECONDS, MINUTES, HOURS, DAYS, WEEKS, MONTHS, YEARS);
    }
}