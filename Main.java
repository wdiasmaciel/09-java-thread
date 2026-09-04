public class Main {
    public static void main(String[] args) {
        Tarefa tarefa = new Tarefa();

        // A Thread A vai entrar primeiro na seção crítica e vai demorar 4 segundos lá dentro:
        Thread t1 = new Thread(() -> tarefa.processarComTimeout("Thread_A"));
        
        // A Thread B tentará entrar, mas como só aceita esperar 2 segundos, vai desistir!
        Thread t2 = new Thread(() -> tarefa.processarComTimeout("Thread_B"));

        t1.start();
        
        // Pequena espera para garantir que a Thread A comece primeiro:
        try { Thread.sleep(100); } catch (Exception e) {e.printStackTrace();} 
        
        t2.start();
    }
}
