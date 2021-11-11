package BankManager;

import java.util.LinkedList;

public class ContoCorrente {
    private final String name;
    private final String surname;
    private final LinkedList<Movimento> movimenti;

    public ContoCorrente(String name, String surname) {
        this.name = name;
        this.surname = surname;
        movimenti = new LinkedList<>();
    }

    public void addMovimento(Movimento m) {
        movimenti.add(m);
    }

    public LinkedList<Movimento> getMovimenti() {
        return movimenti;
    }

    @Override
    public String toString() {
        return "Conto corrente di " + name + " " + surname + "\n Numero Movimenti: " + movimenti.size();
    }
}
