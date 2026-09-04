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

## 1. Tentativa imediata

Crie uma classe `Tarefa` com um `ReentrantLock` e implemente um método que use `tryLock()` sem timeout. Inicie quatro threads para acessar a mesma seção crítica e exibir uma mensagem informando qual thread conseguiu o lock e qual desistiu imediatamente. Na seção crítica, cada tarefa deve incrementar um contador de uma quantidade informada como parâmetro e imprimir o valor do contador no console. O algoritmo deve ser executado quantas vezes o usuário desejar.

## 2. Timeout configurável

Crie uma classe `Tarefa` com um `ReentrantLock` e implemente um método que use `tryLock()` com timeout. O tempo máximo de espera deve ser informado como parâmetro. Inicie cinco threads para acessar a mesma seção crítica e exibir uma mensagem informando qual thread conseguiu o lock e qual desistiu. Teste o programa com diferentes valores de timeout e observe em quais situações a thread consegue entrar na seção crítica. Na seção crítica, cada thread deve simular uma ativade de tempo diferente no intervalo de 1 segundo a 4 segundos. O tempo de simulação deve ser gerado de forma aleatória. O algoritmo deve ser executado quantas vezes o usuário desejar.

## 3. Liberação garantida

Crie uma classe `Tarefa` com um `ReentrantLock` e implemente um método que use `tryLock()` com timeout. O tempo máximo de espera deve ser informado como parâmetro. Inicie três threads para acessar a mesma seção crítica e exibir uma mensagem informando qual thread conseguiu o lock e qual desistiu. Teste o programa com diferentes valores de timeout e observe em quais situações a thread consegue entrar na seção crítica. Na seção crítica, cada thread deve realizar a divisão entre dois números interiros informados como parâmetro. Quando ocorrer uma divisão por zero, faça a seção crítica lançar uma exceção propositalmente. Garanta, usando `try`/`finally`, que o lock seja liberado mesmo quando a exceção ocorrer. Depois, verifique se outra thread consegue obter o lock. O algoritmo deve ser executado quantas vezes o usuário desejar.

## 4. Conta Bancária
Num sistema bancário, se uma thread tentar realizar um saque enquanto outra estiver operando o saldo, ela fica travada na fila por tempo indeterminado esperando a sua vez. Para melhorar a experiência do usuário e evitar travamentos na interface, é necessário que a thread desista imediatamente caso o recurso esteja ocupado. Implemente a classe ContaBancaria. Empregue a chamada do método mutex.tryLock() (sem parâmetro de tempo). Se o método retornar true, o saque deve prosseguir normalmente, alterando o saldo. Se retornar false, a thread não deve esperar: ela deve desviar o fluxo para um bloco correspondente e exibir na tela a mensagem: "[Nome da Thread] desistiu do saque porque a conta estava ocupada por outra operação concorrente."

## 5. Sistema de Votação Online (Contador Global Segurado)

Uma prefeitura está realizando uma votação online e precisa contabilizar os votos em tempo real. Múltiplas seções eleitorais (threads) enviarão lotes de votos simultaneamente para um único servidor que possui uma variável global `int totalVotos = 0`. Sem a proteção adequada, ocorre uma condição de corrida onde votos válidos são sobrescritos e perdidos. Crie uma classe chamada UrnaEletronica que implementa a interface Runnable.Essa classe deve compartilhar um objeto centralizador que possui a variável totalVotos. No método run(), cada urna deve executar um loop para adicionar exatamente 100 votos, um a um, ao contador global. Instancie e dispare 3 threads simulando urnas ligadas ao mesmo tempo. Use um Mutex (ReentrantLock) com o método tryLock() dentro de um laço de tentativa. Se a thread não conseguir o lock de primeira, ela deve continuar tentando até conseguir computar todos os seus 100 votos. No final, a thread principal (main) deve exibir na tela que o total acumulado foi rigorosamente 300 votos.

## 6. Reserva de Assentos de Cinema (Com Timeout de Tolerância)

