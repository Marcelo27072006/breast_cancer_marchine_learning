# 📈 Hipóteses Estatísticas e Resultados

Com base na análise exploratória, foram formuladas hipóteses estatísticas para investigar a relação entre variáveis clínicas e o tempo de sobrevivência dos pacientes.

Foi adotado nível de significância de:

$$
\alpha = 0.05
$$

\alpha = 0.05

Quando:

$$
p < 0.05
$$

p < 0.05

considera-se que existe diferença estatisticamente significativa.

---

## 🎯 Hipótese 1 — Pacientes positivos sobrevivem mais?

### Resultado

* **p-valor:** 2.8191768683102073e-16
* **Média Positive:** 72.09 meses
* **Média Negative:** 60.30 meses

### Conclusão

Existe diferença estatisticamente significativa entre os grupos.

Como a média dos pacientes com **Estrogen Status positivo** é superior, conclui-se que:

> pacientes positivos sobrevivem mais, em média.

---

## 🎯 Hipótese 2 — Pacientes que morreram tinham tumores maiores?

### Resultado

* **p-valor:** 1.2377493765575529e-17
* **Média Alive:** 29.27
* **Média Dead:** 37.14

### Conclusão

Existe diferença estatisticamente significativa entre os grupos.

A média do tamanho do tumor foi maior nos pacientes classificados como `Dead`, indicando que:

> pacientes que morreram apresentavam tumores maiores.

Esse resultado reforça a relevância clínica do tamanho tumoral como fator prognóstico.

---

## 🎯 Hipótese 3 — Pacientes que morreram tinham mais linfonodos positivos?

### Resultado

* **p-valor:** 1.5290313935284383e-61
* **Média Alive:** 3.60
* **Média Dead:** 7.24

### Conclusão

Existe diferença estatisticamente significativa entre os grupos.

Pacientes classificados como `Dead` apresentaram maior média de linfonodos positivos.

Portanto:

> maior número de linfonodos positivos está associado a pior prognóstico e menor sobrevida.

Este foi um dos resultados estatisticamente mais fortes da análise.

---

## 🎯 Hipótese 4 — Pacientes positivos possuem tumores menores?

### Resultado

* **p-valor:** 0.00015561625114507565
* **Média Positive:** 30.14
* **Média Negative:** 35.17

### Conclusão

Existe diferença estatisticamente significativa entre os grupos.

Como a média do tamanho tumoral foi menor no grupo positivo, conclui-se que:

> pacientes positivos possuem tumores menores, em média.

Esse resultado pode ajudar a explicar a maior sobrevida observada na hipótese 1.

---

## 🎯 Hipótese 5 — O estágio do tumor influencia a sobrevivência?

### Resultado

* **p-valor:** 8.362503589348084e-07

### Médias por estágio

* **T1:** 73.56 meses
* **T2:** 70.30 meses
* **T3:** 68.89 meses
* **T4:** 65.75 meses

### Conclusão

Existe diferença estatisticamente significativa entre os estágios.

Observa-se uma redução progressiva na média de sobrevivência conforme o estágio avança:

$$
T1 > T2 > T3 > T4
$$

T1 > T2 > T3 > T4

Portanto:

> estágios tumorais mais avançados influenciam negativamente a sobrevivência.

---

## 🎯 Hipótese 6 — Pacientes casados sobrevivem mais?

### Resultado

* **p-valor:** 0.12477266010236526
* **Média casados:** 71.98 meses
* **Média solteiros:** 70.42 meses

### Conclusão

Não foi identificada diferença estatisticamente significativa entre os grupos.

Apesar da média dos pacientes casados ser ligeiramente maior, como:

$$
p > 0.05
$$

p > 0.05

não é possível afirmar que o estado civil influencia a sobrevivência de forma significativa.
