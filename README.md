# Apdex Tests -

Projeto Maven com JUnit 5 para TDD de **Apdex**. JDK 17. RM fixo em **556607**. Nome em **primeira linha de cada classe**

## Objetivo
Calcular o Apdex por contagem e classificar o score. Cobrir todas as faixas de classificação com testes unitários.

## Requisitos
- JDK 17
- Maven 3.9+
- IntelliJ IDEA (opcional)

## Estrutura
```
src/
  main/java/org/example/
    Apdex.java
    Main.java            
  test/java/org/example/
    ApdexTest.java
pom.xml
```

## Como rodar
```bash
mvn clean test
```

### Onde ver o resultado
- Console: `Tests run: … Failures: 0, Errors: 0` e `BUILD SUCCESS`.
- Relatórios: `target/surefire-reports/TEST-org.example.ApdexTest.{txt,xml}`.
- IntelliJ: Tool Windows → Run ou Maven → Lifecycle → test.

## Como funciona (resumo técnico)
- Fórmula: **APDEX = (S + T/2) / TOTAL_AMOSTRAS**.  
- `TOTAL_AMOSTRAS`: constante = **556607**.
- Limiares por tempo com threshold **T**:
  - `Satisfied`: `d <= T`
  - `Tolerating`: `T < d <= 4T`
  - `Frustrated`: `d > 4T`
- Classificação adotada:
  - `>= 0.94` → **EXCELLENT**
  - `0.85–0.9399` → **GOOD**
  - `0.70–0.8499` → **FAIR**
  - `0.50–0.6999` → **POOR**
  - `< 0.50` → **UNACCEPTABLE**

## Testes cobertos
- Score máximo e `EXCELLENT`.
- Faixas `GOOD`, `FAIR`, `POOR`, `UNACCEPTABLE`.
- Validação de soma `S+T+F == RM`.
- Rejeição de contagens negativas.
- Utilitário de contagem por duração com `T = 300 ms`.

## Padrões e conformidade
- Nome e RM na **primeira linha** de cada classe.
- `@BeforeAll` para setup único.
- Denominador fixo no RM, conforme enunciado.
- JUnit 5 + Surefire 3.2.5.

## Principais arquivos

### `pom.xml`
- JDK 17, JUnit 5, Surefire configurado para `**/*Test.java`.

### `Apdex.java`
- API pública:
  - `computeScoreByCounts(int s, int t, int f)`  
  - `classify(double score)`  
  - `countsFromDurations(List<Double> durs)`  
  - `Rating` enum e `Counts` record.
- Validações: faixa do score, contagens negativas e soma igual ao RM.

### `ApdexTest.java`
- Casos por faixa, limites e utilitário de duração.
- Dados de exemplo com `T = 300 ms`: `100, 200` (S), `1100, 1200` (T), `1301` (F).

## Como alterar
- **RM**: mude `TOTAL_AMOSTRAS` em `Apdex.java` e ajuste os testes que usam percentuais, se necessário.
- **Threshold**: mude o valor passado ao construtor em `@BeforeAll`.

## Troubleshooting
- **“class … is public, should be declared in a file named …”**  
  Arquivo e classe devem ter o mesmo nome. `Apdex.java` contém `public class Apdex`.
- **Pacote divergente**  
  Garanta `package org.example;` em todos os arquivos.
- **Falha em utilitário de durações**  
  Revise o limite: `Tolerating` inclui **`<= 4T`**.
- **Soma não bate o RM**  
  `s + t + f` deve ser **exatamente** `TOTAL_AMOSTRAS`.

## Comandos úteis
```bash
mvn -q test
open target/surefire-reports
cat target/surefire-reports/TEST-org.example.ApdexTest.txt
```

## Licença
Uso acadêmico. Ajuste conforme sua instituição exigir.
