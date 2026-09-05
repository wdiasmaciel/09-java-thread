import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Tarefa {
    private final ReentrantLock mutex = new ReentrantLock();

    public void processarSemTimeout(String nome) {
        System.out.println(nome + " tentando pegar o Mutex...");

        try {
            // Tenta pegar o cadeado. Se estiver ocupado, desiste imediatamente.
            if (mutex.tryLock()) {
                try {
                    // --- REGIÃO CRÍTICA ---
                    System.out.println(nome + " conseguiu trancar o Mutex e entrou na seção crítica!");
                    Thread.sleep(4000); // Simula uma tarefa demorada (4 segundos).
                } finally {
                    mutex.unlock(); // Sempre libera no finally.
                    System.out.println(nome + " liberou o Mutex.");
                }
            } else {
                System.out.println(nome + " CANCELOU a operação!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
