package ir.mhdr.bmi.lib;


public enum HistoryType {
    Weight(1),
    Height(2);

    private final int history;

    private HistoryType(int value) {
        this.history = value;
    }
}
