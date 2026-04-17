# 📈 Hipóteses Estatísticas e Resultados

Com base na análise exploratória, foram formuladas hipóteses estatísticas para investigar a relação entre variáveis clínicas e o tempo de sobrevivência dos pacientes.

Foi adotado nível de significância de $\alpha = 0.05$. Quando $p < 0.05$, considera-se que existe diferença estatisticamente significativa entre os grupos.

---

## 🎯 Hipótese 1 — Pacientes com Estrogen Status positivo sobrevivem mais?

**Teste utilizado:** t de Student (comparação de médias entre dois grupos independentes)

| Grupo | Média de Sobrevivência |
|---|---|
| Estrogen Status Positive | 72.09 meses |
| Estrogen Status Negative | 60.30 meses |

**p-valor:** 2.82e-16

**Conclusão:** Existe diferença estatisticamente significativa. Pacientes com Estrogen Status positivo sobrevivem em média **11.79 meses a mais** do que os negativos, sugerindo que o status hormonal é um fator protetor relevante.

---

## 🎯 Hipótese 2 — Pacientes que morreram tinham tumores maiores?

**Teste utilizado:** t de Student

| Grupo | Média do Tumor (mm) |
|---|---|
| Alive | 29.27 |
| Dead | 37.14 |

**p-valor:** 1.24e-17

**Conclusão:** Existe diferença estatisticamente significativa. Pacientes que vieram a óbito apresentavam tumores em média **7.87mm maiores**, reforçando o tamanho tumoral como fator prognóstico relevante.

---

## 🎯 Hipótese 3 — Pacientes que morreram tinham mais linfonodos positivos?

**Teste utilizado:** t de Student

| Grupo | Média de Linfonodos Positivos |
|---|---|
| Alive | 3.60 |
| Dead | 7.24 |

**p-valor:** 1.53e-61

**Conclusão:** Existe diferença estatisticamente significativa. Este foi o resultado mais expressivo da análise — pacientes que morreram apresentaram em média **o dobro de linfonodos positivos**, evidenciando o comprometimento nodal como o principal indicador de agravamento da doença.

---

## 🎯 Hipótese 4 — Pacientes com Estrogen Status positivo possuem tumores menores?

**Teste utilizado:** t de Student

| Grupo | Média do Tumor (mm) |
|---|---|
| Estrogen Status Positive | 30.14 |
| Estrogen Status Negative | 35.17 |

**p-valor:** 1.56e-04

**Conclusão:** Existe diferença estatisticamente significativa. Pacientes positivos possuem tumores em média **5.03mm menores**, o que complementa o achado da Hipótese 1 — o melhor prognóstico desse grupo pode estar parcialmente associado à menor progressão tumoral.

---

## 🎯 Hipótese 5 — O estágio do tumor influencia a sobrevivência?

**Teste utilizado:** ANOVA one-way (comparação entre múltiplos grupos)

| Estágio | Média de Sobrevivência |
|---|---|
| T1 | 73.56 meses |
| T2 | 70.30 meses |
| T3 | 68.89 meses |
| T4 | 65.75 meses |

**p-valor:** 8.36e-07

**Conclusão:** Existe diferença estatisticamente significativa entre os estágios. Observa-se redução progressiva na sobrevivência conforme o avanço do estágio tumoral ($T1 > T2 > T3 > T4$), confirmando que o estadiamento é um preditor relevante do desfecho clínico.

---

## 🎯 Hipótese 6 — Pacientes casados sobrevivem mais?

**Teste utilizado:** t de Student

| Grupo | Média de Sobrevivência |
|---|---|
| Casados | 71.98 meses |
| Solteiros | 70.42 meses |

**p-valor:** 0.1248

**Conclusão:** Não existe diferença estatisticamente significativa ($p > 0.05$). Apesar de uma diferença numérica de 1.56 meses, ela não é suficiente para afirmar que o estado civil influencia a sobrevivência. Essa variável tem baixo potencial preditivo para o modelo de ML.

---

## 📌 Síntese dos Resultados

| Hipótese | Variável | Resultado | Relevância para o modelo |
|---|---|---|---|
| H1 | Estrogen Status | ✅ Significativa | Alta |
| H2 | Tumor Size | ✅ Significativa | Alta |
| H3 | Reginol Node Positive | ✅ Significativa | Muito alta |
| H4 | Estrogen Status × Tumor Size | ✅ Significativa | Alta |
| H5 | T Stage | ✅ Significativa | Alta |
| H6 | Marital Status | ❌ Não significativa | Baixa |