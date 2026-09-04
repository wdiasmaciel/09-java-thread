# 09-java-thread


# tryLock()

- O método tryLock() evita esperas infinitas.

- Quando usamos o tradicional mutex.lock(), a thread fica "bloqueada" na fila até conseguir a chave:
  - O que pode causar travamentos se outra thread esquecer de liberá-la ou demorar demais.
  
- O tryLock() tenta pegar o cadeado. 
  - Se estiver livre, ele tranca na hora e retorna true. 
  - Se estiver ocupado, ele não espera: retorna false imediatamente, permitindo que a sua thread faça outra coisa (desista, tente mais tarde ou registre um log).
  - Existe também uma variação que espera por um tempo limite (timeout) antes de desistir. 

  # Exercícios

  1. **Tentativa imediata**

    Crie uma classe `Tarefa` com um `ReentrantLock` e implemente um método que use `tryLock()` sem timeout. Inicie quatro threads para acessar a mesma seção crítica e exiba uma mensagem informando qual thread conseguiu o lock e qual desistiu imediatamente. Na seção crítica, cada thread deve incrementar um contador de uma quantidade informada como parâmetro e imprimir o valor do contador no console.

  2. **Timeout configurável**

      Crie uma classe `Tarefa` com um `ReentrantLock` e implemente um método que use `tryLock()` com timeout. O tempo máximo de espera deve ser informado como parâmetro. Inicie cinco threads para acessar a mesma seção crítica e exiba uma mensagem informando qual thread conseguiu o lock e qual desistiu. Teste o programa com diferentes valores de timeout e observe em quais situações a thread consegue entrar na seção crítica. Na seção crítica, cada thread deve incrementar um contador de uma quantidade informada como parâmetro e imprimir o valor do contador no console.

  3. **Liberação garantida**

    Faça a seção crítica lançar uma exceção propositalmente. Garanta, usando `try`/`finally`, que o lock seja liberado mesmo quando a exceção ocorrer. Depois, verifique se outra thread consegue obter o lock.

  4. **Fila de tarefas com tentativa**

    Simule uma fila de tarefas compartilhada por várias threads. Cada thread deve tentar obter o lock com `tryLock()`; quando não conseguir, deve registrar a tarefa como pendente para uma nova tentativa, sem ficar bloqueada indefinidamente.

  5. **Comparação entre as estratégias**

    Execute versões do programa usando `lock()`, `tryLock()` e `tryLock(tempo, unidade)`. Registre o tempo de execução, a quantidade de tarefas concluídas e a quantidade de desistências. Compare os resultados e explique quando cada estratégia é mais adequada.

  