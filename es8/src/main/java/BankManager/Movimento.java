package BankManager;

import java.util.Date;

public class Movimento {
    private final Causale causale;
    private final String date;

    private Movimento(Causale causale, String date) {
        this.causale = causale;
        this.date = date;
    }

    public static Movimento newMovimento(Causale c) {
        return new Movimento(c, new Date().toString());
    }

    public Causale getCasuale() {
        return causale;
    }

    @Override
    public String toString() {
        return "BankManager.Casuale: " + causale + ", svolto in data: " + date;
    }
}