Em um aplicativo de venda de ingressos de cinema, quando um usuário clica em um assento, o sistema bloqueia aquele lugar temporariamente enquanto verifica a operadora de cartão. Se outro usuário tentar clicar no mesmo assento, o aplicativo não deve travá-lo para sempre, mas sim dar um tempo limite de tolerância antes de avisar que o assento está indisponível. Crie uma classe chamada Cinema que gerencie um vetor de assentos: `boolean[] assentosOccupados = new boolean[10]`. Crie um método chamado `reservarAssento(int numeroAssento)`. Proteja esse método com um ReentrantLock. No entanto, utilize o método `tryLock(3, TimeUnit.SECONDS)` para que a thread espere no máximo 3 segundos pela liberação do assento. Dentro da seção crítica (se conseguir o lock), a thread deve verificar se o assento é false (livre). Se sim, a thread deve mudar o valor para true, simular um processamento de 4 segundos (para estourar o tempo das outras) e finalizar. Dispare 3 threads concorrentes tentando comprar exatamente o assento de número 5. Imprima quais threads estouraram o timeout de 3 segundos e exibiram a mensagem: "Desistência por timeout: Assento muito disputado no momento!"

## 7. Gerador de Identificadores Únicos

Sistemas de bancos de dados relacionais e emissores de nota fiscal precisam gerar sequências numéricas estritamente sequenciais (1, 2, 3, 4...) para chaves primárias. Se duas requisições paralelas incrementarem a variável de controle no mesmo milissegundo, o sistema gerará IDs duplicados, quebrando a integridade dos dados. Crie uma classe GeradorIdentificador que possui um atributo privado `int proximoId = 1` e um Mutex. Implemente o método obterProximoId(). Use o método tryLock(). Se a thread obtiver o lock, ela deve capturar o valor atual de proximoId, incrementá-lo em 1, exibir no console qual thread pegou qual número e liberar o lock. Se não conseguir o lock (retornar false), deve exibir: "[Thread X] Falha ao gerar ID: barramento de memória ocupado. Tentando novamente..." e refazer a operação até conseguir um ID único e exclusivo. Dispare 5 threads simultâneas coletando IDs para provar que nenhum número foi pulado ou duplicado.

## 8. Gravação de Mensagens em Arquivo de Log Compartilhado (Com Timeout)
Em servidores corporativos, diferentes microsserviços (módulo de autenticação, módulo de vendas, módulo de frete) gravam suas atividades em um único arquivo texto centralizado de Log (log.txt). Se as threads escreverem juntas sem sincronização, os caracteres e palavras vão se misturar, tornando o arquivo ilegível. Cada módulo aceita esperar apenas um breve momento para registrar o log, caso contrário, joga a mensagem no console local para não atrasar o sistema. Crie uma classe chamada GerenciadorLog com um método registrarLog(String mensagem). Use um Mutex para proteger a escrita. A tentativa de tranca deve usar `tryLock(500, TimeUnit.MILLISECONDS)` (tolerância de apenas 500 milissegundos). Na seção crítica, simule a escrita lenta imprimindo: "[INÍCIO LOG] " + mensagem, faça a thread dormir por 800 milissegundos (`Thread.sleep(800)`) e depois imprima "[FIM LOG]". Instancie 3 threads (representando os módulos do sistema) disparando mensagens de log ao mesmo tempo. As threads que não conseguirem entrar no arquivo dentro do limite de 500ms devem rodar o bloco de falha e exibir: "[Módulo X] Timeout de arquivo! Log descartado/redirecionado para o console local."

## 9. Jogo de Dados Concorrente

Um grupo de jogadores (threads) está participando de um jogo de tabuleiro cooperativo. O objetivo do grupo é fazer com que um contador de pontos central atinja ou ultrapasse exatamente o valor alvo de 100 pontos. Cada jogador, na sua vez, joga um dado de 6 lados (gerando um número aleatório de 1 a 6) e adiciona o resultado ao painel geral. O jogo deve encerrar imediatamente assim que os 100 pontos forem alcançados. Se algum jogador tentar jogar após a vitória do grupo, o sistema deve avisar que o jogo já acabou.

Crie a classe da Tarefa (O Jogador). Antes de acessar a região crítica, a classe Tarefa deve gerar um número inteiro aleatório entre 1 e 6 (simulando a rolagem do dado). Esse número gerado deve ser passado como o parâmetro. Na seção crítica, antes de somar, use uma estrutura condicional para verificar se o contador atual ainda é menor que o valor alvo (100). 

-Se for menor: adicione o valor do dado ao contador e apresente na tela: "[JOGADA] [Nome da Thread] somou X. Total do grupo: Y/100". Logo em seguida, verifique se essa jogada específica bateu ou passou de 100. Se sim, exiba a mensagem: "[VITÓRIA] [Nome da Thread] fez o grupo atingir o objetivo!". 
 
- Se não for menor (o grupo já ganhou): não faça alteração e apenas exiba: "[FIM DE JOGO] [Nome da Thread] tentou jogar, mas a pontuação máxima já foi atingida.".
 
O jogo deve ser executado quantas vezes o usuário desejar.