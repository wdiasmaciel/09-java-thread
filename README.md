# 09-java-thread


# tryLock()

- O método tryLock() evita esperas infinitas.

- Quando usamos o tradicional mutex.lock(), a thread fica "bloqueada" na fila até conseguir a chave:
  - O que pode causar travamentos se outra thread esquecer de liberá-la ou demorar demais.
  
- O tryLock() tenta pegar o cadeado. 
  - Se estiver livre, ele tranca na hora e retorna true. 
  - Se estiver ocupado, ele não espera: retorna false imediatamente, permitindo que a sua thread faça outra coisa (desista, tente mais tarde ou registre um log).
  - Existe também uma variação que espera por um tempo limite (timeout) antes de desistir. 